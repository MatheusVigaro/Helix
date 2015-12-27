package tk.vigaro.helix.listener;

import org.json.JSONObject;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import tk.vigaro.helix.Helix;
import tk.vigaro.helix.Util;

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
public class ListenerLinkYouTube extends ListenerAdapter {

    @Override
    public void onMessage(MessageEvent event) throws Exception {
        for (String word : event.getMessage().split(" ")){
            String id;
            if (word.contains("youtube.com/watch?v=")){
                String[] a = word.split("youtube\\.com/watch\\?v=");
                id = a[a.length-1];

            } else if (word.contains("youtu.be/")){
                String[] a = word.split("youtu\\.be/");
                id = a[a.length-1];
            } else {
                continue;
            }

            if (id.length() < 11) {continue;} else {
                id = id.substring(0, 11);
            }

            JSONObject v = new JSONObject(Util.getHTTPResponse(String.format("https://www.googleapis.com/youtube/v3/videos?part=contentDetails,snippet,statistics&id=%s&key=%s", id, Helix.properties.getProperty("google.apikey")))).getJSONArray("items").getJSONObject(0);

            JSONObject snip = v.getJSONObject("snippet");
            JSONObject stat = v.getJSONObject("statistics");

            String views = Helix.numberFormat.format(Long.parseLong(stat.getString("viewCount")));
            String dislike = Helix.numberFormat.format(Integer.parseInt(stat.getString("dislikeCount")));
            String like = Helix.numberFormat.format(Integer.parseInt(stat.getString("likeCount")));
            String len = v.getJSONObject("contentDetails").getString("duration");

            String m = Colors.BOLD + Colors.BLACK + ",00[You" + Colors.WHITE + ",04Tube]" + Colors.NORMAL + " " + snip.getString("title") + " [" + Colors.OLIVE + (len.equals("PT0S")? "Live" : Util.parseYouTubeTime(len)) + Colors.NORMAL + "] [" + Colors.TEAL + views + Colors.NORMAL + "] [" + Colors.DARK_GREEN + "+" + like + Colors.NORMAL + "] [" + Colors.RED + "-" + dislike + Colors.NORMAL + "] [" + Colors.MAGENTA + snip.getString("channelTitle") + Colors.NORMAL + "]";
            event.getChannel().send().message(m);


        }

    }
}
