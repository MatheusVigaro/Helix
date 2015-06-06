package tk.vigaro.helix.listener;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatistics;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import tk.vigaro.helix.Helix;

import java.net.InetAddress;
import java.text.NumberFormat;
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
public class ListenerCommandYoutubeSearch extends ListenerAdapter{
    static final Character[] valid = {'1', '2', '3', '4', '5','6', '6', '8', '9'};

    @Override
    public void onMessage(MessageEvent event) throws Exception {
        String msg = event.getMessage();
        if (msg.startsWith(Helix.botPrefix + "yt") && ((msg.length() > 4 && msg.charAt(3) == ' ') || (msg.length() > 5 && Arrays.asList(valid).contains(msg.charAt(3)) && msg.charAt(4) == ' '))){
            String q = msg.split(" ", 2)[1];
            long max = msg.charAt(3) == ' ' ? 1 : Long.parseLong(Character.toString(msg.charAt(3)));

            YouTube.Search.List search = Helix.youtube.search().list("id");
            search.setKey(Helix.properties.getProperty("google.apikey"));
            search.setType("video");
            search.setFields("items(id/videoId)");
            search.setUserIp(InetAddress.getByName(event.getUser().getHostmask()).getHostAddress());
            search.setRegionCode("pt");
            search.setQ(q);
            search.setMaxResults(max);

            SearchListResponse result = search.execute();
            String m;
            try {
                YouTube.Videos.List v = Helix.youtube.videos().list(result.getItems().get((int) max - 1).getId().getVideoId(), "snippet,statistics");
                v.setKey(Helix.properties.getProperty("google.apikey"));
                Video video = v.execute().getItems().get(0);

                VideoSnippet snippet = video.getSnippet();
                VideoStatistics statistics = video.getStatistics();

                m = Colors.BOLD + Colors.BLACK + ",00[You" + Colors.WHITE + ",04Tube]" + Colors.NORMAL + " " + snippet.getTitle() + "  [" + Colors.TEAL + Helix.numberFormat.format(statistics.getViewCount()) + Colors.NORMAL + "]  [" + Colors.DARK_GREEN + "+" + Helix.numberFormat.format(statistics.getViewCount()) + Colors.NORMAL + "]  [" + Colors.RED + "-" + Helix.numberFormat.format(statistics.getDislikeCount()) + Colors.NORMAL + "]  [" + Colors.MAGENTA + snippet.getChannelTitle() + Colors.NORMAL + "] http://youtu.be/" + video.getId();
            } catch (IndexOutOfBoundsException e){
                m = "Video not found! \"" + Colors.BOLD + q + Colors.NORMAL + "\"";
            }
            event.getChannel().send().message(m);


        }
    }
}
