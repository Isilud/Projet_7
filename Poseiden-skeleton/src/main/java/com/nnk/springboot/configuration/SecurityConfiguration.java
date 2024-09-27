package com.nnk.springboot.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.nnk.springboot.services.LoginService;

/**
 * Gére la configuration de la sécurité de Springboot.
 * 
 * Définit les stratégies d'authentification et d'autorisation, ainsi que les
 * formulaires
 * et la redirection lors du login et du logout.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

        private final LoginService loginService;

        /**
         * Constructeur de la configuration de sécurité.
         * 
         * @param loginService Le service de gestion de l'authentification.
         */
        public SecurityConfiguration(LoginService loginService) {
                this.loginService = loginService;
        }

        /**
         * Configure la chaîne de filtres de sécurité.
         * 
         * @param http L'objet HttpSecurity permettant de configurer les règles de
         *             sécurité.
         * @return SecurityFilterChain configuré.
         * @throws Exception En cas d'erreur de configuration.
         */
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                return http
                                .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(requests -> {
                                        requests.requestMatchers("/", "/login/oauth2/code/github", "/app/login",
                                                        "/app/error", "/css/*")
                                                        .permitAll();
                                        requests.requestMatchers("/user/**").hasRole("ADMIN");
                                        requests.anyRequest().authenticated();
                                })
                                .formLogin(form -> form
                                                .defaultSuccessUrl("/bidList/list", true)
                                                .permitAll())
                                .oauth2Login(auth -> auth
                                                .defaultSuccessUrl("/bidList/list", true)
                                                .permitAll())
                                .logout(logout -> logout.logoutUrl("/app-logout").logoutSuccessUrl(
                                                "/app/login").clearAuthentication(true)
                                                .permitAll())
                                .userDetailsService(loginService)
                                .build();
        }

        /**
         * Configure le gestionnaire d'authentification.
         * 
         * @param http                  L'objet HttpSecurity utilisé pour récupérer le
         *                              constructeur du gestionnaire d'authentification.
         * @param bCryptPasswordEncoder L'encodeur de mots de passe utilisé.
         * @return AuthenticationManager configuré.
         * @throws Exception En cas d'erreur de configuration.
         */
        @Bean
        public AuthenticationManager authenticationManager(HttpSecurity http,
                        BCryptPasswordEncoder bCryptPasswordEncoder)
                        throws Exception {
                AuthenticationManagerBuilder authenticationManagerBuilder = http
                                .getSharedObject(AuthenticationManagerBuilder.class);
                authenticationManagerBuilder.userDetailsService(loginService)
                                .passwordEncoder(bCryptPasswordEncoder);
                return authenticationManagerBuilder.build();
        }

        /**
         * Définit un bean pour l'encodeur de mots de passe BCrypt.
         * 
         * @return Une instance de BCryptPasswordEncoder.
         */
        @Bean
        public BCryptPasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }
}