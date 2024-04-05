package me.flyingtaco725.buildnboom;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public final class BuildNBoom extends JavaPlugin implements Listener {
    public double chance;
    public int tntDelay;
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);

        // Load configuration
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        chance = getConfig().getDouble("chance");
        tntDelay = getConfig().getInt("tntDelay");
    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Random random = new Random();
        double randomNumber = random.nextDouble();
        Block placedBlock = event.getBlockPlaced();
        if (randomNumber <= chance) {
            double lavaOrTnt = random.nextDouble();
            if (lavaOrTnt <= 0.5) {
                getServer().getScheduler().runTaskLater(this, () -> {
                    if (placedBlock.getType() != Material.AIR) {
                        TNTPrimed tnt = placedBlock.getWorld().spawn(placedBlock.getLocation().add(0.5, 0.5, 0.5), TNTPrimed.class);
                        tnt.setFuseTicks(tntDelay);
                        placedBlock.setType(Material.AIR);
                    }
                }, 1);
            } else {
                placedBlock.setType(Material.LAVA);
                placedBlock.getState().update(true, false);
            }
        }
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
