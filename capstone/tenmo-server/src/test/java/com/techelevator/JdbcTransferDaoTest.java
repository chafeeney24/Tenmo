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
import java.util.List;

public class JdbcTransferDaoTest extends TenmoDaoSut {
    private JdbcTransferDao sut;
    private JdbcUserDao sut2;
    private TransferDTO testTransfer;
    private TransferDTO testTransfer2;
    private TransferDTO testTransfer3;
    private TransferDTO testTransfer4;
    private UserDTO testUser;
    private UserDTO testUser2;
    private UserDTO testUser3;


    @Before
    public void setup() {
        sut = new JdbcTransferDao(new JdbcTemplate(dataSource));
        testTransfer.setTransferId(3050);
        testTransfer.setTransferTypeId(2);
        testTransfer.setStatusId(2);
        testTransfer.setFromUserId(2050);
        testTransfer.setToUserId(2051);
        testTransfer.setAmount(new BigDecimal("200"));

        testTransfer2.setTransferId(3051);
        testTransfer2.setTransferTypeId(2);
        testTransfer2.setStatusId(2);
        testTransfer2.setFromUserId(2051);
        testTransfer2.setToUserId(2050);
        testTransfer2.setAmount(new BigDecimal("100"));

        testTransfer3.setTransferTypeId(2);
        testTransfer3.setStatusId(2);
        testTransfer3.setFromUserId(2051);
        testTransfer3.setToUserId(2050);
        testTransfer3.setAmount(new BigDecimal("100"));

        testTransfer3.setTransferTypeId(2);
        testTransfer3.setStatusId(2);
        testTransfer3.setFromUserId(2051);
        testTransfer3.setToUserId(2050);
        testTransfer3.setAmount(new BigDecimal("100"));

        testUser = new UserDTO();
        testUser.setUserId(1050);
        testUser.setUsername("John");
        testUser.setBalance(new BigDecimal("1000"));

        testUser2 = new UserDTO();
        testUser2.setUserId(1051);
        testUser2.setUsername("Dan");
        testUser2.setBalance(new BigDecimal("1000"));
    }

    @Test
    public void userTransfers_returns_user_transfers(){
        List<TransferDTO> transfers = sut.userTransfers(testUser);
        List<TransferDTO> transfers2 = sut.userTransfers(testUser2);
        Assert.assertEquals(1, transfers.size());
        Assert.assertEquals(1, transfers.size());
    }

    @Test
    public void sendRequestBucks_returns_new_transfer_id(){
        int newId = sut.sendRequestBucks(testTransfer3);
        int newId2 = sut.sendRequestBucks(testTransfer4);
        Assert.assertEquals(3001, newId);
        Assert.assertEquals(3002, newId);
    }

    @Test
    public void updateSenderAccountBalance_updates_correct_balance(){
        sut.updateSenderAccountBalance(3050, testTransfer);
        sut.updateSenderAccountBalance(3051, testTransfer2);
        BigDecimal accountBalance = sut2.getAccountBalance(1050);
        BigDecimal accountBalance2 = sut2.getAccountBalance(1051);
        Assert.assertEquals(accountBalance, new BigDecimal("800"));
        Assert.assertEquals(accountBalance, new BigDecimal("1100"));
    }

    @Test
    public void updateReceiverAccountBalance_updates_correct_balance(){
        sut.updateSenderAccountBalance(3050, testTransfer);
        sut.updateSenderAccountBalance(3051, testTransfer2);
        BigDecimal accountBalance = sut2.getAccountBalance(1051);
        BigDecimal accountBalance2 = sut2.getAccountBalance(1050);
        Assert.assertEquals(accountBalance, new BigDecimal("1200"));
        Assert.assertEquals(accountBalance, new BigDecimal("900"));
    }

    @Test
    public void updateTransfer_updates_transfer() {
        testTransfer.setTransferId(3);
        sut.updateTransfer(testTransfer);
        int transferID = testTransfer.getTransferId();
        Assert.assertEquals(3, transferID);
    }
}
