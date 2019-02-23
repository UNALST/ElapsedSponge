package org.elapsed.sponge.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.elapsed.sponge.Configuration;
import org.elapsed.sponge.Sponges;

public class LiquidFlowListener implements Listener {
    private final Configuration configuration;
    private final Sponges sponges;

    public LiquidFlowListener(final Configuration configuration, final Sponges sponges) {
        this.configuration = configuration;
        this.sponges = sponges;
    }

    @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
    public void onFlow(final BlockFromToEvent event) {
        final Block from = event.getBlock();

        if (this.configuration.isLiquid(from) && (this.sponges.isSponged(event.getToBlock()) || this.sponges.isSponged(from))) {
            event.setCancelled(true);
        }
    }
}