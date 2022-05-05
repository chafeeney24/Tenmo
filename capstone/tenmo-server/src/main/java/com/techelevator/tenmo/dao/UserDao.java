package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.UserDTO;

import java.math.BigDecimal;
import java.util.List;

public interface UserDao {

//    List<User> findAll(String username);

    UserDTO findByUsername(String username);

    List<UserDTO> findAllUsersWithAccount(String username);

    UserDTO findAccountByUsername(String username);

    int findIdByUsername(String username);

    boolean create(String username, String password);

    BigDecimal getAccountBalance (Integer userId);
}
