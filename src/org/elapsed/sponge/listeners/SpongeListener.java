package org.elapsed.sponge.listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.elapsed.sponge.Configuration;
import org.elapsed.sponge.Sponges;

public class SpongeListener implements Listener {
    private final Configuration configuration;
    private final Sponges sponges;

    public SpongeListener(final Configuration configuration, final Sponges sponges) {
        this.configuration = configuration;
        this.sponges = sponges;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlace(final BlockPlaceEvent event) {
        final Block block = event.getBlockPlaced();

        if (this.configuration.isSponge(block.getType())) {
            this.sponges.sponge(block);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBreak(final BlockBreakEvent event) {
        final Block block = event.getBlock();

        if (block.getType() == Material.SPONGE) {
            this.sponges.unsponge(block);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onExplode(final EntityExplodeEvent event) {
        for (int i = 0; i < event.blockList().size(); i++) {
            final Block block = event.blockList().get(i);
            if (block.getType() == Material.SPONGE) {
                this.sponges.unsponge(block);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPhysics(final BlockPhysicsEvent event) {
        if (event.getBlock().getType() == Material.SPONGE) {
            event.setCancelled(true);
        }
    }
}