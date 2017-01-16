package spamcop;

import java.util.UUID;
import org.bukkit.scheduler.BukkitRunnable;

public class SpamCopKickWorker extends BukkitRunnable{
    
    public UUID uuid;
    
    public SpamCopKickWorker(UUID uuid){
        this.uuid = uuid;   
    }

    //Runs for a given amount of time to see if a player should be kicked for excessive spamming
    
    @Override
    public void run() {
        SpamCop.getTimesWarned().remove(uuid);
        SpamCop.getKickReset().remove(uuid, this);
        this.cancel();
    }

}
