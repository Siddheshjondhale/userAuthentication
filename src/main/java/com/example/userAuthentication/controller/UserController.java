package com.example.userAuthentication.controller;

import com.example.userAuthentication.entity.User;
import com.example.userAuthentication.service.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/google-signin")
    public ResponseEntity<String> googleSignIn(@RequestBody Map<String, String> tokenRequest) {
        String idTokenString = tokenRequest.get("idToken");

        try {
            // Retrieve the Google Client ID from environment variables
            String clientId = System.getenv("GOOGLE_CLIENT_ID");

            // If the client ID is not set throw an exception
            if (clientId == null || clientId.isEmpty()) {
                throw new RuntimeException("Google Client ID is not set.");
            }

            // Use the GSON factory to verify the token
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(clientId))
                    .build();    // verfier is created from the clientid given by the firebase to verfiy the token got while signin is valid or not so we need verfier

            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();
                String userId = payload.getSubject(); // Use this to identify the user
                String email = payload.getEmail(); // Get user's email
                String name = (String) payload.get("name");

                // Process user information (e.g., save to DB or create session)
                System.out.println("Signed in successfully, user: " + email);

                User user = userService.registerOrUpdateUser(email, userId, name);
                return ResponseEntity.ok(String.valueOf(user));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid ID Token");
            }
        } catch (Exception e) {
            System.err.println("Exception during verification: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Verification failed: " + e.getMessage());
        }
    }
}
