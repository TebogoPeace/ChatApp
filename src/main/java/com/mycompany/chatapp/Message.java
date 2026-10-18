/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Student
 */
public class Message {
     private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;

    private static int totalMessages = 0;
    private static List<String> sentMessagesList = new ArrayList<>();
    private static final String JSON_FILE = "messages.json";

    public Message() {
    }

    public Message(int messageNumber, String recipient, String messageText) {
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
    }

    private String generateMessageID() {
        Random rand = new Random();
        long id = (long) (rand.nextDouble() * 9_000_000_000L) + 1_000_000_000L;
        return String.valueOf(id);
    }

    public boolean checkMessageID() {
        return this.messageID != null && this.messageID.length() <= 10;
    }

    public String checkRecipientCell() {
        if (this.recipient != null && this.recipient.matches("^\\+\\d{10,12}$") && this.recipient.length() <= 13) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    public String createMessageHash() {
        if (this.messageText == null || this.messageText.trim().isEmpty()) {
            return this.messageID.substring(0, 2) + ":" + this.messageNumber + ":EMPTY";
        }
        
        String[] words = this.messageText.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^a-zA-Z]", "").toUpperCase();
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z]", "").toUpperCase();
        String firstTwoID = this.messageID.substring(0, 2);
        
        return firstTwoID + ":" + this.messageNumber + ":" + firstWord + lastWord;
    }

    public String checkMessageLength() {
        if (this.messageText.length() <= 250) {
            return "Message ready to send.";
        } else {
            int excess = this.messageText.length() - 250;
            return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
        }
    }

    public String SentMessage() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nOptions: 1) Send Message 2) Disregard Message 3) Store Message");
        System.out.print("Choose: ");
        int choice = scanner.nextInt();
        scanner.nextLine(); 
        
        switch (choice) {
            case 1:
                totalMessages++;
                sentMessagesList.add(this.messageText);
                saveToJson("Sent");
                return "Message successfully sent.";
            case 2:
                saveToJson("Disregard");
                return "Press 0 to delete the message.";
            case 3:
                saveToJson("Stored");
                return "Message successfully stored.";
            default:
                return "Invalid option.";
        }
    }

    public String printMessages() {
        if (sentMessagesList.isEmpty()) {
            return "No messages sent yet.";
        }
        StringBuilder sb = new StringBuilder();
        for (String msg : sentMessagesList) {
            sb.append(msg).append("\n");
        }
        return sb.toString();
    }

    public int returnTotalMessages() {
        return totalMessages;
    }

    private void saveToJson(String flag) {
        JSONArray jsonArray = new JSONArray();
        File file = new File(JSON_FILE);

        if (file.exists()) {
            try (Scanner fileScanner = new Scanner(file)) {
                StringBuilder jsonContent = new StringBuilder();
                while (fileScanner.hasNextLine()) {
                    jsonContent.append(fileScanner.nextLine());
                }
                if (jsonContent.length() > 0) {
                    jsonArray = new JSONArray(jsonContent.toString());
                }
            } catch (FileNotFoundException e) {
                System.out.println("File error.");
            }
        }

        JSONObject msgObj = new JSONObject();
        msgObj.put("MessageID", this.messageID);
        msgObj.put("MessageNumber", this.messageNumber);
        msgObj.put("Recipient", this.recipient);
        msgObj.put("Message", this.messageText);
        msgObj.put("Hash", this.messageHash);
        msgObj.put("Flag", flag);
        
        jsonArray.put(msgObj);

        try (FileWriter writer = new FileWriter(JSON_FILE)) {
            writer.write(jsonArray.toString(4));
        } catch (IOException e) {
            System.out.println("Error saving to file.");
        }
    }
    
    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public void setMessageID(String id) { this.messageID = id; }
}

