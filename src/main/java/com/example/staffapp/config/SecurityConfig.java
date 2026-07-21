package com.example.staffapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. CSRF保護を一時的に無効化（PostmanなどのAPIテストで403エラーになるのを防ぐため）
            .csrf(csrf -> csrf.disable())

            // 2. URLごとのアクセス権限設定
            .authorizeHttpRequests(authorize -> authorize
                // /api/ から始まるすべてのパス（スタッフCRUD・シフトAPIを含む）は認証なしでアクセス可能にする
                .requestMatchers("/api/**").permitAll()
                // H2データベースの管理画面（コンソール）も許可する
                .requestMatchers("/h2-console/**").permitAll()
                // 静的ファイル（画面）も許可する
                .requestMatchers(
                    "/",
                    "/*.html",
                    "/*.js",
                    "/*.css",
                    "/admin/**",
                    "/user/**"
                ).permitAll()
                // それ以外のURL（もしあれば）は通常通りログイン認証を求める
                .anyRequest().authenticated()
            )

            // 3. H2データベースのコンソール画面をブラウザで正常に表示するための設定
            .headers(headers -> headers
                .addHeaderWriter(new XFrameOptionsHeaderWriter(
                    XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();

    }
}
