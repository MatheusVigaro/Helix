package tk.vigaro.helix.listener;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import tk.vigaro.helix.Util;

import static tk.vigaro.helix.Helix.admins;

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
public class LIstenerPrivateMessage extends ListenerAdapter {

    @Override
    public void onPrivateMessage(PrivateMessageEvent event) throws Exception {
        if (event.getMessage().contains(" ") && !event.getUser().getNick().contains(".") && !event.getUser().getNick().endsWith("Serv") && admins.contains(Util.getLogin(event.getUser()).toLowerCase()) && Util.isVerified(event.getUser())) {
            String m[] = event.getMessage().split(" ", 2);
            event.getBot().sendIRC().message(m[0], m[1]);
        }
    }
}
