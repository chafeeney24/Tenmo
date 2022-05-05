package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.tenmo.model.UserDTO;

import java.util.List;

public interface TransferDao {

    int sendRequestBucks(TransferDTO transfer);

    void updateSenderAccountBalance(int transferId, TransferDTO transfer);

    void updateReceiverAccountBalance(int transferId, TransferDTO transfer);

    void updateTransfer (TransferDTO transfer);

    List<TransferDTO> userTransfers(UserDTO user);



   // public Transfer getTransfer(int transferId);

}
