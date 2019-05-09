package io.github.apfelcreme.Karma.Bungee;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.themoep.vnpbungee.VNPBungee;
import io.github.apfelcreme.Karma.Bungee.Particle.Effect;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
public class BukkitMessenger {

    /**
     * sends a message to a players current server to change the players particles
     *
     * @param player the players
     * @param effect the effect
     */
    public static void applyParticles(ProxiedPlayer player, Effect effect) {
        ServerInfo target = player.getServer().getInfo();
        if (target != null) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF(player.getUniqueId().toString());
            out.writeUTF(effect != null ? effect.getName() : "NONE");
            out.writeLong(effect != null ? effect.getDelay() : 0);
            target.sendData("karma:applyparticles", out.toByteArray());
        }
    }
}
