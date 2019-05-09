package io.github.apfelcreme.Karma.Bungee.Particle;

import io.github.apfelcreme.Karma.Bungee.KarmaPluginConfig;

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
    private final String displayName;
    private final List<String> aliases;

    public Effect(String name, long delay, String displayName, List<String> aliases) {
        this.name = name;
        this.delay = delay;
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

    @Override
    public String toString() {
        return "Effect{"
                + "name=" + getName()
                + ", delay=" + getDelay()
                + ", displayName=" + getDisplayName()
                + ", aliases=" + aliases
                + "}";
    }

}
