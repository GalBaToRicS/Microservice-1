package ru.itmentor.spring.boot_security.demo.config;

import ru.itmentor.spring.boot_security.demo.config.filter.SecurityFilter;
import ru.itmentor.spring.boot_security.demo.config.service.UserDetailsServiceImpl;
import ru.itmentor.spring.boot_security.demo.constant.RoleName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Конфигурация безопасности приложения.
 * <p>
 * Этот класс управляет настройками безопасности, включая правила доступа к
 * различным конечным точкам API, обработку аутентификации и настройку фильтров
 * безопасности.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Lazy
    @Autowired
    private SecurityFilter securityFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Настраивает правила безопасности для веб-приложения.
     * <p>
     * Этот метод создает {@link WebSecurityCustomizer}, который игнорирует
     * запросы, соответствующие заданному паттерну, в данном случае запросы
     * к "/api/auth/**". Это позволяет избегать проверки безопасности
     * для аутентификационных API.
     *
     * @return Настроенный {@link WebSecurityCustomizer}.
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers(
                "/api/auth/**"
        );
    }

    /**
     * Создает {@link SecurityFilterChain} для настройки правил доступа к конечным точкам API.
     * <p>
     * Этот метод определяет политику создания сессии, правила доступа
     * и добавляет кастомный фильтр безопасности.
     *
     * @param httpSecurity объект {@link HttpSecurity} для настройки безопасности
     * @return настроенная цепочка фильтров безопасности
     * @throws Exception если возникают проблемы с настройкой безопасности
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/api/users/me").hasAnyRole(RoleName.ADMIN.name(), RoleName.USER.name())
                        .requestMatchers(HttpMethod.GET, "/api/users/logout").permitAll()
                        .requestMatchers("/api/users/**").hasRole(RoleName.ADMIN.name())
                        .anyRequest().authenticated())
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Создает провайдер аутентификации, который использует
     * {@link DaoAuthenticationProvider}. Провайдер настраивается
     * с использованием {@link UserDetailsServiceImpl} и
     * {@link PasswordEncoder}, что позволяет аутентифицировать
     * пользователей на основе их учетных данных.
     *
     * @return Настроенный {@link AuthenticationProvider}.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Настраивает менеджер аутентификации.
     * <p>
     * Этот метод предоставляет {@link AuthenticationManager}
     * из {@link AuthenticationConfiguration}, что позволяет
     * управлять процессом аутентификации в приложении.
     *
     * @param config Конфигурация аутентификации.
     * @return Настроенный {@link AuthenticationManager}.
     * @throws Exception Если не удается получить менеджер аутентификации.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Создание кодировщика пароля.
     *
     * @return кодировщик пароля
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}