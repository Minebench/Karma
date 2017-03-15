package io.github.apfelcreme.Karma.Bungee.Command;

import io.github.apfelcreme.Karma.Bungee.KarmaPlugin;
import io.github.apfelcreme.Karma.Bungee.KarmaPluginConfig;
import io.github.apfelcreme.Karma.Bungee.Particle.Effect;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.Collections;

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
                }
            }
        } else if (KarmaPluginConfig.getInstance().useParticles()
                && KarmaPlugin.getCommandAliases(plugin.getParticlesCommand()).contains(args[0].replace("/", ""))) {
            if (args.length > 1) {
                SubCommand subCommand = plugin.getParticlesCommand().getSubCommand(args[1]);
                if (subCommand != null) {
                    event.getSuggestions().addAll(subCommand.getTabCompletions(sender, args));
                }
            }
        } else if (KarmaPlugin.getCommandAliases(plugin.getThxCommand()).contains(args[0].replace("/", ""))) {
            if (args.length == 1) {
                for (ProxiedPlayer player : plugin.getProxy().getPlayers()) {
                    if (!player.equals(sender)) {
                        event.getSuggestions().add(player.getName());
                    }
                }
            } else if (args.length == 2) {
                for (ProxiedPlayer player : plugin.getProxy().getPlayers()) {
                    if (!player.equals(sender) && player.getName().startsWith(args[1])) {
                        event.getSuggestions().add(player.getName());
                    }
                }
            }
        }

        Collections.sort(event.getSuggestions());

    }
}
