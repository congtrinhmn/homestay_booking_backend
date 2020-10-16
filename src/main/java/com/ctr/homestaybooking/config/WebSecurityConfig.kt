package com.ctr.homestaybooking.config

import com.ctr.homestaybooking.config.filter.JwtAuthenticationFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter(), WebMvcConfigurer {
    @Autowired
    private val userDetailsService: UserDetailsService? = null

    @Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Autowired
    @Throws(Exception::class)
    fun globalUserDetails(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(encoder())
    }

    @Bean
    fun authenticationTokenFilterBean(): JwtAuthenticationFilter {
        return JwtAuthenticationFilter()
    }

    @Bean
    fun encoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers("/**").permitAll()
                .anyRequest()
                .authenticated()
        http
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter::class.java)
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH").allowedOrigins("*")
                .allowedHeaders("*")
    }
}
