package me.sablednah.impact;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;

import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Impact extends JavaPlugin {
    
    public static Impact plugin;
    public final static Logger logger = Logger.getLogger("Minecraft");
    public final DamageEntityListener EntityListener = new DamageEntityListener(this);
    
    public static Boolean debugMode;
    public static boolean playersImpact;
    public static boolean playersHeavy;
    public static List<String> heavyMobs;
    public static List<String> impactMobs;
    public static ImpactType impactType;
    public static int minFall;
    public static boolean fixHoles;
    public static double explosionScale;
    public static double throwScale;
    public static double spread;
    public static boolean dropBlocks;
    public static double dropChance;
    public static boolean blockFalling;
    
    public static String impactMessage;
    
    private ImpactCommandExecutor myExecutor;
    
    public static boolean hasFactions;
    public static boolean hasCreeperHeal;
    
    public static PluginDescriptionFile pdfFile;
    public static String myName;
    
    public static FileConfiguration LangConfig = null;
    public static File LangConfigurationFile = null;

    
    @Override
    public void onDisable() {
        plugin.getServer().getScheduler().cancelTasks(plugin);
        logger.info("[" + myName + "] --- END OF LINE ---");
    }
    
    @Override
    public void onEnable() {
        plugin = this;
        pdfFile = plugin.getDescription();
        myName = pdfFile.getName();
        
        hasFactions = this.getServer().getPluginManager().isPluginEnabled("Factions");
        if (hasFactions) {
            logger.info("[" + myName + "] Factions Support Enabled");
        }
        hasCreeperHeal = this.getServer().getPluginManager().isPluginEnabled("CreeperHeal");
        if (hasCreeperHeal) {
            logger.info("[" + myName + "] Creeper Heal Support Enabled");
        }
        
        PluginManager pm = getServer().getPluginManager();
        
        pm.registerEvents(this.EntityListener, this);
        
        myExecutor = new ImpactCommandExecutor(plugin);
        getCommand("impact").setExecutor(myExecutor);
        
        loadConfiguration(true);
        
        if (debugMode) {
            logger.info("[" + myName + "] DebugMode Enabled.");
        }
    }
    
    /**
     * Initialise config file
     * @param firstrun 
     * 
     * @param playersImpact
     */
    @SuppressWarnings("unchecked")
    public void loadConfiguration(boolean firstrun) {
        getConfig().options().copyDefaults(true);
    
        if (firstrun) {
            String headertext;
            headertext = "Default Impact Config file\r\n";
            headertext += "\r\n";
            headertext += "\r\n";
            headertext += "debugMode: [true|false] Enable extra debug info in logs.\r\n";
            headertext += "heavyMobs: [list] List of entities not thrown into air by Impacts.\r\n";
            headertext += "playersImpact: [true|false] Players cause impact crators.\r\n";
            headertext += "# playersHeavy: [true|false] Players thrown into air by impact.\r\n";
            headertext += "#  impactTpye: [explode|scatter|rise] type of impact mechanic\r\n";
            headertext += "\r\n";
            
            getConfig().options().header(headertext);
            getConfig().options().copyHeader(true);
        } else {
            this.reloadConfig();
        } 
        
        debugMode = getConfig().getBoolean("debugMode", false);
        playersImpact = getConfig().getBoolean("playersImpact", true);
        playersHeavy = getConfig().getBoolean("playersHeavy", false);
        impactType = ImpactType.fromString(getConfig().getString("impactType", "explode"));
        fixHoles = getConfig().getBoolean("fixHoles", false);
        dropBlocks = getConfig().getBoolean("dropBlocks", false);
        dropChance = (getConfig().getInt("dropChance", 95)/100.00D);
        minFall = getConfig().getInt("minFall", 5);
        explosionScale = getConfig().getDouble("explosionScale", 10.0D);
        throwScale = getConfig().getDouble("throwScale", 5.0D);
        spread = getConfig().getDouble("spread", 0.1D);
        blockFalling = getConfig().getBoolean("blockFalling", false);
        
        logger.info("Impact Type: " + impactType.getValue());
        
        List<String> mobList = null;
        HashSet<String> hmSet = null;
        
        mobList = (List<String>) getConfig().getList("heavyMobs");
        // dedupe
        hmSet = new HashSet<String>();
        hmSet.addAll(mobList);
        mobList.clear();
        mobList.addAll(hmSet);
        
        heavyMobs = mobList;
        
        mobList = (List<String>) getConfig().getList("impactMobs");
        
        hmSet = new HashSet<String>();
        hmSet.addAll(mobList);
        mobList.clear();
        mobList.addAll(mobList);
        
        impactMobs = mobList;
        
        saveConfig();
        
        getLangConfig();
        
        impactMessage = getLangConfig().getString("impactMessage");
        
        saveLangConfig();
        
    }
    
    public void reloadLangConfig() {
        if (LangConfigurationFile == null) {
            LangConfigurationFile = new File(getDataFolder(), "lang.yml");
        }
        LangConfig = YamlConfiguration.loadConfiguration(LangConfigurationFile);
        LangConfig.options().copyDefaults(true);
        
        // Look for defaults in the jar
        InputStream defConfigStream = getResource("lang.yml");
        if (defConfigStream != null) {
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            LangConfig.setDefaults(defConfig);
        }
    }
    
    public FileConfiguration getLangConfig() {
        if (LangConfig == null) {
            reloadLangConfig();
        }
        return LangConfig;
    }
    
    public void saveLangConfig() {
        if (LangConfig == null || LangConfigurationFile == null) {
            return;
        }
        try {
            LangConfig.save(LangConfigurationFile);
        } catch (IOException ex) {
            logger.severe("Could not save Lang config to " + LangConfigurationFile + " " + ex);
        }
    }
    
    public enum ImpactType {
        EXPLODE("explode"),
        SCATTER("scatter"),
        RISE("rise");
        
        private String value;
        
        private ImpactType(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
        
        public static ImpactType fromString(String text) {
            if (text != null) {
                for (ImpactType b : ImpactType.values()) {
                    if (text.equalsIgnoreCase(b.value)) {
                        return b;
                    }
                }
            }
            return null;
        }
    }
}
