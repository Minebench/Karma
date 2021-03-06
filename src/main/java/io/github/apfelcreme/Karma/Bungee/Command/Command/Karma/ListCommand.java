package io.github.apfelcreme.Karma.Bungee.Command.Command.Karma;

import io.github.apfelcreme.Karma.Bungee.Command.SubCommand;
import io.github.apfelcreme.Karma.Bungee.Command.TabCompleter;
import io.github.apfelcreme.Karma.Bungee.KarmaPlugin;
import io.github.apfelcreme.Karma.Bungee.KarmaPluginConfig;
import io.github.apfelcreme.Karma.Bungee.User.PlayerData;
import io.github.apfelcreme.Karma.Bungee.User.Relation;
import io.github.apfelcreme.Karma.Bungee.Utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.text.DecimalFormat;
import java.util.*;

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
public class ListCommand implements SubCommand {

    /**
     * executes a subcommand
     *
     * @param sender the sender
     * @param args   the string arguments in an array
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("karma.command.karma.list")) {
            UUID targetUUID = Utils.getUuid(sender);
            if (sender.hasPermission("karma.command.karma.list.others") && args.length > 0) {
                targetUUID = KarmaPlugin.getInstance().getUUIDByName(args[0]);
            }
            if (targetUUID != null) {
                PlayerData playerData = KarmaPlugin.getInstance().getDatabaseController().getPlayerData(targetUUID);
                if (playerData != null) {
                    List<Relation> relations = new ArrayList<>(playerData.getRelations().values());
                    if (args.length > 2 && "received".equalsIgnoreCase(args[2])) {
                        Collections.sort(relations, (o1, o2) -> Double.compare(o2.getAmountReceived(), o1.getAmountReceived()));
                    } else {
                        Collections.sort(relations);
                    }
                    int page = 0;
                    if (args.length > 1 && KarmaPlugin.isNumeric(args[1])) {
                        page = Integer.parseInt(args[1]) - 1;
                    }
                    if (page < 0) {
                        page = 0;
                    }
                    int pageSize = KarmaPluginConfig.getInstance().getConfiguration().getInt("pageSize");
                    int maxPages = (int) Math.ceil((float) relations.size() / pageSize);
                    if (page >= maxPages - 1) {
                        page = maxPages - 1;
                    }
                    if (Utils.getUuid(sender).equals(targetUUID)) {
                        KarmaPlugin.sendMessage(sender, KarmaPluginConfig.getInstance().getText("info.karma.list.headerYou")
                                .replace("{0}", String.valueOf(page + 1))
                                .replace("{1}", String.valueOf(maxPages)));
                    } else {
                        KarmaPlugin.sendMessage(sender, KarmaPluginConfig.getInstance().getText("info.karma.list.headerSomeoneElse")
                                .replace("{0}", args[0])
                                .replace("{1}", String.valueOf(page + 1))
                                .replace("{2}", String.valueOf(maxPages)));
                    }
                    if (relations.size() != 0) {
                        for (int i = page * pageSize; i < (page + 1) * pageSize && i < relations.size(); i++) {
                            Relation relation = relations.get(i);
                            String toName = KarmaPlugin.getInstance().getNameByUUID(relation.getTo());
                            if (toName != null) {
                                KarmaPlugin.sendMessage(sender, KarmaPluginConfig.getInstance().getText("info.karma.list.element")
                                        .replace("{0}", toName)
                                        .replace("{1}", new DecimalFormat("0.##").format(relation.getAmountGiven()))
                                        .replace("{2}", new DecimalFormat("0.##").format(relation.getAmountReceived())));
                            }
                        }
                    } else {
                        KarmaPlugin.sendMessage(sender, KarmaPluginConfig.getInstance().getText("info.karma.list.noElements"));
                    }
                }
            } else {
                KarmaPlugin.sendMessage(sender, KarmaPluginConfig.getInstance().getText("error.unknownPlayer"));
            }
        } else {
            KarmaPlugin.sendMessage(sender, KarmaPluginConfig.getInstance().getText("error.noPermission"));
        }
    }


    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (sender.hasPermission("karma.command.karma.list.others")) {
            if (args.length == 2) {
                suggestions.addAll(TabCompleter.getPlayers(sender));
            } else if (args.length == 3) {
                suggestions.addAll(TabCompleter.getPlayers(sender, args[2]));
            }
        }
        return suggestions;
    }
}
