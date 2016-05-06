package tk.vigaro.helix.listener;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.WaitForQueue;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import tk.vigaro.helix.Commands;
import tk.vigaro.helix.Helix;
import tk.vigaro.helix.Util;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
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
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library.
 **/
public class ListenerCommandAnime extends ListenerAdapter {

    @Override
    public void onMessage(MessageEvent event) throws Exception {
        if (event.getMessage().startsWith(Helix.botPrefix + Commands.anime + " ") && event.getMessage().length() > (Commands.anime.length() + 2)){
            String s[] = event.getMessage().split(" ", 3);
            if (s.length > 2) {
                switch (s[1]) {
                    case "search": {
                        String q = s[2];
                        String m = "";
                        try {
                            SyndFeed feed = Util.getSyndFeed("http://www.nyaa.se/?page=rss&cats=1_37&term=" + URLEncoder.encode(q, "UTF-8"));
                            SyndEntry entry = feed.getEntries().get(0);
                            String link = entry.getLink();
                            String title = entry.getTitle();
                            String desc = entry.getDescription().getValue();

                            m = Colors.BOLD + title + Colors.NORMAL + " " + desc + " - " + link;
                            event.respond(m);
                        } catch (FeedException | IOException | IndexOutOfBoundsException e) {
                            m = "Anime not found!";
                            event.respond(m);
                        }
                        break;
                    }

                    case "add": {
                        String q = s[2];
                        String m = "";
                        try {
                            if (!Util.isVerified(event.getUser())) {
                                event.getUser().send().notice("You are not logged in!");
                                break;
                            } else if (Helix.waitingForQuery.contains(event.getUser().getNick().toLowerCase())) {
                                event.getUser().send().notice("Already waiting for a response!");
                                break;
                            }
                            SyndFeed feed = Util.getSyndFeed("http://www.nyaa.se/?page=rss&cats=1_37&term=" + URLEncoder.encode(q, "UTF-8"));
                            List<SyndEntry> recent = feed.getEntries().subList(0, feed.getEntries().size() < 5 ? feed.getEntries().size() : 5);
                            for (SyndEntry r : recent) {
                                m += Colors.TEAL + r.getTitle() + Colors.NORMAL + " | ";
                            }
                            if (recent.size() < 5) m += Colors.RED + "No more results!   ";
                            m = m.substring(0, m.length()-3);
                            event.getUser().send().message(m);
                            event.getUser().send().message("Do the results match the anime you want? (" + Colors.DARK_GREEN + "y" + Colors.NORMAL + "/" + Colors.RED + "n" + Colors.NORMAL + ")");
                            Helix.waitingForQuery.add(event.getUser().getNick().toLowerCase());
                            WaitForQueue queue = new WaitForQueue(Helix.helix);
                            boolean confirmed = false;
                            for (int i = 0; i < 100; i++) {
                                PrivateMessageEvent e = queue.waitFor(PrivateMessageEvent.class);
                                if (e.getUser() == event.getUser()) {
                                    confirmed = "y".equals(e.getMessage());
                                    break;
                                }
                            }
                            Helix.waitingForQuery.remove(event.getUser().getNick().toLowerCase());
                            if (confirmed) {
                                String l = Util.getLogin(event.getUser());
                                List<String> w = new ArrayList<>();
                                if (Helix.animeWatchlist.containsKey(l)) {
                                    w = Helix.animeWatchlist.get(l);
                                }
                                w.add(q.toLowerCase());
                                Helix.animeWatchlist.put(l, w);
                                event.getUser().send().message(Colors.DARK_GREEN + "Anime successfully added to your watchlist!");
                            } else {
                                event.getUser().send().message(Colors.RED + "Canceled.");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            break;
                        }
                        break;
                    }
                    case "del": {
                        if (!Util.isVerified(event.getUser())) {
                            event.getUser().send().notice("You are not logged in!");
                            break;
                        }
                        String l = Util.getLogin(event.getUser());
                        if (!Helix.animeWatchlist.containsKey(l)) {
                            event.getUser().send().notice("You don't have any watchlisted animes!");
                            break;
                        }
                        int q;
                        try {
                            q = Integer.parseInt(s[2]);
                        } catch (NumberFormatException e) {
                            event.getUser().send().notice(Colors.BOLD + s[2] + Colors.NORMAL + " is not a valid number!");
                            break;
                        }
                        List<String> a = Helix.animeWatchlist.get(l);
                        String removed = a.remove(q-1);
                        event.getUser().send().notice("Removed " + Colors.BOLD + removed + Colors.BOLD + " from your watchlist!");
                    }
                }
            } else {
                switch (s[1]) {
                    case "list": {
                        if (!Util.isVerified(event.getUser())) {
                            event.getUser().send().notice("You are not logged in!");
                            break;
                        }
                        String l = Util.getLogin(event.getUser());
                        if (!Helix.animeWatchlist.containsKey(l)) {
                            event.getUser().send().notice("You don't have any watchlisted animes!");
                            break;
                        }
                        event.getUser().send().notice("Your watchlisted animes:");
                        List<String> a = Helix.animeWatchlist.get(l);
                        for (int i = 1; i < a.size()+1; i++) {
                            event.getUser().send().notice("(" + Colors.BOLD + i + Colors.BOLD + "): " + a.get(i-1));
                        }

                    }
                }
            }
        }
    }
}
