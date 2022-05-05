package com.techelevator;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.tenmo.model.UserDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JdbcUserDaoTest extends TenmoDaoSut {
    private JdbcUserDao sut;
    private UserDTO testUser;
    private UserDTO testUser2;

    @Before
    public void setup() {
        sut = new JdbcUserDao(new JdbcTemplate(dataSource));
        testUser = new UserDTO();
        testUser.setUserId(1050);
        testUser.setUsername("John");
        testUser.setBalance(new BigDecimal("1000"));

        testUser2 = new UserDTO();
        testUser2.setUserId(1051);
        testUser2.setUsername("Dan");
        testUser2.setBalance(new BigDecimal("1000"));

    }

    private void assertUsersMatch(UserDTO expected, UserDTO actual) {
        Assert.assertEquals(expected.getUserId(), actual.getUserId());
        Assert.assertEquals(expected.getUsername(), actual.getUsername());
        Assert.assertEquals(expected.getPassword(), actual.getPassword());
    }

    @Test
    public void findIDByUsername_returns_correct_user() {
        int userID = sut.findIdByUsername("John");
        int userID2 = sut.findIdByUsername("Dan");
        Assert.assertEquals(1050, userID);
        Assert.assertEquals(1051, userID2);

    }

    @Test
    public void findAllUsersWithAccount_retrieves_correct_number_of_users() {
        List<UserDTO> users= sut.findAllUsersWithAccount("John");
        List<UserDTO> users2 = sut.findAllUsersWithAccount("Dan");
        Assert.assertEquals(1, users.size());
        Assert.assertEquals(1, users2.size());
    }

    @Test
    public void findByUsername_returns_correct_user_for_username() {
        UserDTO user = sut.findByUsername("John");
        UserDTO user2 = sut.findByUsername("Dan");
        assertUsersMatch(testUser, user);
        assertUsersMatch(testUser2, user2);
    }

    @Test
    public void getAccountBalance_returns_correct_balance() {
       BigDecimal balance = sut.getAccountBalance(1050);
       BigDecimal balance2 = sut.getAccountBalance(1051);
        Assert.assertEquals(new BigDecimal("1000"), balance);
        Assert.assertEquals(new BigDecimal("1000"), balance);
    }

    @Test
    public void findAccountByUsername_returns_correct_account() {
       UserDTO user = sut.findAccountByUsername("John");
       UserDTO user2 = sut.findAccountByUsername("Dan");
       Assert.assertEquals(testUser, user);
       Assert.assertEquals(testUser2, user);
    }
}
