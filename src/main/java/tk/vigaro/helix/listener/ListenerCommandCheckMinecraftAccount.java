package tk.vigaro.helix.listener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import tk.vigaro.helix.Commands;
import tk.vigaro.helix.Helix;
import tk.vigaro.helix.Util;

import java.net.URLEncoder;

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
public class ListenerCommandCheckMinecraftAccount extends ListenerAdapter {

    @Override
    public void onMessage(MessageEvent event) throws Exception {
        if (event.getMessage().startsWith(Helix.botPrefix + Commands.checkMinecraftAccount + " ") && event.getMessage().length() > (Commands.checkMinecraftAccount.length() + 2)) {
            String a = event.getMessage().split(" ", 2)[1].replace(" ", "");
            String m;
            try {
                JSONObject r = new JSONObject(Util.getHTTPResponse("https://api.mojang.com/users/profiles/minecraft/" + URLEncoder.encode(a, "UTF-8")));

                String name = r.getString("name");
                String id = r.getString("id");

                JSONArray h = new JSONArray(Util.getHTTPResponse("https://api.mojang.com/user/profiles/" + id + "/names"));

                m = "Username: " + Colors.GREEN + name + Colors.NORMAL + " UUID: " + Colors.MAGENTA + id + Colors.NORMAL + " Username history: " + Colors.BLUE + h.getJSONObject(0).getString("name") + Colors.NORMAL;

                for (int i = 1; i < h.length(); i++){
                    m += ", " + Colors.BLUE + h.getJSONObject(i).getString("name") + Colors.NORMAL;
                }


            } catch (JSONException e) {
                m = Colors.RED + "User " + a + " not found!";
            }

            event.getChannel().send().message(m);

        }
    }
}
