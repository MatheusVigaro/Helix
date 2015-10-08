package tk.vigaro.helix.listener;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import tk.vigaro.helix.Commands;
import tk.vigaro.helix.Helix;
import tk.vigaro.helix.Util;

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
public class ListenerCommandAnime extends ListenerAdapter {

    @Override
    public void onMessage(MessageEvent event) throws Exception {
        if (event.getMessage().startsWith(Helix.botPrefix + Commands.anime + " ") && event.getMessage().length() > (Commands.anime.length() + 2)){
            String s[] = event.getMessage().split(" ", 3);
            if (s.length > 2) {
                String q = s[2];
                String m = null;
                switch (s[1]) {
                    case "search": {
                        try {
                            SyndFeed feed = Util.getSyndFeed("http://www.nyaa.se/?page=rss&cats=1_37&term=" + URLEncoder.encode(q, "UTF-8"));
                            SyndEntry entry = feed.getEntries().get(0);
                            String link = entry.getLink();
                            String title = entry.getTitle();
                            String desc = entry.getDescription().getValue();

                            m = Colors.BOLD + title + Colors.NORMAL + " " + desc + " - " + link;
                        } catch (FeedException | IOException | IndexOutOfBoundsException e) {
                            m = "Anime not found!";
                        }
                        break;
                    }
                    case "test": {
                        try {
                            SyndFeed feed = Util.getSyndFeed("http://www.nyaa.se/?page=rss&cats=1_37&term=" + URLEncoder.encode(q, "UTF-8"));
                            SyndEntry entry = feed.getEntries().get(0);
                            assert q.equals(entry.getTitle());
                            m = "Anime found! " + entry.getDescription().getValue() + " " + entry.getLink().replace("download", "view");
                        } catch (FeedException | IOException | IndexOutOfBoundsException | AssertionError e) {
                            m = "Anime not found";
                        }
                        break;
                    }
                }
                if (m != null) event.respond(m);
            }
        }
    }
}
