package org.justt.antiSpamTool;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public final class AntiSpamTool extends JavaPlugin implements Listener {

    private Map<String, Long> commandTimeout = new HashMap<>();
    private Map<String, Long> chatTimeout = new HashMap<>();
    private boolean commandEnabled;
    private boolean chatEnabled;
    private int chatCooldown;
    private int commandCooldown;
    private boolean chatCoolDisabled;
    private boolean commandsCoolDisabled;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getServer().getPluginManager().registerEvents(this, this);
        this.getCommand("reload").setExecutor(new ReloadCommand(this));
        this.getCommand("chatcooldisable").setExecutor(new ChatCoolDisableCommand(this));
        this.getCommand("commandscooldisable").setExecutor(new CommandsCoolDisableCommand(this));
        commandEnabled = getConfig().getBoolean("cooldowns.commands.enabled");
        chatEnabled = getConfig().getBoolean("cooldowns.chat.enabled");
        chatCooldown = getConfigCooldown("cooldowns.chat.seconds");
        commandCooldown = getConfigCooldown("cooldowns.commands.seconds");
        chatCoolDisabled = false;
        commandsCoolDisabled = false;
        getLogger().info("AST has been loaded successfully!");
    }

    @Override
    public void onDisable() {
        getLogger().info("AST is shutting down!");
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if(chatCoolDisabled){
            return;
        }
        if (chatEnabled) {
            if(player.hasPermission("ast.chatbypass") || player.hasPermission("antispamtool.chatbypass")){
                return;
            }
            long remainingTime = getChatCooldownRemaining(player.getName());
            if (remainingTime > 0) {
                player.sendMessage(getConfigString("cooldowns.chat.message")
                        .replace("%seconds%", String.valueOf(remainingTime / 1000)));
                event.setCancelled(true);
            } else {
                chatTimeout.put(player.getName(), System.currentTimeMillis() + chatCooldown);
            }
        }
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if(commandsCoolDisabled){
            return;
        }
        if (commandEnabled) {
            if(player.hasPermission("ast.commandsbypass") || player.hasPermission("antispamtool.commandsbypass")){
                return;
            }
            long remainingTime = getCommandCooldownRemaining(player.getName());
            if (remainingTime > 0) {
                player.sendMessage(getConfigString("cooldowns.commands.message")
                        .replace("%seconds%", String.valueOf(remainingTime / 1000)));
                event.setCancelled(true);
            } else {
                commandTimeout.put(player.getName(), System.currentTimeMillis() + commandCooldown);
            }
        }
    }

    public String getConfigString(String key) {
        String prefix = "&b[&eAST&b]";
        String message = getConfig().getString(key);
        return ChatColor.translateAlternateColorCodes('&', prefix + " " + message);
    }

    public int getConfigCooldown(String key) {
        int cooldown = getConfig().getInt(key);
        return cooldown * 1000;
    }

    public long getChatCooldownRemaining(String playername) {
        Long cooldownEnd = chatTimeout.get(playername);
        if (cooldownEnd != null) {
            long remaining = cooldownEnd - System.currentTimeMillis();
            if (remaining > 0) {
                return remaining;
            } else {
                chatTimeout.remove(playername);
            }
        }
        return 0;
    }

    public long getCommandCooldownRemaining(String playername) {
        Long cooldownEnd = commandTimeout.get(playername);
        if (cooldownEnd != null) {
            long remaining = cooldownEnd - System.currentTimeMillis();
            if (remaining > 0) {
                return remaining;
            } else {
                commandTimeout.remove(playername);
            }
        }
        return 0;
    }

    public void DisableChatCool(Player player){
        if (chatCoolDisabled){
            chatCoolDisabled = false;
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aChat cooldown reactived successfully."));
        } else{
            chatCoolDisabled = true;
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aChat cooldown disabled successfully."));
        }
    }

    public void DisableCommandsCool(Player player){
        if (commandsCoolDisabled){
            commandsCoolDisabled = false;
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aCommands cooldown reactived successfully."));
        } else{
            commandsCoolDisabled = true;
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aCommands cooldown disabled successfully."));
        }
    }

    public void DisableChatCoolConsole(){
        Logger player = getLogger();
        if (chatCoolDisabled){
            chatCoolDisabled = false;
            player.info(ChatColor.translateAlternateColorCodes('&', "&aChat cooldown reactived successfully."));
        } else{
            chatCoolDisabled = true;
            player.info(ChatColor.translateAlternateColorCodes('&', "&aChat cooldown disabled successfully."));
        }
    }

    public void DisableCommandsCoolConsole(){
        Logger player = getLogger();
        if (commandsCoolDisabled){
            commandsCoolDisabled = false;
            player.info(ChatColor.translateAlternateColorCodes('&', "&aCommands cooldown reactived successfully."));
        } else{
            commandsCoolDisabled = true;
            player.info(ChatColor.translateAlternateColorCodes('&', "&aCommands cooldown disabled successfully."));
        }
    }
}
