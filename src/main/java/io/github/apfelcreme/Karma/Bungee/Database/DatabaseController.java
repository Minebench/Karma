package io.github.apfelcreme.Karma.Bungee.Database;

import io.github.apfelcreme.Karma.Bungee.User.PlayerData;
import io.github.apfelcreme.Karma.Bungee.User.Relation;
import io.github.apfelcreme.Karma.Bungee.User.Transaction;

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
 *
 * @author Lord36 aka Apfelcreme
 */
public interface DatabaseController {

    /**
     * selects a players userdata
     *
     * @param uuid the players uuid
     */
    PlayerData getPlayerData(UUID uuid);

    /**
     * inserts a transaction
     *
     * @param transaction the transaction that is being inserted
     * @return the transaction id
     */
    Integer insertTransaction(Transaction transaction);

    /**
     * deletes a transaction
     *
     * @param transaction the transaction that shall be deleted
     */
    void deleteTransaction(Transaction transaction);

    /**
     * inserts a players data
     *
     * @param playerData the playerData that is being inserted
     * @return the player id
     */
    Integer insertPlayerData(PlayerData playerData);

    /**
     * prints the n highest ranking players
     *
     * @param size the number of displayed players
     * @return a sorted list
     */
    List<PlayerData> getTopList(Integer size);

}
