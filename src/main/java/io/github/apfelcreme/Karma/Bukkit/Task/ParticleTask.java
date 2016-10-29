package io.github.apfelcreme.Karma.Bukkit.Task;

import io.github.apfelcreme.Karma.Bukkit.KarmaPlugin;
import io.github.apfelcreme.Karma.Bukkit.ParticleCloud;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
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
public class ParticleTask {

    /**
     * the task id of the repeating task
     */
    private int taskId;

    /**
     * the scheduler instance
     */
    private static ParticleTask instance = null;

    /**
     * a map of players with their particle clouds
     */
    private Map<Player, ParticleCloud> playerEffectMap = null;

    /**
     * the number of runs the task has done
     */
    private int runs;

    private ParticleTask() {
        taskId = -1;
        runs = 0;
        playerEffectMap = new HashMap<>();
    }

    /**
     * returns the scheduler instance
     *
     * @return the scheduler instance
     */
    public static ParticleTask getInstance() {
        if (instance == null) {
            instance = new ParticleTask();
        }
        return instance;
    }

    /**
     * starts a task
     */
    private void create() {
        if (isActive()) {
            kill();
        }
        taskId = KarmaPlugin.getInstance().getServer().getScheduler().scheduleSyncRepeatingTask(KarmaPlugin.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (Map.Entry<Player, ParticleCloud> entry : playerEffectMap.entrySet()) {
                    Player player = entry.getKey();
                    ParticleCloud particleCloud = entry.getValue();
                    if ((runs % particleCloud.getDelay()) == 0) {
                        particleCloud.display(player.getLocation());
                    }
                    runs++;
                }
            }
        }, 20L, 1L);
    }

    /**
     * kills the task
     */
    private void kill() {
        KarmaPlugin.getInstance().getServer().getScheduler().cancelTask(taskId);
        taskId = -1;
        runs = 0;
        playerEffectMap.clear();
    }

    /**
     * adds a particle cloud
     *
     * @param player        the particle cloud owner
     * @param particleCloud the particle cloud
     */
    public void addCloud(Player player, ParticleCloud particleCloud) {
        playerEffectMap.put(player, particleCloud);
        if (!isActive()) {
            create();
        }
    }

    /**
     * adds a particle cloud
     *
     * @param player the particle cloud owner
     */
    public void removeCloud(Player player) {
        playerEffectMap.remove(player);
        if (playerEffectMap.size() == 0) {
            kill();
        }
    }

    /**
     * is the task running at the moment?
     *
     * @return true or false
     */
    public boolean isActive() {
        return taskId != -1;
    }

}
