package com.newnil.cas.oauth2.provider.config;

import com.newnil.cas.oauth2.provider.dao.entity.GrantTypeEntity;
import com.newnil.cas.oauth2.provider.dao.entity.ScopeEntity;
import com.newnil.cas.oauth2.provider.dao.repository.GrantTypeRepository;
import com.newnil.cas.oauth2.provider.dao.repository.ScopeRepository;
import com.newnil.cas.oauth2.provider.service.OAuth2DatabaseClientDetailsService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

@Configuration
@Profile("default-user-and-roles")
public class DefaultClientDetailsConfig implements InitializingBean {

    private static final String[] DEFAULT_GRANT_TYPES = {"authorization_code", "refresh_token", "password"};

    private static final String[] DEFAULT_SCOPES = {"read", "write", "trust"};


    @Autowired
    private GrantTypeRepository grantTypeRepository;

    @Autowired
    private ScopeRepository scopeRepository;

    @Autowired
    private OAuth2DatabaseClientDetailsService oAuth2DatabaseClientDetailsService;

    @Override
    public void afterPropertiesSet() throws Exception {
        grantTypeRepository.save(Arrays.stream(DEFAULT_GRANT_TYPES).map(
                grantType -> GrantTypeEntity.builder().value(grantType).build()
        ).collect(Collectors.toList()));

        scopeRepository.save(Arrays.stream(DEFAULT_SCOPES).map(
                scope -> ScopeEntity.builder().value(scope).build()
        ).collect(Collectors.toList()));

        BaseClientDetails clientDetails = new BaseClientDetails("test-client-id", null, "read,write,trust", "authorization_code,refresh_token", null);
        clientDetails.setClientSecret("test-client-id-secret-123");
        clientDetails.setRegisteredRedirectUri(Collections.emptySet());

        oAuth2DatabaseClientDetailsService.addClientDetails(clientDetails);

        clientDetails = new BaseClientDetails("test-res-client", null, null, null, null);
        clientDetails.setClientSecret("test-res-client-secret-123");
        clientDetails.setRegisteredRedirectUri(Collections.singleton("http://test.com"));

        oAuth2DatabaseClientDetailsService.addClientDetails(clientDetails);

        clientDetails = new BaseClientDetails("test_password_client", null, "trust", "password,refresh_token", null);
        clientDetails.setClientSecret("1234567");
        clientDetails.setRegisteredRedirectUri(Collections.emptySet());

        oAuth2DatabaseClientDetailsService.addClientDetails(clientDetails);
    }
}
