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

        System.out.print("Enter Username (must contain '_' and be <= 5 chars): ");
        String username = scanner.nextLine();

        System.out.print("Enter Password (8+ chars, 1 Capital, 1 Number, 1 Special): ");
        String password = scanner.nextLine();

        System.out.print("Enter Cell Phone Number (e.g., +27838968976): ");
        String cellNumber = scanner.nextLine();

        String registrationMessage = loginSystem.registerUser(username, password, cellNumber, firstName, lastName);
        System.out.println("\n--- Registration Status ---");
        System.out.println(registrationMessage);

        if (registrationMessage.contains("successfully")) {
            System.out.println("\n=== Login ===");
            System.out.print("Enter Username: ");
            String loginUser = scanner.nextLine();

            System.out.print("Enter Password: ");
            String loginPass = scanner.nextLine();

            boolean isAuthenticated = loginSystem.loginUser(loginUser, loginPass);
            String loginStatusMessage = loginSystem.returnLoginStatus(isAuthenticated);
            System.out.println("\n--- Login Status ---");
            System.out.println(loginStatusMessage);

            // --- PART 2: If login is successful, start the QuickChat Menu ---
            if (isAuthenticated) {
                runChatMenu(scanner);
            }

        } else {
            System.out.println("\nRegistration failed. Please restart the application and try again.");
        }

        scanner.close();
    }

    // --- PART 2: The Menu Loop ---
    private static void runChatMenu(Scanner scanner) {
        System.out.println("\nWelcome to QuickChat.");
        boolean running = true;

        while (running) {
            System.out.println("\n1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Choose an option: ");
            
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("How many messages do you wish to enter? ");
                    int numMessages = Integer.parseInt(scanner.nextLine());

                    for (int i = 1; i <= numMessages; i++) {
                        System.out.println("\n--- Message " + i + " ---");
                        System.out.print("Enter recipient number: ");
                        String recipient = scanner.nextLine();

                        System.out.print("Enter message: ");
                        String text = scanner.nextLine();

                        // Create the message object
                        Message msg = new Message(i, recipient, text);

                        // Display checks
                        System.out.println("\nMessage Length Check: " + msg.checkMessageLength());
                        System.out.println("Recipient Check: " + msg.checkRecipientCell());
                        System.out.println("Message Hash: " + msg.getMessageHash());

                        // Only ask to send if the length is valid
                        if (msg.checkMessageLength().equals("Message ready to send.")) {
                            System.out.println(msg.SentMessage());
                            
                            // Display full details after sending
                            System.out.println("\n--- Message Details ---");
                            System.out.println("Message ID: " + msg.getMessageID());
                            System.out.println("Message Hash: " + msg.getMessageHash());
                            System.out.println("Recipient: " + msg.getRecipient());
                            System.out.println("Message: " + msg.getMessageText());
                        }
                    }
                    System.out.println("\nTotal messages sent: " + new Message().returnTotalMessages());
                    break;

                case "2":
                    System.out.println("\nComing Soon.");
                    break;

                case "3":
                    System.out.println("\nExiting Chat App. Goodbye!");
                    running = false;
                    break;

                default:
                    System.out.println("Invalid option. Try again.");
                    
            }
        }
    }
}
