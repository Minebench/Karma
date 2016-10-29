package io.github.apfelcreme.Karma.Bukkit;

import org.bukkit.Effect;
import org.bukkit.Location;

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
public class ParticleCloud {

    private UUID owner;
    private Effect effect;
    private long delay;

    public ParticleCloud(UUID owner, Effect effect, long delay) {
        this.owner = owner;
        this.effect = effect;
        this.delay = delay;
    }

    /**
     * displays the particle effects at the given location
     *
     * @param location the location where the particles are displayed
     */
    public void display(Location location) {
        int data = 0;

        if (effect.equals(Effect.NOTE)) {
            data = (int) (Math.random() * 24);
        }
        if (effect.equals(Effect.COLOURED_DUST)) {
            data = (int) (Math.random() * 15);
        }
        if (effect.equals(Effect.PORTAL)) {
            // makes them move instead of just falling down
            data = 1;
        }
        location.getWorld().spigot().playEffect(location, effect, 0, 0,
                0.5f, 1.5f, 0.5f, data, 1, 20);
    }

    /**
     * returns the owner
     *
     * @return the owner
     */
    public UUID getOwner() {
        return owner;
    }

    /**
     * returns the effect
     *
     * @return the effect
     */
    public Effect getEffect() {
        return effect;
    }

    /**
     * returns the time delay between two particles in ticks
     *
     * @return the time delay between two particles in ticks
     */
    public long getDelay() {
        return delay;
    }

    @Override
    public String toString() {
        return "ParticleCloud{" +
                "owner=" + owner +
                ", effect=" + effect +
                ", delay=" + delay +
                '}';
    }
}
