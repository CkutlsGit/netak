package ru.netak.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.netak.security.service.CustomUserDetailService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailService userDetailService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException
    {
        String token = getTokenFromRequest(request);

        if (token != null && jwtService.checkValidateToken(token)) {
            addInSecurityContext(token);
        }

        doFilter(request, response, filterChain);
    }

    private void addInSecurityContext(String token) {
        String email = jwtService.getEmailFromToken(token);
        UserDetails user = userDetailService.loadUserByUsername(email);

        if (jwtService.isAccessTokenValid(token, user)) {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (token != null && token.startsWith("Bearer ")) {
            return token.substring(7);
        }

        return null;
    }
}
