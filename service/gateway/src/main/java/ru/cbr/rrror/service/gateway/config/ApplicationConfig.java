package ru.cbr.rrror.service.gateway.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class ApplicationConfig {

    private static class EXPRESSION
    {
        private static final String DB_SERVICE_URL_PREFIX = "${gateway.db-service.url-prefix}";
        private static final String DB_SERVICE_WEBSOCKET_MESSAGE_PREFIX = "${gateway.db-service.websocket-message-prefix}";
        private static final String WEBSOCKET_ENDPOINT = "${gateway.websocket.endpoint}";
    }


    @Bean
    Config getConfig(@Value(EXPRESSION.DB_SERVICE_URL_PREFIX) String dbServiceUrlPrefix,
                     @Value(EXPRESSION.DB_SERVICE_WEBSOCKET_MESSAGE_PREFIX) String dbServiceWebsocketMessagePrefix,
                     @Value(EXPRESSION.WEBSOCKET_ENDPOINT) String websocketEndpoint) {

        ValidatedConfig cfg = new ValidatedConfig(
                                new Config(
                                    new Config.DbServiceConfig(
                                            dbServiceUrlPrefix,
                                            dbServiceWebsocketMessagePrefix),
                                    new Config.WebsocketConfig(websocketEndpoint)));
        return cfg.get();
    }

    @AllArgsConstructor
    static class ValidatedConfig {

        private final Config config;

        public Config get() {
            Validator validator = new ConfigValidator(config);
            validator.validate();
            if (validator.hasErros()) {
                throw new ConfigValidationException(validator.getErrors().toString());
            };

            return config;
        }

        static class ConfigValidationException extends RuntimeException {
            public ConfigValidationException(String mes) {
                super(mes);
            }
        }
    }


    static private class ConfigValidator implements Validator {

        private final Config config;
        private final Validator.FieldVaildator<Object> notNullVaildator = new Validator.FieldVaildator.NotNullValidator();

        @Getter
        private final Validator.Errors errors = new Validator.Errors();

        public ConfigValidator(Config config) {
            this.config = config;
        }

        public boolean hasErros() {
            return errors.hasErrors();
        }

        public void validate() {
            notNullVaildator.validate(EXPRESSION.DB_SERVICE_URL_PREFIX, config.getDbServiceConfig().getUrlPrefix(), errors);
            notNullVaildator.validate(EXPRESSION.DB_SERVICE_WEBSOCKET_MESSAGE_PREFIX, config.getDbServiceConfig().getWebsocketMessagePrefix(), errors);
            notNullVaildator.validate(EXPRESSION.WEBSOCKET_ENDPOINT, config.getWebsocketConfig().getEndpoint(), errors);
        }
    }

    private interface Validator {
        boolean hasErros();
        Errors getErrors();
        void validate();

        interface FieldVaildator<T> {
            void validate(String field, T value, ConfigValidator.Errors errors);

            class NotNullValidator implements FieldVaildator<Object> {

                public void validate(String field, Object value, ConfigValidator.Errors errors) {
                    if (value == null) {
                        errors.add(field, "value can't be null");
                    }
                }
            }
        }

        class Errors {
            @Getter
            private final List<Error> errors = new ArrayList<Error>();

            public void add(String field, String mes) {
                errors.add(new Error(field, mes));
            }

            public boolean hasErrors() {
                return !errors.isEmpty();
            }

            @Override
            public String toString() {
                StringBuilder sb = new StringBuilder();
                for (Error err : errors) {
                    sb.append(err).append("\\n");
                }
                return sb.toString();
            }

            @AllArgsConstructor
            @Getter
            private static class Error {
                private final String field;
                private final String message;

                public String getMessage() {
                    return field + " validation error: " + message;
                }

                @Override
                public String toString() {
                    return getMessage();
                }
            }
        }
    }

    @AllArgsConstructor @Getter @ToString
    public static class Config {

        private final DbServiceConfig dbServiceConfig;
        private final WebsocketConfig websocketConfig;

        @AllArgsConstructor @Getter @ToString
        public static class DbServiceConfig {
            private final String urlPrefix;
            private final String websocketMessagePrefix;
        }

        @AllArgsConstructor @Getter @ToString
        public static class WebsocketConfig {
            private final String endpoint;
        }
    }
}
