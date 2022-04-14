//package org.burningokr.config.authorizationserver;
///*
// * Copyright 2020-2022 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//import java.security.KeyPair;
//import java.security.KeyPairGenerator;
//import java.security.NoSuchAlgorithmException;
//import java.security.interfaces.RSAPrivateKey;
//import java.security.interfaces.RSAPublicKey;
//import java.util.UUID;
//
//import com.nimbusds.jose.jwk.JWKSet;
//import com.nimbusds.jose.jwk.RSAKey;
//import com.nimbusds.jose.jwk.source.JWKSource;
//import com.nimbusds.jose.proc.SecurityContext;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
//import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
//import org.springframework.security.oauth2.core.AuthorizationGrantType;
//import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
//import org.springframework.security.oauth2.core.oidc.OidcScopes;
//import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
//import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
//import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
//import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
//import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
//import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
//import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
//import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
//import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
//import org.springframework.security.web.SecurityFilterChain;
//
///**
// * @author Joe Grandja
// * @since 0.0.1
// */
//@Configuration(proxyBeanMethods = false)
//public class AuthorizationServerConfig {
//
////  @Bean
////  @Order(Ordered.HIGHEST_PRECEDENCE)
////  public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
////    OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
////    return http.formLogin(Customizer.withDefaults()).build();
////  }
//
//  // @formatter:off
//  @Bean
//  public RegisteredClientRepository registeredClientRepository() {
//    RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
//      .clientId("m2rvidwwqwua6tso9s0n7fvdqqtk033swydb99d7b64f6742v3zxebm7clxy7emh")
//      .clientSecret("{noop}2bxjuon6mjw0p2z94mbianjxlyijm8dfe9k92yhima9h4us29qbs0gv8up6hozh4")
//      .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
//      .authorizationGrantType(AuthorizationGrantType.PASSWORD)
//      .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
//      .redirectUri("http://127.0.0.1:8080/authorized")
//      .scope(OidcScopes.OPENID)
//      .scope("USER")
//      .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
//      .build();
//
//    // Save registered client in db as if in-memory
//    return new InMemoryRegisteredClientRepository(registeredClient);
//
//  }
//  // @formatter:on
//
////  @Bean
////  public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
////    return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
////  }
////
////  @Bean
////  public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
////    return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
////  }
//
//  @Bean
//  public JWKSource<SecurityContext> jwkSource() throws NoSuchAlgorithmException {
//    RSAKey rsaKey = generateRsa();
//    JWKSet jwkSet = new JWKSet(rsaKey);
//    return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
//  }
//
//  @Bean
//  public ProviderSettings providerSettings() {
//    return ProviderSettings.builder().issuer("http://localhost:8080").build();
//  }
//
////  @Bean
////  public EmbeddedDatabase embeddedDatabase() {
////    // @formatter:off
////    return new EmbeddedDatabaseBuilder()
////      .generateUniqueName(true)
////      .setType(EmbeddedDatabaseType.H2)
////      .setScriptEncoding("UTF-8")
////      .addScript("org/springframework/security/oauth2/server/authorization/oauth2-authorization-schema.sql")
////      .addScript("org/springframework/security/oauth2/server/authorization/oauth2-authorization-consent-schema.sql")
////      .addScript("org/springframework/security/oauth2/server/authorization/client/oauth2-registered-client-schema.sql")
////      .build();
////    // @formatter:on
////  }
//
//  private static RSAKey generateRsa() throws NoSuchAlgorithmException {
//    KeyPair keyPair = generateRsaKey();
//    RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
//    RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
//
//    return new RSAKey.Builder(publicKey)
//      .privateKey(privateKey)
//      .keyID(UUID.randomUUID().toString())
//      .build();
//  }
//
//  private static KeyPair generateRsaKey() throws NoSuchAlgorithmException {
//    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
//    keyPairGenerator.initialize(2048);
//
//    return keyPairGenerator.generateKeyPair();
//  }
//
//}
