package io.github.apfelcreme.Karma.Bukkit;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import io.github.apfelcreme.Karma.Bukkit.Task.ParticleTask;
import org.bukkit.Effect;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

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
public class BungeeMessageListener implements PluginMessageListener {

    /**
     * the plugin instance
     */
    private KarmaPlugin plugin;

    public BungeeMessageListener(KarmaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onPluginMessageReceived(String s, Player player, byte[] bytes) {
        if (!s.equals("karma:applyparticles")) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(bytes);
        Player p = plugin.getServer().getPlayer(UUID.fromString(in.readUTF()));
        Effect effect = getEffect(in.readUTF());
        long delay = in.readLong();
        if (p != null) {
            ParticleTask.getInstance().removeCloud(p);
            if (effect != null) {
                ParticleTask.getInstance().addCloud(p, new ParticleCloud(p.getUniqueId(), effect, delay));
            }
        }
    }

    /**
     * returns a matching effect without throwing any exceptions
     *
     * @param effectName the effect name
     * @return the effect with that name
     */
    public Effect getEffect(String effectName) {
        for (Effect effect : Effect.values()) {
            if (effect.name().equalsIgnoreCase(effectName)) {
                return effect;
            }
        }
        return null;
    }
}
