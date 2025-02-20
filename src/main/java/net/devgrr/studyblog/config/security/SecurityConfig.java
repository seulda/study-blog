package net.devgrr.studyblog.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.devgrr.studyblog.jwt.JwtService;
import net.devgrr.studyblog.jwt.filter.JwtAuthenticationProcessingFilter;
import net.devgrr.studyblog.login.LoginService;
import net.devgrr.studyblog.login.filter.JsonUsernamePasswordAuthenticationFilter;
import net.devgrr.studyblog.login.handler.LoginFailureHandler;
import net.devgrr.studyblog.login.handler.LoginSuccessJWTProvideHandler;
import net.devgrr.studyblog.member.MemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final LoginService loginService;
  private final ObjectMapper objectMapper;
  private final MemberRepository memberRepository;
  private final JwtService jwtService;
  private final ApiLoggingFilter apiLoggingFilter;

  @Bean
  SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .headers(headers -> headers.frameOptions(FrameOptionsConfig::disable))
        .sessionManagement(
            (sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            (authorizeHttpRequests) ->
                authorizeHttpRequests
                    .requestMatchers(
                        //                        new AntPathRequestMatcher("/**"),
                        new AntPathRequestMatcher("/h2-console/**"),
                        new AntPathRequestMatcher("/swagger-ui/**"),
                        new AntPathRequestMatcher("/v3/api-docs/**"))
                    .permitAll()
                    .requestMatchers(
                        new AntPathRequestMatcher("/login"),
                        new AntPathRequestMatcher("/api/user/signup"),
                        new AntPathRequestMatcher("/error"))
                    .permitAll()
                    .requestMatchers(new AntPathRequestMatcher("/admin"))
                    .hasRole("ADMIN")
                    .anyRequest()
                    .authenticated())
        .addFilterBefore(apiLoggingFilter, LogoutFilter.class)
        .addFilterBefore(jwtAuthenticationProcessingFilter(), LogoutFilter.class)
        .addFilterBefore(
            jsonUsernamePasswordLoginFilter(), UsernamePasswordAuthenticationFilter.class);

    return httpSecurity.build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setPasswordEncoder(passwordEncoder());
    provider.setUserDetailsService(loginService);
    return new ProviderManager(provider);
  }

  @Bean
  public LoginSuccessJWTProvideHandler loginSuccessJWTProvideHandler() {
    return new LoginSuccessJWTProvideHandler(jwtService, memberRepository);
  }

  @Bean
  public LoginFailureHandler loginFailureHandler() {
    return new LoginFailureHandler();
  }

  @Bean
  public JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordLoginFilter() {
    JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordLoginFilter =
        new JsonUsernamePasswordAuthenticationFilter(objectMapper);
    jsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
    jsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(
        loginSuccessJWTProvideHandler());
    jsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
    return jsonUsernamePasswordLoginFilter;
  }

  @Bean
  public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
    return new JwtAuthenticationProcessingFilter(jwtService, memberRepository);
  }
}
