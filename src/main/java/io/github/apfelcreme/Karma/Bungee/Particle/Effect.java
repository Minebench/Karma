package io.github.apfelcreme.Karma.Bungee.Particle;

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
public class Effect {

    private final String name;
    private final long delay;
    private final int count;
    private final double extra;
    private final String displayName;
    private final List<String> aliases;
    private int karma = -1;

    public Effect(String name, long delay, int count, double extra, String displayName, List<String> aliases) {
        this.name = name;
        this.delay = delay;
        this.count = count;
        this.extra = extra;
        this.displayName = displayName;
        this.aliases = aliases;
    }

    /**
     * returns the name of the effect like it's in the config
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * returns the delay that is used on the effect when being scheduled in a thread
     *
     * @return the delay
     */
    public Long getDelay() {
        return delay;
    }

    /**
     * the particle count
     *
     * @return the count
     */
    public int getCount() {
        return count;
    }

    /**
     * the particle extra
     *
     * @return extra
     */
    public double getExtra() {
        return extra;
    }

    /**
     * returns the name of the effect that is displayed in lists and so on
     *
     * @return the name of the effect
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * returns a list of aliases and synonyms that a user can enter to refer to a effect
     *
     * @return a list of strings representing the effect
     */
    public List<String> getAliases() {
        return aliases;
    }

    /**
     * set the Karma required to use this effect
     *
     * @param karma the amount of karma, -1 if it can't be acquired with karma
     */
    public void setKarma(int karma) {
        this.karma = karma;
    }

    /**
     * get the Karma required to use this effect
     *
     * @return the amount of karma, -1 if it can't be acquired with karma
     */
    public int getKarma() {
        return karma;
    }

    @Override
    public String toString() {
        return "Effect{"
                + "name=" + getName()
                + ", karma=" + karma
                + ", delay=" + getDelay()
                + ", count=" + getCount()
                + ", extra=" + getExtra()
                + ", displayName=" + getDisplayName()
                + ", aliases=" + aliases
                + "}";
    }
}
