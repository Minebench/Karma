package io.github.apfelcreme.Karma.Bungee;

import io.github.apfelcreme.Karma.Bungee.Particle.Effect;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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
public class KarmaPluginConfig {

    /**
     * the singleton instance
     */
    private static KarmaPluginConfig instance = null;

    /**
     * the configuration
     */
    private Configuration configuration;
    /**
     * the language configuration
     */
    private Configuration languageConfiguration;

    /**
     * the configuration provider
     */
    private final ConfigurationProvider yamlProvider = ConfigurationProvider
            .getProvider(net.md_5.bungee.config.YamlConfiguration.class);

    /**
     * debugging
     */
    private boolean debug = true;

    /**
     * the configured effects
     */
    private Map<String, Effect> effects = new HashMap<>();

    /**
     * map names to effects
     */
    private Map<String, Effect> effectsByName = new HashMap<>();

    /**
     * constructor
     */
    public KarmaPluginConfig() {
        File configurationFile = new File(KarmaPlugin.getInstance().getDataFolder().getAbsoluteFile() + "/config.yml");
        try {
            if (!KarmaPlugin.getInstance().getDataFolder().exists()) {
                KarmaPlugin.getInstance().getDataFolder().mkdir();
            }
            if (!configurationFile.exists()) {
                createConfigFile("config.yml", configurationFile);
            }
            configuration = yamlProvider.load(configurationFile);

            File languageConfigurationFile = new File(KarmaPlugin.getInstance().getDataFolder().getAbsoluteFile()
                    + "/" + configuration.getString("languageFile"));
            if (!languageConfigurationFile.exists()) {
                createConfigFile(configuration.getString("languageFile"), languageConfigurationFile);
            }
            languageConfiguration = yamlProvider.load(languageConfigurationFile);

            debug = configuration.getBoolean("debug", debug);

            Configuration effectsConfig = configuration.getSection("effects");
            for (String name : effectsConfig.getKeys()) {
                Effect effect = new Effect(
                        name,
                        effectsConfig.getLong(name + ".delay"),
                        effectsConfig.getInt(name + ".count", 10),
                        effectsConfig.getDouble(name + ".extra", -1),
                        languageConfiguration.getString("effects." + name + ".displayName", name.replace('_', '-').toLowerCase()),
                        languageConfiguration.getStringList("effects." + name + ".aliases")
                );
                effects.put(name.toLowerCase(), effect);
                effectsByName.put(name.toLowerCase(), effect);
                effectsByName.put(effect.getDisplayName().toLowerCase(), effect);
                for (String alias : effect.getAliases()) {
                    effectsByName.putIfAbsent(alias.toLowerCase(), effect);
                }
                if (effectsConfig.contains(name + ".karma")) {
                    int karma = effectsConfig.getInt(name + ".karma");
                    effect.setKarma(karma);
                }
                logDebug("Loaded effect " + effect);
            }
            KarmaPlugin.getInstance().getLogger().info("Loaded " + effects.size() + " effects from config!");
            logDebug(effectsByName.size() + " effects names and alias mappings.");

            yamlProvider.save(configuration, configurationFile);
            yamlProvider.save(languageConfiguration, languageConfigurationFile);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * copies a resource to the data folder
     *
     * @param source the resource file name
     * @param dest   the destination file
     * @throws IOException
     */
    private void createConfigFile(String source, File dest) throws IOException {
        Configuration configuration = yamlProvider.load(new InputStreamReader(KarmaPlugin.getInstance().getResourceAsStream(source)));
        yamlProvider.save(configuration, dest);
    }

    /**
     * reloads both configs
     */
    public void reload() {
        instance = null;
        getInstance();
    }

    /**
     * returns the username of the sql user
     *
     * @return the username of the sql user
     */
    public String getSqlUser() {
        return configuration.getString("sql.user");
    }

    /**
     * returns the sql password
     *
     * @return the sql password
     */
    public String getSqlPassword() {
        return configuration.getString("sql.password");
    }

    /**
     * returns the sql database name
     *
     * @return the sql database name
     */
    public String getSqlDatabase() {
        return configuration.getString("sql.database");
    }

    /**
     * returns the sql database url
     *
     * @return the sql database url
     */
    public String getSqlUrl() {
        return configuration.getString("sql.url");
    }

    /**
     * returns the transactions table name
     *
     * @return the transactions table name
     */
    public String getTransactionsTable() {
        return configuration.getString("sql.tables.transactions");
    }

    /**
     * returns the player table name
     *
     * @return the player table name
     */
    public String getPlayerTable() {
        return configuration.getString("sql.tables.players");
    }

    /**
     * returns the URL for API-Calls with the mojang API
     *
     * @return the URL for API-Calls with the mojang API
     */
    public String getAPINameUrl() {
        return configuration.getString("apiUrlName");
    }

    /**
     * returns the URL for API-Calls with the mojang API
     *
     * @return the URL for API-Calls with the mojang API
     */
    public String getAPIUUIDUrl() {
        return configuration.getString("apiUrlUUID");
    }

    /**
     * returns whether particles are enabled or not
     *
     * @return true or false
     */
    public boolean useParticles() {
        return configuration.getBoolean("useParticles", true);
    }

    /**
     * returns whether vanished players should be hidden or not
     *
     * @return true or false
     */
    public boolean hideVanished() {
        return configuration.getBoolean("hideVanished", false);
    }

    /**
     * returns the configuration object
     *
     * @return the configuration object
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * returns the language configuration object
     *
     * @return the language configuration object
     */
    public Configuration getLanguageConfiguration() {
        return languageConfiguration;
    }

    /**
     * get all effects
     *
     * @return all effects
     */
    public Collection<Effect> getEffects() {
        return effects.values();
    }

    /**
     * returns a texty string
     *
     * @param key the config path
     * @return the text
     */
    public String getText(String key, String... replacements) {
        String ret = languageConfiguration.getString("texts." + key);
        if (ret != null && !ret.isEmpty()) {
            for (int i = 0; i + 1 < replacements.length; i += 2) {
                ret = ret.replace("{" + replacements[i] + "}", replacements[i+1]);
                ret = ret.replace("{" + (i / 2) + "}", replacements[i+1]);
            }
            ret = ChatColor.translateAlternateColorCodes('&', ret);
            return ChatColor.translateAlternateColorCodes('§', ret);
        } else {
            return "Missing text node: " + key;
        }
    }

    /**
     * returns the effect with the given name
     *
     * @param effectName an effect name
     * @return an effect enum
     */
    public Effect getEffect(String effectName) {
        if (effectName == null) {
            return null;
        }
        return effectsByName.get(effectName.toLowerCase());
    }

    /**
     * returns the singleton instance
     *
     * @return the singleton instance
     */
    public static KarmaPluginConfig getInstance() {
        if (instance == null) {
            instance = new KarmaPluginConfig();
        }
        return instance;
    }

    /**
     * log a debug message
     *
     * @param message the message to log
     */
    public void logDebug(String message) {
        if (isDebug()) {
            KarmaPlugin.getInstance().getLogger().info("[Debug] " + message);
        }
    }

    /**
     * whether or not debugging is enabled
     *
     * @return if debugging is enabled
     */
    public boolean isDebug() {
        return debug;
    }
}
