package bg.softuni.childrenkitchen.config;

import bg.softuni.childrenkitchen.model.entity.enums.UserRoleEnum;
import bg.softuni.childrenkitchen.repository.UserRepository;
import bg.softuni.childrenkitchen.service.impl.UserDetailService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers("/admin",  "/admin/**", "/api/admin/**").hasRole(UserRoleEnum.ADMIN.name())
                .requestMatchers("/", "/users/login", "/users/register", "/about-us", "/contacts",
                        "/healthy-food", "/menus", "/api/points", "/thanks-feedback", "users/login-error").permitAll()
                .anyRequest().authenticated()

        ).formLogin(loginConfigurer -> {loginConfigurer
                        .loginPage("/users/login")
                        .defaultSuccessUrl("/users/profile", true)
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .failureForwardUrl("/users/login-error");
            })
            .logout(logoutConfigurer -> {
                logoutConfigurer
                        .logoutRequestMatcher(new AntPathRequestMatcher("/users/logout"))
                        .deleteCookies("JSESSIONID")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .logoutSuccessUrl("/");
            })
            .csrf(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Primary
    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository){
        return new UserDetailService(userRepository);
    }


}
