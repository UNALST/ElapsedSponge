package org.elapsed.sponge.managers;

import org.apache.commons.lang3.mutable.MutableInt;
import org.bukkit.plugin.Plugin;
import org.elapsed.lib.objects.BlockLocation;
import org.elapsed.lib.utils.FileUtils;
import org.elapsed.lib.utils.IntegerUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class FileManager {
    private final File file;

    public FileManager(final Plugin plugin) {
        this.file = new File(plugin.getDataFolder().getAbsolutePath() + File.separator + "sponges.txt");
    }

    public Map<BlockLocation, MutableInt> load() {
        if (!this.file.exists()) {
            return new HashMap<>();
        }
        final List<String> lines = FileUtils.getLines(this.file);
        final Map<BlockLocation, MutableInt> spongeCache = new HashMap<>(lines.size());

        for (final String line : lines) {
            final String[] split = line.split("\t");
            spongeCache.put(new BlockLocation(split[0]), new MutableInt(IntegerUtils.unsafelyParseInt(split[1])));
        }
        return spongeCache;
    }

    public void save(final Map<BlockLocation, MutableInt> spongeCache) {
        if (this.file.exists()) {
            this.file.delete();
        }
        final StringBuilder builder = new StringBuilder(32 * spongeCache.size());

        boolean first = true;

        for (final Entry<BlockLocation, MutableInt> entry : spongeCache.entrySet()) {
            if (first) {
                first = false;
            } else {
                builder.append('\n');
            }
            builder.append(entry.getKey()).append('\t').append(entry.getValue().intValue());
        }
        FileUtils.write(this.file, builder);
    }
}