package com.bhoomika.ExpenseTrackerApp.util;

import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

public class KeyGenerator {
    public static void main(String[] args) {
        // Generate a secure key for HS512
        SecretKey key = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS512);
        
        // Print the Base64-encoded key (this is what you'll use in application.properties)
        String base64Key = java.util.Base64.getEncoder().encodeToString(key.getEncoded());
        System.out.println("✅ Use this key in application.properties:");
        System.out.println(base64Key);
    }
}

