package com.ctr.homestaybooking.config.filter

import com.ctr.homestaybooking.config.TokenProvider
import com.ctr.homestaybooking.shared.Constants
import io.jsonwebtoken.ExpiredJwtException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.annotation.Resource
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationFilter : OncePerRequestFilter() {
    @Resource
    private val userDetailsService: UserDetailsService? = null

    @Autowired
    private val jwtTokenUtil: TokenProvider? = null

    @Throws(IOException::class, ServletException::class)
    override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse, chain: FilterChain) {
        val header = req.getHeader(Constants.HEADER_STRING)
        var username: String? = null
        var authToken: String? = null
        if (header != null && header.startsWith(Constants.TOKEN_PREFIX)) {
            authToken = header.replace(Constants.TOKEN_PREFIX, "")
            try {
                username = jwtTokenUtil!!.getUsernameFromToken(authToken)
            } catch (e: IllegalArgumentException) {
                logger.error("An error occurred during getting username from token", e)
            } catch (e: ExpiredJwtException) {
                logger.warn("The token is expired and not valid anymore", e)
            }
        }
        if (username != null /*&& SecurityContextHolder.getContext().getAuthentication() == null*/) {
            val userDetails = userDetailsService!!.loadUserByUsername(username)
            if (userDetails != null) {
                if (jwtTokenUtil!!.validateToken(authToken, userDetails)) {
                    val authentication = jwtTokenUtil.getAuthentication(authToken, SecurityContextHolder.getContext().authentication, userDetails)
                    authentication.details = WebAuthenticationDetailsSource().buildDetails(req)
                    SecurityContextHolder.getContext().authentication = authentication
                }
            }
        }
        chain.doFilter(req, res)
    }
}
