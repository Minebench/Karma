package io.github.apfelcreme.Karma.Bukkit;

import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.Random;

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

    private final Player owner;
    private final Particle particle;
    private final Effect effect;
    private final long delay;
    private final int count;
    private final double extra;

    public ParticleCloud(Player owner, Particle particle, Effect effect, long delay, int count, double extra) {
        this.owner = owner;
        this.particle = particle;
        this.effect = effect;
        this.delay = delay;
        this.count = count;
        if (extra < 0 && particle != Particle.NOTE) {
            // notes get random
            extra = 0;
        }
        this.extra = extra;
    }

    /**
     * displays the particle effects at the given location
     */
    public void display() {
        display(count);
    }

    /**
     * displays the particle effects at the given location
     *
     * @param count the count to use
     */
    public void display(int count) {
        if (!owner.isOnline()) {
            return;
        }
        Location location = owner.getLocation();
        if (particle != null) {
            Object data = null;
            double extra = this.extra;
            if (extra < 0) {
                extra = (int) (Math.random() * 24);
            }
            if (particle == Particle.REDSTONE) {
                data = new Particle.DustOptions(COLORS[RANDOM.nextInt(COLORS.length)], 1);
            }
            location.getWorld().spawnParticle(particle, location.getWorld().getPlayers(), owner,
                    location.getX(), location.getY(), location.getZ(), count, 0.5f, 1.5f, 0.5f, extra, data);
        }
        if (effect != null) {
            for (int i = 0; i < count; i++) {
                location.getWorld().playEffect(location, effect, 0, 1);
            }
        }
    }

    /**
     * returns the owner
     *
     * @return the owner
     */
    public Player getOwner() {
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

    /**
     * returns the count of particles
     *
     * @return the particle count
     */
    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return "ParticleCloud{" +
                "owner=" + owner +
                ", particle=" + particle +
                ", effect=" + effect +
                ", delay=" + delay +
                ", count=" + count +
                ", extra=" + extra +
                '}';
    }
}
