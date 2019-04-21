package io.github.apfelcreme.Karma.Bukkit;

import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;

import java.util.Random;
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

    private static final Random RANDOM = new Random();
    private static final Color[] COLORS = {
            Color.YELLOW,
            Color.RED,
            Color.AQUA,
            Color.PURPLE,
            Color.GREEN,
            Color.FUCHSIA,
            Color.BLUE,
            Color.LIME,
            Color.MAROON,
            Color.NAVY,
            Color.OLIVE,
            Color.ORANGE,
            Color.RED,
            Color.TEAL
    };

    private UUID owner;
    private Particle particle;
    private Effect effect;
    private long delay;

    public ParticleCloud(UUID owner, Particle particle, Effect effect, long delay) {
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

        if (particle != null) {
            Object data = null;
            double extra = 0;
            if (particle == Particle.NOTE) {
                extra = (int) (Math.random() * 24);
            }
            if (particle == Particle.REDSTONE) {
                data = new Particle.DustOptions(COLORS[RANDOM.nextInt(COLORS.length)], 1);
            }
            if (particle == Particle.PORTAL) {
                // makes them move instead of just falling down
                extra = 1;
            }
            location.getWorld().spawnParticle(particle, location, 20, 0.5f, 1.5f, 0.5f, extra, data);
        }
        if (effect != null) {
            location.getWorld().playEffect(location, effect, 0, 0);
        }
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
                ", particle=" + particle +
                ", effect=" + effect +
                ", delay=" + delay +
                '}';
    }
}
