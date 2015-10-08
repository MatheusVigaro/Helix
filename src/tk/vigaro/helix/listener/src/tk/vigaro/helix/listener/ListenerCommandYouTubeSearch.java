package tk.vigaro.helix.listener.src.tk.vigaro.helix.listener;

import org.json.JSONException;
import org.json.JSONObject;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import tk.vigaro.helix.listener.src.tk.vigaro.helix.Commands;
import tk.vigaro.helix.listener.src.tk.vigaro.helix.Helix;
import tk.vigaro.helix.listener.src.tk.vigaro.helix.Util;

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
public class ListenerCommandYouTubeSearch extends ListenerAdapter{

    @Override
    public void onMessage(MessageEvent event) throws Exception {
        String msg = event.getMessage();
        if (msg.startsWith(Helix.botPrefix + Commands.youTubeSearch) && ((msg.length() > (Commands.youTubeSearch.length()+2) && msg.charAt(Commands.youTubeSearch.length()+1) == ' ') || (msg.length() > (Commands.youTubeSearch.length()+3) && Arrays.asList(Helix.valid).contains(msg.charAt(Commands.youTubeSearch.length()+1)) && msg.charAt(Commands.youTubeSearch.length()+2) == ' '))){
            String q = msg.split(" ", 2)[1];
            String max = msg.charAt(3) == ' ' ? "1" : String.valueOf(msg.charAt(3));


            String a = String.format("https://www.googleapis.com/youtube/v3/search?part=id&maxResults=%s&regionCode=br&type=video&q=%s&key=%s", max, URLEncoder.encode(q, "UTF-8"), Helix.properties.get("google.apikey"));

            JSONObject v;
            JSONObject snip;
            String views;
            String dislike;
            String like;
            String len;
            String m;
            try {
                v = new JSONObject(Util.getHTTPResponse(String.format("https://www.googleapis.com/youtube/v3/videos?part=contentDetails,snippet,statistics&id=%s&key=%s", (new JSONObject(Util.getHTTPResponse(a))).getJSONArray("items").getJSONObject(Integer.valueOf(max) - 1).getJSONObject("id").getString("videoId"), Helix.properties.get("google.apikey")))).getJSONArray("items").getJSONObject(0);

                snip = v.getJSONObject("snippet");
                JSONObject stat = v.getJSONObject("statistics");

                views = Helix.numberFormat.format(Long.parseLong(stat.getString("viewCount")));
                dislike = Helix.numberFormat.format(Integer.parseInt(stat.getString("dislikeCount")));
                like = Helix.numberFormat.format(Integer.parseInt(stat.getString("likeCount")));
                len = v.getJSONObject("contentDetails").getString("duration");
                m = Colors.BOLD + Colors.BLACK + ",00[You" + Colors.WHITE + ",04Tube]" + Colors.NORMAL + " " + snip.getString("title") + " [" + Colors.OLIVE + (len.equals("PT0S")? "Live" : Util.parseYouTubeTime(len)) + Colors.NORMAL + "] [" + Colors.TEAL + views + Colors.NORMAL + "] [" + Colors.DARK_GREEN + "+" + like + Colors.NORMAL + "] [" + Colors.RED + "-" + dislike + Colors.NORMAL + "] [" + Colors.MAGENTA + snip.getString("channelTitle") + Colors.NORMAL + "] http://youtu.be/" + v.getString("id");
            } catch (JSONException e){
                m = "Video not found! \"" + Colors.BOLD + q + Colors.NORMAL + "\"";
            }

            event.getChannel().send().message(m);


        }
    }
}
