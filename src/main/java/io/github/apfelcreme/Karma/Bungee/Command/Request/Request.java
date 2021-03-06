package io.github.apfelcreme.Karma.Bungee.Command.Request;

import net.md_5.bungee.api.CommandSender;

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
public abstract class Request {

    /**
     * the sender who created the request
     */
    private final CommandSender sender;

    /**
     * constructor
     *
     * @param sender the sender who created the request
     */
    public Request(CommandSender sender) {
        this.sender = sender;
    }

    /**
     * executes the request
     */
    public abstract void execute();

    /**
     * returns the sender who created the request
     *
     * @return the sender who created the request
     */
    public CommandSender getSender() {
        return sender;
    }
}
