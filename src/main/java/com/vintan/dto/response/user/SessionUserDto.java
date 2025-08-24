package com.vintan.dto.response.user;

import com.vintan.domain.User;
import lombok.Getter;

import java.io.Serializable;

/**
 * DTO for storing minimal user information in the session.
 * Implements Serializable to allow storage in HttpSession.
 */
@Getter
public class SessionUserDto implements Serializable {

    private final String id;             // User ID
    private final String name;           // User name
    private final String email;          // User email
    private final int businessNumber;    // Business number (0 if not a business user)
    private final int point;             // User points

    /**
     * Constructor that maps a User entity to session DTO.
     * @param user the User entity
     */
    public SessionUserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.businessNumber = user.getBusinessNumber();
        this.point = user.getPoint();
    }
}
