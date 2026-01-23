package com.cropguard;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import java.util.ArrayList;
import java.util.List;

public class CommandHandler implements CommandExecutor, TabCompleter {
    
    private final ConfigManager configManager;
    
    public CommandHandler(ConfigManager configManager) {
        this.configManager = configManager;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
            sendHelp(sender);
            return true;
        }
        
        if (args[0].equalsIgnoreCase("reload")) {
            if (!sender.hasPermission("cropguard.reload")) {
                sender.sendMessage("§c你没有权限执行此命令！");
                return true;
            }
            
            configManager.reloadConfig();
            sender.sendMessage("§a配置已重载！");
            return true;
        }
        
        if (args[0].equalsIgnoreCase("info")) {
            sender.sendMessage("§6=== 农作物防踩踏插件信息 ===");
            sender.sendMessage("§e保护作物数量: §f" + configManager.getProtectedCrops().size());
            sender.sendMessage("§e生效世界: §f" + (configManager.getEnabledWorlds().isEmpty() ? "所有世界" : configManager.getEnabledWorlds()));
            sender.sendMessage("§e提示消息: §f" + (configManager.isMessagesEnabled() ? "启用" : "禁用"));
            sender.sendMessage("§e绕过权限: §f" + configManager.getBypassPermission());
            return true;
        }
        
        sendHelp(sender);
        return true;
    }
    
    private void sendHelp(CommandSender sender) {
        sender.sendMessage("§6=== 农作物防踩踏插件帮助 ===");
        sender.sendMessage("§e/cropguard reload §f- 重载插件配置");
        sender.sendMessage("§e/cropguard info §f- 查看插件信息");
        sender.sendMessage("§e/cropguard help §f- 显示此帮助");
        
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("cropguard.bypass")) {
                sender.sendMessage("§a你拥有绕过权限，可以踩踏农作物");
            } else {
                sender.sendMessage("§c你没有绕过权限，无法踩踏受保护的农作物");
            }
        }
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            String partial = args[0].toLowerCase();
            if ("reload".startsWith(partial) && sender.hasPermission("cropguard.reload")) {
                completions.add("reload");
            }
            if ("info".startsWith(partial)) {
                completions.add("info");
            }
            if ("help".startsWith(partial)) {
                completions.add("help");
            }
        }
        
        return completions;
    }
}