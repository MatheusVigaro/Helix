package tk.vigaro.helix.listener.src.tk.vigaro.helix;

import org.json.JSONObject;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.managers.BackgroundListenerManager;
import tk.vigaro.helix.listener.src.tk.vigaro.helix.config.ConfigurationEsperNet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

    public static final NumberFormat numberFormat = NumberFormat.getInstance();
    public static final Properties properties = new Properties();
    public static JSONObject seen;
    public static String botPrefix = ".";
    public static String[] admins = {"vigaro"};
    public static PircBotX helix;
    public static final Character[] valid = {'1', '2', '3', '4', '5','6', '6', '8', '9'};
    public static BackgroundListenerManager backgroundListenerManager = new BackgroundListenerManager();

    public static void main(String[] args) throws Exception{
        System.setProperty("http.agent", "Wget/1.9.1");
        initializeProperties();
        helix = new PircBotX(new ConfigurationEsperNet().buildConfiguration());
        helix.startBot();

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
