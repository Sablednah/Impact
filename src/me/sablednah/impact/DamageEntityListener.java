package me.sablednah.impact;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class DamageEntityListener implements Listener {

	public Impact plugin;

	public DamageEntityListener(Impact instance) {
		this.plugin=instance;
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onEntityDamage(EntityDamageEvent event){
		if (event.getEntity() instanceof Player) {
			if( ((Player) event.getEntity()).hasPermission("impact.nocrater") ) {
				return; // exempt
			}
		}
		if (event.getCause() != DamageCause.FALL) {
			return;  //No falling occured here!
		}
		Entity target = event.getEntity();
		float fallDistance = target.getFallDistance();
		if (Impact.debugMode) {
			System.out.print(event.getEntity().getType() + " fell " + fallDistance);
		}
		if (fallDistance > 5) {
			float explody = ((fallDistance-5)/10)+1;
			if (target instanceof Player) {
				//set temporary invulnerability so explosion doesn't kill entity (although the fall still can).
				((Player) target).setMaximumNoDamageTicks(20);
				((Player) target).setNoDamageTicks(0);
				((Player) target).setLastDamage(99);
			}
			target.getWorld().createExplosion(target.getLocation(), explody);
		}
	}
}
