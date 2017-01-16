package spamcop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

public class SpamCop extends JavaPlugin implements Listener{

    public SpamCopEvents sce = new SpamCopEvents(this);
    private static HashMap<UUID, List<String>> playerMessages;
    private static HashMap<UUID, StringBuilder> lastMessage;
    private static HashMap<UUID, Integer> timesWarned;
    private static HashMap<UUID, BukkitTask> kickReset;
    public static String tag = ChatColor.RED + "[Spam" + ChatColor.BLUE + "Cop] ";

    
    @Override
    public void onEnable(){
        File SpamCopOptions = getDataFolder();
        if ((!SpamCopOptions.exists()) && 
            (!SpamCopOptions.mkdir())) {
            System.out.println("[SpamCop] Could not create directory for options!");
         }
        loadOptions(new File (getDataFolder(), "options.txt"));
        this.getServer().getPluginManager().registerEvents(sce, this);
        setPlayerMessages(new HashMap<>());
        setLastMessage(new HashMap<>());
        setTimesWarned(new HashMap<>());
        setKickReset(new HashMap<>());
    }
    
    @Override
    public void onDisable(){
        
    }
    
    public void saveOptions(Object uuid, File options){
        try{
            if (!options.exists()) {
                options.createNewFile();
            }
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(options));
            oos.writeObject(uuid);
            oos.flush();
            oos.close();
        }catch (Exception e){}
    }
  
    public Object loadOptions(File options){
        try{
            if(!options.exists()) {
                options.createNewFile();
                SpamCopOptions.writeDefaults(options);
            }
            SpamCopOptions.parseOptionArguments(options);
            SpamCopOptions.setOptions();
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(options));
            Object returnToggles = ois.readObject();
            ois.close();
            return returnToggles;
        }catch (Exception e) {
            return null;
        } 
    }
    
    public static HashMap<UUID, StringBuilder> getLastMessage() {
        return lastMessage;
    }

    
    public static void setLastMessage(HashMap<UUID, StringBuilder> aLastMessage) {
        lastMessage = aLastMessage;
    }
    
    public static HashMap<UUID, List<String>> getPlayerMessages() {
        return playerMessages;
    }

    public static void setPlayerMessages(HashMap<UUID, List<String>> playerMsgs) {
        playerMessages = playerMsgs;
    }
    
    public static HashMap<UUID, Integer> getTimesWarned() {
        return timesWarned;
    }

    public static void setTimesWarned(HashMap<UUID, Integer> aTimesWarned) {
        timesWarned = aTimesWarned;
    }

    public static HashMap<UUID, BukkitTask> getKickReset() {
        return kickReset;
    }

    public static void setKickReset(HashMap<UUID, BukkitTask> aKickReset) {
        kickReset = aKickReset;
    }
    
    
    
    
}
