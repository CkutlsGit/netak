package ru.netak.security.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import ru.netak.security.exception.model.SecurityErrorResponse;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthEntryPointHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException
    ) throws IOException, ServletException
    {
        log.error("Auth fail on EntryPoint {}", authException.getMessage());

        SecurityErrorResponse.send(
                response,
                HttpServletResponse.SC_UNAUTHORIZED,
                "Auth failed Token"
        );
    }
}
