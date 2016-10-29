package io.github.apfelcreme.Karma.Bungee.Exception;

import io.github.apfelcreme.Karma.Bungee.User.Relation;

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
public class OncePerDayException extends Exception {

    /**
     * the relation
     */
    private Relation relation;

    /**
     * gets called when a transaction is saved and the latest transaction isn't 24 hours old yet
     *
     * @param relation the relation of both players
     */
    public OncePerDayException(Relation relation) {
        this.relation = relation;
    }

    /**
     * returns the relation
     *
     * @return the relation
     */
    public Relation getRelation() {
        return relation;
    }
}
