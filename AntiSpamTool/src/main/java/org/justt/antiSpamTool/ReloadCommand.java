package org.justt.antiSpamTool;

import org.bukkit.ChatColor;
import org.justt.antiSpamTool.AntiSpamTool;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReloadCommand implements CommandExecutor {
    private final AntiSpamTool plugin;

    public ReloadCommand(AntiSpamTool plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            plugin.reloadConfig();
            plugin.getLogger().info(ChatColor.translateAlternateColorCodes('&', "&aAntiSpamTool configuration has been reloaded successfully!"));
            return true;
        }
        Player player = (Player) sender;

        if(player.hasPermission("ast.reload") || player.hasPermission("antispamtool.reload")) {
            plugin.reloadConfig();
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aAntiSpamTool configuration has been reloaded successfully!"));
        } else{
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have permissions to execute this command."));
        }

        return true;
    }
}
