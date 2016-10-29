package io.github.apfelcreme.Karma.Bungee.Command.Command.Karma;

import io.github.apfelcreme.Karma.Bungee.Command.SubCommand;
import io.github.apfelcreme.Karma.Bungee.KarmaPlugin;
import io.github.apfelcreme.Karma.Bungee.KarmaPluginConfig;
import io.github.apfelcreme.Karma.Bungee.User.PlayerData;
import io.github.apfelcreme.Karma.Bungee.User.Relation;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.text.DecimalFormat;
import java.util.Map;
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
public class ListCommand implements SubCommand {

    /**
     * executes a subcommand
     *
     * @param sender the sender
     * @param args   the string arguments in an array
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (player.hasPermission("Karma.user")) {
            UUID targetUUID = player.getUniqueId();
            if (player.hasPermission("Karma.mod") && args.length > 1) {
                targetUUID = KarmaPlugin.getInstance().getUUIDByName(args[1]);
            }
            if (targetUUID != null) {
                PlayerData playerData = KarmaPlugin.getInstance().getDatabaseController().getPlayerData(targetUUID);
                if (playerData != null) {
                    for (Map.Entry<UUID, Relation> entry : playerData.getRelations().entrySet()) {
                        Relation relation = entry.getValue();
                        if (player.getUniqueId().equals(targetUUID)) {
                            KarmaPlugin.sendMessage(player, KarmaPluginConfig.getInstance().getText("info.karma.list.headerYou"));
                        } else {
                            KarmaPlugin.sendMessage(player, KarmaPluginConfig.getInstance().getText("info.karma.list.headerSomeoneElse")
                                    .replace("{0}", args[1]));
                        }
                        String toName = KarmaPlugin.getInstance().getNameByUUID(entry.getKey());
                        if (toName != null) {
                            KarmaPlugin.sendMessage(player, KarmaPluginConfig.getInstance().getText("info.karma.list.element")
                                    .replace("{0}", toName)
                                    .replace("{1}", new DecimalFormat("0.##").format(relation.getAmountGiven()))
                                    .replace("{2}", new DecimalFormat("0.##").format(relation.getAmountReceived())));
                        }
                    }
                }
            } else {
                KarmaPlugin.sendMessage(player, KarmaPluginConfig.getInstance().getText("error.unknownPlayer"));
            }
        } else {
            KarmaPlugin.sendMessage(player, KarmaPluginConfig.getInstance().getText("error.noPermission"));
        }
    }
}
