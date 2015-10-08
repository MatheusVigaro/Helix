package tk.vigaro.helix.listener;

import com.sun.xml.internal.fastinfoset.util.CharArray;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import tk.vigaro.helix.Commands;
import tk.vigaro.helix.Helix;

import java.io.*;
import java.nio.CharBuffer;
import java.util.*;

/**
 * Created by artur on 6/13/15.
 * Arrumar a resposta da letra!.
 *  if (letra.compareTo(word.charAt(word.length()-i)) > 0)
 */
public class ListenerCommandForca extends ListenerAdapter {
    boolean breaker =true;
    String word;
    Character[] worda;
    Character[] misterious;
    Character letra;
    Map<String, String> map = new HashMap<String, String>();
    @Override
    public void onMessage(MessageEvent event) throws IOException {

        if(event.getMessage().startsWith(Helix.botPrefix + Commands.forca) && map.isEmpty()) {
                String wordlist = "Wordlist.txt";

                FileReader filereader = new FileReader(wordlist);
                BufferedReader bufferedreader = new BufferedReader(filereader);

                String line;
                List<String> words = new ArrayList<String>();
                while ((line = bufferedreader.readLine()) != null) words.add(line);
                word = words.get((new Random()).nextInt(words.size()));
            //word = "asdfg";

                int z =0;
                while (z < word.length()) {
                    misterious[z] = '_';
                    worda[z] = word.charAt(z);
                }
                //breaker =false;

                map.put(event.getChannel().getName(), word);
                event.getChannel().send().message("O jogo da forca começouo: A palavra tem: " + word.length() + "  " + misterious);
                event.getChannel().send().message("Use .letra para chutar uma letra; Use .palavra para chutar uma palavra");
            }

        else if (event.getMessage().startsWith(Helix.botPrefix + Commands.forca)){
            breaker=false;
            event.getChannel().send().message("Já existe um jogo em andamento");
            event.getChannel().send().message("A palavra tem: " + word.length() + "  " + misterious);
            event.getChannel().send().message("Use .letra para chutar uma letra; Use .palavra para chutar uma palavra");

        }else if (event.getMessage().startsWith(Helix.botPrefix + Commands.letra)) {

            letra = event.getMessage().charAt(event.getMessage().length() - 1);
            breaker = false;
            boolean n = false;
            //StringBuilder stringbuilder = new StringBuilder(misterious);
            for (int i = 0; i<= word.length()-1; i++) {
                //letra.append(event.getMessage().charAt(event.getMessage().length()-1);
                if(letra.equals(worda[i])){
                    misterious[i] =  letra;
                    //stringbuilder.toString();
                    n = true;
                }
                else  if (letra.compareTo(word.charAt(word.length()-i)) > 0){
                    misterious[i] = letra;
                    //stringbuilder.toString();
                    n = true;
                    //event.getChannel().send().message("YAY "+event.getUser()+"acertou a letra : "+ letra + " : " + misterious);
                }

            }
            //misterious = stringbuilder.toString();
            if (n){
                event.getChannel().send().message("YAY "+event.getUser().getNick()+" acertou a letra : "+ letra + " : " + misterious);
            }
            else{
                event.getChannel().send().message("YAY "+event.getUser().getNick() +" errou a letra : "+ letra + " : " + misterious);
            }
        }
        else if (event.getMessage().startsWith(Helix.botPrefix + Commands.palavra)){
            if(event.getMessage().endsWith(word)){
                event.getChannel().send().message("YAY "+ event.getUser().getNick()+" acertou a palavra : "+word);
            }
        }

    }
}
