package org.elapsed.lib.objects;

import org.bukkit.World;
import org.elapsed.lib.utils.IntegerUtils;

public class BlockLocation {
    private final int world, x, y, z, hash;
    private final String string;

    public BlockLocation(final World world, final int x, final int y, final int z) {
        this.world = world.hashCode();
        this.x = x;
        this.y = y;
        this.z = z;
        this.hash = 31 * (31 * (31 * this.world + this.x) + this.y) + this.z;
        this.string = this.world + "," + this.x + "," + this.y + "," + this.z;
    }

    public BlockLocation(final String string) {
        final String[] split = string.split(",");
        this.world = IntegerUtils.unsafelyParseInt(split[0]);
        this.x = IntegerUtils.unsafelyParseInt(split[1]);
        this.y = IntegerUtils.unsafelyParseInt(split[2]);
        this.z = IntegerUtils.unsafelyParseInt(split[3]);
        this.hash = 31 * (31 * (31 * this.world + this.x) + this.y) + this.z;
        this.string = string;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BlockLocation)) {
            return false;
        }
        final BlockLocation location = (BlockLocation) o;
        return location.world == this.world && location.x == this.x && location.y == this.y && location.z == this.z;
    }

    @Override
    public int hashCode() {
        return this.hash;
    }


    @Override
    public String toString() {
        return this.string;
    }
}