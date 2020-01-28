package ru.cbr.rrror.service.gateway.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@AllArgsConstructor
@RestController
public class TokenController {

    @GetMapping("/tokenInfo")
    public TokenInfo servicesApiList(
            @AuthenticationPrincipal OAuth2User oauth2User,
            @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {

        return new TokenInfo(oauth2User.getName(),
                authorizedClient.getAccessToken().getTokenValue(),
                authorizedClient.getAccessToken().getTokenType().getValue());
    }

    @AllArgsConstructor @NoArgsConstructor
    @Getter @Setter
    static class TokenInfo {
        private String user;
        private String token;
        private String tokenType;
    }
}
