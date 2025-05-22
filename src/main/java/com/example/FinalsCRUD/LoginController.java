package com.example.FinalsCRUD;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controller handling login routes
 */
@Controller
public class LoginController {

    /**
     * Handles GET requests to the login page.
     *
     * @param error Optional error parameter if authentication failed
     * @param logout Optional logout parameter if user logged out
     * @param model The model for the view
     * @return The login view template
     */
    @GetMapping("/login")
    public String login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {
        
        if (error != null) {
            model.addAttribute("loginError", "Invalid username or password");
        }
        
        if (logout != null) {
            model.addAttribute("logoutMessage", "You have been logged out successfully");
        }
        
        return "login";
    }
    
    /**
     * Simple endpoint to check authentication status.
     * This is helpful for troubleshooting login issues.
     * 
     * @return A string indicating authentication status
     */
    @GetMapping("/auth-status")
    @ResponseBody
    public String authStatus() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return "Authentication: " + auth + 
                "<br>Authenticated: " + auth.isAuthenticated() + 
                "<br>Principal: " + auth.getPrincipal() + 
                "<br>Authorities: " + auth.getAuthorities();
    }
} 