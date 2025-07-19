package pe.isil.saturno_4631.config;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import pe.isil.saturno_4631.security.UserDetailsServiceImp;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private UserDetailsServiceImp userDetailsServiceImp;

    @SneakyThrows
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
    {
        return http
                .formLogin(form-> form.loginPage("/login").permitAll())
                .authorizeHttpRequests((authz)->authz
                        .requestMatchers("/llaves/**").authenticated()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/ligas/**").hasRole("LIGA")
                        .anyRequest().permitAll())
                .userDetailsService(userDetailsServiceImp)
                .logout(logout->logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/"))
                .exceptionHandling(customizer->customizer.accessDeniedHandler(accessDeniedHandlerApp()))
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    AccessDeniedHandler accessDeniedHandlerApp()
    {
        return (
                ((request, response,
                  accessDeniedException)
                        -> response.sendRedirect(request.getContextPath() + "/403"))
                );
    }
}
