package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;

public class AccountService {

    private String baseUrl;
    private RestTemplate restTemplate = new RestTemplate();
    private String authToken;
    private User currentUser;

    public AccountService(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setAuthToken(AuthenticatedUser currentUser) {
        this.authToken = currentUser.getToken();
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }


    public User[] getUsers() {
        User[] users = null;
        try {
            ResponseEntity<User[]> response = restTemplate.exchange(baseUrl + "user/allusers/", HttpMethod.GET,makeAuthEntity(), User[].class);
            users = response.getBody();
        }  catch (RestClientResponseException e) {
            BasicLogger.log(e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return users;
    }

    public User getUser(String userName) {
        User user = null;
         try {
            ResponseEntity<User> response =
                    restTemplate.exchange(baseUrl + "user/account/", HttpMethod.GET, makeAuthEntity(), User.class);
            user = response.getBody();
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return user;
    }


    public BigDecimal getAccountBalance() {
        BigDecimal accountBalance = null;
        try {
            ResponseEntity<BigDecimal> response =
                    restTemplate.exchange(baseUrl + "account/", HttpMethod.GET, makeAuthEntity(), BigDecimal.class);
            accountBalance = response.getBody();
        } catch (RestClientResponseException e) {
            BasicLogger.log(e.getStatusText());
        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        return accountBalance;
    }


    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }


}
