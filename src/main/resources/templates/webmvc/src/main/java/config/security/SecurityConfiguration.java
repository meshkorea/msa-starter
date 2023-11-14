package {{packageName}}.config.security;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static org.springframework.security.oauth2.jwt.NimbusJwtDecoder.withJwkSetUri;

import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.info.InfoEndpoint;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {

  private final OAuth2ClientProperties properties;

  public SecurityConfiguration(OAuth2ClientProperties properties) {
    this.properties = properties;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    // @formatter:off
    return http
        .cors(Customizer.withDefaults())
        .csrf(spec -> spec.disable())
        .headers(spec -> spec.frameOptions(FrameOptionsConfig::disable))
        .sessionManagement(spec -> spec.sessionCreationPolicy(STATELESS))
        .authorizeHttpRequests(spec ->
            spec.requestMatchers(EndpointRequest.to(HealthEndpoint.class, InfoEndpoint.class)).permitAll()
                .requestMatchers("/management/**").hasAuthority("ROLE_ADMIN")
                .requestMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                .anyRequest().permitAll())
        .oauth2ResourceServer(customizer -> customizer.jwt(Customizer.withDefaults()))
        .build();
    // @formatter:on
  }

  @Bean
  public JwtDecoder jwtDecoder() {
    return withJwkSetUri(properties.getProvider().get("uaa").getJwkSetUri()).build();
  }

  @Bean
  public JwtAuthenticationConverter customJwtAuthenticationConverter() {
    final JwtGrantedAuthoritiesConverter authoritiesConverter = new JwtGrantedAuthoritiesConverter();
    authoritiesConverter.setAuthorityPrefix("");
    authoritiesConverter.setAuthoritiesClaimName("authorities");

    final JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();
    authenticationConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);

    return authenticationConverter;
  }
}
