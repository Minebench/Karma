package io.github.apfelcreme.Karma.Bungee.Command.Command.Karma;

import io.github.apfelcreme.Karma.Bungee.Command.SubCommand;
import io.github.apfelcreme.Karma.Bungee.Command.TabCompleter;
import io.github.apfelcreme.Karma.Bungee.Exception.OncePerDayException;
import io.github.apfelcreme.Karma.Bungee.KarmaPlugin;
import io.github.apfelcreme.Karma.Bungee.KarmaPluginConfig;
import io.github.apfelcreme.Karma.Bungee.User.Transaction;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Copyright (C) 2016 Lord36 aka Apfelcreme
 * <p>
 * This program is free software;
 * you can redistribute it and/or modify it under the terms of the GNU General
 * Public License as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses/>.
 *
 * @author Lord36 aka Apfelcreme
 */
public class GiveCommand implements SubCommand {

    /**
     * executes a subcommand
     *
     * @param sender the sender
     * @param args   the string arguments in an array
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (player.hasPermission("karma.command.karma.give")) {
            if (args.length > 0) {
                UUID uuid = KarmaPlugin.getInstance().getUUIDByName(args[0]);
                if (uuid != null) {
                    if (!uuid.equals(player.getUniqueId())) {
                        if (ProxyServer.getInstance().getPlayer(uuid) != null) {
                            try {
                                Transaction transaction = new Transaction(player.getUniqueId(), uuid);
                                transaction.save(sender);
                                KarmaPlugin.sendMessage(player, KarmaPluginConfig.getInstance().getText("info.thx.thxGiven")
                                        .replace("{0}", args[0])
                                        .replace("{1}", new DecimalFormat("0.##").format(transaction.getAmount())));
                                KarmaPlugin.sendMessage(uuid, KarmaPluginConfig.getInstance().getText("info.thx.thxReceived")
                                        .replace("{0}", sender.getName())
                                        .replace("{1}", new DecimalFormat("0.##").format(transaction.getAmount())));
                            } catch (OncePerDayException e) {
                                KarmaPlugin.sendMessage(player, KarmaPluginConfig.getInstance().getText("error.oncePerDay")
                                        .replace("{0}", args[0]));
                            }
                        } else {
                            KarmaPlugin.sendMessage(player, KarmaPluginConfig.getInstance().getText("error.offlinePlayer"));
                        }
                    } else {
                        KarmaPlugin.sendMessage(player, KarmaPluginConfig.getInstance().getText("error.noSelfTransaction"));
                    }
                } else {
                    KarmaPlugin.sendMessage(player, KarmaPluginConfig.getInstance().getText("error.unknownPlayer"));
                }
            } else {
                KarmaPlugin.sendMessage(player, KarmaPluginConfig.getInstance().getText("error.wrongUsage.thx.thx"));
            }
        } else {
            KarmaPlugin.sendMessage(player, KarmaPluginConfig.getInstance().getText("error.noPermission"));
        }
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 2) {
            suggestions.addAll(TabCompleter.getPlayers(sender));
        } else if (args.length == 3) {
            suggestions.addAll(TabCompleter.getPlayers(sender, args[2]));
        }
        return suggestions;
    }
}
