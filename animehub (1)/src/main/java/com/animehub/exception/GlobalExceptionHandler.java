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
                "Anime data isn't available right now. Please try again in a few seconds.");
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception ex, Model model) {
        log.error("Unexpected error", ex);
        model.addAttribute("errorMessage",
                "Something went wrong on our end. Please try again later.");
        return "error";
    }
}
