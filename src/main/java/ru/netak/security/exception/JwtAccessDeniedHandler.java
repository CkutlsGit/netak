package ru.netak.security.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import ru.netak.security.exception.model.SecurityErrorResponse;

import java.io.IOException;

@Component
@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException, ServletException
    {
        log.error("Access failed");

        SecurityErrorResponse.send(
                response,
                HttpServletResponse.SC_FORBIDDEN,
                "Access failed Token"
        );
    }
}
