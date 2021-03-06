package io.github.apfelcreme.Karma.Bungee.Command;

import io.github.apfelcreme.Karma.Bungee.KarmaPlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

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
public class ThxCommandExecutor extends Command implements TabExecutor {

    public ThxCommandExecutor(String name, String... aliases) {
        super(name, null, aliases);
    }

    @Override
    public void execute(final CommandSender commandSender, final String[] strings) {
        ProxyServer.getInstance().getScheduler().runAsync(KarmaPlugin.getInstance(),
                () -> KarmaPlugin.getInstance().getKarmaCommand().getSubCommand("give").execute(commandSender, strings)
        );
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        return KarmaPlugin.getInstance().getKarmaCommand().getSubCommand("give").getTabCompletions(sender, args);
    }
}
