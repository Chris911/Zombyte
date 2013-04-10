package com.game.utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.bag.lib.FileIO;
import com.game.store.Account;

public class Settings {
	
    public final static String file = ".zombyte";

    // load the settings from the phone's file if available
    public static void load(FileIO files) {
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(files.readFile(file)));
            
            // Set the player's previous money
            Account.setMoney(Integer.parseInt(in.readLine()));
            
            // Set the player's previous tokens
            Account.setTokens(Integer.parseInt(in.readLine()));

        } catch (IOException e) {
            // :( It's ok we have defaults
        } catch (NumberFormatException e) {
            // :/ It's ok, defaults save our day
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
            }
        }
    }

    // Save the money to the phone's sdcard
    public static void save(FileIO files) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    files.writeFile(file)));
            
            // Write the player's money
            out.write(Integer.toString(Account.getMoney()));
            out.write("\n");
            
            // Write the player's tokens
            out.write(Integer.toString(Account.getTokens()));
            out.write("\n");

        } catch (IOException e) {
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (IOException e) {
            }
        }
    }
}
