package tk.vigaro.helix.listener;

import org.apache.commons.lang3.StringUtils;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import tk.vigaro.helix.Commands;
import tk.vigaro.helix.Helix;
import tk.vigaro.helix.Util;

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
 * You should have received Helix copy of the GNU Lesser General Public
 * License along with this library.
 */
public class ListenerCommandHangman extends ListenerAdapter {

    @Override
    public void onMessage(MessageEvent event) {
        if (event.getMessage().replace(" ", "").equals(Helix.botPrefix + Commands.letter) && Helix.hangman.containsKey(event.getChannel().getChannelId())) {
            event.getChannel().send().message("Letras utilizadas: " + Util.join("-", getHangman(event)[2]));
        } else if (event.getMessage().startsWith(Helix.botPrefix + Commands.letter) && event.getMessage().length() > Commands.letter.length() + 2) {
            if (Helix.hangman.containsKey(event.getChannel().getChannelId())) {
                String letter = event.getMessage().split(" ", 2)[1].substring(0, 1);
                String[] status = getHangman(event);
                if (!status[2].contains(letter)) {
                    status[2] += letter;
                    if (status[0].contains(letter)) {
                        char[] chars = status[0].toCharArray();
                        char letterc = letter.charAt(0);
                        for (int i = 0; i < chars.length; i++) {
                            if (letterc == chars[i]) {
                                char[] s = status[1].toCharArray();
                                s[i] = letterc;
                                status[1] = new String(s);
                            }
                        }
                        if (!status[1].contains("_")) {
                            event.getChannel().send().message("Palavra completa! [" + status[1] + "]");
                            endGame(event);
                        } else {
                            event.getChannel().send().message("Letra encontrada! [" + status[1].replaceAll(letter, Colors.BOLD + letter + Colors.BOLD) + "] " + StringUtils.countMatches(status[1], "_") + " letras restantes");
                        }


                    } else {
                        event.getChannel().send().message("Letra nao encontrada! " + Colors.BOLD + letter);
                    }
                } else {
                    event.getChannel().send().message("Letra ja foi utilizada! " + Colors.BOLD + letter);
                }
            }


        } else if (event.getMessage().startsWith(Helix.botPrefix + Commands.word) && event.getMessage().length() > Commands.word.length() + 2) {
            if (getHangman(event)[0].equals(event.getMessage().split(" ", 2)[1].replace(" ", ""))) {
                event.getChannel().send().message("Palavra correta! [" + getHangman(event)[0] + "]");
                endGame(event);
            } else {
                event.getChannel().send().message("Palavra incorreta! [" + getHangman(event)[1] + "]");
            }
        } else if (event.getMessage().startsWith(Helix.botPrefix + Commands.hangman)) {
            if (!Helix.hangman.containsKey(event.getChannel().getChannelId())) {
                String word = Helix.wordList.get(Helix.random.nextInt(Helix.wordList.size() - 1));
                String blank = "";
                for (int i = 0; i < word.length(); i++) {
                    blank += word.charAt(i) == '-' ? "-" : "_";
                }
                Helix.hangman.put(event.getChannel().getChannelId(), new String[]{word, blank, ""});
                event.getChannel().send().message("O jogo da forca comecou! [" + getHangman(event)[1] + "] " + StringUtils.countMatches(getHangman(event)[1], "_") + " letras");
            } else {
                event.getChannel().send().message("Um jogo ja esta em progresso! [" + getHangman(event)[1] + "] " + StringUtils.countMatches(getHangman(event)[1], "_") + " letras restantes");
            }
        }

    }

    private void endGame(MessageEvent event) {
        Helix.hangman.remove(event.getChannel().getChannelId());
    }

    private String[] getHangman(MessageEvent event) {
        return Helix.hangman.get(event.getChannel().getChannelId());
    }
}

