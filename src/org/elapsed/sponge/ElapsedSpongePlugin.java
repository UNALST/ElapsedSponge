package org.elapsed.sponge;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.elapsed.sponge.commands.ElapsedSpongeCommand;
import org.elapsed.sponge.listeners.LiquidFlowListener;
import org.elapsed.sponge.listeners.SpongeListener;
import org.elapsed.sponge.managers.FileManager;

public class ElapsedSpongePlugin extends JavaPlugin {
    private Configuration configuration;
    private Sponges sponges;
    private FileManager fileManager;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        this.configuration = new Configuration(this);
        this.configuration.reload();

        this.sponges = new Sponges(this.configuration);
        this.fileManager = new FileManager(this);
        this.sponges.setSpongeCache(this.fileManager.load());

        final PluginManager manager = this.getServer().getPluginManager();
        manager.registerEvents(new LiquidFlowListener(this.configuration, this.sponges), this);
        manager.registerEvents(new SpongeListener(this.configuration, this.sponges), this);

        this.getCommand("elapsedsponge").setExecutor(new ElapsedSpongeCommand(this.configuration, this.sponges));

        final long interval = this.configuration.getSaveInterval();
        Bukkit.getScheduler().runTaskTimer(this, () -> this.fileManager.save(this.sponges.getSpongeCache()), interval, interval);
    }

    @Override
    public void onDisable() {
        this.fileManager.save(this.sponges.getSpongeCache());
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

    public Sponges getSponges() {
        return this.sponges;
    }
}