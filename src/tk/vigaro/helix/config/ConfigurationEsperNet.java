package tk.vigaro.helix.config;

import com.google.common.reflect.ClassPath;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
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
        this.setName(Helix.properties.getProperty("irc.nickname"));
        this.setFinger("VBot");
        this.setVersion("VBot");
        this.setRealName("VBot");
        this.setAutoNickChange(true);
        this.setLogin(Helix.properties.getProperty("irc.nickserv.login"));
        this.setNickservPassword(Helix.properties.getProperty("irc.nickserv.pw"));
        this.setServer("irc.esper.net", 6697);
        this.setSocketFactory(new UtilSSLSocketFactory().trustAllCertificates());
        JSONArray chans = new JSONArray(Helix.properties.getProperty("irc.channels"));
        for (int i = 0; i < chans.length();i++) this.addAutoJoinChannel(chans.getString(i));
        ClassPath classPath = null;
        try {
            classPath = ClassPath.from(Thread.currentThread().getContextClassLoader());
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (ClassPath.ClassInfo classInfo : classPath.getTopLevelClasses("tk.vigaro.helix.listener")) {
            this.addListener(((Class<? extends ListenerAdapter>) classInfo.load()).newInstance());
        }
    }
}
