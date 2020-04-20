package me.c10coding.commands;

import me.c10coding.OneBlock;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Command implements CommandExecutor {

    private OneBlock plugin;

    public Command(OneBlock plugin){
        this.plugin = plugin;
        Bukkit.getPluginCommand("oneblock").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if(args[0].equalsIgnoreCase("create") && args.length == 1){

        }else if(args[0].equals("home") && args.length == 1){

        }else if(args[0].equals("delete") && args.length == 1){

        }
        return false;
    }
}
