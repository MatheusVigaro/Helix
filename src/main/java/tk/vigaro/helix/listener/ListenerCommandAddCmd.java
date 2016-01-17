package tk.vigaro.helix.listener;

import org.json.JSONObject;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.DisconnectEvent;
import org.pircbotx.hooks.events.MessageEvent;
import tk.vigaro.helix.Commands;
import tk.vigaro.helix.Helix;
import tk.vigaro.helix.Util;

import java.io.*;
import java.util.Arrays;

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
public class ListenerCommandAddCmd extends ListenerAdapter {
    public ListenerCommandAddCmd() throws IOException {
        StringBuilder s = new StringBuilder();
        BufferedReader r = new BufferedReader(new FileReader("commands.json"));
        String l;
        while ((l = r.readLine()) != null){
            s.append(l);
        }
        Helix.commands = new JSONObject(s.toString());

    }


    @Override
    public void onMessage(MessageEvent event) throws Exception {
        if (event.getMessage().startsWith(Helix.botPrefix + Commands.addCmd + " ") && event.getMessage().length() > (Commands.addCmd.length() + 2) && Arrays.asList(Helix.admins).contains(Util.getLogin(event.getUser())) && Util.isVerified(event.getUser())) {
            String s[] = event.getMessage().split(" ", 3);
            if (Helix.hardCommands.contains(s[1].toLowerCase())) {
                event.respond("Can't override hardcoded commands!");
                return;
            }
            Helix.commands.put(s[1].toLowerCase(), s[2]);

            try {
                BufferedWriter wr = new BufferedWriter(new FileWriter("commands.json", false));
                wr.write(Helix.commands.toString(4));
                wr.flush();
                wr.close();
            } catch (IOException e) {
                event.respond(Colors.BOLD + Colors.MAGENTA + "Warning! Failed to save commands.");
                e.printStackTrace();
            }

        }
        try {
            BufferedWriter wr = new BufferedWriter(new FileWriter("commands.json", false));
            wr.write(Helix.commands.toString(4));
            wr.flush();
            wr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisconnect(DisconnectEvent event) throws Exception {
        try {
            BufferedWriter wr = new BufferedWriter(new FileWriter("commands.json", false));
            wr.write(Helix.commands.toString(4));
            wr.flush();
            wr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
