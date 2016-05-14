package tk.vigaro.helix.listener;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import tk.vigaro.helix.Commands;
import tk.vigaro.helix.Helix;
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
public class ListenerCommandKill extends ListenerAdapter {

    @Override
    public void onMessage(MessageEvent event) throws Exception {
        if (event.getMessage().startsWith(Helix.botPrefix + Commands.kill) && admins.contains(Util.getLogin(event.getUser()).toLowerCase()) && Util.isVerified(event.getUser())) {
            event.getBot().stopBotReconnect();
            event.getBot().sendIRC().quitServer("Killed by: " + event.getUser().getNick());
        }
    }
}
