package com.COFFEE.COFFEE_FINALS;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final Map<String, User> users = new HashMap<>();

    public UserService() {
        loadUsersFromCSV();
    }

    private void loadUsersFromCSV() {
        try {
            ClassPathResource resource = new ClassPathResource("users.csv");
            logger.info("Loading users from CSV file");
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            
            // Skip header line
            reader.readLine();
            
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    User user = new User(parts[0], parts[1], parts[2]);
                    users.put(user.getUsername(), user);
                    logger.info("Loaded user: {}", user.getUsername());
                }
            }
            reader.close();
            logger.info("Total users loaded: {}", users.size());
        } catch (Exception e) {
            logger.error("Error loading users from CSV: ", e);
        }
    }

    public User authenticateUser(String username, String password) {
        logger.info("Attempting to authenticate user: {}", username);
        
        if (username == null || password == null) {
            logger.warn("Username or password is null");
            return null;
        }

        User user = users.get(username);
        if (user == null) {
            logger.warn("User not found: {}", username);
            return null;
        }

        if (user.getPassword().equals(password)) {
            logger.info("Authentication successful for user: {}", username);
            return new User(user.getUsername(), "", user.getRole());
        }

        logger.warn("Invalid password for user: {}", username);
        return null;
    }
} 