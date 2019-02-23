package org.elapsed.sponge;

import org.apache.commons.lang3.mutable.MutableInt;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.elapsed.lib.objects.BlockLocation;
import org.elapsed.lib.objects.SmallCachedRegion;

import java.util.Map;

public class Sponges {
    private Map<BlockLocation, MutableInt> spongeCache;

    private int radius;
    private boolean doPhysics;

    private final Configuration configuration;

    public Sponges(final Configuration configuration) {
        this.configuration = configuration;
        this.radius = configuration.getRadius();
        this.doPhysics = configuration.getDoPhysics();
    }

    public void recalculate() {
        if (this.radius != this.configuration.getRadius()) {
            this.spongeCache.clear();
        }
        this.radius = this.configuration.getRadius();
        this.doPhysics = this.configuration.getDoPhysics();
    }

    public Map<BlockLocation, MutableInt> getSpongeCache() {
        return this.spongeCache;
    }

    public void setSpongeCache(final Map<BlockLocation, MutableInt> spongeCache) {
        this.spongeCache = spongeCache;
    }

    public boolean isSponged(final Block location) {
        return this.spongeCache.get(new BlockLocation(location.getWorld(), location.getX(), location.getY(), location.getZ())) != null;
    }

    public void sponge(final Block origin) {
        final World world = origin.getWorld();
        final int x = origin.getX();
        final int y = origin.getY();
        final int z = origin.getZ();

        final SmallCachedRegion region = new SmallCachedRegion(world, x, z, this.radius);

        Block relative;
        int rx, ry, rz;

        BlockLocation location;
        MutableInt sponges;

        for (int dx = -this.radius; dx <= this.radius; ++dx) {
            for (int dy = -this.radius; dy <= this.radius; ++dy) {
                for (int dz = -this.radius; dz <= this.radius; ++dz) {
                    relative = region.getBlock(rx = x + dx, ry = y + dy, rz = z + dz);

                    if (this.configuration.isLiquid(relative)) {
                        relative.setType(Material.AIR, this.doPhysics);
                    }
                    location = new BlockLocation(world, rx, ry, rz);

                    if ((sponges = this.spongeCache.get(location)) == null) {
                        this.spongeCache.put(location, new MutableInt(1));
                    } else {
                        sponges.increment();
                    }
                }
            }
        }
    }

    public void unsponge(final Block origin) {
        final World world = origin.getWorld();
        final int x = origin.getX();
        final int y = origin.getY();
        final int z = origin.getZ();

        BlockLocation location;
        MutableInt sponges;

        for(int dx = -this.radius; dx <= this.radius; ++dx) {
            for(int dy = -this.radius; dy <= this.radius; ++dy) {
                for(int dz = -this.radius; dz <= this.radius; ++dz) {
                    location = new BlockLocation(world, x + dx, y + dy, z + dz);

                    if ((sponges = this.spongeCache.get(location)) == null) {
                        continue;
                    }
                    sponges.decrement();

                    if (sponges.intValue() == 0) {
                        this.spongeCache.remove(location);
                    }
                }
            }
        }
    }
}