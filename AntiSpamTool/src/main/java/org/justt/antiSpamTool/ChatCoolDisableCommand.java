package org.justt.antiSpamTool;

import org.bukkit.ChatColor;
import org.justt.antiSpamTool.AntiSpamTool;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChatCoolDisableCommand implements CommandExecutor {
    private final AntiSpamTool plugin;

    public ChatCoolDisableCommand(AntiSpamTool plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            plugin.DisableChatCoolConsole();
            return true;
        }
        Player player = (Player) sender;

        if(player.hasPermission("ast.cooldownsdisable") || player.hasPermission("antispamtool.cooldownsdisable")) {
            plugin.DisableChatCool(player);
        } else{
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not have permissions to execute this command."));
        }

        return true;
    }
}
