package io.github.apfelcreme.Karma.Bungee.Listener;

import io.github.apfelcreme.Karma.Bungee.BukkitMessenger;
import io.github.apfelcreme.Karma.Bungee.KarmaPlugin;
import io.github.apfelcreme.Karma.Bungee.User.PlayerData;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

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
public class ServerSwitchListener implements Listener {

    /**
     * the plugin instance
     */
    private KarmaPlugin plugin;

    public ServerSwitchListener(KarmaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onServerSwitch(final ServerSwitchEvent event) {
        plugin.getProxy().getScheduler().runAsync(plugin, new Runnable() {
            @Override
            public void run() {
                PlayerData playerData = plugin.getDatabaseController().getPlayerData(event.getPlayer().getUniqueId());
                if (playerData != null) {
                    BukkitMessenger.applyParticles(event.getPlayer(), playerData.getEffect());
                }
            }
        });
    }
}
