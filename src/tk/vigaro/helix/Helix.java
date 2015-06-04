package tk.vigaro.helix;

import org.pircbotx.PircBotX;
import tk.vigaro.helix.config.ConfigurationEsperNet;

import java.util.List;

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

    public static String botName = "VBot";
    public static String botPrefix = ".";
    public static String[] admins = {"Vigaro", "Vigaro|AFK"};
    public static PircBotX helix;

    public static void main(String[] args) throws Exception{
        helix = new PircBotX(new ConfigurationEsperNet().buildConfiguration());
        helix.startBot();

    }

}
