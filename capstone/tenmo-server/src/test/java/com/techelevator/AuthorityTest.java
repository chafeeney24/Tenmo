package com.techelevator;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;


import com.techelevator.tenmo.model.Authority;

public class AuthorityTest {
    Authority authority = new Authority("User");

    @Test
    public void authority_name_equals_user() {
        authority.setName("User");
        assertEquals(authority.getName(), "User");
    }

    @Test
    public void authority_name_equals_admin() {
        authority.setName("Admin");
        assertEquals(authority.getName(), "Admin");
    }
}
