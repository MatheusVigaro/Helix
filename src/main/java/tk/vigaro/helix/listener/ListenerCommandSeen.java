package tk.vigaro.helix.listener;

import com.eaio.util.text.HumanTime;
import org.json.JSONException;
import org.json.JSONObject;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import tk.vigaro.helix.Commands;
import tk.vigaro.helix.Helix;

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
public class ListenerCommandSeen extends ListenerAdapter {

    @Override
    public void onMessage(MessageEvent event) throws Exception {
        if (event.getMessage().startsWith(Helix.botPrefix + Commands.seen + " ") && event.getMessage().length() > (Commands.seen.length() + 2)){
            String m;
            String user = event.getMessage().split(" ", 2)[1].replace(" ", "");
            try {
                JSONObject u = Helix.seen.getJSONObject(user.toLowerCase());
                String time = HumanTime.approximately(System.currentTimeMillis()-u.getLong("date"));
                m = "User " + u.getString("nick") + " was seen " + time + " ago, on " + u.getString("chan") + " :" + u.getString("msg");
            } catch (JSONException e) {
                m = "User " + user + " hasn't been seen!";
            }
            event.getChannel().send().message(m);
        }
    }
}
