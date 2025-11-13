package com.bajorski.billingapp.config;

import lombok.Getter;

@Getter
public enum Role {

    ADMIN("ADMIN", "ROLE_ADMIN"),
    USER("USER", "ROLE_USER");

    private final String role;
    private final String authority;

    Role(String role, String authority) {
        this.role = role;
        this.authority = authority;
    }

    public static String getAuthorityByRole(String role) {
        for (Role r : Role.values()) {
            if (r.getRole().equalsIgnoreCase(role)) {
                return r.getAuthority();
            }
        }
        throw new IllegalArgumentException("No authority found for role: " + role);
    }
}
