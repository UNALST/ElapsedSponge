package org.elapsed.lib.objects;

import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;

public class SmallCachedRegion {
    private final Chunk[][] chunks;
    private final int clx, clz;

    public SmallCachedRegion(final World world, final int clx, final int cgx, final int clz, final int cgz) {
        final Chunk c, c1, c2, c3;
        c = world.getChunkAt(clx, clz);

        if (clx == cgx) {
            c2 = c;
            c3 = c1 = clz == cgz ? c : world.getChunkAt(clx, cgz);
        } else if (clz == cgz) {
            c1 = c;
            c3 = c2 = world.getChunkAt(cgx, clz);
        } else {
            c1 = world.getChunkAt(clx, cgz);
            c2 = world.getChunkAt(cgx, clz);
            c3 = world.getChunkAt(cgx, cgz);
        }
        this.chunks = new Chunk[][] {
                new Chunk[] { c, c1 },
                new Chunk[] { c2, c3 }
        };
		this.clx = clx;
        this.clz = clz;
    }

    public SmallCachedRegion(final World world, final int x, final int z, final int radius) {
        this(world, (x - radius) >> 4, (x + radius) >> 4, (z - radius) >> 4, (z + radius) >> 4);
    }

    public Block getBlock(final int x, final int y, final int z) {
        return this.chunks[(x >> 4) - this.clx][(z >> 4) - this.clz].getBlock(x, y, z);
    }
}