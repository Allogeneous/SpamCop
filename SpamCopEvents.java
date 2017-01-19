package spamcop;

import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitTask;

public class SpamCopEvents implements Listener{
    public SpamCop plugin;
    
    public SpamCopEvents(SpamCop instance){
        plugin = instance;
    }
    
    @EventHandler
    public void processChat(AsyncPlayerChatEvent e){
        
        //Different types of cancellations for chat
        
        boolean isCancelledTimerDouble = false;
        boolean isCancelledLastDouble =  false; 
        boolean isCancelledLetters = false;
        boolean isCancelledWords = false;
        boolean isCancelledLettersPercent = false;
        boolean isCancelledWordsPercent = false;
        boolean isCancelledLength = false;
        
        
        Player p = e.getPlayer();
        String msg = e.getMessage();
        String savedLastMsg = "";
        
        //Adds player to a HashMap of recently said messagees
        
        if(SpamCopOptions.useMessageRepeatTimer && !p.hasPermission("spamCop.canBypassDoubleMessagesTimer")){
            if(!SpamCop.getPlayerMessages().containsKey(e.getPlayer().getUniqueId())){
                List<String> msgs = new ArrayList<>(); 
                SpamCop.getPlayerMessages().put(e.getPlayer().getUniqueId(), msgs);
            }
        }
        //Trys to load the players last saved message
        if(SpamCopOptions.useMessageRepeatTimer && !p.hasPermission("spamCop.canBypassDoubleMessages")){
            if(SpamCop.getLastMessage().containsKey(p.getUniqueId())){
                savedLastMsg = SpamCop.getLastMessage().get(p.getUniqueId()).toString();
            }
        }
        //Cancelles chat message if the message is an exact repeat
        
        if(!p.hasPermission("spamCop.canBypassDoubleMessages") && SpamCopOptions.useNoRepeatMessage && SpamCop.getLastMessage().containsKey(p.getUniqueId())){
            if(msg.equalsIgnoreCase(savedLastMsg)){
                isCancelledLastDouble = true;
            }
        }
        
         //Cancelles chat message if the message contains no spaces and is greater than or equal to a given amount
        
        if(!p.hasPermission("spamCop.canBypassMaxWordLength") && SpamCopOptions.useMaxSingleWordMessageLength && isCancelledLastDouble == false && !msg.contains(" ") && msg.length() >= SpamCopOptions.maxWordLength){
            isCancelledLength = true;
        }
        
         //Cancelles chat message if the player has said this message within a give amount of time

        if(!p.hasPermission("spamCop.canBypassDoubleMessagesTimer") && SpamCopOptions.useMessageRepeatTimer && !SpamCop.getPlayerMessages().get(p.getUniqueId()).isEmpty() && isCancelledLastDouble == false && isCancelledLength == false){
            for(String oldMsg : SpamCop.getPlayerMessages().get(p.getUniqueId())){
                if(oldMsg.equalsIgnoreCase(msg)){
                    isCancelledTimerDouble = true;
                    break;
                }
            }
        }   
         //Cancelles chat message if the message contains a given percentage of similar characters in the same order as one of the players pervious messages saved in tge code block above 
           
            
        if(!p.hasPermission("spamCop.canBypassCharacterPercent") && SpamCopOptions.useMatchingLetters && isCancelledLastDouble == false && isCancelledTimerDouble == false && isCancelledLength == false && SpamCop.getLastMessage().containsKey(p.getUniqueId())){
            if(SpamCopFindPercentages.findStringPercentSimilarity(msg, savedLastMsg, SpamCopFindPercentages.findLevDistance(msg, savedLastMsg)) >= (SpamCopOptions.characterPercent / 100)){
                isCancelledLettersPercent = true;
           }           
        }        
        
        if(!p.hasPermission("spamCop.canBypassWordPercent") && SpamCopOptions.useMatchingWords && isCancelledLastDouble == false &&  isCancelledTimerDouble == false && isCancelledLettersPercent == false && isCancelledLength == false && SpamCop.getLastMessage().containsKey(p.getUniqueId())){
             if(msg.contains(" ")){
                 if(SpamCopFindPercentages.findWordPercent(msg, savedLastMsg, " ") >= SpamCopOptions.wordPercent / 100){
                     isCancelledWordsPercent = true;
                 }
             }
        }
        
        
       //Cancelles chat message if the message contains more than a given number of the same character in a row
       
        if(!p.hasPermission("spamCop.canUseRepeatLetters") && SpamCopOptions.useRepeatLetters && isCancelledLastDouble == false &&  isCancelledTimerDouble == false && isCancelledLettersPercent == false && isCancelledLength == false && isCancelledWordsPercent == false){
            char[] letters = msg.toCharArray();
            char last;
            int repeats = 0;
            
            for(int i = 1; i < letters.length; i++){
                last = letters[i - 1];
                if(Character.toLowerCase(last) == Character.toLowerCase(letters[i])){
                    repeats++;
                    
                }else{
                    repeats = 0;
  
                }
                if(repeats >= SpamCopOptions.repeatLetters){
                    isCancelledLetters = true;
                    break;
                }
            }
        }
        
        //Cancelles chat message if the message contains more than a given number of the same character in a row
       
        if(!p.hasPermission("spamCop.canUseRepeatWords") && SpamCopOptions.useRepeatWords && isCancelledLastDouble == false &&  isCancelledTimerDouble == false && isCancelledLettersPercent == false && isCancelledLength == false && isCancelledWordsPercent == false && isCancelledLetters == false){
                if(msg.contains(" ")){
                    String[] words = msg.split(" ");
                    String last;
                    int repeats = 0;
                
                    for(int i = 1; i < words.length; i++){
                        last = words[i - 1];
                            if(words[i].equalsIgnoreCase(last)){
                                repeats++;
                            }else{
                                repeats = 0;
                            }
                        if(repeats >= SpamCopOptions.repeatWords){
                            isCancelledWords = true;
                            break;
                        }
                    }
            }
            
        }
        
        //Some code to check and see if the player is trying to correct a mistake in chat
        if(p.hasPermission("spamCop.canUseCorrections") && SpamCopOptions.useCorrectionFilter){
            char first = msg.charAt(0);
            char last = msg.charAt(msg.length() - 1);
        
            try{
                String[] words = msg.split(" ");
                if(containsCorrection(p) && first == '*' && words.length < SpamCopOptions.correctionsWordLeght && !SpamCop.getPlayerMessages().get(p.getUniqueId()).contains(msg) && msg.length() < SpamCopOptions.correctionsMessageLength){
                    if(isCancelledLetters == false){
                        isCancelledLettersPercent = false;
                        isCancelledTimerDouble = false;
                    }
                }
            }catch(Exception ex){
                if(containsCorrection(p) && first == '*' && !SpamCop.getPlayerMessages().get(p.getUniqueId()).contains(msg) && msg.length() < SpamCopOptions.correctionsMessageLength){
                    if(isCancelledLetters == false){
                        isCancelledLettersPercent = false;
                        isCancelledTimerDouble = false;
                    }
                }
            }
        }
            
            //Cancelles chat event and sends the player the approptiate message
        
            if(isCancelledLetters == true || isCancelledLettersPercent == true || isCancelledTimerDouble == true || isCancelledLastDouble == true || isCancelledLength == true || isCancelledWordsPercent == true || isCancelledWords == true){
                e.setCancelled(true);
                if(!p.hasPermission("spamCop.canBypassSpamKickTimer") && SpamCopOptions.useWarnTimer){
                    if(SpamCop.getTimesWarned().containsKey(p.getUniqueId())){
                        Integer times = SpamCop.getTimesWarned().get(p.getUniqueId());
                        times++;
                        SpamCop.getTimesWarned().put(p.getUniqueId(), times);
                    }else{
                        SpamCop.getTimesWarned().put(p.getUniqueId(), 1);
                    }
                }
                if(isCancelledLastDouble == true){
                    p.sendMessage(SpamCop.tag + ChatColor.DARK_RED + "You can't say the same message twice in a row!");
                }else if(isCancelledLetters == true){
                    p.sendMessage(SpamCop.tag + ChatColor.DARK_RED + "You can't say more than " + SpamCopOptions.repeatLetters + " of the same character in a row!");
                }else if(isCancelledTimerDouble == true){
                    p.sendMessage(SpamCop.tag + ChatColor.DARK_RED + "You can't say the same message that fast!");
                }else if(isCancelledLettersPercent == true){
                    p.sendMessage(SpamCop.tag + ChatColor.DARK_RED + "You can't say this message due to a high amount of matching characters!");
                }else if(isCancelledLength == true){
                    p.sendMessage(SpamCop.tag + ChatColor.DARK_RED + "You can't say a word that is that long!");
                }else if(isCancelledWordsPercent == true){
                    p.sendMessage(SpamCop.tag + ChatColor.DARK_RED + "You can't say this message due to a high amount of matching words!"); 
                }else if(isCancelledWords == true){
                    p.sendMessage(SpamCop.tag + ChatColor.DARK_RED + "You can't say more than " + SpamCopOptions.repeatWords + " of the same word in a row!");
                }
                
                if(!p.hasPermission("spamCop.canBypassSpamKickTimer") && SpamCopOptions.useWarnTimer){
                    if(SpamCop.getTimesWarned().get(p.getUniqueId()) >= SpamCopOptions.spamTimesToKick){
                    Bukkit.getScheduler().runTask(plugin, new Runnable() {
                        @Override
                        public void run() {
                            SpamCop.getKickReset().get(p.getUniqueId()).cancel();
                            SpamCop.getKickReset().remove(p.getUniqueId());
                            SpamCop.getTimesWarned().remove(p.getUniqueId());
                            p.kickPlayer(SpamCop.tag + ChatColor.DARK_RED + "You have been kicked for having more than " + SpamCopOptions.spamTimesToKick + " spam warnings in the last " + SpamCopOptions.kickTimerSpeed + " minutes!");
                        }
                    });
                    
                }
                
                if(!SpamCop.getKickReset().containsKey(p.getUniqueId())){
                    BukkitTask kicker = new SpamCopKickWorker(p.getUniqueId()).runTaskLaterAsynchronously(this.plugin, SpamCopOptions.kickTimerSpeed * 1200);
                    SpamCop.getKickReset().put(p.getUniqueId(), kicker);
                } 
               }
           }
          
        //Starts timer for being able to say a repeat message again

            if(SpamCopOptions.useMessageRepeatTimer && !p.hasPermission("spamCop.canBypassDoubleMessagesTimer")){
                if(!SpamCop.getPlayerMessages().get(p.getUniqueId()).contains(msg)){
                    SpamCop.getPlayerMessages().get(p.getUniqueId()).add(msg);
                    BukkitTask remover = new SpamCopWordListWorker(p.getUniqueId(), msg).runTaskLater(this.plugin, SpamCopOptions.repeatTimerSpeed * 20);
                }
            }
        
        //Saves the players last said message
        if(SpamCopOptions.useNoRepeatMessage && !p.hasPermission("spamCop.canBypassDoubleMessages")){
            if(SpamCop.getLastMessage().containsKey(p.getUniqueId())){
                SpamCop.getLastMessage().get(p.getUniqueId()).setLength(0);
                SpamCop.getLastMessage().get(p.getUniqueId()).append(msg);
            }else{
                StringBuilder lastMsg = new StringBuilder(msg);
                SpamCop.getLastMessage().put(p.getUniqueId(), lastMsg);
            }
        }
    }
    
    //Checks if the first character in a message is a '*'
    
    public boolean containsCorrection(Player p){
        if(SpamCop.getPlayerMessages().containsKey(p.getUniqueId())){
            for(String msg : SpamCop.getPlayerMessages().get(p.getUniqueId())){
                if(msg.charAt(0) == '*'){
                    return true;
                }
            }
        }
        return false;
    }
    
}
