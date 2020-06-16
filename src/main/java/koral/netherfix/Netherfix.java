package koral.netherfix;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public final class Netherfix extends JavaPlugin implements Listener {


    public void CheckConfig() {

        if(getConfig().get("Name") == null){ //if the setting has been deleted it will be null
            getConfig().set("Name", "Value"); //reset the setting
            saveConfig();
            reloadConfig();

        }

    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        runNetherPortalTimer();

        File file = new File(getDataFolder() + File.separator + "config.yml"); //This will get the config file
        if (!file.exists()) { //This will check if the file exist
            //Situation A, File doesn't exist
            getConfig().addDefault("time", 15); //adding default settings
            getConfig().addDefault("commandaftertime", "spawn"); //adding default settings
            //Save the default settings
            getConfig().options().copyDefaults(true);
            saveConfig();
        } else {
            //situation B, Config does exist
            CheckConfig(); //function to check the important settings
            saveConfig(); //saves the config
            reloadConfig();    //reloads the config
            // Plugin startup logic
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public boolean onNetherPortal(Entity entity) {
        return entity.getLocation().getBlock().getType() == Material.NETHER_PORTAL;
    }

    int i = 0;

    public void runNetherPortalTimer() {
        Bukkit.getScheduler().runTaskTimer(this, () -> {
                Bukkit.getOnlinePlayers().forEach(player -> {
                    if (onNetherPortal(player)) {
                        i++;
                        if (i > getConfig().getInt("time")) {
                            player.performCommand(getConfig().getString("commandaftertime"));
                            i =0;
                        }
                    }
                });
                }, 0, 20);
    }
}




