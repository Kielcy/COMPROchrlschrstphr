package com.COFFEE.COFFEE_FINALS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }
    
    @PostMapping("/login")
    public String login(@RequestParam String username, 
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {
        
        logger.info("Login attempt for user: {}", username);
        
        User authenticatedUser = userService.authenticateUser(username, password);
        
        if (authenticatedUser != null) {
            // Store user in session
            session.setAttribute("user", authenticatedUser);
            logger.info("Login successful for user: {}", username);
            
            // Redirect to homepage after login
            return "redirect:/";
        }
        
        // If authentication fails, add error message and go back to login page
        model.addAttribute("error", "Invalid username or password");
        logger.warn("Login failed for user: {}", username);
        return "login";
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        logger.info("Logout request received");
        
        // Invalidate the session
        session.invalidate();
        
        // Redirect to login page
        return "redirect:/login";
    }
} 