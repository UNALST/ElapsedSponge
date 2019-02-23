package org.elapsed.sponge;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.elapsed.lib.messaging.Messages;

import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Configuration {
    private int radius;
    private boolean spongeLava, doPhysics;
    private long saveInterval;
    private Set<Material> sponges;
    private String noPermissionCommand;

    private final Plugin plugin;

    public Configuration(final Plugin plugin) {
        this.plugin = plugin;
    }

    public int getRadius() {
        return this.radius;
    }

    public boolean getSpongeLava() {
        return this.spongeLava;
    }

    public boolean isLiquid(final Block block) {
        final Material material = block.getType();
        return material == Material.WATER || material == Material.STATIONARY_WATER || (this.spongeLava && (material == Material.LAVA || material == Material.STATIONARY_LAVA));
    }

    public boolean getDoPhysics() {
        return this.doPhysics;
    }

    public long getSaveInterval() {
        return this.saveInterval;
    }

    public boolean isSponge(final Material material) {
        return this.sponges.contains(material);
    }

    public String getNoPermissionCommand() {
        return this.noPermissionCommand;
    }

    public void reload() {
        this.plugin.reloadConfig();

        final FileConfiguration configuration = this.plugin.getConfig();
        this.radius = configuration.getInt("radius");
        this.spongeLava = configuration.getBoolean("sponge-lava");
        this.doPhysics = configuration.getBoolean("do-physics");
        this.saveInterval = configuration.getLong("save-interval");
        this.sponges = EnumSet.copyOf(configuration.getStringList("sponge-materials").stream().map(Material::valueOf).collect(Collectors.toSet()));

        final ConfigurationSection messages = configuration.getConfigurationSection("messaging");
        this.noPermissionCommand = Messages.color('&', messages.getString("no-permission-command"));
    }
}