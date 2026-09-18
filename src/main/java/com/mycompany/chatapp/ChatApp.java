/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.chatapp;

import javax.swing.JOptionPane;
/**
 *
 * @author Student
 */
public class ChatApp {

    public static void main(String[] args) {
        
       Login loginSystem = new Login();
        
        JOptionPane.showMessageDialog(null, "  Welcome to the Chat App Registration   ");
        
        String firstName = JOptionPane.showInputDialog("Enter First Name: ");
        
        String lastName = JOptionPane.showInputDialog("Enter Last Name: ");
        
        String username = JOptionPane.showInputDialog("Enter Username (must contain '_' and be <= 5 chars): ");

        String password = JOptionPane.showInputDialog("Enter Password (8+ chars, 1 Capital, 1 Number, 1 Special): ");
        
        String cellNumber = JOptionPane.showInputDialog("Enter Cell Phone Number (e.g., +27838968976): ");

        String registrationMessage = loginSystem.registerUser(username, password, cellNumber, firstName, lastName);
        JOptionPane.showMessageDialog(null, registrationMessage, "Registration Status", JOptionPane.INFORMATION_MESSAGE);
        
        if (registrationMessage.contains("successfully")) {
            
             JOptionPane.showMessageDialog(null,"  Login  ");
             
            String loginUser = JOptionPane.showInputDialog("Enter Username: ");

            String loginPass = JOptionPane.showInputDialog("Enter Password: ");

            boolean isAuthenticated = loginSystem.loginUser(loginUser, loginPass);
            
            String loginStatusMessage = loginSystem.returnLoginStatus(isAuthenticated);
            JOptionPane.showMessageDialog(null, loginStatusMessage, "Login Status", JOptionPane.INFORMATION_MESSAGE);
            
        } else {
            JOptionPane.showMessageDialog(null, "Registration failed. Please restart the application and try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}