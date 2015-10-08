package tk.vigaro.helix.listener.src.tk.vigaro.helix.listener;

import org.json.JSONException;
import org.json.JSONObject;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import tk.vigaro.helix.listener.src.tk.vigaro.helix.Commands;
import tk.vigaro.helix.listener.src.tk.vigaro.helix.Helix;
import tk.vigaro.helix.listener.src.tk.vigaro.helix.Util;

import java.io.IOException;
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
public class ListenerCommandShortenLink extends ListenerAdapter {

    @Override
    public void onMessage(MessageEvent event) throws Exception {
        if (event.getMessage().startsWith(Helix.botPrefix + Commands.shorthenLink + " ") && (event.getMessage().length() > (Commands.shorthenLink.length() + 2))) {
            String q = event.getMessage().split(Helix.botPrefix + Commands.shorthenLink + " ", 2)[1];

            try {
                String l = (new JSONObject(Util.getHTTPResponse("https://api.waa.ai/shorten?url=" + URLEncoder.encode(q, "UTF-8") + "&key=" + Helix.properties.getProperty("waaai.apikey")))).getJSONObject("data").getString("url");
                event.respond(l);
            } catch (JSONException | IOException e) {
                event.respond("Invalid link!");
            }
        }
    }
}
