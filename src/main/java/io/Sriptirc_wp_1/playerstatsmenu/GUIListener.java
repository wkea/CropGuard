package io.Sriptirc_wp_1.playerstatsmenu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GUIListener implements Listener {
    private final PlayerStatsMenu plugin;
    private final ConfigManager configManager;
    private final StatsGUI statsGUI;
    
    public GUIListener(PlayerStatsMenu plugin, ConfigManager configManager, StatsGUI statsGUI) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.statsGUI = statsGUI;
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }
        
        Player player = (Player) event.getWhoClicked();
        Inventory inventory = event.getInventory();
        ItemStack clickedItem = event.getCurrentItem();
        
        // 检查是否是我们的菜单
        String title = ChatColor.translateAlternateColorCodes('&', configManager.getGuiTitle());
        if (!inventory.getView().getTitle().equals(title)) {
            return;
        }
        
        // 取消点击事件
        event.setCancelled(true);
        
        if (clickedItem == null || clickedItem.getType() == Material.AIR) {
            return;
        }
        
        // 获取点击的槽位
        int slot = event.getRawSlot();
        
        // 处理不同槽位的点击
        switch (slot) {
            case 33: // 刷新按钮 (第4行第7列)
                handleRefreshClick(player, inventory);
                break;
                
            case 36: // 关闭按钮 (第5行第1列)
                handleCloseClick(player);
                break;
                
            case 44: // 帮助按钮 (第5行第9列)
                handleHelpClick(player);
                break;
                
            default:
                // 信息物品点击
                handleInfoItemClick(player, slot, clickedItem);
                break;
        }
    }
    
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        // 可以在这里添加关闭菜单时的逻辑
        // 例如：保存数据、清理缓存等
    }
    
    private void handleRefreshClick(Player player, Inventory inventory) {
        statsGUI.refreshMenu(player, inventory);
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', 
            configManager.getMessage("info-refreshed")));
    }
    
    private void handleCloseClick(Player player) {
        player.closeInventory();
    }
    
    private void handleHelpClick(Player player) {
        player.closeInventory();
        player.sendMessage(ChatColor.GOLD + "=== 玩家状态信息菜单帮助 ===");
        player.sendMessage(ChatColor.YELLOW + "这个菜单显示你的各项游戏数据：");
        player.sendMessage(ChatColor.WHITE + "• 血量: 当前生命值和最大生命值");
        player.sendMessage(ChatColor.WHITE + "• 护甲: 穿戴装备提供的防护");
        player.sendMessage(ChatColor.WHITE + "• 移动速度: 行走和飞行速度");
        player.sendMessage(ChatColor.WHITE + "• 饥饿值: 饥饿等级和饱和度");
        player.sendMessage(ChatColor.WHITE + "• 经验等级: 当前等级和经验进度");
        player.sendMessage(ChatColor.WHITE + "• 游戏模式: 当前游戏模式和状态");
        player.sendMessage(ChatColor.WHITE + "• 在线时间: 累计游戏时间");
        player.sendMessage(ChatColor.WHITE + "• 当前世界: 所在世界信息");
        player.sendMessage("");
        player.sendMessage(ChatColor.YELLOW + "使用方法:");
        player.sendMessage(ChatColor.WHITE + "• 命令: /stats");
        player.sendMessage(ChatColor.WHITE + "• 右键状态查看器物品");
        player.sendMessage(ChatColor.WHITE + "• 管理员: /stats give 获取物品");
    }
    
    private void handleInfoItemClick(Player player, int slot, ItemStack item) {
        // 根据槽位发送详细信息
        switch (slot) {
            case 11: // 血量
                sendHealthDetails(player);
                break;
                
            case 13: // 护甲
                sendArmorDetails(player);
                break;
                
            case 15: // 移动速度
                sendSpeedDetails(player);
                break;
                
            case 20: // 饥饿值
                sendHungerDetails(player);
                break;
                
            case 22: // 经验等级
                sendExpDetails(player);
                break;
                
            case 24: // 游戏模式
                sendGameModeDetails(player);
                break;
                
            case 29: // 在线时间
                sendPlayTimeDetails(player);
                break;
                
            case 31: // 当前世界
                sendWorldDetails(player);
                break;
                
            default:
                // 其他槽位不处理
                break;
        }
    }
    
    private void sendHealthDetails(Player player) {
        player.sendMessage(ChatColor.GOLD + "=== 血量详细信息 ===");
        player.sendMessage(ChatColor.WHITE + "当前血量: " + ChatColor.RED + 
                         String.format("%.1f", player.getHealth()) + " / " + 
                         String.format("%.1f", player.getMaxHealth()));
        player.sendMessage(ChatColor.WHITE + "生命值恢复: " + ChatColor.GREEN + "自然恢复");
        player.sendMessage(ChatColor.WHITE + "伤害吸收: " + ChatColor.GOLD + "无");
        player.sendMessage(ChatColor.GRAY + "提示: 生命值低于3❤时屏幕会变红");
    }
    
    private void sendArmorDetails(Player player) {
        player.sendMessage(ChatColor.GOLD + "=== 护甲详细信息 ===");
        
        ItemStack[] armor = player.getInventory().getArmorContents();
        String[] armorNames = {"头盔", "胸甲", "护腿", "靴子"};
        
        for (int i = 0; i < armor.length; i++) {
            ItemStack item = armor[i];
            if (item != null && item.getType() != Material.AIR) {
                player.sendMessage(ChatColor.WHITE + armorNames[i] + ": " + 
                                 ChatColor.AQUA + item.getType().toString());
            } else {
                player.sendMessage(ChatColor.WHITE + armorNames[i] + ": " + 
                                 ChatColor.RED + "未穿戴");
            }
        }
        
        player.sendMessage(ChatColor.GRAY + "提示: 穿戴完整套装可能有额外效果");
    }
    
    private void sendSpeedDetails(Player player) {
        player.sendMessage(ChatColor.GOLD + "=== 移动速度详细信息 ===");
        player.sendMessage(ChatColor.WHITE + "基础行走速度: " + ChatColor.GREEN + 
                         String.format("%.0f%%", player.getWalkSpeed() * 100));
        player.sendMessage(ChatColor.WHITE + "基础飞行速度: " + ChatColor.GREEN + 
                         String.format("%.0f%%", player.getFlySpeed() * 100));
        
        boolean hasSpeedEffect = false;
        for (org.bukkit.potion.PotionEffect effect : player.getActivePotionEffects()) {
            if (effect.getType().equals(org.bukkit.potion.PotionEffectType.SPEED)) {
                hasSpeedEffect = true;
                player.sendMessage(ChatColor.WHITE + "速度效果: " + ChatColor.GREEN + 
                                 "等级 " + (effect.getAmplifier() + 1));
                player.sendMessage(ChatColor.WHITE + "剩余时间: " + ChatColor.GREEN + 
                                 effect.getDuration() / 20 + "秒");
            }
        }
        
        if (!hasSpeedEffect) {
            player.sendMessage(ChatColor.WHITE + "速度效果: " + ChatColor.RED + "无");
        }
        
        player.sendMessage(ChatColor.GRAY + "提示: 速度药水可提升移动速度");
    }
    
    private void sendHungerDetails(Player player) {
        player.sendMessage(ChatColor.GOLD + "=== 饥饿值详细信息 ===");
        player.sendMessage(ChatColor.WHITE + "饥饿等级: " + ChatColor.YELLOW + 
                         player.getFoodLevel() + " / 20");
        player.sendMessage(ChatColor.WHITE + "饱和度: " + ChatColor.YELLOW + 
                         String.format("%.1f", player.getSaturation()));
        player.sendMessage(ChatColor.WHITE + "消耗状态: " + 
                         (player.getFoodLevel() > 6 ? ChatColor.GREEN + "正常" : 
                          player.getFoodLevel() > 0 ? ChatColor.YELLOW + "饥饿" : 
                          ChatColor.RED + "极度饥饿"));
        player.sendMessage(ChatColor.GRAY + "提示: 饥饿值过低会影响生命恢复和移动");
    }
    
    private void sendExpDetails(Player player) {
        player.sendMessage(ChatColor.GOLD + "=== 经验等级详细信息 ===");
        player.sendMessage(ChatColor.WHITE + "当前等级: " + ChatColor.LIGHT_PURPLE + 
                         player.getLevel() + "级");
        player.sendMessage(ChatColor.WHITE + "经验进度: " + ChatColor.LIGHT_PURPLE + 
                         String.format("%.1f%%", player.getExp() * 100));
        
        int nextLevelExp = getExpForLevel(player.getLevel());
        player.sendMessage(ChatColor.WHITE + "升级所需: " + ChatColor.LIGHT_PURPLE + 
                         nextLevelExp + "经验");
        
        player.sendMessage(ChatColor.GRAY + "提示: 等级越高，升级所需经验越多");
    }
    
    private void sendGameModeDetails(Player player) {
        player.sendMessage(ChatColor.GOLD + "=== 游戏模式详细信息 ===");
        player.sendMessage(ChatColor.WHITE + "当前模式: " + ChatColor.DARK_GREEN + 
                         player.getGameMode().toString());
        player.sendMessage(ChatColor.WHITE + "飞行状态: " + 
                         (player.isFlying() ? ChatColor.GREEN + "飞行中" : ChatColor.RED + "未飞行"));
        player.sendMessage(ChatColor.WHITE + "潜行状态: " + 
                         (player.isSneaking() ? ChatColor.GREEN + "潜行中" : ChatColor.RED + "未潜行"));
        player.sendMessage(ChatColor.WHITE + "疾跑状态: " + 
                         (player.isSprinting() ? ChatColor.GREEN + "疾跑中" : ChatColor.RED + "未疾跑"));
        player.sendMessage(ChatColor.WHITE + "滑翔状态: " + 
                         (player.isGliding() ? ChatColor.GREEN + "滑翔中" : ChatColor.RED + "未滑翔"));
    }
    
    private void sendPlayTimeDetails(Player player) {
        player.sendMessage(ChatColor.GOLD + "=== 在线时间详细信息 ===");
        
        int ticks = player.getStatistic(org.bukkit.Statistic.PLAY_ONE_MINUTE);
        int totalSeconds = ticks / 20;
        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int seconds = totalSeconds % 60;
        
        player.sendMessage(ChatColor.WHITE + "总游戏时间: " + ChatColor.GOLD + 
                         String.format("%02d:%02d:%02d", hours, minutes, seconds));
        player.sendMessage(ChatColor.WHITE + "游戏刻数: " + ChatColor.GOLD + ticks + " ticks");
        
        // 获取首次加入时间（如果支持）
        long firstPlayed = player.getFirstPlayed();
        if (firstPlayed > 0) {
            java.util.Date firstDate = new java.util.Date(firstPlayed);
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            player.sendMessage(ChatColor.WHITE + "首次加入: " + ChatColor.GOLD + sdf.format(firstDate));
        }
    }
    
    private void sendWorldDetails(Player player) {
        player.sendMessage(ChatColor.GOLD + "=== 世界详细信息 ===");
        org.bukkit.World world = player.getWorld();
        
        player.sendMessage(ChatColor.WHITE + "世界名称: " + ChatColor.BLUE + world.getName());
        player.sendMessage(ChatColor.WHITE + "环境类型: " + ChatColor.BLUE + world.getEnvironment().toString());
        player.sendMessage(ChatColor.WHITE + "游戏时间: " + ChatColor.BLUE + 
                         formatTime(world.getTime()));
        player.sendMessage(ChatColor.WHITE + "天气状态: " + ChatColor.BLUE + 
                         (world.hasStorm() ? "雷雨" : world.isThundering() ? "雷暴" : "晴朗"));
        player.sendMessage(ChatColor.WHITE + "难度: " + ChatColor.BLUE + world.getDifficulty().toString());
        player.sendMessage(ChatColor.WHITE + "世界类型: " + ChatColor.BLUE + world.getWorldType().toString());
    }
    
    private int getExpForLevel(int level) {
        if (level <= 15) {
            return 2 * level + 7;
        } else if (level <= 30) {
            return 5 * level - 38;
        } else {
            return 9 * level - 158;
        }
    }
    
    private String formatTime(long ticks) {
        // Minecraft时间：0=黎明，6000=正午，12000=黄昏，18000=午夜
        long hour = (ticks / 1000 + 6) % 24;
        long minute = (ticks % 1000) * 60 / 1000;
        return String.format("%02d:%02d", hour, minute);
    }
}