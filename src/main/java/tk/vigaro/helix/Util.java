package tk.vigaro.helix;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import org.pircbotx.Colors;
import org.pircbotx.User;
import org.pircbotx.hooks.WaitForQueue;
import org.pircbotx.hooks.events.NoticeEvent;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

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
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 **/
public class Util {

    private static final HashMap<String, String> regexMap = new HashMap<>();

    public static String getHTTPResponse(String url) throws IOException {
        HttpURLConnection con = (HttpURLConnection)(new URL(url)).openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader("gzip".equals(con.getContentEncoding()) ? new GZIPInputStream(con.getInputStream()) : con.getInputStream()));
        StringBuilder response = new StringBuilder();
        String l;

        while ((l = reader.readLine()) != null){
            response.append(l);
            response.append('\r');
        }

        reader.close();
        return response.toString();
    }

    public static SyndFeed getSyndFeed(String url) throws IOException, FeedException {
        HttpURLConnection con = (HttpURLConnection)(new URL(url)).openConnection();
        return new SyndFeedInput().build(new InputSource("gzip".equals(con.getContentEncoding()) ? new GZIPInputStream(con.getInputStream()) : con.getInputStream()));
    }

    public static String parseYouTubeTime(String date){
        regexMap.put("PT(\\d\\d)S", "00:$1");
        regexMap.put("PT(\\d\\d)M", "$1:00");
        regexMap.put("PT(\\d\\d)H", "$1:00:00");
        regexMap.put("P(\\d\\d)D", "$1:00:00:00");
        regexMap.put("PT(\\d\\d)M(\\d\\d)S", "$1:$2");
        regexMap.put("PT(\\d\\d)H(\\d\\d)S", "$1:00:$2");
        regexMap.put("PT(\\d\\d)H(\\d\\d)M", "$1:$2:00");
        regexMap.put("P(\\d\\d)DT(\\d\\d)H", "$1:$2:00:00");
        regexMap.put("P(\\d\\d)DT(\\d\\d)M", "$1:00:$2:00");
        regexMap.put("P(\\d\\d)DT(\\d\\d)S", "$1:00:00:$2");
        regexMap.put("P(\\d\\d)DT(\\d\\d)H(\\d\\d)M", "$1:$2:$3:00");
        regexMap.put("P(\\d\\d)DT(\\d\\d)H(\\d\\d)S", "$1:$2:00:$3");
        regexMap.put("PT(\\d\\d)H(\\d\\d)M(\\d\\d)S", "$1:$2:$3");
        regexMap.put("P(\\d\\d)DT(\\d\\d)H(\\d\\d)M(\\d\\d)S", "$1:$2:$3:$4");

        String regex2two = "(?<=[^\\d])(\\d)(?=[^\\d])";
        String two = "0$1";
        String d = date.replaceAll(regex2two, two);
        String regex = getRegex(d);
        return d.replaceAll(regex, regexMap.get(regex));
    }

    private static String getRegex(String date) {
        for (String r : regexMap.keySet())
            if (Pattern.matches(r, date))
                return r;
        return null;
    }

    public static boolean isVerified(User user) throws InterruptedException {
        String nick = user.getNick();
        WaitForQueue queue = new WaitForQueue(Helix.helix);
        Helix.helix.sendIRC().message("NickServ", "ACC " + nick);
        for (int i = 0; i < 50; i++) {
            NoticeEvent event = queue.waitFor(NoticeEvent.class);
            if (event.getUser().getNick().equals("NickServ")) {
                if (event.getMessage().startsWith(nick + " ACC 3 ")) {
                    return true;
                } else if (event.getMessage().startsWith(nick + " ACC ")) {
                    return false;
                }
            }
        }
        return false;
    }

    public static String getLogin(User user) throws InterruptedException {
        String nick = user.getNick().toLowerCase();
        WaitForQueue queue = new WaitForQueue(Helix.helix);
        Helix.helix.sendIRC().message("NickServ", "INFO " + nick);
        for (int i = 0; i < 50; i++) {
            NoticeEvent event = queue.waitFor(NoticeEvent.class);
            String m = Colors.removeFormattingAndColors(event.getNotice()).toLowerCase();
            if (m.startsWith("information on " + nick + " (account ")) {
                String[] n = m.split(" ");
                String login = n[n.length - 1];
                return login.substring(0, login.length() - 2);
            } else if (m.startsWith(nick + " is not registered")) return null;
        }
        return null;
    }

    public static String join(String delimiter, String string) {
        String result = "";
        for (char c : string.toCharArray()) {
            result = result + c + delimiter;
        }
        return result.substring(0, result.length() - 1);
    }
}
