package me.c10coding.commands;

import me.c10coding.OneBlock;
import me.c10coding.managers.OneBlockManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Command implements CommandExecutor {

    private OneBlock plugin;
    private OneBlockManager obManager;

    public Command(OneBlock plugin){
        this.plugin = plugin;
        this.obManager = new OneBlockManager(plugin);
        Bukkit.getPluginCommand("oneblock").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(args[0].equalsIgnoreCase("create") && args.length == 1){
                obManager.createArea(p);
            }else if(args[0].equals("home") && args.length == 1){
                obManager.teleportHome(p);
            }else if(args[0].equals("delete") && args.length == 1){
                obManager.deleteArea(p);
            }else if(args[0].equals("sethome") && args.length == 1){
                obManager.setHome(p);
            }else if(args[0].equals("delhome") && args.length == 1){
                obManager.delHome(p);
            }
        }
        return false;

    }
}
