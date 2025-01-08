package com.metro.routeplanner.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.metro.routeplanner.config.SpringUserDetails; // Import your custom UserDetails class
import com.metro.routeplanner.exception.NotFoundException;

@Component
public class UserExtractor {

    public static SpringUserDetails getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof SpringUserDetails) {
            return (SpringUserDetails) authentication.getPrincipal();
        } else {
            throw new NotFoundException("user", "User details not found in the authentication object.");
        }
    }

    // Helper method to directly get the userId
    public static String getUserId() {
        SpringUserDetails userDetails = getPrincipal();
        return userDetails.getUserId(); // Now you can access the userId
    }
}
