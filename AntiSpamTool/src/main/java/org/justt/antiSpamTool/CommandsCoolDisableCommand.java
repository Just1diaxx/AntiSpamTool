package org.justt.antiSpamTool;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandsCoolDisableCommand implements CommandExecutor {
    private final AntiSpamTool plugin;

    public CommandsCoolDisableCommand(AntiSpamTool plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            plugin.DisableCommandsCoolConsole();
            return true;
        }
        Player player = (Player) sender;

        if(player.hasPermission("ast.cooldownsdisable") || player.hasPermission("antispamtool.cooldownsdisable")) {
            plugin.DisableCommandsCool(player);
        } else{
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have permissions to execute this command."));
        }

        return true;
    }
}
