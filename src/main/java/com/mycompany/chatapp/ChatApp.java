/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.chatapp;

import java.util.Scanner;
/**
 *
 * @author Student
 */
public class ChatApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login loginSystem = new Login();

        System.out.println("=== Welcome to the Chat App Registration ===");
        System.out.print("Enter First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter Username: ");
        String username = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        System.out.print("Enter Cell Phone Number: ");
        String cellNumber = scanner.nextLine();

        String reg = loginSystem.registerUser(username, password, cellNumber, firstName, lastName);
        System.out.println("\n--- Registration Status ---");
        System.out.println(reg);

        if (!reg.contains("successfully")) {
            System.out.println("Registration failed. Exiting.");
            scanner.close();
            return;
        }

        System.out.println("\n=== Login ===");
        System.out.print("Enter Username: ");
        String loginUser = scanner.nextLine();
        System.out.print("Enter Password: ");
        String loginPass = scanner.nextLine();

        boolean authenticated = loginSystem.loginUser(loginUser, loginPass);
        System.out.println("\n--- Login Status ---");
        System.out.println(loginSystem.returnLoginStatus(authenticated));

        if (!authenticated) { scanner.close(); return; }

        System.out.println("\nWelcome to QuickChat.");
        System.out.print("How many messages do you wish to enter? ");
        int numMessages;
        try {
            numMessages = Integer.parseInt(scanner.nextLine().trim());
            if (numMessages < 0) { System.out.println("Enter a valid number."); scanner.close(); return; }
        } catch (NumberFormatException e) { System.out.println("Invalid number."); scanner.close(); return; }

        boolean running = true;
        while (running) {
            System.out.println("\n===== QUICKCHAT MENU =====");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.println("4) Stored Messages");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    for (int i = 0; i < numMessages; i++) {
                        Message message = new Message();
                        message.generateMessageID();
                        System.out.println("\nMessage ID: " + message.getMessageID());
                        message.setMessageNumber(i);
                        message.setSender(firstName);

                        System.out.print("Enter recipient cell number: ");
                        String recipient = scanner.nextLine().trim();
                        String res = message.checkRecipientCell(recipient);
                        System.out.println(res);
                        if (!res.contains("successfully")) { i--; continue; }
                        message.setRecipient(recipient);

                        System.out.print("Enter message (max 250 characters): ");
                        String text = scanner.nextLine();
                        if (text.isEmpty() || text.length() > 250) {
                            System.out.println("Please enter a message between 1 and 250 characters.");
                            i--; continue;
                        }
                        message.setMessageText(text);
                        System.out.println("Message Hash: " + message.createMessageHash());

                        System.out.print("Choose: Send / Store / Disregard: ");
                        String option = scanner.nextLine().trim();
                        System.out.println(message.SentMessage(option));

                        System.out.println("\n--- Message Details ---");
                        System.out.println("Message ID: " + message.getMessageID());
                        System.out.println("Message Hash: " + message.getMessageHash());
                        System.out.println("Recipient: " + recipient);
                        System.out.println("Message: " + text);
                    }
                    System.out.println("\nTotal messages sent: " + new Message().returnTotalMessages());
                    break;

                case "2":
                    System.out.println("\n--- Sent Messages ---");
                    System.out.println(new Message().printMessages());
                    break;

                case "3":
                    running = false;
                    System.out.println("Goodbye!");
                    break;

                case "4":
                    storedMessagesMenu(scanner);
                    break;

                default:
                    System.out.println("Invalid option. Choose 1-4.");
            }
        }
        scanner.close();
    }

    public static void storedMessagesMenu(Scanner scanner) {
        boolean open = true;
        while (open) {
            System.out.println("\n===== STORED MESSAGES =====");
            System.out.println("1) Display senders and recipients");
            System.out.println("2) Display longest message");
            System.out.println("3) Search message using Message ID");
            System.out.println("4) Search messages by recipient");
            System.out.println("5) Delete message using Message Hash");
            System.out.println("6) Display message report");
            System.out.println("7) Return to main menu");
            System.out.print("Choose an option: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1": System.out.println(Message.displayStoredSendersAndRecipients()); break;
                case "2": Message.loadStoredMessagesFromFile(); System.out.println(Message.getLongestMessage()); break;
                case "3":
                    System.out.print("Enter Message ID: ");
                    System.out.println(Message.searchMessageByID(scanner.nextLine().trim()));
                    break;
                case "4":
                    System.out.print("Enter recipient: ");
                    System.out.println(Message.searchMessagesByRecipient(scanner.nextLine().trim()));
                    break;
                case "5":
                    System.out.print("Enter Message Hash: ");
                    System.out.println(Message.deleteMessageByHash(scanner.nextLine().trim()));
                    break;
                case "6": System.out.println(Message.displayReport()); break;
                case "7": open = false; System.out.println("Returning to main menu."); break;
                default: System.out.println("Invalid option.");
            }
        }
    }
}