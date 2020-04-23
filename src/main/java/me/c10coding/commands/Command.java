package me.c10coding.commands;

import me.c10coding.OneBlock;
import me.c10coding.managers.OneBlockManager;
import me.c10coding.utils.Chat;
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
            }else if(args[0].equalsIgnoreCase("home")){
                if(args.length == 1){
                    obManager.teleportHome(p);
                }else if(args.length == 2 && args[1].equalsIgnoreCase("origin")){
                    obManager.teleportToOrigin(p);
                }
            }else if(args[0].equalsIgnoreCase("delete") && args.length == 1){
                obManager.deleteArea(p);
            }else if(args[0].equalsIgnoreCase("sethome") && args.length == 1){
                obManager.setHome(p);
            }else if(args[0].equalsIgnoreCase("delhome") && args.length == 1){
                obManager.delHome(p);
            }else if(args[0].equalsIgnoreCase("help")){
                obManager.sendHelp(p);
            }else{
                Chat.sendPlayerMessage("I did not recognize your command! Do /ob help to see the list of commands!", true, p, plugin.getConfig().getString("Prefix"));
            }
        }
        return false;

    }
}
