/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */

package com.mycompany.chatapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Student
 */
public class MessageTest {

    @BeforeEach
    public void setUp() {
        Message.clearData();
        create("1000000001", 0, "+27834557896", "Did you get the cake?", "Send");
        create("1000000002", 1, "+27838884567", "Where are you? You are late! I have asked you to be on time.", "Store");
        create("1000000003", 2, "+27834484567", "Yohoooo, I am at your gate.", "Disregard");
        create("0838884567", 3, "0838884567", "It is dinner time !", "Send");
        create("1000000005", 4, "+27838884567", "Ok, I am leaving without you.", "Store");
    }

    private void create(String id, int number, String recipient, String text, String option) {
        Message m = new Message();
        m.setMessageID(id);
        m.setMessageNumber(number);
        m.setSender("Developer");
        m.setRecipient(recipient);
        m.setMessageText(text);
        m.createMessageHash();
        m.SentMessage(option);
    }

    // ---------- Part 3 tests (6) ----------

    @Test
    public void testSentMessagesArray() {
        assertEquals(2, Message.sentMessages.length);
        assertEquals("Did you get the cake?", Message.sentMessages[0]);
        assertEquals("It is dinner time !", Message.sentMessages[1]);
    }

    @Test
    public void testLongestMessage() {
        assertEquals("Where are you? You are late! I have asked you to be on time.", Message.getLongestMessage());
    }

    @Test
    public void testSearchMessageByID() {
        String result = Message.searchMessageByID("0838884567");
        assertTrue(result.contains("It is dinner time !"));
    }

    @Test
    public void testSearchMessagesByRecipient() {
        String result = Message.searchMessagesByRecipient("+27838884567");
        assertTrue(result.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(result.contains("Ok, I am leaving without you."));
    }

    @Test
    public void testDeleteMessageByHash() {
        String hash = Message.messageHashes[1];
        String result = Message.deleteMessageByHash(hash);
        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.", result);
        assertEquals(1, Message.storedMessages.length);
    }

    @Test
    public void testDisplayReport() {
        String report = Message.displayReport();
        assertTrue(report.contains("Message Hash:"));
        assertTrue(report.contains("Recipient:"));
        assertTrue(report.contains("Message:"));
        assertTrue(report.contains("Did you get the cake?"));
        assertTrue(report.contains("It is dinner time !"));
        assertTrue(report.contains("Where are you? You are late! I have asked you to be on time."));
        assertTrue(report.contains("Ok, I am leaving without you."));
    }

    // ---------- Part 2 tests ----------

    @Test
    public void testMessageLengthSuccess() {
        Message m = new Message();
        assertEquals("Message ready to send.", m.checkMessageLength("Hi Mike, can you join us for dinner tonight?"));
    }

    @Test
    public void testMessageLengthFailure() {
        Message m = new Message();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 260; i++) sb.append("a");
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.", m.checkMessageLength(sb.toString()));
    }

    @Test
    public void testRecipientValid() {
        Message m = new Message();
        assertEquals("Cell phone number successfully captured.", m.checkRecipientCell("+27718693002"));
    }

    @Test
    public void testRecipientInvalid() {
        Message m = new Message();
        assertEquals(
            "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
            m.checkRecipientCell("08575975889"));
    }

    @Test
    public void testMessageHash() {
        Message m = new Message();
        m.setMessageID("0012345678");
        m.setMessageNumber(0);
        m.setMessageText("Hi Mike, can you join us for dinner tonight?");
        assertEquals("00:0:HITONIGHT", m.createMessageHash());
    }

    @Test
    public void testMessageHashesInLoop() {
        String[][] data = {
            {"0012345678", "0", "Hi Mike, can you join us for dinner tonight?", "00:0:HITONIGHT"},
            {"9912345678", "1", "Hi Keegan, did you receive the payment?", "99:1:HIPAYMENT"}
        };
        for (String[] row : data) {
            Message m = new Message();
            m.setMessageID(row[0]);
            m.setMessageNumber(Integer.parseInt(row[1]));
            m.setMessageText(row[2]);
            assertEquals(row[3], m.createMessageHash());
        }
    }

    @Test
    public void testReturnTotalMessages() {
        Message m = new Message();
        m.setMessageID("1111111111");
        m.setMessageNumber(0);
        m.setRecipient("+27838884567");
        m.setMessageText("Hello");
        m.createMessageHash();
        m.SentMessage("Send");
        assertTrue(m.returnTotalMessages() >= 1);
    }
}