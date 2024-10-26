package com.example.ratelimitingpoc.filter;

import com.example.ratelimitingpoc.UrlPatternMatcher;
import com.example.ratelimitingpoc.exception.TooManyRequests;
import com.example.ratelimitingpoc.service.JwtService;
import com.example.ratelimitingpoc.service.RateLimitService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    JwtService jwtService;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    HandlerExceptionResolver handlerExceptionResolver;

    @Autowired
    @Lazy
    private RateLimitService rateLimitService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // 1. Get the Authorization header
            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            // 2. Read Jwt token from header
            final String jwt = authHeader.substring(7);
            // 3. Extract Subject(aka username) from Jwt token
            final String userEmail = jwtService.extractUserName(jwt);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (userEmail != null && authentication == null) {
                // 4. Match the token's username with the username set in the security context and look for expiry date
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // 5. If the token is valid, set it in the security context
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                    // Check if the user is allowed to call the API
                    if(UrlPatternMatcher.matches(request.getRequestURI())){
                        boolean isAllowed = rateLimitService.tryConsume(userDetails.getUsername());
                        if(!isAllowed) {
                           throw new TooManyRequests("Too Many Requests");
                        }
                    }
                }
                filterChain.doFilter(request, response);
            }
        }catch(BadCredentialsException | AccessDeniedException | ExpiredJwtException | SignatureException |
               MalformedJwtException e ){
            handlerExceptionResolver.resolveException(request, response, null, e);
        }catch (Exception e){
            handlerExceptionResolver.resolveException(request, response, null, e);
        }

    }

    private void sendErrorResponse(HttpServletResponse response, HttpStatus status, String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}
