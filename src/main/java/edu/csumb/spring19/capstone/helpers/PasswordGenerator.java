package edu.csumb.spring19.capstone.helpers;

public class PasswordGenerator {
    /**
     * Generates a 20-character alphanumeric string for a password
     */
    public static String newPass() {
        char[] alph = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        StringBuilder pass = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            pass.append(alph[(int) (Math.random() * alph.length)]);
        }
        return pass.toString();
    }
}
