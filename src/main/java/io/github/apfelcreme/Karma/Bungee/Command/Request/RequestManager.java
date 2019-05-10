package io.github.apfelcreme.Karma.Bungee.Command.Request;

import io.github.apfelcreme.Karma.Bungee.Utils.Utils;
import net.md_5.bungee.api.CommandSender;

import java.util.HashMap;
import java.util.Map;
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
public class RequestManager {

    /**
     * the RequestManager instance
     */
    private static RequestManager instance = null;

    /**
     * the map of requests
     */
    private Map<UUID, Request> requests;

    /**
     * constructor
     */
    private RequestManager() {
        requests = new HashMap<>();
    }

    /**
     * adds a request
     *
     * @param sender  the request sender
     * @param request the request
     */
    public void addRequest(CommandSender sender, Request request) {
        requests.put(Utils.getUuid(sender), request);
    }

    /**
     * returns a senders latest request
     *
     * @param sender a sender
     * @return his latest request
     */
    public Request getRequest(CommandSender sender) {
        return requests.get(Utils.getUuid(sender));
    }

    /**
     * removes a request
     *
     * @param sender the request sender
     */
    public void removeRequest(CommandSender sender) {
        requests.remove(Utils.getUuid(sender));
    }

    /**
     * returns the RequestManager instance
     *
     * @return the RequestManager instance
     */
    public static RequestManager getInstance() {
        if (instance == null) {
            instance = new RequestManager();
        }
        return instance;
    }
}
