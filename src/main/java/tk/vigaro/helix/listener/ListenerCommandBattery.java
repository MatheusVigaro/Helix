package tk.vigaro.helix.listener;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import tk.vigaro.helix.Commands;
import tk.vigaro.helix.Helix;

import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
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

public class ListenerCommandBattery extends ListenerAdapter {

    @Override
    public void onMessage(MessageEvent event) throws Exception {
        if (event.getMessage().equals(Helix.botPrefix + Commands.battery)) {
            List<String> bat = Files.readAllLines(FileSystems.getDefault().getPath("/sys/devices/platform/battery_manager/power_supply/battery/uevent"), Charset.defaultCharset());
            List<String> ac = Files.readAllLines(FileSystems.getDefault().getPath("/sys/devices/platform/battery_manager/power_supply/ac/uevent"), Charset.defaultCharset());
            List<String> usb = Files.readAllLines(FileSystems.getDefault().getPath("/sys/devices/platform/battery_manager/power_supply/usb/uevent"), Charset.defaultCharset());
            String status = bat.get(1).split("=")[1];
            String health = bat.get(2).split("=")[1];
            String temp = String.valueOf(Float.parseFloat(bat.get(4).split("=")[1])/10);
            String charge = bat.get(7).split("=")[1];
            String source = null;
            if ("1".equals(ac.get(1).split("=")[1])) {
                if ("0".equals(usb.get(1).split("=")[1])) {
                    source = "AC";
                } else if ("1".equals(usb.get(1).split("=")[1])) {
                    source = "USB";
                } else {
                    source = "None";
                }
            }

            event.getChannel().send().message("Status: " + status + " | Health: " + health + " | Temp: " + temp + "ºC | Charge: " + charge + "% | Power Source: " + source);

        }
    }
}
