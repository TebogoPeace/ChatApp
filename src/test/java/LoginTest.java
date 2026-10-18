/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

package com.mycompany.chatapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {
    
    Login login = new Login();
    
   @Test
    public void testUsernameCorrectlyFormatted() {
        boolean result = login.checkUserName("kyl_1");
        assertTrue(result);
    }

    @Test
    public void testUsernameIncorrectlyFormatted() {
        boolean result = login.checkUserName("kyle!!!!!!!");
        assertFalse(result);
    }

    @Test
    public void testPasswordMeetsComplexity() {
        boolean result = login.checkPasswordComplexity("Ch&sec@ke99!");
        assertTrue(result);
    }

    @Test
    public void testPasswordDoesNotMeetComplexity() {
        boolean result = login.checkPasswordComplexity("password");
        assertFalse(result);
    }

    @Test
    public void testCellPhoneCorrectlyFormatted() {
        boolean result = login.checkCellPhoneNumber("+27838968976");
        assertTrue(result);
    }

    @Test
    public void testCellPhoneIncorrectlyFormatted() {
        boolean result = login.checkCellPhoneNumber("08966553");
        assertFalse(result);
    }

    @Test
    public void testLoginSuccessful() {
        login.registerUser("kyl_1", "Ch&sec@ke99!", "+27838968976", "Kyle", "Smith");
        boolean result = login.loginUser("kyl_1", "Ch&sec@ke99!");
        assertTrue(result);
    }

    @Test
    public void testLoginFailed() {
        login.registerUser("kyl_1", "Ch&sec@ke99!", "+27838968976", "Kyle", "Smith");
        boolean result = login.loginUser("kyl_1", "WrongPassword123!");
        assertFalse(result);
    }

    @Test
    public void testRegisterUsernameMessage() {
        String result = login.registerUser("kyle!!!!!!", "Ch&sec@ke99!", "+27838968976", "Kyle", "Smith");
        assertEquals("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.", result);
    }

    @Test
    public void testRegisterPasswordMessage() {
        String result = login.registerUser("kyl_1", "password", "+27838968976", "Kyle", "Smith");
        assertEquals("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.", result);
    }

    @Test
    public void testRegisterCellMessage() {
        String result = login.registerUser("kyl_1", "Ch&sec@ke99!", "08966553", "Kyle", "Smith");
        assertEquals("Cell number is incorrectly formatted or does not contain an international code; please correct the number and try again.", result);
    }

    @Test
    public void testRegisterSuccessMessage() {
        String result = login.registerUser("kyl_1", "Ch&sec@ke99!", "+27838968976", "Kyle", "Smith");
        assertEquals("Username successfully captured.\nPassword successfully captured.\nCell number successfully captured.\nUser registered successfully.", result);
    }

    @Test
    public void testLoginStatusSuccessMessage() {
        login.registerUser("kyl_1", "Ch&sec@ke99!", "+27838968976", "Kyle", "Smith");
        boolean isLoggedIn = login.loginUser("kyl_1", "Ch&sec@ke99!");
        String result = login.returnLoginStatus(isLoggedIn);
        assertEquals("Welcome Kyle, Smith it is great to see you again.", result);
    }

    @Test
    public void testLoginStatusFailureMessage() {
        login.registerUser("kyl_1", "Ch&sec@ke99!", "+27838968976", "Kyle", "Smith");
        boolean isLoggedIn = login.loginUser("kyl_1", "WrongPassword123!");
        String result = login.returnLoginStatus(isLoggedIn);
        assertEquals("Username or password incorrect, please try again.", result);
    }
}