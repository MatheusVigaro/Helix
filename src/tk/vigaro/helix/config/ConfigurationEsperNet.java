package tk.vigaro.helix.config;

import com.google.common.reflect.ClassPath;
import org.json.JSONArray;
import org.pircbotx.Configuration;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.hooks.ListenerAdapter;
import tk.vigaro.helix.Helix;

import java.io.IOException;

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
public class ConfigurationEsperNet extends Configuration.Builder {

    public ConfigurationEsperNet() throws IllegalAccessException, InstantiationException {
        this.setName("true".equals(System.getProperty("helix.isDebug")) ? Helix.properties.getProperty("irc.nickname") + "|debug" : Helix.properties.getProperty("irc.nickname"));
        this.setFinger("VBot");
        this.setVersion("VBot");
        this.setRealName("VBot");
        this.setAutoNickChange(true);
        this.setLogin(Helix.properties.getProperty("irc.nickserv.login"));
        this.setNickservPassword(Helix.properties.getProperty("irc.nickserv.pw"));
        this.setServer("irc.esper.net", 6697);
        this.setSocketFactory(new UtilSSLSocketFactory().trustAllCertificates());
        this.setAutoReconnect(true);
        JSONArray chans = new JSONArray(Helix.properties.getProperty("irc.channels"));
        for (int i = 0; i < chans.length();i++) this.addAutoJoinChannel(chans.getString(i));
        this.setListenerManager(Helix.backgroundListenerManager);
        ClassPath classPath = null;
        try {
            classPath = ClassPath.from(Thread.currentThread().getContextClassLoader());
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (ClassPath.ClassInfo classInfo : classPath.getTopLevelClasses("tk.vigaro.helix.listener")) {
            this.addListener(((Class<? extends ListenerAdapter>) classInfo.load()).newInstance());
        }
        for (ClassPath.ClassInfo classInfo : classPath.getTopLevelClasses("tk.vigaro.helix.backgroundlistener")) {
            Helix.backgroundListenerManager.addListener(((Class<? extends ListenerAdapter>) classInfo.load()).newInstance(), true);
        }
    }
}
