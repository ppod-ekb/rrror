package ru.cbr.rrror.service.db.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import java.util.List;
import java.util.Map;

@Slf4j
@EnableWebSecurity
public class SecurityConfig extends ResourceServerConfigurerAdapter {

    @Bean
    AuthoritiesExtractor getAuthoritiesExtractor() {
        return new AuthoritiesExtractorImpl();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/api/**").authorizeRequests().anyRequest().authenticated();
    }

    static class AuthoritiesExtractorImpl implements AuthoritiesExtractor {

        @Override
        public List<GrantedAuthority> extractAuthorities(Map<String, Object> map) {
            log.debug(">>> extracted authorities: " + map);
            return AuthorityUtils.createAuthorityList("ROLE_AIB", "ROLE_USER");
        }
    }

}
