package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.tenmo.model.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {
    private TransferDao transferDao;
    private UserDao userDao;
    private final static int PENDING_STATUS = 1;
    private final static int APPROVED_STATUS = 2;
    private final static int REJECTED_STATUS = 3;
    private final static int TYPE_SEND = 2;
    private final static int TYPE_REQUEST = 1;

    public TransferController(UserDao userDao, TransferDao transferDao) {
        this.userDao = userDao;
        this.transferDao = transferDao;
    }

    @ApiOperation("Creates an approved transfer and then updates account balances of the sender and receiver of TE Bucks.")
    @RequestMapping(path = "/send/", method = RequestMethod.POST)
    public void sendBucks(@RequestBody @ApiParam("Transfer info for the sent transfer") TransferDTO transfer) {
        int transferId = transferDao.sendRequestBucks(transfer);
        transferDao.updateSenderAccountBalance(transferId, transfer);
        transferDao.updateReceiverAccountBalance(transferId, transfer);
    }
    @ApiOperation("Returns a list of transfers the signed in user has on record")
    @RequestMapping(path = "/transfer/history/", method = RequestMethod.GET)
    public List<TransferDTO> userTransfers(@ApiParam("Signed in user") Principal principal) {
        String username = principal.getName();
        UserDTO user = userDao.findAccountByUsername(username);
        return transferDao.userTransfers(user);
    }
    @ApiOperation("Creates a transfer Request with signed in user as the recipient ")
    @RequestMapping(path = "/request/", method = RequestMethod.POST)
    public void requestBucks(@RequestBody @ApiParam("Transfer info with involved users and amount to be approved") TransferDTO transfer) {
        transferDao.sendRequestBucks(transfer);
    }
    @ApiOperation("Updates the transfers for a request transfer that has been approved.")
    @RequestMapping(path = "/transfer/", method = RequestMethod.PUT)
    public void updateTransfer(@RequestBody @ApiParam("Balance change and status change") TransferDTO transfer) {
        transferDao.updateTransfer(transfer);
        UserDTO payingUser = userDao.findAccountByUsername(transfer.getAccountFromUsername());
        if (transfer.getStatusId() == APPROVED_STATUS && transfer.getAmount().compareTo(payingUser.getBalance()) <= 0) {
            transferDao.updateSenderAccountBalance(transfer.getTransferId(), transfer);
            transferDao.updateReceiverAccountBalance(transfer.getTransferId(), transfer);
        } else {
            transfer.setStatusId(PENDING_STATUS);
        }
    }


}
