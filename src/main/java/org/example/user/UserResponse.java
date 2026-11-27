package org.example.user;
import java.util.UUID;
public record UserResponse(
        UUID userId,
        String name
) {
}
