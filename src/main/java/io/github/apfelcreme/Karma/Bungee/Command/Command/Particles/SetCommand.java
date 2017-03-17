package io.github.apfelcreme.Karma.Bungee.Command.Command.Particles;

import de.themoep.vnpbungee.VNPBungee;
import io.github.apfelcreme.Karma.Bungee.BukkitMessenger;
import io.github.apfelcreme.Karma.Bungee.Command.SubCommand;
import io.github.apfelcreme.Karma.Bungee.Command.TabCompleter;
import io.github.apfelcreme.Karma.Bungee.KarmaPlugin;
import io.github.apfelcreme.Karma.Bungee.KarmaPluginConfig;
import io.github.apfelcreme.Karma.Bungee.Particle.Effect;
import io.github.apfelcreme.Karma.Bungee.User.PlayerData;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
 * @author Lord36 aka Apfelcreme
 */
public class SetCommand implements SubCommand {

    /**
     * executes a subcommand
     * @param sender the sender
     * @param args   the string arguments in an array
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        ProxiedPlayer player = (ProxiedPlayer) sender;
        if (player.hasPermission("karma.command.particles.set")) {
            if (args.length > 1) {
                UUID targetUUID = KarmaPlugin.getInstance().getUUIDByName(args[0]);
                if (targetUUID != null) {
                    Effect effect = Effect.getEffect(args[1]);
                    if (effect != null && KarmaPluginConfig.getInstance().getParticles().values().contains(effect)) {
                        PlayerData playerData = KarmaPlugin.getInstance().getDatabaseController().getPlayerData(targetUUID);
                        if (playerData != null) {
                            playerData.setEffect(effect);
                            playerData.save();
                            KarmaPlugin.sendMessage(player, KarmaPluginConfig.getInstance().getText("info.particles.set.success"));
                            KarmaPlugin.getInstance().getLogger().info(args[0] + "s Particles were set to "
                                    + effect.getDisplayName() + " by " + player.getName());
                            ProxiedPlayer targetPlayer = ProxyServer.getInstance().getPlayer(targetUUID);
                            if ((targetPlayer != null)
                                    && (VNPBungee.getInstance().getVanishStatus(targetPlayer) != VNPBungee.VanishStatus.VANISHED)) {
                                BukkitMessenger.applyParticles(targetPlayer, effect);
                            }
                        }
                    } else {
                        KarmaPlugin.sendMessage(player, KarmaPluginConfig.getInstance().getText("error.unknownEffect")
                                .replace("{0}", args[1]));
                    }
                } else {
                    KarmaPlugin.sendMessage(player, KarmaPluginConfig.getInstance().getText("error.unknownPlayer"));
                }
            } else {
                KarmaPlugin.sendMessage(player, KarmaPluginConfig.getInstance().getText("error.wrongUsage.particles.set"));
            }
        } else {
            KarmaPlugin.sendMessage(player, KarmaPluginConfig.getInstance().getText("error.noPermission"));
        }
    }

    @Override
    public List<String> getTabCompletions(CommandSender sender, String[] args) {
        List<String> suggestions = new ArrayList<>();
        if (sender.hasPermission("karma.command.particles.set")) {
            if (args.length == 2) {
                suggestions.addAll(TabCompleter.getPlayers(sender));
            } else if (args.length == 3) {
                for (Effect effect : KarmaPluginConfig.getInstance().getParticles().values()) {
                    suggestions.add(effect.getDisplayName());
                }
            }
        }
        return suggestions;
    }
}
