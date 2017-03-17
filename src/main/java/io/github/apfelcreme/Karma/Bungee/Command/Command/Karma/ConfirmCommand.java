package io.github.apfelcreme.Karma.Bungee.Command.Command.Karma;

import io.github.apfelcreme.Karma.Bungee.Command.Request.Request;
import io.github.apfelcreme.Karma.Bungee.Command.Request.RequestManager;
import io.github.apfelcreme.Karma.Bungee.Command.SubCommand;
import io.github.apfelcreme.Karma.Bungee.KarmaPlugin;
import io.github.apfelcreme.Karma.Bungee.KarmaPluginConfig;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;

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
public class ConfirmCommand implements SubCommand {

    /**
     * executes a subcommand
     *
     * @param sender the sender
     * @param args   the string arguments in an array
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (player.hasPermission("karma.command.karma.karma.confirm")) {
            Request request = RequestManager.getInstance().getRequest(player);
            if (request != null) {
                request.execute();
            } else {
                KarmaPlugin.sendMessage(player, KarmaPluginConfig.getInstance().getText("error.noRequest"));
            }
        }
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }
}
