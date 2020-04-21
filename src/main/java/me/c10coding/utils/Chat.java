package me.c10coding.utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class Chat {

    public static String firstUppercaseRestLowercase(String s) {
        return s.substring(0,1) + s.substring(1);
    }

    public static String valueOf(int x) {
        return String.valueOf(x);
    }

    public static String valueOf(double x) {
        return String.valueOf(x);
    }

    public static String valueOf(boolean x) {
        return String.valueOf(x);
    }

    public static String chat(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static void sendPlayerMessage(String s, boolean wantPrefix, Player p, String prefix) {
        if(wantPrefix) {
            p.sendMessage(chat(prefix + " &r" + s));
        }else {
            p.sendMessage(chat("&r" + s));
        }
    }

    public static void sendConsoleMessage(String s, String prefix) {
        Bukkit.getConsoleSender().sendMessage(chat(prefix + " &r" + s));
    }

    public static void bsm(String s) {
        Bukkit.broadcastMessage(s);
    }
}
