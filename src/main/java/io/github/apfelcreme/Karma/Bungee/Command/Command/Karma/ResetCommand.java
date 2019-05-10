package io.github.apfelcreme.Karma.Bungee.Command.Command.Karma;

import io.github.apfelcreme.Karma.Bungee.Command.Request.RequestManager;
import io.github.apfelcreme.Karma.Bungee.Command.Request.ResetRequest;
import io.github.apfelcreme.Karma.Bungee.Command.SubCommand;
import io.github.apfelcreme.Karma.Bungee.Command.TabCompleter;
import io.github.apfelcreme.Karma.Bungee.KarmaPlugin;
import io.github.apfelcreme.Karma.Bungee.KarmaPluginConfig;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
public class ResetCommand implements SubCommand {

    /**
     * executes a subcommand
     *
     * @param sender the sender
     * @param args   the string arguments in an array
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("karma.command.karma.reset")) {
            if (args.length > 0) {
                UUID targetUUID = KarmaPlugin.getInstance().getUUIDByName(args[0]);
                if (targetUUID != null) {
                    RequestManager.getInstance().addRequest(sender, new ResetRequest(sender, targetUUID));
                    KarmaPlugin.sendMessage(sender, KarmaPluginConfig.getInstance().getText("info.karma.confirm.confirm"));
                } else {
                    KarmaPlugin.sendMessage(sender, KarmaPluginConfig.getInstance().getText("error.unknownPlayer"));
                }
            } else {
                KarmaPlugin.sendMessage(sender, KarmaPluginConfig.getInstance().getText("error.wrongUsage.karma.reset"));
            }
        } else {
            KarmaPlugin.sendMessage(sender, KarmaPluginConfig.getInstance().getText("error.noPermission"));
        }
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (sender.hasPermission("karma.command.karma.reset")) {
            if (args.length == 2) {
                suggestions.addAll(TabCompleter.getPlayers(sender));
            } else if (args.length == 3) {
                suggestions.addAll(TabCompleter.getPlayers(sender, args[2]));
            }
        }
        return suggestions;
    }
}
