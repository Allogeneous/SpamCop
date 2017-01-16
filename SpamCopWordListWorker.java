package spamcop;

import java.util.UUID;
import org.bukkit.scheduler.BukkitRunnable;


public class SpamCopWordListWorker extends BukkitRunnable{
    
    private final UUID uuid;
    private final String msg;
    
    public SpamCopWordListWorker(UUID uuid, String msg){
  
        this.uuid = uuid;
        this.msg = msg;
    }

    @Override
    public void run() {
        
        if(SpamCop.getPlayerMessages().containsKey(uuid)){
            SpamCop.getPlayerMessages().get(uuid).remove(msg);
        }
        
    }
    
}
