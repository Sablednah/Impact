package me.sablednah.impact;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class Stomp {
    
    public static void stomp(Location from, Entity stomper, int radius, int damage) {
        stomp(from, stomper, radius, damage, 0.0D);
    }
    
    public static void stomp(Location from, Entity stomper, int radius, int damage, double spread) {
        for (Entity bounced : stomper.getNearbyEntities(radius, radius, radius)) {
            if (bounced != stomper) {
                if (bounced instanceof Player) {
                    if (!Impact.playersHeavy) {
                        Player p = (Player) bounced;
                        // p.damage(damage, stomper);
                        Vector v = p.getVelocity();
                        v.setY(v.getY() + 1.5);
                        p.setVelocity(v);
                    }
                } else if ((bounced instanceof Monster)) {
                    Monster c = (Monster) bounced;
                    String type = c.getType().name();
                    if (Impact.heavyMobs.contains("All") || Impact.heavyMobs.contains(type)) {
                        // c.damage(damage, stomper); // damage
                        Vector vc = c.getVelocity();
                        vc.setY(vc.getY() + 1.5);
                        c.setVelocity(vc);
                    }
                }
            }
        }
        
        // from.getWorld().strikeLightningEffect(from);
        Block block = from.getBlock();
        
        ArrayList<BlockState> blocks = new ArrayList<BlockState>();
        for (int x = (radius); x >= (0 - radius); x--) {
            for (int zed = (radius); zed >= (0 - radius); zed--) {
                for (int y = (radius); y >= (0 - radius); y--) {
                    if (x == 0 && zed == 0) {
                        // skip own blocks...
                    } else {
                        Block b = block.getRelative(x, y, zed);
                        Location distanceBlock;
                        if (y >= 0) { // sphere if y<0 cylinder above
                            distanceBlock = block.getRelative(x, 0, zed).getLocation();
                        } else {
                            distanceBlock = b.getLocation();
                        }
                        if (block.getLocation().distance(distanceBlock) < radius) {
                            BlockState thisstate = b.getState();
                            if (b.getType() != Material.AIR) {
                                Material blocktype = thisstate.getType();
                                if (Impact.fixHoles && Impact.hasCreeperHeal) { // should we just let CreeperHeal handle this ?
                                    if (blocktype != Material.BEDROCK) {
                                        blocks.add(thisstate);
                                        CHUtils.recordBlock(b);
                                    }
                                } else {
                                    
                                    switch (blocktype) {
                                        case CHEST:
                                        case TRAPPED_CHEST:
                                        case BREWING_STAND:
                                        case MOB_SPAWNER:
                                        case DISPENSER:
                                        case DROPPER:
                                        case FURNACE:
                                        case ENDER_CHEST:
                                        case SIGN:
                                        case SIGN_POST:
                                        case JUKEBOX:
                                        case HOPPER:
                                        case BEDROCK:
                                            // do nothing for tiles so we don't loose stuff
                                            break;
                                        default:
                                            if (Impact.hasFactions) {
                                                String fName = FactionsUtils.getFactName(block.getLocation());
                                                if (fName == null || fName.equals("") || fName.equals("warzone") || fName.equals("wilderness")) {
                                                    blocks.add(thisstate);
                                                    b.setType(Material.AIR);
                                                }
                                            }
                                            break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        for (BlockState bs : blocks) {
            Material m = bs.getType();
            @SuppressWarnings("deprecation")
            byte d = bs.getData().getData();
            int bsX = bs.getX();
            int bsY = bs.getY();
            int bsZ = bs.getZ();
            double depth = (block.getY() + radius) - bsY + 1;
            double speedUp = .5 + ((1.00D / depth) * 2); // (1.00D/distance)
            double speedX = 0 - ((from.getX() - bsX) * spread);
            double speedZ = 0 - ((from.getZ() - bsZ) * spread);
            Location fbl = new Location(block.getWorld(), bsX, bsY, bsZ);
            FallingBlock fb = fbl.getWorld().spawnFallingBlock(fbl, m, d);
            fb.setVelocity(new Vector(speedX, speedUp, speedZ));
            if (Impact.dropBlocks && Math.random() < Impact.dropChance) {
                fb.setDropItem(true);
            } else {
                fb.setDropItem(false);
            }
            fb.setMetadata("impactblock", new FixedMetadataValue(Impact.plugin, "true"));
        }
        blocks = null;
    }
    
}
