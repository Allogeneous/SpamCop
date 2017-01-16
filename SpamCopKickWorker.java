/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spamcop;

import java.util.UUID;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Joshua
 */
public class SpamCopKickWorker extends BukkitRunnable{
    
    public UUID uuid;
    
    public SpamCopKickWorker(UUID uuid){
        this.uuid = uuid;   
    }

    @Override
    public void run() {
        SpamCop.getTimesWarned().remove(uuid);
        SpamCop.getKickReset().remove(uuid, this);
        this.cancel();
    }
    
    public void externalCancel(){
        this.cancel();
    }
    
}
