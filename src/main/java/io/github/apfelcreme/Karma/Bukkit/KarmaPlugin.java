package io.github.apfelcreme.Karma.Bukkit;

import io.github.apfelcreme.Karma.Bukkit.Listener.PlayerQuitListener;
import org.bukkit.plugin.java.JavaPlugin;

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
public class KarmaPlugin extends JavaPlugin {

    private static KarmaPlugin instance = null;

    @Override
    public void onEnable() {
        instance = this;

        // register the Plugin channels for the bungee communication
        getServer().getMessenger().registerOutgoingPluginChannel(this, "Karma");
        getServer().getMessenger().registerIncomingPluginChannel(this, "Karma",
                new BungeeMessageListener(this));

        // register a listener
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
    }

    /**
     * returns the plugin instance
     *
     * @return the plugin instance
     */
    public static KarmaPlugin getInstance() {
        return instance;
    }
}
