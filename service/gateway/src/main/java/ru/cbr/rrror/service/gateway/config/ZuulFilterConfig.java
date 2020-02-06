package ru.cbr.rrror.service.gateway.config;

import com.google.common.io.ByteStreams;
import com.google.common.io.CharStreams;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.javatuples.Pair;
import org.json.JSONObject;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Slf4j
@AllArgsConstructor
@Configuration
public class ZuulFilterConfig {

    private final SimpMessagingTemplate websocket;
    private final ApplicationConfig.Config config;

    @Bean
    ZuulFilter getZuulFilter() {
        return new ZuulFilter() {
            private final Supplier<RequestContext> ctx = () -> RequestContext.getCurrentContext();
            private final Predicate<RequestContext> uriFilterCondition = (ctx) -> ctx.getRequest().getRequestURI().startsWith("/db-service/api");
            private final Supplier<List<Runnable>> actions = () -> new DbServiceZuulFilterAction(websocket,config).getActions();

            @Override
            public String filterType() {
                return FilterConstants.POST_TYPE;
            }

            @Override
            public int filterOrder() {
                return FilterConstants.SEND_RESPONSE_FILTER_ORDER - 1;
            }

            private boolean isDbServiceRequest() {
                return uriFilterCondition.test(ctx.get());
            }

            @Override
            public boolean shouldFilter() {
                return isDbServiceRequest();
            }

            @Override
            public Object run() throws ZuulException {
                RequestContext context = ctx.get();
                HttpServletRequest request = context.getRequest();
                HttpServletResponse response = context.getResponse();

                log.info(String.format(">>> zuul filter: %s request to %s with status %s", request.getMethod(), request.getRequestURL().toString(), response.getStatus()));
                for (Runnable action : actions.get()) {
                    action.run();
                }
                return null;
            }
        };
    }

    @Slf4j
    @AllArgsConstructor
    static class DbServiceZuulFilterAction {

        private final SimpMessagingTemplate websocket;
        private final ApplicationConfig.Config config;
        private final ActionFactory factory = new ActionFactory();

        private enum ACTION {
            CREATE_ENTITY,
            UPDATE_ENTITY,
            DELETE_ENTITY,
            HREF_MODIFY;
        }

        public List<Runnable> getActions() {
            return Arrays.asList(
                    factory.get(ACTION.HREF_MODIFY),
                    factory.get(ACTION.CREATE_ENTITY),
                    factory.get(ACTION.UPDATE_ENTITY),
                    factory.get(ACTION.DELETE_ENTITY));
        }

        interface EntityExtractorFunction extends Supplier<Function<URI, String>> {

            default Function<URI, String> get() {
                return (uri) -> {
                    String[] arr = uri.getRawPath().split("/");
                    if (arr.length > 2) {
                        String x = arr[3];
                        String e = x.substring(0, x.length() - 1);
                        return e.substring(0,1).toUpperCase().concat(e.substring(1, e.length()));
                    } else {
                        return "unknown";
                    }
                };
            }
        }

        interface RequestCtxPredicate {

            default Predicate<RequestContext> getMethod() {
                return (ctx) -> ctx.getRequest().getMethod().equalsIgnoreCase("GET");
            };

            default Predicate<RequestContext> putMethod() {
                return (ctx) -> ctx.getRequest().getMethod().equalsIgnoreCase("PUT");
            };

            default Predicate<RequestContext> postMethod() {
                return (ctx) -> ctx.getRequest().getMethod().equalsIgnoreCase("POST");
            }

            default Predicate<RequestContext> deleteMethod() {
                return (ctx) -> ctx.getRequest().getMethod().equalsIgnoreCase("DELETE");
            }

            default Predicate<RequestContext> status201() {
                return (ctx) -> ctx.getResponse().getStatus() == 201;
            }

            default Predicate<RequestContext> status200() {
                return (ctx) -> ctx.getResponse().getStatus() == 200;
            }

            default Predicate<RequestContext> status204() {
                return (ctx) -> ctx.getResponse().getStatus() == 204;
            }
        }

        private class ActionFactory {
            private final Map<ACTION, Supplier<Runnable>> actions = getActions();

            private Map<ACTION, Supplier<Runnable>> getActions() {
                RequestCtxPredicate predicate = new RequestCtxPredicate() {};
                EntityExtractorFunction entityExtractorFunction = new EntityExtractorFunction() {};

                Predicate<RequestContext> getMethod = predicate.getMethod();
                Predicate<RequestContext> putMethod = predicate.putMethod();
                Predicate<RequestContext> postMethod = predicate.postMethod();
                Predicate<RequestContext> deleteMethod = predicate.deleteMethod();
                Predicate<RequestContext> status201 = predicate.status201();
                Predicate<RequestContext> status200 = predicate.status200();
                Predicate<RequestContext> status204 = predicate.status204();

                Function<Pair<String, RequestContext>, String> getDestination = (p) ->
                        p.getValue0() + entityExtractorFunction.get().apply(URI.create(p.getValue1().getRequest().getRequestURI()));

                Supplier<String> messagePrefix = () -> config.getDbServiceConfig().getWebsocketMessagePrefix();

                Consumer<Message> toWebsocket = (mes) ->  websocket.convertAndSend(mes.destination, mes.body);

                Map<ACTION, Supplier<Runnable>> m = new HashMap<>();
                m.put(ACTION.CREATE_ENTITY, ()->
                {
                    Supplier<String> createPrefix = () -> messagePrefix.get() + "/create";
                    NotifyAction action = new NotifyAction();
                    action
                            .on(postMethod.and(status201))
                            .getMessage((ctx) ->
                                    Optional.of(Message.create(getDestination.apply(Pair.with(createPrefix.get(), ctx)),
                                            new HalParser(
                                                    new DbServiceZuulFilterAction.ContextData(ctx).get()).parse().getSelfLink()))
                            )
                            .sendMessage(toWebsocket);

                    return action;
                });

                m.put(ACTION.UPDATE_ENTITY, () -> {
                    Supplier<String> updatePrefix = () -> messagePrefix.get() + "/update";
                    NotifyAction action = new NotifyAction();
                    action
                            .on(status200.and(putMethod))
                            .getMessage((ctx) ->
                                    Optional.of(Message.create(getDestination.apply(Pair.with(updatePrefix.get(), ctx)),
                                            new HalParser(
                                                    new DbServiceZuulFilterAction.ContextData(ctx).get()).parse().getSelfLink()))
                            )
                            .sendMessage(toWebsocket);
                    return action;
                });

                m.put(ACTION.DELETE_ENTITY, () -> {
                    Supplier<String> deletePrefix = () -> messagePrefix.get() + "/delete";
                    NotifyAction action = new NotifyAction();
                    action
                            .on(status204.and(deleteMethod))
                            .getMessage((ctx) -> Optional.of(Message.create(getDestination.apply(Pair.with(deletePrefix.get(), ctx)),
                                    ctx.getRequest().getRequestURL().toString())))
                            .sendMessage(toWebsocket);
                    return action;
                });

                m.put(ACTION.HREF_MODIFY, () -> {
                    LinkModifyAction action = new LinkModifyAction();
                    action
                            .on(status200.and(getMethod.or(putMethod).or(postMethod))
                                    .or(status201.and(putMethod.or(postMethod))))
                            .modify((ctx) ->
                                    new ContextData(ctx).write(
                                            new HalHrefModifier(
                                                    new DbServiceZuulFilterAction.ContextData(ctx).get(), config.getDbServiceConfig().getUrlPrefix()).modify()
                                    )
                            );
                    return action;
                });
                return m;
            }

            public Runnable get(ACTION type) {
                if (actions.containsKey(type)) {
                    return actions.get(type).get();
                } else {
                    throw new RuntimeException("action not found by code: " + type.toString());
                }
            }
        }

        private static class LinkModifyAction implements Runnable {
            Supplier<RequestContext> ctx = RequestContext::getCurrentContext;
            Predicate<RequestContext> predicate;
            Consumer<RequestContext> modify;

            public LinkModifyAction on(Predicate<RequestContext> predicate) {
                this.predicate = predicate;
                return this;
            }

            public LinkModifyAction modify(Consumer<RequestContext> modify) {
                this.modify = modify;
                return this;
            }

            @Override
            public void run() {
                if (predicate.test(ctx.get())) {
                    modify.accept(ctx.get());
                }
            }
        }

        private static class NotifyAction implements Runnable {
            Supplier<RequestContext> ctx = RequestContext::getCurrentContext;
            Predicate<RequestContext> predicate;
            Function<RequestContext, Optional<Message>> getMessage;
            Consumer<Message> sendMessage;

            public NotifyAction with(Supplier<RequestContext> ctx) {
                this.ctx = ctx;
                return this;
            }

            public NotifyAction on(Predicate<RequestContext> predicate) {
                this.predicate = predicate;
                return this;
            }

            public NotifyAction sendMessage(Consumer<Message> sendMessage) {
                this.sendMessage = sendMessage;
                return this;
            }

            public NotifyAction getMessage(Function<RequestContext, Optional<Message>> getMessage) {
                this.getMessage = getMessage;
                return this;
            }

            @Override
            public void run() {
                if (predicate.test(ctx.get())) {
                    try {
                        getMessage.apply(ctx.get()).ifPresent(sendMessage);
                    } catch (Exception e) {
                        log.error("can't send notify message, cause: " + e.toString(), e);
                    }
                }
            }
        }

        @AllArgsConstructor
        private static class Message {

            public static Message create(String dest, String body) {
               return new Message(body, dest);
            }

            private final String body;
            private final String destination;

            public String body() {
                return body;
            }

            public String dest() {
                return destination;
            }
        }

        private static class HALSyntax {
            private static final String LINKS = "_links";
            private static final String SELF = "self";
            private static final String HREF = "href";
        }

        private static class HalParser {
            private final String halJsonStr;
            private String selfLink = "";

            HalParser(String halJsonStr) {
                this.halJsonStr = halJsonStr;
            }

            public HalObject parse() {
                try {
                    return parseOrError();
                } catch (Exception e) {
                    throw new HalParserException("can't parse hal json: " + halJsonStr + ", cause: " + e.toString(), e);
                }
            }

            private HalObject parseOrError() {
                JSONObject halJson = new JSONObject(halJsonStr);
                if (halJson.has(HALSyntax.LINKS)) {
                    JSONObject links = halJson.getJSONObject(HALSyntax.LINKS);
                    selfLink = links.getJSONObject(HALSyntax.SELF).getString(HALSyntax.HREF);
                }
                return new HalObject(this);
            }

            static class HalParserException extends RuntimeException {
                public HalParserException(String mes, Exception cause) {
                    super(mes, cause);
                }
            }

            @Getter
            static class HalObject {
                private final String selfLink;

                HalObject(HalParser p) {
                    selfLink = p.selfLink;
                }

            }
        }

        @AllArgsConstructor
        static class HalHrefModifier {
            private final String halJsonStr;
            private final String toHrefPrefix;

            private final static String regex = "(https?|http)://[\\w\\d\\.:\\d]*";

            public String modify() {
                return halJsonStr.replaceAll(regex, toHrefPrefix);
            }
        }

        interface ContextDataWriter extends ContextDataReader {
            void write(String str);

            class WriteContextDataException extends RuntimeException {
                public WriteContextDataException(String mes, Exception cause) {
                    super(mes,cause);
                }
            }
        }

        interface ContextDataReader {
            String get();

            class ReadContextDataException extends RuntimeException {
                public ReadContextDataException(String mes, Exception cause) {
                    super(mes,cause);
                }
            }
        }

        static class ContextData implements ContextDataReader, DbServiceZuulFilterAction.ContextDataWriter {
            private static final Charset CHARSET = StandardCharsets.UTF_8;

            private final RequestContext context;

            public ContextData(RequestContext context) {
                this.context = context;
            }

            public String get() {
                byte[] bytes = readBytes();
                write(bytes);
                String data = "";
                if (bytes.length > 0) {
                   data = bytesToString(bytes);
                }
                return data;
            }

            public void write(String str) {
                write(str.getBytes());
            }

            private byte[] readBytes() {
                try {
                    return ByteStreams.toByteArray(context.getResponseDataStream());
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new ReadContextDataException("can't read context data: " + e.toString(), e);
                }
            }

            private String bytesToString(byte[] bytes) {
                try {
                    return CharStreams.toString(new InputStreamReader(new ByteArrayInputStream(bytes),CHARSET));
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new ReadContextDataException("can't transform bytes to string: " + e.toString(), e);
                }
            }

            private void write(byte[] bytes) {
                write(new ByteArrayInputStream(bytes));
            }

            private void write(InputStream stream) {
                context.setResponseDataStream(stream);
            }

        }
    }
}
