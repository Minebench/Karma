package io.github.apfelcreme.Karma.Bungee.Command.Command.Particles;

import io.github.apfelcreme.Karma.Bungee.Command.SubCommand;
import io.github.apfelcreme.Karma.Bungee.KarmaPlugin;
import io.github.apfelcreme.Karma.Bungee.KarmaPluginConfig;
import io.github.apfelcreme.Karma.Bungee.Particle.Effect;
import io.github.apfelcreme.Karma.Bungee.User.PlayerData;
import io.github.apfelcreme.Karma.Bungee.Utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
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
public class ListCommand implements SubCommand {

    /**
     * executes a subcommand
     *
     * @param sender the sender
     * @param args   the string arguments in an array
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (sender.hasPermission("karma.command.particles.list")) {
            PlayerData playerData = KarmaPlugin.getInstance().getDatabaseController().getPlayerData(Utils.getUuid(sender));
            double karma = 0.0;
            if (playerData != null) {
                karma = playerData.getKarma();
            }
            Collection<Effect> effects = KarmaPluginConfig.getInstance().getEffects();
            KarmaPlugin.sendMessage(sender, KarmaPluginConfig.getInstance().getText("info.particles.list.header"));
            double finalKarma = karma;
            effects.stream()
                    .sorted(Comparator.comparingInt(Effect::getKarma))
                    .filter(effect -> effect.getKarma() > -1 || sender.hasPermission("karma.effect." + effect.getName().toLowerCase()))
                    .map(effect -> {
                        if (finalKarma >= effect.getKarma() || sender.hasPermission("karma.effect." + effect.getName().toLowerCase())) {
                            return KarmaPluginConfig.getInstance().getText("info.particles.list.elementOk")
                                    .replace("{0}", effect.getDisplayName())
                                    .replace("{1}", String.valueOf(effect.getKarma() > -1 ? effect.getKarma() : 0));
                        }
                        return KarmaPluginConfig.getInstance().getText("info.particles.list.elementNotOk")
                                .replace("{0}", effect.getDisplayName())
                                .replace("{1}", String.valueOf(effect.getKarma()));
                    })
                    .forEachOrdered(message -> KarmaPlugin.sendMessage(sender, message));
        } else {
            KarmaPlugin.sendMessage(sender, KarmaPluginConfig.getInstance().getText("error.noPermission"));
        }
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }
}
