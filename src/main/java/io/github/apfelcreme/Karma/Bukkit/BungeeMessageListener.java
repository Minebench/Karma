package io.github.apfelcreme.Karma.Bukkit;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import io.github.apfelcreme.Karma.Bukkit.Task.ParticleTask;
import org.bukkit.Effect;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.UUID;
import java.util.logging.Level;

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
        String particleStr = in.readUTF();
        Particle particle = null;
        Effect effect = null;
        if (!particleStr.equalsIgnoreCase("none")) {
            try {
                particle = Particle.valueOf(particleStr.toUpperCase());
            } catch (IllegalArgumentException e1) {
                try {
                    effect = Effect.valueOf(particleStr.toUpperCase());
                } catch (IllegalArgumentException e2) {
                    plugin.getLogger().log(Level.WARNING, particleStr + " is neither a Particle nor an Effect?");
                    return;
                }
            }
        }
        long delay = in.readLong();
        if (p != null) {
            ParticleTask.getInstance().removeCloud(p);
            if (effect != null) {
                ParticleTask.getInstance().addCloud(p, new ParticleCloud(p.getUniqueId(), particle, effect, delay));
            }
        }
    }
}
