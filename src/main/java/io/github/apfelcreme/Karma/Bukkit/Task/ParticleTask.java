package io.github.apfelcreme.Karma.Bukkit.Task;

import io.github.apfelcreme.Karma.Bukkit.KarmaPlugin;
import io.github.apfelcreme.Karma.Bukkit.ParticleCloud;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

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
public class ParticleTask {

    /**
     * the task id of the repeating task
     */
    private BukkitTask task;

    /**
     * the scheduler instance
     */
    private static ParticleTask instance = null;

    /**
     * a map of players with their particle clouds
     */
    private Map<UUID, ParticleCloud> playerEffectMap = new HashMap<>();

    /**
     * the number of runs the task has done
     */
    private int runs;

    private ParticleTask() {
        task = null;
        runs = 0;
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
        KarmaPlugin.getInstance().logDebug("Creating new particle task");
        task = new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, ParticleCloud> entry : playerEffectMap.entrySet()) {
                    ParticleCloud particleCloud = entry.getValue();
                    if (particleCloud.getDelay() / 100 > 1) {
                        long r = runs % particleCloud.getDelay();
                        if (r < particleCloud.getDelay() / 10) {
                            particleCloud.display((int) (particleCloud.getCount() * (r / (particleCloud.getDelay() / 10.0))));
                        }
                    } else {
                        if ((runs % particleCloud.getDelay()) == 0) {
                            particleCloud.display();
                        }
                    }
                    runs++;
                }
            }
        }.runTaskTimer(KarmaPlugin.getInstance(), 20L, 1L);
    }

    /**
     * kills the task
     */
    private void kill() {
        if (task != null) {
            task.cancel();
            KarmaPlugin.getInstance().logDebug("Cancelling old particle task");
        }
        task = null;
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
        playerEffectMap.put(player.getUniqueId(), particleCloud);
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
        playerEffectMap.remove(player.getUniqueId());
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
        return task != null && task.getTaskId() != -1;
    }

}
