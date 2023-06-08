package panomete.jwtauth.config;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import panomete.jwtauth.utility.JwtEntryPoint;
import panomete.jwtauth.utility.JwtFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
//@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    final JwtEntryPoint jwtEntryPoint;

    private static final String[] FREE_AREA = {
            "/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/favicon.ico",
            "/webjars/**",
            "/api/v1/auth/",
            "/api/v1/auth/signup",
    };

    private static final String[] ACCOUNT_WHITELIST = {
            "/api/v1/auth/password",
            "/api/v1/auth/email",
            "/api/v1/auth/username",
            "/api/v1/auth/forgetpassword",
            "/api/v1/auth/refresh",
    };

    private static final String[] ADMIN_WHITELIST = {
            "/api/v1/admin/**"
    };

    private static final String[] USER_WHITELIST = {
            "/api/v1/user/**"
    };

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling((exception) -> exception.authenticationEntryPoint(jwtEntryPoint))
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((request) -> request
                        .requestMatchers(FREE_AREA).permitAll()
                        .requestMatchers(ACCOUNT_WHITELIST).hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers(ADMIN_WHITELIST).hasAuthority("ADMIN")
                        .requestMatchers(USER_WHITELIST).hasAuthority("USER")
                        .anyRequest().authenticated()
                );
        return http.build();
    }

    @SneakyThrows
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.httpFirewall(allowUrlEncodedSlashHttpFirewall());
    }

    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        DefaultHttpFirewall firewall = new DefaultHttpFirewall();
        firewall.setAllowUrlEncodedSlash(true);
        return firewall;
    }
}
