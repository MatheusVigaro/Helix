package tk.vigaro.helix.listener.src.tk.vigaro.helix.backgroundlistener;

import org.json.JSONObject;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.DisconnectEvent;
import org.pircbotx.hooks.events.MessageEvent;
import tk.vigaro.helix.listener.src.tk.vigaro.helix.Helix;

import java.io.*;

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
public class BackgroundListenerLastMessage extends ListenerAdapter {

    public BackgroundListenerLastMessage() throws IOException {
        StringBuilder s = new StringBuilder();
        BufferedReader r = new BufferedReader(new FileReader("seen.json"));
        String l;
        while ((l = r.readLine()) != null){
            s.append(l);
        }
        Helix.seen = new JSONObject(s.toString());

    }
    @Override
    public void onMessage(MessageEvent event) throws Exception {
        JSONObject j = new JSONObject();
        j.put("nick", event.getUser().getNick());
        j.put("date", event.getTimestamp());
        j.put("msg", event.getMessage());
        j.put("chan", event.getChannel().getName());
        Helix.seen.put(event.getUser().getNick().toLowerCase(), j);
    }

    @Override
    public void onDisconnect(DisconnectEvent event) throws Exception {
        try {
            BufferedWriter wr = new BufferedWriter(new FileWriter("seen.json", false));
            wr.write(Helix.seen.toString(4));
            wr.flush();
            wr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
