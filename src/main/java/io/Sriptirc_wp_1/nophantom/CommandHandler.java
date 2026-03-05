package io.Sriptirc_wp_1.nophantom;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandHandler implements CommandExecutor, TabCompleter {
    
    private final NoPhantom plugin;
    private final ConfigManager configManager;
    
    public CommandHandler(NoPhantom plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sendHelp(sender);
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "reload":
                return handleReload(sender);
            case "status":
                return handleStatus(sender);
            case "bypass":
                return handleBypass(sender, args);
            case "world":
                return handleWorld(sender, args);
            case "global":
                return handleGlobal(sender, args);
            case "warning":
                return handleWarning(sender, args);
            case "help":
            default:
                sendHelp(sender);
                return true;
        }
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            List<String> subCommands = Arrays.asList("reload", "status", "bypass", "world", "global", "warning", "help");
            for (String sub : subCommands) {
                if (sub.startsWith(args[0].toLowerCase())) {
                    completions.add(sub);
                }
            }
        } else if (args.length == 2) {
            String subCommand = args[0].toLowerCase();
            switch (subCommand) {
                case "bypass":
                    completions.add("add");
                    completions.add("remove");
                    completions.add("list");
                    break;
                case "world":
                    completions.add("enable");
                    completions.add("disable");
                    completions.add("list");
                    break;
                case "global":
                    completions.add("enable");
                    completions.add("disable");
                    break;
                case "warning":
                    completions.add("enable");
                    completions.add("disable");
                    completions.add("message");
                    break;
            }
        } else if (args.length == 3) {
            String subCommand = args[0].toLowerCase();
            if (subCommand.equals("bypass") && (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove"))) {
                // 这里可以添加在线玩家列表，但为了简单起见，我们返回空列表
            } else if (subCommand.equals("world") && (args[1].equalsIgnoreCase("enable") || args[1].equalsIgnoreCase("disable"))) {
                // 返回服务器中的世界列表
                for (org.bukkit.World world : plugin.getServer().getWorlds()) {
                    completions.add(world.getName());
                }
            }
        }
        
        return completions;
    }
    
    private boolean handleReload(CommandSender sender) {
        if (!sender.hasPermission("nophantom.admin")) {
            sender.sendMessage(ChatColor.RED + "你没有权限执行此命令！");
            return true;
        }
        
        configManager.reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "NoPhantom 配置已重新加载！");
        return true;
    }
    
    private boolean handleStatus(CommandSender sender) {
        if (!sender.hasPermission("nophantom.use")) {
            sender.sendMessage(ChatColor.RED + "你没有权限执行此命令！");
            return true;
        }
        
        sender.sendMessage(ChatColor.GOLD + "=== NoPhantom 状态 ===");
        sender.sendMessage(ChatColor.YELLOW + "全局启用: " + (configManager.isGlobalEnabled() ? ChatColor.GREEN + "是" : ChatColor.RED + "否"));
        sender.sendMessage(ChatColor.YELLOW + "发送警告: " + (configManager.shouldSendWarning() ? ChatColor.GREEN + "是" : ChatColor.RED + "否"));
        sender.sendMessage(ChatColor.YELLOW + "警告消息: " + ChatColor.WHITE + configManager.getWarningMessage());
        
        sender.sendMessage(ChatColor.GOLD + "世界设置:");
        for (String world : configManager.getWorldSettings().keySet()) {
            boolean enabled = configManager.getWorldSettings().get(world);
            sender.sendMessage(ChatColor.YELLOW + "  " + world + ": " + 
                (enabled ? ChatColor.GREEN + "允许幻翼" : ChatColor.RED + "禁止幻翼"));
        }
        
        sender.sendMessage(ChatColor.GOLD + "绕过玩家 (" + configManager.getBypassPlayers().size() + "):");
        for (String player : configManager.getBypassPlayers()) {
            sender.sendMessage(ChatColor.YELLOW + "  " + player);
        }
        
        return true;
    }
    
    private boolean handleBypass(CommandSender sender, String[] args) {
        if (!sender.hasPermission("nophantom.admin")) {
            sender.sendMessage(ChatColor.RED + "你没有权限执行此命令！");
            return true;
        }
        
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "用法: /nophantom bypass <add|remove|list> [玩家名]");
            return true;
        }
        
        String action = args[1].toLowerCase();
        
        switch (action) {
            case "add":
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "用法: /nophantom bypass add <玩家名>");
                    return true;
                }
                configManager.addBypassPlayer(args[2]);
                sender.sendMessage(ChatColor.GREEN + "已添加玩家 " + args[2] + " 到绕过列表");
                break;
                
            case "remove":
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "用法: /nophantom bypass remove <玩家名>");
                    return true;
                }
                configManager.removeBypassPlayer(args[2]);
                sender.sendMessage(ChatColor.GREEN + "已从绕过列表移除玩家 " + args[2]);
                break;
                
            case "list":
                sender.sendMessage(ChatColor.GOLD + "绕过玩家列表:");
                for (String player : configManager.getBypassPlayers()) {
                    sender.sendMessage(ChatColor.YELLOW + "  " + player);
                }
                break;
                
            default:
                sender.sendMessage(ChatColor.RED + "未知操作: " + action);
                sender.sendMessage(ChatColor.RED + "可用操作: add, remove, list");
                break;
        }
        
        return true;
    }
    
    private boolean handleWorld(CommandSender sender, String[] args) {
        if (!sender.hasPermission("nophantom.admin")) {
            sender.sendMessage(ChatColor.RED + "你没有权限执行此命令！");
            return true;
        }
        
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "用法: /nophantom world <enable|disable|list> [世界名]");
            return true;
        }
        
        String action = args[1].toLowerCase();
        
        switch (action) {
            case "enable":
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "用法: /nophantom world enable <世界名>");
                    return true;
                }
                configManager.setWorldRule(args[2], true);
                sender.sendMessage(ChatColor.GREEN + "已在世界 " + args[2] + " 启用幻翼生成");
                break;
                
            case "disable":
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "用法: /nophantom world disable <世界名>");
                    return true;
                }
                configManager.setWorldRule(args[2], false);
                sender.sendMessage(ChatColor.GREEN + "已在世界 " + args[2] + " 禁用幻翼生成");
                break;
                
            case "list":
                sender.sendMessage(ChatColor.GOLD + "世界设置:");
                for (String world : configManager.getWorldSettings().keySet()) {
                    boolean enabled = configManager.getWorldSettings().get(world);
                    sender.sendMessage(ChatColor.YELLOW + "  " + world + ": " + 
                        (enabled ? ChatColor.GREEN + "允许幻翼" : ChatColor.RED + "禁止幻翼"));
                }
                break;
                
            default:
                sender.sendMessage(ChatColor.RED + "未知操作: " + action);
                sender.sendMessage(ChatColor.RED + "可用操作: enable, disable, list");
                break;
        }
        
        return true;
    }
    
    private boolean handleGlobal(CommandSender sender, String[] args) {
        if (!sender.hasPermission("nophantom.admin")) {
            sender.sendMessage(ChatColor.RED + "你没有权限执行此命令！");
            return true;
        }
        
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "用法: /nophantom global <enable|disable>");
            return true;
        }
        
        String action = args[1].toLowerCase();
        
        if (action.equals("enable")) {
            configManager.setGlobalEnabled(true);
            sender.sendMessage(ChatColor.GREEN + "已全局启用幻翼生成限制");
        } else if (action.equals("disable")) {
            configManager.setGlobalEnabled(false);
            sender.sendMessage(ChatColor.GREEN + "已全局禁用幻翼生成限制");
        } else {
            sender.sendMessage(ChatColor.RED + "未知操作: " + action);
            sender.sendMessage(ChatColor.RED + "可用操作: enable, disable");
        }
        
        return true;
    }
    
    private boolean handleWarning(CommandSender sender, String[] args) {
        if (!sender.hasPermission("nophantom.admin")) {
            sender.sendMessage(ChatColor.RED + "你没有权限执行此命令！");
            return true;
        }
        
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "用法: /nophantom warning <enable|disable|message> [消息]");
            return true;
        }
        
        String action = args[1].toLowerCase();
        
        switch (action) {
            case "enable":
                configManager.setSendWarning(true);
                sender.sendMessage(ChatColor.GREEN + "已启用幻翼生成警告");
                break;
                
            case "disable":
                configManager.setSendWarning(false);
                sender.sendMessage(ChatColor.GREEN + "已禁用幻翼生成警告");
                break;
                
            case "message":
                if (args.length < 3) {
                    sender.sendMessage(ChatColor.RED + "用法: /nophantom warning message <警告消息>");
                    return true;
                }
                StringBuilder message = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    message.append(args[i]).append(" ");
                }
                configManager.setWarningMessage(message.toString().trim());
                sender.sendMessage(ChatColor.GREEN + "已更新警告消息为: " + message.toString().trim());
                break;
                
            default:
                sender.sendMessage(ChatColor.RED + "未知操作: " + action);
                sender.sendMessage(ChatColor.RED + "可用操作: enable, disable, message");
                break;
        }
        
        return true;
    }
    
    private void sendHelp(CommandSender sender) {
        if (!sender.hasPermission("nophantom.use")) {
            sender.sendMessage(ChatColor.RED + "你没有权限使用此插件！");
            return;
        }
        
        sender.sendMessage(ChatColor.GOLD + "=== NoPhantom 帮助 ===");
        sender.sendMessage(ChatColor.YELLOW + "/nophantom help - 显示此帮助信息");
        sender.sendMessage(ChatColor.YELLOW + "/nophantom status - 显示插件状态");
        
        if (sender.hasPermission("nophantom.admin")) {
            sender.sendMessage(ChatColor.YELLOW + "/nophantom reload - 重新加载配置");
            sender.sendMessage(ChatColor.YELLOW + "/nophantom bypass <add|remove|list> [玩家] - 管理绕过玩家");
            sender.sendMessage(ChatColor.YELLOW + "/nophantom world <enable|disable|list> [世界] - 管理世界设置");
            sender.sendMessage(ChatColor.YELLOW + "/nophantom global <enable|disable> - 全局开关");
            sender.sendMessage(ChatColor.YELLOW + "/nophantom warning <enable|disable|message> [消息] - 管理警告设置");
        }
        
        sender.sendMessage(ChatColor.GOLD + "=========================");
    }
}