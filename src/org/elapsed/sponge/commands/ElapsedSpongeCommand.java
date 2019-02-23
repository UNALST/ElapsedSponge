package org.elapsed.sponge.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.elapsed.lib.messaging.Messages;
import org.elapsed.sponge.Configuration;
import org.elapsed.sponge.Sponges;

import java.text.DecimalFormat;

public class ElapsedSpongeCommand implements CommandExecutor {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#0.00");

    private final Configuration configuration;
    private final Sponges sponges;

    public ElapsedSpongeCommand(final Configuration configuration, final Sponges sponges) {
        this.configuration = configuration;
        this.sponges = sponges;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (!sender.hasPermission("elapsedsponge.commands.reload")) {
            sender.sendMessage(this.configuration.getNoPermissionCommand());
            return false;
        }
        if (args.length == 0 || !args[0].equalsIgnoreCase("reload")) {
            return Messages.error(sender, "/elapsedsponge reload");
        }
        long time = System.nanoTime();

        this.configuration.reload();
        this.sponges.recalculate();

        time = System.nanoTime() - time;

        return Messages.send(sender, "&ElapsedSponge has been reloaded! (%sms)", ElapsedSpongeCommand.DECIMAL_FORMAT.format(time / 1000000.0D));
    }
}