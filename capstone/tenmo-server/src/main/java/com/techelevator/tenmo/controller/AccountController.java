package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class AccountController {

private UserDao userDao;

public AccountController (UserDao userDao){
    this.userDao = userDao;
}
    @ApiOperation("Retrieves the account balance of the signed in User")
    @RequestMapping (path = "/account/", method = RequestMethod.GET)
    public BigDecimal getBalance (Principal principal) {
        int id = userDao.findIdByUsername(principal.getName());
        return userDao.getAccountBalance(id);
    }
    @ApiOperation("Returns the selected user for authentication")
    @RequestMapping (path = "/user/", method = RequestMethod.GET)
    public UserDTO getUser(Principal principal){
        return userDao.findByUsername(principal.getName());
    }

    @ApiOperation("Returns the selected user with account information")
    @RequestMapping (path = "/user/account/", method = RequestMethod.GET)
    public UserDTO getUserAccount(Principal principal){
        return userDao.findAccountByUsername(principal.getName());
    }

    @ApiOperation("Returns available users for transfer selection")
    @RequestMapping (path = "/user/allusers/", method = RequestMethod.GET)
    public List<UserDTO> getUsers ( Principal principal) {
        return userDao.findAllUsersWithAccount(principal.getName());
    }
}
