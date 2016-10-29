package io.github.apfelcreme.Karma.Bungee.Command.Request;

import io.github.apfelcreme.Karma.Bungee.KarmaPlugin;
import io.github.apfelcreme.Karma.Bungee.KarmaPluginConfig;
import io.github.apfelcreme.Karma.Bungee.User.PlayerData;
import io.github.apfelcreme.Karma.Bungee.User.Relation;
import io.github.apfelcreme.Karma.Bungee.User.Transaction;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Collection;
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
 *
 * @author Lord36 aka Apfelcreme
 */
public class ResetRequest extends Request {

    /**
     * the uuid of the players whose transaction are being reset
     */
    private UUID target;

    /**
     * constructor
     *
     * @param sender the player who created the request
     */
    public ResetRequest(ProxiedPlayer sender, UUID target) {
        super(sender);
        this.target = target;
    }

    /**
     * executes the request
     */
    @Override
    public void execute() {
        PlayerData playerData = KarmaPlugin.getInstance().getDatabaseController().getPlayerData(target);
        if (playerData != null) {
            Collection<Relation> relations = playerData.getRelations().values();
            // delete all transaction a player has done
            int s = 0;
            int i = 0;
            for (Relation relation : relations) {
                s++;
                for (Transaction transaction : relation.getTransactionsDoneTo()) {
                    transaction.delete();
                    i++;
                }
            }
            KarmaPlugin.sendMessage(getSender(), KarmaPluginConfig.getInstance().getText("info.karma.reset.reset")
                    .replace("{0}", String.valueOf(i))
                    .replace("{1}", String.valueOf(s)));
        }
    }
}
