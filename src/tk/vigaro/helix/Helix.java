package tk.vigaro.helix;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import org.pircbotx.PircBotX;
import tk.vigaro.helix.config.ConfigurationEsperNet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.text.NumberFormat;
import java.util.Properties;

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

    public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    public static final JsonFactory JSON_FACTORY = new JacksonFactory();
    public static final NumberFormat numberFormat = NumberFormat.getInstance();
    public static GoogleCredential credential;

    public static Properties properties = new Properties();
    public static YouTube youtube;
    public static YouTube.Search.List search;
    public static String botName = "VBot";
    public static String botPrefix = ".";
    public static String[] admins = {"Vigaro", "Vigaro|AFK"};
    public static PircBotX helix;

    public static void main(String[] args) throws Exception{
        initializeProperties();
        initializeGoogle();

        helix = new PircBotX(new ConfigurationEsperNet().buildConfiguration());
        helix.startBot();

    }

    private static void initializeGoogle() throws GeneralSecurityException, IOException {
        credential = new GoogleCredential.Builder()
                .setTransport(HTTP_TRANSPORT)
                .setJsonFactory(JSON_FACTORY)
                .build();

        youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName("VBot").build();
    }

    private static void initializeProperties(){
        InputStream in = null;
        try {
            in = new FileInputStream("helix.properties");
            properties.load(in);
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
