package com.techelevator;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.techelevator.tenmo.model.Authority;
import com.techelevator.tenmo.model.UserDTO;

public class UserDTOTest {
    UserDTO user = new UserDTO();

    @Test
    public void userId_equal_to_1005() {
        int id = 1005;
        user.setUserId(id);
        assertEquals(user.getUserId(), id);
    }

    @Test
    public void username_equals_Chuck() {
        user.setUsername("Chuck");
        assertEquals(user.getUsername(), "Chuck");
    }

    @Test
    public void password_equals_password() {
        user.setPassword("Password");
        assertEquals(user.getPassword(), "Password");
    }

    @Test
    public void isActivated_equals_true() {
        user.setActivated(true);
        assertEquals(user.isActivated(), true);
    }

    @Test
    public void setAuthorities_equals_authority() {
        Set<Authority> authority = new HashSet<Authority>();
        Set<Authority> authority2 = new HashSet<Authority>();
        user.setAuthorities(authority);
        assertEquals(user.getAuthorities(), authority2);

    }
}
