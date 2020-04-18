package me.c10coding.commands;

import me.c10coding.OneBlock;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Command implements CommandExecutor {

    private OneBlock plugin;

    public Command(OneBlock plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        return false;
    }
}
