package io.github.apfelcreme.Karma.Bungee.Command;

import io.github.apfelcreme.Karma.Bungee.KarmaPlugin;
import io.github.apfelcreme.Karma.Bungee.KarmaPluginConfig;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
public class TabCompleter implements Listener {

    /**
     * the plugin instance
     */
    private KarmaPlugin plugin;

    /**
     * constructor
     *
     * @param plugin the plugin instance
     */
    public TabCompleter(KarmaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onTab(TabCompleteEvent event) {
        if (!event.getSuggestions().isEmpty()) {
            return;
        }
        if (event.getCursor().isEmpty()) {
            return;
        }
        if (!(event.getSender() instanceof ProxiedPlayer)) {
            return;
        }

        ProxiedPlayer sender = (ProxiedPlayer) event.getSender();
        String[] args = event.getCursor().split(" ");

        if (KarmaPlugin.getCommandAliases(plugin.getKarmaCommand()).contains(args[0].replace("/", ""))) {
            if (args.length > 1) {
                SubCommand subCommand = plugin.getKarmaCommand().getSubCommand(args[1]);
                if (subCommand != null) {
                    event.getSuggestions().addAll(subCommand.getTabCompletions(sender, args));
                } else {
                    for (String subCommandName : plugin.getKarmaCommand().getSubCommands().keySet()) {
                        if (subCommandName.startsWith(args[1].toLowerCase()) && sender.hasPermission("karma.command.karma." + subCommandName)) {
                            event.getSuggestions().add(subCommandName);
                        }
                    }
                }
            } else {
                for (String subCommandName : plugin.getKarmaCommand().getSubCommands().keySet()) {
                    if (sender.hasPermission("karma.command.karma." + subCommandName)) {
                        event.getSuggestions().add(subCommandName);
                    }
                }
            }
        } else if (KarmaPluginConfig.getInstance().useParticles()
                && KarmaPlugin.getCommandAliases(plugin.getParticlesCommand()).contains(args[0].replace("/", ""))) {
            if (args.length > 1) {
                SubCommand subCommand = plugin.getParticlesCommand().getSubCommand(args[1]);
                if (subCommand != null) {
                    event.getSuggestions().addAll(subCommand.getTabCompletions(sender, args));
                } else {
                    for (String subCommandName : plugin.getParticlesCommand().getSubCommands().keySet()) {
                        if (subCommandName.startsWith(args[1].toLowerCase()) && sender.hasPermission("karma.command.particles." + subCommandName)) {
                            event.getSuggestions().add(subCommandName);
                        }
                    }
                }

            } else {
                for (String subCommandName : plugin.getParticlesCommand().getSubCommands().keySet()) {
                    if (sender.hasPermission("karma.command.particles." + subCommandName)) {
                        event.getSuggestions().add(subCommandName);
                    }
                }
            }
        } else if (KarmaPlugin.getCommandAliases(plugin.getThxCommand()).contains(args[0].replace("/", ""))) {
            if (args.length == 1) {
                event.getSuggestions().addAll(TabCompleter.getPlayers(sender));
            } else if (args.length == 2) {
                event.getSuggestions().addAll(TabCompleter.getPlayers(sender, args[1]));
            }
        }

        Collections.sort(event.getSuggestions());

    }

    public static Collection<String> getPlayers(CommandSender sender) {
        List<String> players = new ArrayList<>();
        for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
            if (KarmaPlugin.getInstance().canSee((ProxiedPlayer) sender, player)) {
                players.add(player.getName());
            }
        }
        return players;
    }

    public static Collection<String> getPlayers(CommandSender sender, String... starts) {
        if (starts.length > 0) {
            List<String> players = new ArrayList<>();
            for (String playerName : getPlayers(sender)) {
                for (String start : starts) {
                    if (playerName.toLowerCase().startsWith(start.toLowerCase())) {
                        players.add(playerName);
                    }
                }
            }
            return players;
        } else {
            return getPlayers(sender);
        }
    }
}
