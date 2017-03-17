package io.github.apfelcreme.Karma.Bungee.Command.Command.Particles;

import io.github.apfelcreme.Karma.Bungee.Command.SubCommand;
import io.github.apfelcreme.Karma.Bungee.KarmaPlugin;
import io.github.apfelcreme.Karma.Bungee.KarmaPluginConfig;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.config.Configuration;

import java.util.ArrayList;
import java.util.Collections;
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
public class HelpCommand implements SubCommand {

    /**
     * executes a subcommand
     *
     * @param sender the sender
     * @param args   the string arguments in an array
     */
    @Override
    public void execute(CommandSender sender, String[] args) {

        Configuration configurationSection =
                KarmaPluginConfig.getInstance().getLanguageConfiguration().getSection("texts.info.particles.help.commands");
        List<String> keys = new ArrayList<>();
        List<String> strings = new ArrayList<>();

        for (String key : configurationSection.getSection("user").getKeys()) {
            if (sender.hasPermission("karma.command.particles." + key)) {
                keys.add("info.particles.help.commands.user." + key);
            }
        }

        if (sender.hasPermission("karma.command.particles.help.mod")) {
            for (String key : configurationSection.getSection("mod").getKeys()) {
                keys.add("info.particles.help.commands.mod." + key);
            }
        }
        for (String key : keys) {
            strings.add(KarmaPluginConfig.getInstance().getText(key));
        }
        Collections.sort(strings);
        KarmaPlugin.sendMessage(sender, KarmaPluginConfig.getInstance().getText("info.particles.help.header"));
        for (Object s : strings) {
            KarmaPlugin.sendMessage(sender, ChatColor.translateAlternateColorCodes('&', s.toString()));
        }
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }
}
