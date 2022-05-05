package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;

import java.math.BigDecimal;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final static int PENDING_STATUS = 1;
    private final static int APPROVED_STATUS = 2;
    private final static int REJECTED_STATUS = 3;
    private final static int TYPE_SEND = 2;
    private final static int TYPE_REQUEST = 1;
    private AuthenticatedUser currentUser;
    private AccountService accountService = new AccountService(API_BASE_URL);
    private TransferService transferService = new TransferService(API_BASE_URL);


    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }

    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);

        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {

        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

    private void viewCurrentBalance() {
        setAuthToken();
        accountService.setCurrentUser(currentUser.getUser());
        System.out.println(accountService.getAccountBalance());
    }

    private void viewTransferHistory() {
        setAuthToken();
        User activeUser = setActiveUser();
        List<Transfer> transfers = List.of(transferService.userTransfers(activeUser));
        consoleService.printTransferListHeader();
        for (Transfer transfer : transfers) {
            if (transfer.getFromUserId() == activeUser.getId()) {
                List<User> users = List.of(accountService.getUsers());
                User receivingUser = users.stream().filter(user -> user.getId() == transfer.getToUserId()).findAny().orElse(null);
                System.out.println(transfer.sendPrint(receivingUser.getUsername()));
            } else if (transfer.getToUserId() == activeUser.getId()) {
                List<User> users = List.of(accountService.getUsers());
                User sendingUser = users.stream().filter(user -> user.getId() == transfer.getFromUserId()).findAny().orElse(null);
                System.out.println(transfer.receivePrint(sendingUser.getUsername()));
            }
        }
        boolean running = true;
        while (running) {
            int choice = consoleService.promptForInt("Please enter transfer ID to view details or (0) to cancel: ");
            if (choice == 0) {
                running = false;
            } else {
                Transfer chosenTransfer = transfers.stream().filter(transfer -> transfer.getTransferId() == choice).findAny().orElse(null);
                if (chosenTransfer != null) {
                    consoleService.printTransferDetails(chosenTransfer);
                    running = false;
                } else {
                    System.out.println("Please enter a Transfer ID from the list.");
                }
            }
        }

    }

    private void viewPendingRequests() {
        setAuthToken();
        User activeUser = setActiveUser();
        List<Transfer> transfers = List.of(transferService.userTransfers(activeUser));

        for (Transfer transfer : transfers) {
            if (transfer.getStatusId() == PENDING_STATUS && transfer.getToUserId() != activeUser.getId()) {
                System.out.println(transfer.sendPrint(transfer.getAccountToUsername()));
            }
        }
        int choice = consoleService.promptForInt("Please enter transfer ID to approve/reject (Enter 0 to cancel): ");
        Transfer chosenTransfer = transfers.stream().filter(transfer -> transfer.getTransferId() == choice).findAny().orElse(null);
        if (chosenTransfer != null) {
            consoleService.approveRejectOrPending();
            int option = consoleService.promptForInt("Please choose an option: ");
            switch (option) {
                case 1:
                    if (chosenTransfer.getAmount().compareTo(activeUser.getAccountBalance()) <= 0) {
                        chosenTransfer.setStatusId(APPROVED_STATUS);
                        transferService.updateTransfer(chosenTransfer);

                    } else {
                        System.out.println("Sorry, the amount requested is larger than your current balance.");

                    }
                    break;
                case 2:
                    chosenTransfer.setStatusId(REJECTED_STATUS);
                    transferService.updateTransfer(chosenTransfer);

                    break;
                case 0:
                    break;
            }
        }
    }


    private void sendBucks() {
        setAuthToken();
        User activeUser = setActiveUser();
        List<User> users = List.of(accountService.getUsers());
        int choice = chooseFromAvailableUsers();
        Transfer transfer = sendBucksTransfer(activeUser);
        User receivingUser = users.stream().filter(user -> user.getId() == choice).findAny().orElse(null);

        receivingUser(receivingUser, activeUser, transfer);
    }


    private void receivingUser(User receivingUser, User sendingUser, Transfer transfer) {
        if (receivingUser != null) {
            transfer.setToUserId(receivingUser.getId());
            boolean running = true;
            while (running) {
                transfer.setAmount(consoleService.promptForBigDecimal("Please enter in a dollar amount: "));
                if (sendingUser.getAccountBalance().compareTo(transfer.getAmount()) >= 0 && transfer.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                    transferService.sendBucks(transfer);
                    running = false;
                } else if (transfer.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("Please enter a valid number above zero.");
                } else {
                    System.out.println("Insufficient funds for entered amount\n");
                }
            }
        } else {
            System.out.println("Please enter a valid User ID");
            sendBucks();
        }
    }

    private Transfer sendBucksTransfer(User activeUser) {
        Transfer transfer = new Transfer();
        transfer.setTransferTypeId(TYPE_SEND);
        transfer.setStatusId(APPROVED_STATUS);
        transfer.setFromUserId(activeUser.getId());
        return transfer;
    }

    private Transfer requestBucksTransfer(User selectedUser) {
        Transfer transfer = new Transfer();
        transfer.setTransferTypeId(TYPE_REQUEST);
        transfer.setStatusId(PENDING_STATUS);
        transfer.setToUserId(setActiveUser().getId());
        transfer.setFromUserId(selectedUser.getId());
        boolean running = true;
        while (running) {
            BigDecimal amount = consoleService.promptForBigDecimal("Please enter in a dollar amount: ");
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("Please enter a dollar amount above $0.");
            } else {
                transfer.setAmount(amount);
                running = false;
            }
        }
        return transfer;
    }

    private void requestBucks() {
        setAuthToken();
        List<User> users = List.of(accountService.getUsers());
        int choice = chooseFromAvailableUsers();
        User selectedUser = users.stream().filter(user -> user.getId() == choice).findAny().orElse(null);
        if (selectedUser != null) {
            Transfer transfer = requestBucksTransfer(selectedUser);
            transferService.requestBucks(transfer);
        } else {
            System.out.println("Invalid UserId entered");
            requestBucks();
        }
    }

    public int chooseFromAvailableUsers() {
        return consoleService.userIdForSendRequestMoney(accountService.getUsers());
    }

    private void setAuthToken() {
        accountService.setAuthToken(currentUser);
        transferService.setAuthToken(currentUser);
    }

    private User setActiveUser() {
        return accountService.getUser(currentUser.getUser().getUsername());
    }
}
