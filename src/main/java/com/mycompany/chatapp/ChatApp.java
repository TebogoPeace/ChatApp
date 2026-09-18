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
            
        } else {
            System.out.println("\nRegistration failed. Please restart the application and try again.");
        }

        scanner.close();
    }
}
        