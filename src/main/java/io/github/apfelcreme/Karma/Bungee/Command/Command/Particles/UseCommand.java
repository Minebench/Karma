package io.github.apfelcreme.Karma.Bungee.Command.Command.Particles;

import de.themoep.vnpbungee.VNPBungee;
import io.github.apfelcreme.Karma.Bungee.BukkitMessenger;
import io.github.apfelcreme.Karma.Bungee.Command.SubCommand;
import io.github.apfelcreme.Karma.Bungee.KarmaPlugin;
import io.github.apfelcreme.Karma.Bungee.KarmaPluginConfig;
import io.github.apfelcreme.Karma.Bungee.Particle.Effect;
import io.github.apfelcreme.Karma.Bungee.User.PlayerData;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
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
public class UseCommand implements SubCommand {

    /**
     * executes a subcommand
     *
     * @param sender the sender
     * @param args   the string arguments in an array
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (player.hasPermission("karma.command.particles.use")) {
            if (args.length > 0) {
                Effect effect = KarmaPluginConfig.getInstance().getEffect(args[0]);
                if (effect != null) {
                    PlayerData playerData = KarmaPlugin.getInstance().getDatabaseController().getPlayerData(player.getUniqueId());
                    if (playerData != null) {
                        int karmaThreshold = -1;
                        for (Map.Entry<Integer, Effect> entry : KarmaPluginConfig.getInstance().getParticles().entrySet()) {
                            if (entry.getValue().equals(effect)) {
                                karmaThreshold = entry.getKey();
                                break;
                            }
                        }
                        if (karmaThreshold == -1) {
                            if (karmaThreshold <= playerData.getKarma()) {
                                playerData.setEffect(effect);
                                playerData.save();
                                KarmaPlugin.sendMessage(player, KarmaPluginConfig.getInstance().getText("info.particles.use.success"));
                                KarmaPlugin.getInstance().getLogger().info(player.getName()
                                        + " changed his particles to " + effect.getDisplayName());
                                if (VNPBungee.getInstance().getVanishStatus(player) != VNPBungee.VanishStatus.VANISHED) {
                                    BukkitMessenger.applyParticles(player, effect);
                                }
                            } else {
                                KarmaPlugin.sendMessage(player, KarmaPluginConfig.getInstance().getText("error.notEnoughKarma"));
                            }
                        } else {
                            KarmaPlugin.sendMessage(player, KarmaPluginConfig.getInstance().getText("error.unavailableEffect")
                                    .replace("{0}", args[0]));
                        }
                    }
                } else {
                    KarmaPlugin.sendMessage(player, KarmaPluginConfig.getInstance().getText("error.unknownEffect")
                            .replace("{0}", args[0]));
                }
            } else {
                KarmaPlugin.sendMessage(player, KarmaPluginConfig.getInstance().getText("error.wrongUsage.particles.use"));
            }
        } else {
            KarmaPlugin.sendMessage(player, KarmaPluginConfig.getInstance().getText("error.noPermission"));
        }
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (sender.hasPermission("karma.command.particles.use")) {
            for (Effect effect : KarmaPluginConfig.getInstance().getParticles().values()) {
                suggestions.add(effect.getDisplayName());
            }
        }
        return suggestions;
    }
}
