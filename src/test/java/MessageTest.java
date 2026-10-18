/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

package com.mycompany.chatapp;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Student
 */
public class MessageTest {

    @Test
    public void testMessageLengthSuccess() {
        Message msg = new Message(1, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        assertEquals("Message ready to send.", msg.checkMessageLength());
    }

    @Test
    public void testMessageLengthFailure() {
        StringBuilder longMsg = new StringBuilder();
        for (int i = 0; i < 260; i++) {
            longMsg.append("a");
        }
        Message msg = new Message(1, "+27718693002", longMsg.toString());
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.", msg.checkMessageLength());
    }

    @Test
    public void testRecipientSuccess() {
        Message msg = new Message(1, "+27718693002", "Hello");
        assertEquals("Cell phone number successfully captured.", msg.checkRecipientCell());
    }

    @Test
    public void testRecipientFailure() {
        Message msg = new Message(1, "08575975889", "Hello");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.", msg.checkRecipientCell());
    }

    @Test
    public void testMessageHashCorrectness() {
        Message msg = new Message(0, "+27718693002", "Hi Mike, can you join us for dinner tonight?");
        msg.setMessageID("0012345678"); 
        String hash = msg.createMessageHash();
        assertEquals("00:0:HITONIGHT", hash);
    }
    
    @Test
    public void testMessageIDLength() {
        Message msg = new Message(1, "+27718693002", "Test");
        assertTrue(msg.checkMessageID());
    }
}