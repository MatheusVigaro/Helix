package tk.vigaro.helix;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.io.FeedException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.managers.BackgroundListenerManager;
import tk.vigaro.helix.config.ConfigurationEsperNet;

import java.io.*;
import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.*;

/**
 * Helix
 * Copyright (c) Matheus Vigaro <matheus@vigaro.tk>, All rights reserved.
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received Helix copy of the GNU Lesser General Public
 * License along with this library.
 */
public class Helix {

    public static final NumberFormat numberFormat = NumberFormat.getInstance();
    public static final Properties properties = new Properties();
    public static final Character[] valid = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};
    public static List<String> hardCommands = new ArrayList<>();
    public static JSONObject commands = new JSONObject();
    public static JSONObject seen;
    public static String botPrefix = ".";
    public static List<String> admins = new ArrayList<>();
    public static PircBotX helix;
    public static BackgroundListenerManager backgroundListenerManager = new BackgroundListenerManager();
    public static Map<String, List<String>> animeWatchlist = new HashMap<>();
    public static Map<String, String> recentAnimes = new HashMap<>();
    public static List<String> waitingForQuery = new ArrayList<>();
    public static Map<UUID, String[]> hangman = new HashMap<>();
    public static List<String> wordList = new ArrayList<>();
    public static Random random = new Random();

    public static void main(String[] args) throws Exception{
        System.setProperty("http.agent", "Wget/1.9.1");
        initializeProperties();
        initializeWatchlist();
        for (Field field : Commands.class.getFields()) hardCommands.add((String)field.get(""));
        Thread animeCheckingThread = new Thread(){
            @Override
            public void run() {
                while (true){
                    try {
                        lookForAnimes();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        helix = new PircBotX(new ConfigurationEsperNet().buildConfiguration());
//        animeCheckingThread.start();
        readWordlist();
        JSONArray a = new JSONArray(properties.getProperty("irc.admins"));
        for (int i = 0; i < a.length(); i++) {
            admins.add(a.getString(i));
        }
        helix.startBot();

    }

    private static void readWordlist() {
        BufferedReader r = null;
        try {
            r = new BufferedReader(new FileReader("wordlist.txt"));
            String l;
            while ((l = r.readLine()) != null) {
                wordList.add(l.toLowerCase());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void lookForAnimes() throws Exception{
        while (true) {
            Thread.sleep(60000);
            checkAnimes();
        }
    }

    private static void initializeProperties() {
        InputStream in = null;
        try {
            in = new FileInputStream("helix.properties");
            properties.load(in);
        } catch (IOException e){
            e.printStackTrace();
            createProperties();
            initializeProperties();
        } finally {
            if (in != null) {
                try {
                    in.close();
                }

                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try(FileWriter fw = new FileWriter("helix.properties", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter writer = new PrintWriter(bw)) {
            if (properties.getProperty("irc.nickname") == null) {
                writer.println("irc.nickname=InsertNameHere");
            }
            if (properties.getProperty("irc.nickserv.login") == null) {
                writer.println("irc.nickserv.login=InsertNickservLoginHere");
            }
            if (properties.getProperty("irc.nickserv.pw") == null) {
                writer.println("irc.nickserv.pw=InsertNickservPasswordHere");
            }
            if (properties.getProperty("irc.channels") == null) {
                writer.println("irc.channels=[\"#VBot\"]");
            }
            if (properties.getProperty("google.apikey") == null) {
                writer.println("google.apikey=InsertGoogleAPIKeyHere");
            }
            if (properties.getProperty("waaai.apikey") == null) {
                writer.println("waaai.apikey=InsertwaaaiAPIKeyHere");
            }
            if (properties.getProperty("irc.admins") == null) {
                writer.println("irc.admins=[\"\"]");
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createProperties() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("helix.properties", "UTF-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        writer.println("irc.nickname=InsertNameHere");
        writer.println("irc.nickserv.login=InsertNickservLoginHere");
        writer.println("irc.nickserv.pw=InsertNickservPasswordHere");
        writer.println("irc.channels=[\"#VBot\"]");
        writer.println("google.apikey=InsertGoogleAPIKeyHere");
        writer.println("waaai.apikey=InsertwaaaiAPIKeyHere");
        writer.flush();
        writer.close();
    }

    private static void initializeWatchlist() throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new FileReader("watchlist.json"));
        String l;
        while ((l = r.readLine()) != null){
            sb.append(l);
        }
        JSONObject o = new JSONObject(sb.toString());
        for (Object k : o.keySet()) {
            List<String> ls = new ArrayList<>();
            JSONArray a = o.getJSONArray((String)k);
            for (int i = 0; i < a.length(); i++) {
                ls.add(a.getString(i));
            }
            animeWatchlist.put((String)k, ls);
        }
    }

    public static void checkAnimes() throws IOException, FeedException {
        Map<String, String> newAnimes = new HashMap<>();
        Map<String, String> curAnimes = new HashMap<>();
        for (SyndEntry e : Util.getSyndFeed("http://www.nyaa.se/?page=rss").getEntries()) {
            curAnimes.put(e.getLink(), e.getTitle());
        }
        for (String s : curAnimes.keySet()) {
            if (!recentAnimes.containsKey(s)) newAnimes.put(s, curAnimes.get(s));
        }
        if (!newAnimes.isEmpty()) {
            if (!recentAnimes.isEmpty()) {
                for (String u : animeWatchlist.keySet()) {
                    for (String s : animeWatchlist.get(u)) {
                        for (String a : newAnimes.keySet()) {
                            int i = 0;
                            String[] p = s.split(" ");
                            for (String d : p) {
                                if (newAnimes.get(a).toLowerCase().contains(d)) i++;
                            }
                            if (i >= p.length)
                                Helix.helix.sendIRC().message("MemoServ", "SEND " + u + " A new anime is out, get it now! " + newAnimes.get(a) + " - " + a);
                        }
                    }
                }
            }
            recentAnimes = curAnimes;
        }
        try {
            JSONObject o = new JSONObject();
            for (String u : animeWatchlist.keySet()) {
                o.put(u, new JSONArray(animeWatchlist.get(u)));
            }
            BufferedWriter wr = new BufferedWriter(new FileWriter("watchlist.json", false));
            wr.write(o.toString(4));
            wr.flush();
            wr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}