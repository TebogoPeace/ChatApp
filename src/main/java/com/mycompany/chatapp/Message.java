/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.chatapp;

import java.util.ArrayList;
import java.util.Random;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    private String sender;
    private boolean processed = false;

    private static int totalMessagesSent = 0;

    public static String[] sentMessages = new String[0];
    public static String[] disregardedMessages = new String[0];
    public static String[] storedMessages = new String[0];
    public static String[] messageHashes = new String[0];
    public static String[] messageIDs = new String[0];

    private static final ArrayList<Message> sentDetails = new ArrayList<>();
    private static final ArrayList<Message> storedDetails = new ArrayList<>();
    private static final ArrayList<Message> allDetails = new ArrayList<>();

    private static final String FILE_NAME = "stored_messages.json";

    public Message() {}

    public String generateMessageID() {
        Random random = new Random();
        StringBuilder id = new StringBuilder();
        id.append(random.nextInt(9) + 1);
        for (int i = 0; i < 9; i++) id.append(random.nextInt(10));
        this.messageID = id.toString();
        return messageID;
    }

    public boolean checkMessageID() {
        return messageID != null && messageID.matches("\\d{1,10}");
    }

    public String checkRecipientCell(String cell) {
        if (cell != null && cell.matches("^\\+27\\d{9}$")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    public String checkMessageLength(String text) {
        if (text == null) return "Message cannot be empty.";
        if (text.length() <= 250) return "Message ready to send.";
        return "Message exceeds 250 characters by " + (text.length() - 250) + "; please reduce the size.";
    }

    public String createMessageHash() {
        String firstTwo = messageID.substring(0, 2);
        String cleaned = messageText.trim();
        String[] words = cleaned.split("\\s+");
        String firstWord = words[0].replaceAll("[^A-Za-z0-9]", "").toUpperCase();
        String lastWord = words[words.length - 1].replaceAll("[^A-Za-z0-9]", "").toUpperCase();
        messageHash = firstTwo + ":" + messageNumber + ":" + firstWord + lastWord;
        return messageHash;
    }

    private static String[] addToArray(String[] array, String value) {
        String[] newArray = new String[array.length + 1];
        for (int i = 0; i < array.length; i++) newArray[i] = array[i];
        newArray[array.length] = value;
        return newArray;
    }

    public String SentMessage(String option) {
        if (processed) return "This message has already been processed.";
        if (messageHash == null) createMessageHash();

        if (option.equalsIgnoreCase("Send")) {
            sentMessages = addToArray(sentMessages, messageText);
            sentDetails.add(this);
            totalMessagesSent++;
        } else if (option.equalsIgnoreCase("Store")) {
            storedMessages = addToArray(storedMessages, messageText);
            storedDetails.add(this);
            storeMessage();
        } else if (option.equalsIgnoreCase("Disregard")) {
            disregardedMessages = addToArray(disregardedMessages, messageText);
        } else {
            return "Invalid option.";
        }

        messageHashes = addToArray(messageHashes, messageHash);
        messageIDs = addToArray(messageIDs, messageID);
        allDetails.add(this);
        processed = true;

        if (option.equalsIgnoreCase("Send")) return "Message successfully sent.";
        if (option.equalsIgnoreCase("Store")) return "Message successfully stored.";
        return "Message disregarded.";
    }

    public String printMessages() {
        if (sentMessages.length == 0) return "No messages sent yet.";
        StringBuilder sb = new StringBuilder();
        for (String s : sentMessages) sb.append(s).append("\n");
        return sb.toString();
    }

    public int returnTotalMessages() {
        return totalMessagesSent;
    }

    private String escapeJSON(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    public void storeMessage() {
        String json = "{"
                + "\"messageID\":\"" + escapeJSON(messageID) + "\","
                + "\"messageNumber\":" + messageNumber + ","
                + "\"sender\":\"" + escapeJSON(sender) + "\","
                + "\"recipient\":\"" + escapeJSON(recipient) + "\","
                + "\"message\":\"" + escapeJSON(messageText) + "\","
                + "\"hash\":\"" + escapeJSON(messageHash) + "\""
                + "}";
        try (FileWriter w = new FileWriter(FILE_NAME, true)) {
            w.write(json);
            w.write(System.lineSeparator());
        } catch (IOException e) {
            System.out.println("Could not store message: " + e.getMessage());
        }
    }

    private static String getJSONValue(String json, String key) {
        Pattern p = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"");
        Matcher m = p.matcher(json);
        if (m.find()) return unescapeJSON(m.group(1));
        return "";
    }

    private static String unescapeJSON(String text) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '\\' && i + 1 < text.length()) {
                char n = text.charAt(++i);
                switch (n) {
                    case 'n': sb.append('\n'); break;
                    case 'r': sb.append('\r'); break;
                    case 't': sb.append('\t'); break;
                    default: sb.append(n); break;
                }
            } else sb.append(c);
        }
        return sb.toString();
    }

    public static void loadStoredMessagesFromFile() {
        storedMessages = new String[0];
        storedDetails.clear();
        File f = new File(FILE_NAME);
        if (!f.exists()) return;
        try (BufferedReader r = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = r.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                Message msg = new Message();
                msg.setMessageID(getJSONValue(line, "messageID"));
                msg.setSender(getJSONValue(line, "sender"));
                msg.setRecipient(getJSONValue(line, "recipient"));
                msg.setMessageText(getJSONValue(line, "message"));
                msg.messageHash = getJSONValue(line, "hash");
                msg.processed = true;
                String num = line.replaceAll(".*\"messageNumber\"\\s*:\\s*(\\d+).*", "$1");
                try { msg.setMessageNumber(Integer.parseInt(num)); }
                catch (NumberFormatException e) { msg.setMessageNumber(0); }
                storedMessages = addToArray(storedMessages, msg.getMessageText());
                storedDetails.add(msg);
            }
        } catch (IOException e) {
            System.out.println("Error reading JSON: " + e.getMessage());
        }
    }

    public static String displayStoredSendersAndRecipients() {
        loadStoredMessagesFromFile();
        if (storedDetails.isEmpty()) return "No stored messages available.";
        StringBuilder sb = new StringBuilder();
        for (Message m : storedDetails) {
            sb.append("Sender: ").append(m.sender)
              .append("\nRecipient: ").append(m.recipient)
              .append("\n--------------------\n");
        }
        return sb.toString();
    }

    public static String getLongestMessage() {
        String longest = "";
        for (Message m : allDetails) if (m.messageText.length() > longest.length()) longest = m.messageText;
        for (Message m : storedDetails) if (m.messageText.length() > longest.length()) longest = m.messageText;
        return longest.isEmpty() ? "No messages available." : longest;
    }

    public static String searchMessageByID(String id) {
        loadStoredMessagesFromFile();
        for (Message m : allDetails)
            if (m.messageID.equals(id)) return "Recipient: " + m.recipient + "\nMessage: " + m.messageText;
        for (Message m : storedDetails)
            if (m.messageID.equals(id)) return "Recipient: " + m.recipient + "\nMessage: " + m.messageText;
        return "Message ID not found.";
    }

    public static String searchMessagesByRecipient(String recipient) {
        loadStoredMessagesFromFile();
        StringBuilder sb = new StringBuilder();
        for (Message m : allDetails)
            if (m.recipient.equals(recipient) && !m.isDisregarded())
                sb.append(m.messageText).append("\n");
        for (Message m : storedDetails) {
            if (m.recipient.equals(recipient)) {
                boolean shown = false;
                for (Message e : allDetails) if (e.messageID.equals(m.messageID)) { shown = true; break; }
                if (!shown) sb.append(m.messageText).append("\n");
            }
        }
        if (sb.length() == 0) return "No messages found for this recipient.";
        return sb.toString().trim();
    }

    private boolean isDisregarded() {
        for (String s : disregardedMessages) if (s.equals(messageText)) return true;
        return false;
    }

    public static String deleteMessageByHash(String hash) {
        loadStoredMessagesFromFile();
        for (int i = 0; i < storedDetails.size(); i++) {
            Message m = storedDetails.get(i);
            if (m.messageHash.equals(hash)) {
                String deleted = m.messageText;
                storedDetails.remove(i);
                storedMessages = new String[0];
                for (Message s : storedDetails) storedMessages = addToArray(storedMessages, s.messageText);
                updateJSONFile();
                return "Message: \"" + deleted + "\" successfully deleted.";
            }
        }
        return "Message hash not found.";
    }

    private static void updateJSONFile() {
        try (FileWriter w = new FileWriter(FILE_NAME, false)) {
            for (Message m : storedDetails) {
                String json = "{"
                        + "\"messageID\":\"" + m.messageID + "\","
                        + "\"messageNumber\":" + m.messageNumber + ","
                        + "\"sender\":\"" + m.sender + "\","
                        + "\"recipient\":\"" + m.recipient + "\","
                        + "\"message\":\"" + m.messageText + "\","
                        + "\"hash\":\"" + m.messageHash + "\""
                        + "}";
                w.write(json);
                w.write(System.lineSeparator());
            }
        } catch (IOException e) {
            System.out.println("Error updating JSON: " + e.getMessage());
        }
    }

    public static String displayReport() {
        loadStoredMessagesFromFile();
        StringBuilder sb = new StringBuilder();
        sb.append("========== MESSAGE REPORT ==========\n");
        int count = 0;
        for (Message m : sentDetails) {
            sb.append("\nMessage Hash: ").append(m.messageHash)
              .append("\nRecipient: ").append(m.recipient)
              .append("\nMessage: ").append(m.messageText)
              .append("\n--------------------\n");
            count++;
        }
        for (Message m : storedDetails) {
            boolean shown = false;
            for (Message s : sentDetails) if (s.messageID.equals(m.messageID)) { shown = true; break; }
            if (!shown) {
                sb.append("\nMessage Hash: ").append(m.messageHash)
                  .append("\nRecipient: ").append(m.recipient)
                  .append("\nMessage: ").append(m.messageText)
                  .append("\n--------------------\n");
                count++;
            }
        }
        return count == 0 ? "No messages available for the report." : sb.toString();
    }

    public void setMessageID(String id) { this.messageID = id; }
    public void setMessageNumber(int n) { this.messageNumber = n; }
    public void setMessageText(String t) { this.messageText = t; }
    public void setRecipient(String r) { this.recipient = r; }
    public void setSender(String s) { this.sender = s; }
    public String getMessageID() { return messageID; }
    public String getMessageHash() { return messageHash; }
    public String getMessageText() { return messageText; }
    public String getRecipient() { return recipient; }

    public static void clearData() {
        sentMessages = new String[0];
        disregardedMessages = new String[0];
        storedMessages = new String[0];
        messageHashes = new String[0];
        messageIDs = new String[0];
        sentDetails.clear();
        storedDetails.clear();
        allDetails.clear();
        totalMessagesSent = 0;
        File f = new File(FILE_NAME);
        if (f.exists()) f.delete();
    }
}