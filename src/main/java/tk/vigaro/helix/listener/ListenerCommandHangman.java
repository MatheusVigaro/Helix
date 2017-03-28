package tk.vigaro.helix.listener;

import org.apache.commons.lang3.StringUtils;
import org.pircbotx.Colors;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import tk.vigaro.helix.Commands;
import tk.vigaro.helix.Helix;

public class ListenerCommandHangman extends ListenerAdapter {

    private final int WORD = 0;
    private final int PARTIAL = 1;
    private final int LETTERS = 2;
    private final int LIFES = 3;

    @Override
    public void onMessage(MessageEvent event) {
        if (event.getMessage().startsWith(Helix.botPrefix + Commands.hangman)) {
            if (!Helix.hangman.containsKey(event.getChannel().getChannelId())) {
                String word = Helix.wordList.get(Helix.random.nextInt(Helix.wordList.size()-1));
                String blank = "";
                for (int i = 0;i < word.length();i++) {
                    blank += word.charAt(i) == '-' ? "-" : "_";
                }
                Helix.hangman.put(event.getChannel().getChannelId(), new String[]{word, blank, "", String.valueOf(Math.round(3+(word.length()/2)))});
                event.getChannel().send().message("O jogo da forca comecou! [" + getHangman(event)[1] + "] " + StringUtils.countMatches(getHangman(event)[1], "_") + " letras, " + getHangman(event)[LIFES] + " vidas");
            } else {
                event.getChannel().send().message("Um jogo ja esta em progresso! [" + getHangman(event)[1] + "] " + StringUtils.countMatches(getHangman(event)[1], "_") + " letras e " + getHangman(event)[LIFES] + " vidas restantes" );
            }
        } else if (event.getMessage().startsWith(Helix.botPrefix + Commands.letter) && event.getMessage().length() > Commands.letter.length() + 2) {
            if (Helix.hangman.containsKey(event.getChannel().getChannelId())) {
                String letter = event.getMessage().split(" ", 2)[1].substring(0, 1);
                String[] status = getHangman(event);
                if (!status[LETTERS].contains(letter)) {
                    status[LETTERS] += letter;
                    if (status[WORD].contains(letter)) {
                        char[] chars = status[WORD].toCharArray();
                        char letterc = letter.charAt(0);
                        for (int i = 0; i < chars.length; i++) {
                            if (letterc == chars[i]) {
                                char[] s = status[PARTIAL].toCharArray();
                                s[i] = letterc;
                                status[PARTIAL] = new String(s);
                            }
                        }
                        if (!status[PARTIAL].contains("_")) {
                            event.getChannel().send().message("Palavra completa! [" + status[PARTIAL] + "]");
                            endGame(event);
                        } else {
                            event.getChannel().send().message("Letra encontrada! [" + status[PARTIAL].replaceAll(letter, Colors.BOLD + letter + Colors.BOLD) + "] " + StringUtils.countMatches(status[PARTIAL], "_") + " letras restantes");
                        }


                    } else {
                        status[LIFES] = String.valueOf(Integer.valueOf(status[LIFES])-1);
                        event.getChannel().send().message("Letra nao encontrada! " + Colors.BOLD + letter + Colors.BOLD + ", " + status[LIFES] + " vidas restantes");
                    }
                } else {
                    event.getChannel().send().message("Letra ja foi utilizada! " + Colors.BOLD + letter);
                }
            }


        } else if (event.getMessage().startsWith(Helix.botPrefix + Commands.word) && event.getMessage().length() > Commands.word.length() + 2) {
            if (getHangman(event)[0].equals(event.getMessage().split(" ", 2)[1].replace(" ", ""))) {
                getHangman(event)[LIFES] = String.valueOf(Integer.valueOf(getHangman(event)[LIFES])-1);
                event.getChannel().send().message("Palavra correta! [" + getHangman(event)[0] + "] " + getHangman(event)[LIFES] + " vidas restantes");
                endGame(event);
            } else {
                event.getChannel().send().message("Palavra incorreta! [" + getHangman(event)[1] + "]");
            }
        } else if ((event.getMessage().equals(Helix.botPrefix + Commands.word)||event.getMessage().equals(Helix.botPrefix + Commands.letter)) && Helix.hangman.containsKey(event.getChannel().getChannelId())) {
            event.getChannel().send().message("Letras utilizadas: " + getHangman(event)[LETTERS] + ", " + getHangman(event)[LIFES] + " vidas restantes");
        }

    }

    private void endGame(MessageEvent event) {Helix.hangman.remove(event.getChannel().getChannelId());}

    private String[] getHangman(MessageEvent event) {
        return Helix.hangman.get(event.getChannel().getChannelId());
    }
}

