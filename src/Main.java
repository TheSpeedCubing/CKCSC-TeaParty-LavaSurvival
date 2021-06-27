import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.Blocks;
import net.minecraft.server.v1_16_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Timer;
import java.util.TimerTask;

public class Main extends JavaPlugin implements CommandExecutor {

    Timer timer = new Timer();
    TimerTask timerTask = null;
    int delay = 10000;
    int x = 0;
WorldServer worldServer;
    public void onEnable() {
        Bukkit.getPluginCommand("lava").setExecutor(this);
        worldServer = ((CraftWorld) Bukkit.getWorld("world")).getHandle();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.isOp()) {
            if (args.length == 2) {
                if (args[0].equals("delay")) {
                    int t;
                    try {
                        t = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        return true;
                    }

                    delay = t;
                    if (timerTask != null) {
                        timerTask.cancel();
                        timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                Bukkit.getScheduler().runTask(Main.getPlugin(Main.class), () -> {
                                    for (int X = -464; X < -288; X++) {
                                        for (int Z = -112; Z < 64; Z++) {
                                            worldServer.setTypeAndData(new BlockPosition(X, x, Z), Blocks.LAVA.getBlockData(), 3);
                                        }
                                    }
                                });
                                x++;
                            }
                        };
                        timer.schedule(timerTask, delay, delay);
                    }
                    return true;
                } else if (args[0].equals("height")) {
                    int t;
                    try {
                        t = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        return true;
                    }
                    x = t;
                }
            } else if (args.length == 1) {
                switch (args[0]) {
                    case "start" -> {
                        timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                Bukkit.getScheduler().runTask(Main.getPlugin(Main.class), () -> {
                                    for (int X = -464; X < -288; X++) {
                                        for (int Z = -112; Z < 64; Z++) {
                                            ((CraftWorld) Bukkit.getWorld("world")).getHandle().setTypeAndData(new BlockPosition(X, x, Z), Blocks.LAVA.getBlockData(), 3);
                                        }
                                    }
                                });
                                x++;
                            }
                        };
                        timer.schedule(timerTask, 0, delay);
                    }
                    case "pause" -> {
                        timerTask.cancel();
                        timerTask = null;
                    }
                }
            }
        } else sender.sendMessage("Unknown command. Type \"/help\" for help.");
        return true;
    }
}