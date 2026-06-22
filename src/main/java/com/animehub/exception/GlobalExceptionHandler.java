package com.animehub.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(JikanApiException.class)
    public String handleJikanApiException(JikanApiException ex, Model model) {
        log.warn("Jikan API error: {}", ex.getMessage());
        model.addAttribute("errorMessage",
                "Hindi available ang anime data ngayon. Pakisubukan ulit sa ilang segundo.");
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        log.error("Unexpected error", ex);
        model.addAttribute("errorMessage",
                "May nagkamali sa server. Pakisubukan ulit mamaya.");
        return "error";
    }
}
