package io.Sriptirc_wp_1.playerstatsmenu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandHandler implements CommandExecutor, TabCompleter {
    private final PlayerStatsMenu plugin;
    private final ConfigManager configManager;
    private final StatsGUI statsGUI;
    
    public CommandHandler(PlayerStatsMenu plugin, ConfigManager configManager, StatsGUI statsGUI) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.statsGUI = statsGUI;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!configManager.isCommandEnabled()) {
            sender.sendMessage(ChatColor.RED + "命令功能已被禁用！");
            return true;
        }
        
        if (args.length == 0) {
            // 打开自己的菜单
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', 
                    configManager.getMessage("only-player")));
                return true;
            }
            
            Player player = (Player) sender;
            if (!player.hasPermission("playerstatsmenu.use")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', 
                    configManager.getMessage("no-permission")));
                return true;
            }
            
            openStatsMenu(player);
            return true;
        }
        
        String subCommand = args[0].toLowerCase();
        
        switch (subCommand) {
            case "open":
                handleOpenCommand(sender, args);
                break;
                
            case "give":
                handleGiveCommand(sender, args);
                break;
                
            case "reload":
                handleReloadCommand(sender);
                break;
                
            case "help":
                handleHelpCommand(sender);
                break;
                
            default:
                sender.sendMessage(ChatColor.RED + "未知命令！使用 /stats help 查看帮助");
                break;
        }
        
        return true;
    }
    
    private void handleOpenCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("playerstatsmenu.admin")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', 
                configManager.getMessage("no-permission")));
            return;
        }
        
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "用法: /stats open <玩家名>");
            return;
        }
        
        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "玩家 " + args[1] + " 不在线！");
            return;
        }
        
        openStatsMenu(target);
        sender.sendMessage(ChatColor.GREEN + "已为玩家 " + target.getName() + " 打开状态菜单");
    }
    
    private void handleGiveCommand(CommandSender sender, String[] args) {
        if (!sender.hasPermission("playerstatsmenu.admin")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', 
                configManager.getMessage("no-permission")));
            return;
        }
        
        if (!configManager.isItemEnabled()) {
            sender.sendMessage(ChatColor.RED + "物品功能已被禁用！");
            return;
        }
        
        Player target;
        if (args.length < 2) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "用法: /stats give <玩家名>");
                return;
            }
            target = (Player) sender;
        } else {
            target = Bukkit.getPlayer(args[1]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "玩家 " + args[1] + " 不在线！");
                return;
            }
        }
        
        giveStatsItem(target);
        if (sender != target) {
            sender.sendMessage(ChatColor.GREEN + "已给予玩家 " + target.getName() + " 状态查看器");
        }
    }
    
    private void handleReloadCommand(CommandSender sender) {
        if (!sender.hasPermission("playerstatsmenu.admin")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', 
                configManager.getMessage("no-permission")));
            return;
        }
        
        plugin.reloadConfig();
        configManager.reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "配置文件已重新加载！");
    }
    
    private void handleHelpCommand(CommandSender sender) {
        sender.sendMessage(ChatColor.GOLD + "=== PlayerStatsMenu 帮助 ===");
        sender.sendMessage(ChatColor.YELLOW + "/stats" + ChatColor.WHITE + " - 打开状态信息菜单");
        sender.sendMessage(ChatColor.YELLOW + "/stats open <玩家>" + ChatColor.WHITE + " - 为指定玩家打开菜单");
        sender.sendMessage(ChatColor.YELLOW + "/stats give [玩家]" + ChatColor.WHITE + " - 给予状态查看器物品");
        sender.sendMessage(ChatColor.YELLOW + "/stats reload" + ChatColor.WHITE + " - 重新加载配置文件");
        sender.sendMessage(ChatColor.YELLOW + "/stats help" + ChatColor.WHITE + " - 显示此帮助");
        sender.sendMessage("");
        sender.sendMessage(ChatColor.GRAY + "权限节点:");
        sender.sendMessage(ChatColor.WHITE + "• playerstatsmenu.use" + ChatColor.GRAY + " - 使用菜单");
        sender.sendMessage(ChatColor.WHITE + "• playerstatsmenu.admin" + ChatColor.GRAY + " - 管理功能");
    }
    
    private void openStatsMenu(Player player) {
        player.openInventory(statsGUI.createStatsMenu(player));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', 
            configManager.getMessage("menu-opened")));
    }
    
    private void giveStatsItem(Player player) {
        // 检查玩家是否已经有这个物品
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.hasItemMeta()) {
                ItemMeta meta = item.getItemMeta();
                if (meta.hasDisplayName() && 
                    meta.getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', 
                        configManager.getItemDisplayName()))) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', 
                        configManager.getMessage("item-already-have")));
                    return;
                }
            }
        }
        
        // 创建物品
        Material material = Material.getMaterial(configManager.getItemMaterial());
        if (material == null) {
            material = Material.COMPASS;
        }
        
        ItemStack statsItem = new ItemStack(material);
        ItemMeta meta = statsItem.getItemMeta();
        
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', 
            configManager.getItemDisplayName()));
        
        List<String> lore = new ArrayList<>();
        String[] loreLines = configManager.getItemLore().split("\\\\n");
        for (String line : loreLines) {
            lore.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        meta.setLore(lore);
        
        statsItem.setItemMeta(meta);
        
        // 给予玩家
        if (player.getInventory().firstEmpty() == -1) {
            // 背包已满，掉落在地上
            player.getWorld().dropItem(player.getLocation(), statsItem);
            player.sendMessage(ChatColor.YELLOW + "你的背包已满，物品已掉落在地上！");
        } else {
            player.getInventory().addItem(statsItem);
        }
        
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', 
            configManager.getMessage("item-given")));
    }
    
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();
        
        if (args.length == 1) {
            // 主命令补全
            List<String> subCommands = Arrays.asList("open", "give", "reload", "help");
            for (String subCmd : subCommands) {
                if (subCmd.startsWith(args[0].toLowerCase())) {
                    completions.add(subCmd);
                }
            }
        } else if (args.length == 2) {
            // 子命令参数补全
            String subCommand = args[0].toLowerCase();
            
            if (subCommand.equals("open") || subCommand.equals("give")) {
                // 玩家名补全
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getName().toLowerCase().startsWith(args[1].toLowerCase())) {
                        completions.add(player.getName());
                    }
                }
            }
        }
        
        return completions;
    }
}