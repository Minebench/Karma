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
public enum Effect {

    CLOUD,
    COLOURED_DUST,
    CRIT,
    ENDER_SIGNAL,
    EXPLOSION,
    EXPLOSION_HUGE,
    EXPLOSION_LARGE,
    FIREWORKS_SPARK,
    FLAME,
    FLYING_GLYPH,
    HAPPY_VILLAGER,
    HEART,
    INSTANT_SPELL,
    LARGE_SMOKE,
    LAVA_POP,
    LAVADRIP,
    MAGIC_CRIT,
    MOBSPAWNER_FLAMES,
    NONE,
    NOTE,
    PARTICLE_SMOKE,
    PORTAL,
    POTION_BREAK,
    POTION_SWIRL,
    POTION_SWIRL_TRANSPARENT,
    SLIME,
    SMALL_SMOKE,
    SMOKE,
    SNOW_SHOVEL,
    SNOWBALL_BREAK,
    SPELL,
    SPLASH,
    VILLAGER_THUNDERCLOUD,
    VOID_FOG,
    WATERDRIP,
    WITCH_MAGIC;

    /**
     * returns the delay that is used on the effect when being scheduled in a thread
     *
     * @return the delay
     */
    public Long getDelay() {
        return KarmaPluginConfig.getInstance().getConfiguration().getLong("effects." + name() + ".delay");
    }

    /**
     * returns the name of the effect that is displayed in lists and so on
     *
     * @return the name of the effect
     */
    public String getDisplayName() {
        return KarmaPluginConfig.getInstance().getLanguageConfiguration().getString("effects." + name() + ".displayName");
    }

    /**
     * returns a list of aliases and synonyms that a user can enter to refer to a effect
     *
     * @return a list of strings representing the effect
     */
    public List<String> getAliases() {
        return KarmaPluginConfig.getInstance().getLanguageConfiguration().getStringList("effects." + name() + ".aliases");
    }

    /**
     * returns the effect with the given name
     *
     * @param effectName an effect name
     * @return an effect enum
     */
    public static Effect getEffect(String effectName) {
        for (Effect effect : Effect.values()) {
            if (effect.name().equalsIgnoreCase(effectName)) {
                return effect;
            }
            if (effect.getDisplayName().equalsIgnoreCase(effectName)) {
                return effect;
            }
            for (String alias : effect.getAliases()) {
                if (alias.equalsIgnoreCase(effectName)) {
                    return effect;
                }
            }
        }
        return null;
    }
}
