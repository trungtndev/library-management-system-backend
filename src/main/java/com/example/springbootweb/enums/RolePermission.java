package com.example.springbootweb.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum RolePermission {
    // user
    BORROW_BOOK("BORROW_BOOK"),
    RETURN_BOOK("RETURN_BOOK"),
    VIEW_LOAN_HISTORY("VIEW_LOAN_HISTORY"),

    WRITE_REVIEW("WRITE_REVIEW"),
    MAKE_PAYMENT("MAKE_PAYMENT"),

    // librarian
    MANAGE_BOOKS("MANAGE_BOOKS"),
    MANAGE_USERS("MANAGE_USERS"),
    MANAGE_CATEGORIES("MANAGE_CATEGORIES"),
    MANAGE_REVIEWS("MANAGE_REVIEWS"),
    MANAGE_PAYMENTS("MANAGE_PAYMENTS"),

    // Admin-only permissions
    MANAGE_ROLES("MANAGE_ROLES"),
    MANAGE_PERMISSIONS("MANAGE_PERMISSIONS"),
    //    VIEW_SYSTEM_LOGS("VIEW_SYSTEM_LOGS"),
//    SYSTEM_CONFIGURATION("SYSTEM_CONFIGURATION"),
    FULL_ACCESS("FULL_ACCESS");

    String permission;
}
