package tk.vigaro.helix;

import org.pircbotx.PircBotX;
import tk.vigaro.helix.config.ConfigurationEsperNet;

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

    public static String googleKey;
    public static Properties properties = new Properties();
    public static String botPrefix = ".";
    public static String[] admins = {"vigaro"};
    public static PircBotX helix;

    public static void main(String[] args) throws Exception{
        initializeProperties();
        googleKey = properties.getProperty("google.apikey");
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
