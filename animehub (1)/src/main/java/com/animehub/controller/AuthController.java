package com.animehub.controller;

import com.animehub.service.UserService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("form", new RegisterForm());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute("form") RegisterForm form, Model model) {
        // Manual validation here to keep the example simple; swap for
        // @Valid + BindingResult if you want a more structured approach.
        if (form.username == null || form.username.trim().length() < 3) {
            model.addAttribute("error", "Username must be at least 3 characters.");
            return "register";
        }
        if (form.password == null || form.password.length() < 6) {
            model.addAttribute("error", "Password must be at least 6 characters.");
            return "register";
        }
        if (!form.password.equals(form.confirmPassword)) {
            model.addAttribute("error", "Password and confirm password do not match.");
            return "register";
        }

        try {
            userService.registerUser(form.username.trim(), form.email.trim(), form.password);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }

        return "redirect:/login?registered=true";
    }

    /** Simple form-backing object for the registration form. */
    public static class RegisterForm {
        @NotBlank private String username;
        @Email private String email;
        @Size(min = 6) private String password;
        private String confirmPassword;

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getConfirmPassword() { return confirmPassword; }
        public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
    }
}
