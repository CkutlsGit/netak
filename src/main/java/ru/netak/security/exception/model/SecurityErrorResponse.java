package ru.netak.security.exception.model;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class SecurityErrorResponse {
    public static void send(
            HttpServletResponse response,
            int httpStatus,
            String message
            ) throws IOException
    {
        response.setStatus(httpStatus);
        response.getWriter().write(message);
    }
}
