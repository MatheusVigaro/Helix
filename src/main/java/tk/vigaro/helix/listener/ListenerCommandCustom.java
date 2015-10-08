package tk.vigaro.helix.listener;

import org.apache.commons.lang3.StringUtils;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
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
public class ListenerCommandCustom extends ListenerAdapter {

    @Override
    public void onMessage(MessageEvent event) throws Exception {
        if (event.getMessage().startsWith(Helix.botPrefix)) {
            String[] s = event.getMessage().split(" ", 2);
            String command = s[0].substring(1).toLowerCase();
            if (Helix.commands.has(command)) {
                String m = s.length > 1 ? Colors.BOLD + StringUtils.join(s[1].split(" ")) + Colors.NORMAL + ": " + Helix.commands.getString(command) : Helix.commands.getString(command);
                event.getChannel().send().message(m);

            }
        }
    }
}
