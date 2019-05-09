package io.github.apfelcreme.Karma.Bungee.Command;

import io.github.apfelcreme.Karma.Bungee.KarmaPlugin;
import io.github.apfelcreme.Karma.Bungee.KarmaPluginConfig;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class SubCommandExecutor extends Command implements TabExecutor {

    private Map<String, SubCommand> subCommands = new HashMap<>();

    public SubCommandExecutor(String name, String... aliases) {
        super(name, "karma.command." + name.toLowerCase(), aliases);
    }

    /**
     * Register a sub command with this executor
     *
     * @param subCommand The SubCommand to register
     */
    public void addSubCommand(SubCommand subCommand) {
        String name = subCommand.getClass().getSimpleName().substring(0, subCommand.getClass().getSimpleName().lastIndexOf("Command"));
        subCommands.put(name.toLowerCase(), subCommand);
    }

    /**
     * Get a sub command registered with this executor
     *
     * @param name The name of the registered sub command; null if not registered
     */
    public SubCommand getSubCommand(String name) {
        return subCommands.get(name.toLowerCase());
    }

    @Override
    public void execute(final CommandSender commandSender, final String[] strings) {
        if (commandSender instanceof ProxiedPlayer) {
            SubCommand subCommand;
            if (strings.length > 0) {
                subCommand = subCommands.get(strings[0].toLowerCase());
                if (subCommand == null) {
                    KarmaPlugin.sendMessage(commandSender, KarmaPluginConfig.getInstance().getText("error.unknownCommand")
                            .replace("{0}", strings[0]));
                }
            } else {
                subCommand = subCommands.get("help");
            }
            if (subCommand != null) {
                final SubCommand finalSubCommand = subCommand;

                // execute the subcommand in a thread
                ProxyServer.getInstance().getScheduler().runAsync(KarmaPlugin.getInstance(),
                        () -> finalSubCommand.execute(commandSender, strings.length > 0 ? Arrays.copyOfRange(strings, 1, strings.length) : new String[0])
                );
            }
        } else {
            ProxyServer.getInstance().getLogger().info("Command cannot be used from console!");
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 0) {
            completions.addAll(subCommands.keySet());
        } else if (args.length == 1) {
            SubCommand subCommand = getSubCommand(args[0]);
            if (subCommand != null && sender.hasPermission(getPermission() + "." + args[0].toLowerCase())) {
                completions.addAll(subCommand.getTabCompletions(sender, args));
            } else {
                subCommands.keySet().stream()
                        .filter(n -> n.startsWith(args[0].toLowerCase()) && sender.hasPermission(getPermission() + "." + n))
                        .forEach(completions::add);
            }
        } else {
            SubCommand subCommand = getSubCommand(args[0]);
            if (subCommand != null && sender.hasPermission(getPermission() + "." + args[0].toLowerCase())) {
                completions.addAll(subCommand.getTabCompletions(sender, args));
            }
        }
        return completions;
    }

    public Map<String, SubCommand> getSubCommands() {
        return subCommands;
    }
}
