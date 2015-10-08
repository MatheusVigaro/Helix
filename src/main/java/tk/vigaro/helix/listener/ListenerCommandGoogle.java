package tk.vigaro.helix.listener;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import tk.vigaro.helix.Commands;
import tk.vigaro.helix.Helix;
import tk.vigaro.helix.Util;

import java.net.URLEncoder;
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
public class ListenerCommandGoogle extends ListenerAdapter {

    @Override
    public void onMessage(MessageEvent event) throws Exception {
        String msg = event.getMessage();
        if (msg.startsWith(Helix.botPrefix + Commands.googleSearch) && ((msg.length() > (Commands.googleSearch.length() + 2) && msg.charAt(Commands.googleSearch.length()+1) == ' ') || (msg.length() > (Commands.googleSearch.length()+3) && Arrays.asList(Helix.valid).contains(msg.charAt(Commands.googleSearch.length()+1)) && msg.charAt(Commands.googleSearch.length()+2) == ' '))) {
            String q = msg.split(" ", 2)[1];
            String max = msg.charAt(Commands.googleSearch.length()+1) == ' ' ? "1" : String.valueOf(msg.charAt(Commands.googleSearch.length()+1));

            String a = "https://www.googleapis.com/customsearch/v1?fields=items(htmlTitle,link,htmlSnippet)&alt=json&key=" + Helix.properties.getProperty("google.apikey") + "&cx=" + Helix.properties.getProperty("google.searchengineid") + "&q=" + URLEncoder.encode(q, "UTF-8");

            String m;
            try {
                JSONObject r = new JSONObject(Util.getHTTPResponse(a)).getJSONArray("items").getJSONObject(Integer.parseInt(max) - 1);
                String title = StringEscapeUtils.unescapeHtml4(r.getString("htmlTitle").replace("<b>", Colors.BOLD).replace("</b>", Colors.NORMAL));
                String link = r.getString("link");
                String snip = StringEscapeUtils.unescapeHtml4(r.getString("htmlSnippet").replace("<b>", Colors.BOLD).replace("</b>", Colors.NORMAL).replace("\n", "").replace("<br>", ""));

                m = "[" + title + "]" + snip + " " + link;

            } catch (JSONException e) {
                m = "No results found for \"" + Colors.BOLD + q + Colors.NORMAL + "\"";
            }

            event.getChannel().send().message(m);
        }

    }
}
