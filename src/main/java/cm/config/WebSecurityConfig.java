package cm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        /*http
                .addFilter(new JwtFilter(authenticationManager()))
                .authorizeRequests()
                .antMatchers("/", "/cm/login").permitAll()
                .anyRequest().authenticated()
                .antMatchers("/teacher/**").hasRole("TEACHER")
                .antMatchers("/student/**").hasRole("STUDENT")
                .antMatchers("/admin/**").hasRole("ADMIN");
        http
                .formLogin()
                .loginPage("/cm/login")
                .loginProcessingUrl("/cm/login")
                .usernameParameter("account")
                .passwordParameter("password")
                .successHandler(new LoginSuccessHandler())
                .failureUrl("/adminLoginError")
                .permitAll();
        http
                .logout()
                .logoutSuccessUrl("/cm/login")
                .permitAll();
        http
                .rememberMe()
                .rememberMeParameter("cm_remember");*/

        http
                .authorizeRequests()
                .antMatchers("/","/login","/login/submit").permitAll()
                .antMatchers("/static/**").permitAll()
                .antMatchers("/cm/**").permitAll()
                .antMatchers("/cm/pc/admin/**").hasRole("admin")
                .antMatchers("/cm/teacher/**").hasRole("teacher")
                .antMatchers("/cm/student/**").hasRole("student")
                .anyRequest()
                .authenticated()

                .and()
                .cors()
                .and()
                .csrf().disable();

    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER");
    }

    @Override
    public void configure(WebSecurity web) throws Exception{
        web
                .ignoring()
                .antMatchers("/css/*.css","/js/*.js");
    }
}
