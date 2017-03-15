package io.github.apfelcreme.Karma.Bungee.Command;

import io.github.apfelcreme.Karma.Bungee.Command.Command.Particles.*;
import io.github.apfelcreme.Karma.Bungee.KarmaPlugin;
import io.github.apfelcreme.Karma.Bungee.KarmaPluginConfig;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.Arrays;

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
public class ParticlesCommandExecutor extends Command {

    public ParticlesCommandExecutor(String... aliases) {
        super("particles", null, aliases);
    }

    @Override
    public void execute(final CommandSender commandSender, final String[] strings) {
        if (commandSender instanceof ProxiedPlayer) {
            SubCommand subCommand = null;
            if (strings.length > 0) {
                Operation operation = Operation.getOperation(strings[0]);
                if (operation != null) {
                    switch (operation) {
                        case HELP:
                            subCommand = new HelpCommand();
                            break;
                        case LIST:
                            subCommand = new ListCommand();
                            break;
                        case SET:
                            subCommand = new SetCommand();
                            break;
                        case USE:
                            subCommand = new UseCommand();
                            break;
                    }
                } else {
                    KarmaPlugin.sendMessage(commandSender, KarmaPluginConfig.getInstance().getText("error.unknownCommand")
                            .replace("{0}", strings[0]));
                }
            } else {
                subCommand = new HelpCommand();
            }
            if (subCommand != null) {
                final SubCommand finalSubCommand = subCommand;

                // execute the subcommand in a thread
                ProxyServer.getInstance().getScheduler().runAsync(KarmaPlugin.getInstance(),
                        () -> finalSubCommand.execute(commandSender, Arrays.copyOfRange(strings, 1, strings.length))
                );
            }
        } else {
            ProxyServer.getInstance().getLogger().info("Command cannot be used from console!");
        }
    }

    /**
     * a list of available subcommands
     */
    public enum Operation {

        HELP,
        LIST,
        SET,
        USE;

        /**
         * returns the matching operation
         *
         * @param key the key
         * @return the matching operation
         */
        public static Operation getOperation(String key) {
            for (Operation operation : Operation.values()) {
                if (operation.name().equalsIgnoreCase(key)) {
                    return operation;
                }
            }
            return null;
        }

    }
}
