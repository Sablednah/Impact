package me.sablednah.impact;

import org.bukkit.ChatColor;
import org.bukkit.Location;

import com.massivecraft.factions.entity.BoardColls;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.mcore.ps.PS;


public class FactionsUtils {
    
    public static String getFactName(Location l) {
        if (Impact.hasFactions) {
            Faction fact = BoardColls.get().getFactionAt(PS.valueOf(l));
            
            String factName;
            
            if (fact != null) {
                factName = fact.getName();
            } else {
                factName = null;
            }
            
            factName = factName.toLowerCase();
            factName = ChatColor.stripColor(factName);
            
            return factName;
        }
        return null;
    }
}
