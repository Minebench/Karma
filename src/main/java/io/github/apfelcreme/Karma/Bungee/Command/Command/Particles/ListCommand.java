package io.github.apfelcreme.Karma.Bungee.Command.Command.Particles;

import io.github.apfelcreme.Karma.Bungee.Command.SubCommand;
import io.github.apfelcreme.Karma.Bungee.KarmaPlugin;
import io.github.apfelcreme.Karma.Bungee.KarmaPluginConfig;
import io.github.apfelcreme.Karma.Bungee.Particle.Effect;
import io.github.apfelcreme.Karma.Bungee.User.PlayerData;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
public class ListCommand implements SubCommand {

    /**
     * executes a subcommand
     *
     * @param sender the sender
     * @param args   the string arguments in an array
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (player.hasPermission("Karma.user")) {
            PlayerData playerData = KarmaPlugin.getInstance().getDatabaseController().getPlayerData(player.getUniqueId());
            Double karma = 0.0;
            if (playerData != null) {
                karma = playerData.getKarma();
            }
            Map<Integer, Effect> effects = KarmaPluginConfig.getInstance().getParticles();
            KarmaPlugin.sendMessage(player, KarmaPluginConfig.getInstance().getText("info.particles.list.header"));
            for (Map.Entry<Integer, Effect> entry : effects.entrySet()) {
                int karmaThreshold = entry.getKey();
                Effect effect = entry.getValue();
                if (karma >= karmaThreshold) {
                    KarmaPlugin.sendMessage(player, KarmaPluginConfig.getInstance().getText("info.particles.list.elementOk")
                            .replace("{0}", effect.getDisplayName())
                            .replace("{1}", String.valueOf(karmaThreshold)));
                } else {
                    KarmaPlugin.sendMessage(player, KarmaPluginConfig.getInstance().getText("info.particles.list.elementNotOk")
                            .replace("{0}", effect.getDisplayName())
                            .replace("{1}", String.valueOf(karmaThreshold)));
                }
            }
        } else {
            KarmaPlugin.sendMessage(player, KarmaPluginConfig.getInstance().getText("error.noPermission"));
        }
    }
}