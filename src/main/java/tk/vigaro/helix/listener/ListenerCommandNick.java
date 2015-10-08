package tk.vigaro.helix.listener;

import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.WaitForQueue;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.NickChangeEvent;
import org.pircbotx.hooks.events.ServerResponseEvent;
import tk.vigaro.helix.Commands;
import tk.vigaro.helix.Helix;
import tk.vigaro.helix.Util;

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
public class ListenerCommandNick extends ListenerAdapter {

    @Override
    public void onMessage(MessageEvent event) throws Exception {
        if (event.getMessage().startsWith(Helix.botPrefix + Commands.nick + " ") && event.getMessage().length() > (Commands.nick.length() + 2) && Arrays.asList(Helix.admins).contains(event.getUser().getLogin().toLowerCase()) && Util.isVerified(event.getUser())) {
            String newNick = event.getMessage().split(" ", 2)[1];
            WaitForQueue queue = new WaitForQueue(event.getBot());
            event.getBot().sendIRC().changeNick(newNick);
            while (true) {
                Event currentEvent = queue.waitFor(Arrays.asList(ServerResponseEvent.class, NickChangeEvent.class));
                if (currentEvent instanceof ServerResponseEvent) {
                    switch (((ServerResponseEvent)currentEvent).getCode()) {
                        case 431:
                            event.getUser().send().notice("No nickname given");
                            return;
                        case 432:
                            event.getUser().send().notice("Erroneous nickname");
                            return;
                        case 433:
                            event.getUser().send().notice("Nickname is already in use");
                            return;
                    }
                } else if (currentEvent instanceof NickChangeEvent && ((NickChangeEvent)currentEvent).getUser() == event.getBot().getUserBot()) return;
            }
        }
    }
}
