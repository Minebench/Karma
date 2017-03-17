package io.github.apfelcreme.Karma.Bungee;

import io.github.apfelcreme.Karma.Bungee.Command.SubCommandExecutor;
import io.github.apfelcreme.Karma.Bungee.Command.TabCompleter;
import io.github.apfelcreme.Karma.Bungee.Command.ThxCommandExecutor;
import io.github.apfelcreme.Karma.Bungee.Database.DatabaseController;
import io.github.apfelcreme.Karma.Bungee.Database.MySQLConnector;
import io.github.apfelcreme.Karma.Bungee.Database.MySQLController;
import io.github.apfelcreme.Karma.Bungee.Listener.ServerSwitchListener;
import io.github.apfelcreme.Karma.Bungee.Listener.VanishStatusChangeListener;
import io.github.apfelcreme.Karma.Bungee.User.PlayerData;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.zaiyers.UUIDDB.core.UUIDDBPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;
import java.util.regex.Pattern;

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
public class KarmaPlugin extends Plugin {

    /**
     * a cache for name -> uuid
     */
    private Map<String, UUID> uuidCache = null;

    /**
     * the playerdata cache
     */
    private Map<UUID, PlayerData> playerDataCache = null;

    /**
     * directly store reference to UUIDDB plugin instead of always getting the instance
     */
    private UUIDDBPlugin uuidDb = null;

    /**
     * the database controller
     */
    private DatabaseController databaseController;

    /**
     * the command executor for /karma
     */
    private SubCommandExecutor karmaCommandExecutor;

    /**
     * the command executor for /particles
     */
    private SubCommandExecutor particlesCommandExecutor;

    /**
     * the command executor for /thx
     */
    private ThxCommandExecutor thxCommandExecutor;

    @Override
    public void onEnable() {

        // initialize the uuid & playerData cache
        if (getProxy().getPluginManager().getPlugin("UUIDDB") != null) {
            uuidDb = (UUIDDBPlugin) getProxy().getPluginManager().getPlugin("UUIDDB");
        }
        uuidCache = new HashMap<>();
        playerDataCache = new HashMap<>();

        // init the config
        KarmaPluginConfig.getInstance();

        // init the database controller
        MySQLConnector.getInstance().initConnection();
        databaseController = new MySQLController();

        // register the commands & listener
        karmaCommandExecutor = new SubCommandExecutor("karma");
        karmaCommandExecutor.addSubCommand(new io.github.apfelcreme.Karma.Bungee.Command.Command.Karma.ConfirmCommand());
        karmaCommandExecutor.addSubCommand(new io.github.apfelcreme.Karma.Bungee.Command.Command.Karma.GiveCommand());
        karmaCommandExecutor.addSubCommand(new io.github.apfelcreme.Karma.Bungee.Command.Command.Karma.HelpCommand());
        karmaCommandExecutor.addSubCommand(new io.github.apfelcreme.Karma.Bungee.Command.Command.Karma.InfoCommand());
        karmaCommandExecutor.addSubCommand(new io.github.apfelcreme.Karma.Bungee.Command.Command.Karma.ListCommand());
        karmaCommandExecutor.addSubCommand(new io.github.apfelcreme.Karma.Bungee.Command.Command.Karma.ReloadCommand());
        karmaCommandExecutor.addSubCommand(new io.github.apfelcreme.Karma.Bungee.Command.Command.Karma.ResetCommand());
        karmaCommandExecutor.addSubCommand(new io.github.apfelcreme.Karma.Bungee.Command.Command.Karma.TopCommand());
        particlesCommandExecutor = new SubCommandExecutor("particles", "particle");
        particlesCommandExecutor.addSubCommand(new io.github.apfelcreme.Karma.Bungee.Command.Command.Particles.HelpCommand());
        particlesCommandExecutor.addSubCommand(new io.github.apfelcreme.Karma.Bungee.Command.Command.Particles.ListCommand());
        particlesCommandExecutor.addSubCommand(new io.github.apfelcreme.Karma.Bungee.Command.Command.Particles.SetCommand());
        particlesCommandExecutor.addSubCommand(new io.github.apfelcreme.Karma.Bungee.Command.Command.Particles.UseCommand());
        thxCommandExecutor = new ThxCommandExecutor("thx", "thanks", "ty", "danke");
        getProxy().getPluginManager().registerCommand(this, karmaCommandExecutor);
        getProxy().getPluginManager().registerCommand(this, thxCommandExecutor);
        getProxy().getPluginManager().registerListener(this, new TabCompleter(this));
        if (KarmaPluginConfig.getInstance().useParticles()) {
            getProxy().getPluginManager().registerCommand(this, particlesCommandExecutor);
            getProxy().getPluginManager().registerListener(this, new ServerSwitchListener(this));
            getProxy().getPluginManager().registerListener(this, new VanishStatusChangeListener(this));
        }
        // register the messaging channel
        getProxy().registerChannel("Karma");

    }

    /**
     * returns the playerdata cache
     *
     * @return the playerdata cache
     */
    public Map<UUID, PlayerData> getPlayerDataCache() {
        return playerDataCache;
    }

    /**
     * returns the database controller
     *
     * @return the database controller
     */
    public DatabaseController getDatabaseController() {
        return databaseController;
    }

    /**
     * sends a message to a player
     *
     * @param receiver the player
     * @param text     the message
     */
    public static void sendMessage(CommandSender receiver, String text) {
        if (receiver != null && text != null) {
            receiver.sendMessage(TextComponent.fromLegacyText(getPrefix() + text));
        }
    }

    /**
     * returns the executor for the /karma command
     *
     * @return the executor for /karma
     */
    public SubCommandExecutor getKarmaCommand() {
        return karmaCommandExecutor;
    }

    /**
     * returns the executor for the /particles command
     *
     * @return the executor for /particles
     */
    public SubCommandExecutor getParticlesCommand() {
        return particlesCommandExecutor;
    }

    /**
     * returns the executor for the /thx command
     *
     * @return the executor for /thx
     */
    public ThxCommandExecutor getThxCommand() {
        return thxCommandExecutor;
    }

    /**
     * returns the command aliases for /thx
     *
     * @return the command aliases for /thx
     */
    public static Set<String> getCommandAliases(Command command) {
        Set<String> aliases = new LinkedHashSet<>();
        aliases.add(command.getName());
        Collections.addAll(aliases, command.getAliases());
        return aliases;
    }

    /**
     * sends a message to a player
     *
     * @param uuid the players uuid
     * @param text the message
     */
    public static void sendMessage(UUID uuid, String text) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);
        if (player != null) {
            sendMessage(player, text);
        }
    }

    /**
     * returns the prefix for messages
     *
     * @return the prefix for messages
     */
    public static String getPrefix() {
        return ChatColor.WHITE + "[" + ChatColor.LIGHT_PURPLE + "\u2665" + ChatColor.WHITE + "]";
    }

    /**
     * returns the plugin instance
     *
     * @return the plugin instance
     */
    public static KarmaPlugin getInstance() {
        return (KarmaPlugin) ProxyServer.getInstance().getPluginManager().getPlugin("Karma");
    }

    /**
     * returns the username of a player with the given uuid
     *
     * @param uuid a players uuid
     * @return his name
     */
    public String getNameByUUID(UUID uuid) {
        ProxiedPlayer player = getProxy().getPlayer(uuid);
        if (player != null) {
            return player.getName();
        }
        String name = null;
        if (uuidDb != null) {
            name = uuidDb.getStorage().getNameByUUID(uuid);
        } else if (uuidCache.containsValue(uuid)) {
            for (Map.Entry<String, UUID> entry : uuidCache.entrySet()) {
                if (entry.getValue().equals(uuid)) {
                    name = entry.getKey();
                    break;
                }
            }
        }

        if (name == null) {
            try {
                URL url = new URL(KarmaPluginConfig.getInstance().getAPINameUrl().replace("{0}", uuid.toString().replace("-", "")));
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                StringBuilder json = new StringBuilder();
                int read;
                while ((read = in.read()) != -1) {
                    json.append((char) read);
                }
                Object obj = new JSONParser().parse(json.toString());
                JSONArray jsonArray = (JSONArray) obj;
                name = (String) ((JSONObject) jsonArray.get(jsonArray.size() - 1)).get("name");
                if (uuidDb != null) {
                    uuidDb.getStorage().insert(uuid, name);
                } else {
                    uuidCache.put(name, uuid);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return name != null ? name : "Unknown Player";
    }

    /**
     * returns the uuid of the player with the given name
     *
     * @param name a players name
     * @return his uuid
     */
    public UUID getUUIDByName(String name) {
        ProxiedPlayer player = getProxy().getPlayer(name);
        if (player != null) {
            return player.getUniqueId();
        }
        UUID uuid = null;
        if (uuidDb != null) {
            String uuidString = uuidDb.getStorage().getUUIDByName(name, false);
            if (uuidString != null) {
                uuid = UUID.fromString(uuidString);
            }
        } else if (uuidCache.containsKey(name)) {
            uuid = uuidCache.get(name);
        } else {
            // Search for name with different case
            for (Map.Entry<String, UUID> entry : uuidCache.entrySet()) {
                if (entry.getKey().equalsIgnoreCase(name)) {
                    uuid = entry.getValue();
                    break;
                }
            }
        }

        if (uuid == null) {
            try {
                URL url = new URL(KarmaPluginConfig.getInstance().getAPIUUIDUrl().replace("{0}", name));
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                StringBuilder json = new StringBuilder();
                int read;
                while ((read = in.read()) != -1) {
                    json.append((char) read);
                }
                if (json.length() == 0) {
                    return null;
                }
                JSONObject jsonObject = (JSONObject) (new JSONParser().parse(json.toString()));
                // Get correct case of the inputted name
                name = jsonObject.get("name").toString();
                String id = jsonObject.get("id").toString();
                uuid = UUID.fromString(id.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
                if (uuidDb != null) {
                    uuidDb.getStorage().insert(uuid, name);
                } else {
                    uuidCache.put(name, uuid);
                }
                return uuid;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return uuid;
    }

    /**
     * checks if a String contains only numbers
     *
     * @param string a string
     * @return true or false
     */
    public static boolean isNumeric(String string) {
        return Pattern.matches("([0-9])*", string);
    }

}
