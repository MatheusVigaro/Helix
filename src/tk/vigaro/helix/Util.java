package tk.vigaro.helix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.regex.Pattern;

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

    private static HashMap<String, String> regexMap = new HashMap<String, String>();
    private static String regex2two = "(?<=[^\\d])(\\d)(?=[^\\d])";
    private static String two = "0$1";

    public static String getHTTPResponse(String url) throws IOException {
        HttpURLConnection con = (HttpURLConnection)(new URL(url)).openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder response = new StringBuilder();
        String l;

        while ((l = reader.readLine()) != null){
            response.append(l);
            response.append('\r');
        }

        reader.close();
        return response.toString();
    }

    public static String parseYouTubeTime(String date){
        regexMap.put("PT(\\d\\d)S", "00:$1");
        regexMap.put("PT(\\d\\d)M", "$1:00");
        regexMap.put("PT(\\d\\d)H", "$1:00:00");
        regexMap.put("PT(\\d\\d)M(\\d\\d)S", "$1:$2");
        regexMap.put("PT(\\d\\d)H(\\d\\d)S", "$1:00:$2");
        regexMap.put("PT(\\d\\d)H(\\d\\d)M", "$1:$2:00");
        regexMap.put("PT(\\d\\d)H(\\d\\d)M(\\d\\d)S", "$1:$2:$3");

        String[] dates = { "PT1S", "PT1M", "PT1H", "PT1M1S", "PT1H1S", "PT1H1M", "PT1H1M1S", "PT10H1M13S", "PT10H1S", "PT1M11S" };

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
}