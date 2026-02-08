package io.Sriptirc_wp_1.playerstatsmenu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class StatsGUI {
    private final PlayerStatsMenu plugin;
    private final ConfigManager configManager;
    private final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    
    public StatsGUI(PlayerStatsMenu plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }
    
    public Inventory createStatsMenu(Player player) {
        String title = ChatColor.translateAlternateColorCodes('&', configManager.getGuiTitle());
        int rows = configManager.getGuiRows();
        Inventory inv = Bukkit.createInventory(null, rows * 9, title);
        
        // 设置边框（可选）
        setBorder(inv, rows);
        
        // 添加信息物品
        addInfoItems(inv, player);
        
        // 添加功能按钮
        addFunctionButtons(inv);
        
        return inv;
    }
    
    private void setBorder(Inventory inv, int rows) {
        ItemStack border = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = border.getItemMeta();
        meta.setDisplayName(" ");
        border.setItemMeta(meta);
        
        // 设置顶部和底部边框
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, border); // 第一行
            inv.setItem((rows - 1) * 9 + i, border); // 最后一行
        }
        
        // 设置左右边框
        for (int i = 1; i < rows - 1; i++) {
            inv.setItem(i * 9, border); // 最左边
            inv.setItem(i * 9 + 8, border); // 最右边
        }
    }
    
    private void addInfoItems(Inventory inv, Player player) {
        // 血量信息 (第2行第3列，对应slot 11)
        addItem(inv, 11, "health", getHealthInfo(player));
        
        // 护甲信息 (第2行第5列，对应slot 13)
        addItem(inv, 13, "armor", getArmorInfo(player));
        
        // 移动速度 (第2行第7列，对应slot 15)
        addItem(inv, 15, "speed", getSpeedInfo(player));
        
        // 饥饿值 (第3行第3列，对应slot 20)
        addItem(inv, 20, "hunger", getHungerInfo(player));
        
        // 经验等级 (第3行第5列，对应slot 22)
        addItem(inv, 22, "exp", getExpInfo(player));
        
        // 游戏模式 (第3行第7列，对应slot 24)
        addItem(inv, 24, "gamemode", getGameModeInfo(player));
        
        // 在线时间 (第4行第3列，对应slot 29)
        addItem(inv, 29, "playtime", getPlayTimeInfo(player));
        
        // 当前世界 (第4行第5列，对应slot 31)
        addItem(inv, 31, "world", getWorldInfo(player));
    }
    
    private void addFunctionButtons(Inventory inv) {
        // 刷新按钮 (第4行第7列，对应slot 33)
        addItem(inv, 33, "refresh", getRefreshLore());
        
        // 关闭按钮 (第5行第1列，对应slot 36)
        addItem(inv, 36, "close", getCloseLore());
        
        // 帮助按钮 (第5行第9列，对应slot 44)
        addItem(inv, 44, "help", getHelpLore());
    }
    
    private void addItem(Inventory inv, int slot, String itemType, List<String> lore) {
        Material material = Material.getMaterial(configManager.getItemMaterial(itemType));
        if (material == null) {
            material = Material.STONE;
        }
        
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        String displayName = ChatColor.translateAlternateColorCodes('&', 
            configManager.getItemDisplayName(itemType));
        meta.setDisplayName(displayName);
        
        meta.setLore(lore);
        item.setItemMeta(meta);
        
        inv.setItem(slot, item);
    }
    
    private List<String> getHealthInfo(Player player) {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "当前血量: " + ChatColor.RED + 
                 decimalFormat.format(player.getHealth()) + " / " + 
                 decimalFormat.format(player.getMaxHealth()));
        lore.add(ChatColor.GRAY + "生命值: " + ChatColor.RED + 
                 (int)player.getHealth() + "❤");
        lore.add("");
        lore.add(ChatColor.DARK_GRAY + "点击查看详细信息");
        return lore;
    }
    
    private List<String> getArmorInfo(Player player) {
        List<String> lore = new ArrayList<>();
        
        // 计算护甲值
        double armorPoints = 0;
        ItemStack[] armor = player.getInventory().getArmorContents();
        for (ItemStack item : armor) {
            if (item != null && item.getType() != Material.AIR) {
                // 简单估算护甲值
                switch (item.getType()) {
                    case LEATHER_HELMET:
                    case LEATHER_BOOTS:
                        armorPoints += 1;
                        break;
                    case LEATHER_CHESTPLATE:
                    case LEATHER_LEGGINGS:
                    case CHAINMAIL_HELMET:
                    case CHAINMAIL_BOOTS:
                    case GOLDEN_HELMET:
                    case GOLDEN_BOOTS:
                        armorPoints += 2;
                        break;
                    case CHAINMAIL_CHESTPLATE:
                    case CHAINMAIL_LEGGINGS:
                    case IRON_HELMET:
                    case IRON_BOOTS:
                    case GOLDEN_CHESTPLATE:
                    case GOLDEN_LEGGINGS:
                        armorPoints += 3;
                        break;
                    case IRON_CHESTPLATE:
                    case IRON_LEGGINGS:
                    case DIAMOND_HELMET:
                    case DIAMOND_BOOTS:
                        armorPoints += 4;
                        break;
                    case DIAMOND_CHESTPLATE:
                    case DIAMOND_LEGGINGS:
                        armorPoints += 5;
                        break;
                    case NETHERITE_HELMET:
                    case NETHERITE_BOOTS:
                        armorPoints += 6;
                        break;
                    case NETHERITE_CHESTPLATE:
                    case NETHERITE_LEGGINGS:
                        armorPoints += 7;
                        break;
                }
            }
        }
        
        lore.add(ChatColor.GRAY + "护甲值: " + ChatColor.AQUA + 
                 decimalFormat.format(armorPoints) + "点");
        lore.add(ChatColor.GRAY + "护甲韧性: " + ChatColor.AQUA + 
                 "基础防护");
        lore.add("");
        lore.add(ChatColor.DARK_GRAY + "点击查看详细信息");
        return lore;
    }
    
    private List<String> getSpeedInfo(Player player) {
        List<String> lore = new ArrayList<>();
        
        // 获取移动速度
        float walkSpeed = player.getWalkSpeed();
        float flySpeed = player.getFlySpeed();
        
        // 检查速度效果
        boolean hasSpeedEffect = false;
        int speedLevel = 0;
        for (PotionEffect effect : player.getActivePotionEffects()) {
            if (effect.getType().equals(PotionEffectType.SPEED)) {
                hasSpeedEffect = true;
                speedLevel = effect.getAmplifier() + 1;
                break;
            }
        }
        
        lore.add(ChatColor.GRAY + "行走速度: " + ChatColor.GREEN + 
                 decimalFormat.format(walkSpeed * 100) + "%");
        lore.add(ChatColor.GRAY + "飞行速度: " + ChatColor.GREEN + 
                 decimalFormat.format(flySpeed * 100) + "%");
        
        if (hasSpeedEffect) {
            lore.add(ChatColor.GRAY + "速度效果: " + ChatColor.GREEN + 
                     "等级 " + speedLevel);
        } else {
            lore.add(ChatColor.GRAY + "速度效果: " + ChatColor.RED + "无");
        }
        
        lore.add("");
        lore.add(ChatColor.DARK_GRAY + "点击查看详细信息");
        return lore;
    }
    
    private List<String> getHungerInfo(Player player) {
        List<String> lore = new ArrayList<>();
        
        int foodLevel = player.getFoodLevel();
        float saturation = player.getSaturation();
        
        lore.add(ChatColor.GRAY + "饥饿值: " + ChatColor.YELLOW + 
                 foodLevel + " / 20");
        lore.add(ChatColor.GRAY + "饱和度: " + ChatColor.YELLOW + 
                 decimalFormat.format(saturation));
        lore.add(ChatColor.GRAY + "消耗速度: " + ChatColor.YELLOW + 
                 "正常");
        lore.add("");
        lore.add(ChatColor.DARK_GRAY + "点击查看详细信息");
        return lore;
    }
    
    private List<String> getExpInfo(Player player) {
        List<String> lore = new ArrayList<>();
        
        int level = player.getLevel();
        float exp = player.getExp();
        int totalExp = getTotalExperience(player);
        
        lore.add(ChatColor.GRAY + "等级: " + ChatColor.LIGHT_PURPLE + 
                 level + "级");
        lore.add(ChatColor.GRAY + "经验进度: " + ChatColor.LIGHT_PURPLE + 
                 decimalFormat.format(exp * 100) + "%");
        lore.add(ChatColor.GRAY + "总经验: " + ChatColor.LIGHT_PURPLE + 
                 totalExp + "点");
        lore.add("");
        lore.add(ChatColor.DARK_GRAY + "点击查看详细信息");
        return lore;
    }
    
    private List<String> getGameModeInfo(Player player) {
        List<String> lore = new ArrayList<>();
        
        String gameMode = player.getGameMode().toString();
        lore.add(ChatColor.GRAY + "当前模式: " + ChatColor.DARK_GREEN + 
                 gameMode);
        lore.add(ChatColor.GRAY + "是否飞行: " + 
                 (player.isFlying() ? ChatColor.GREEN + "是" : ChatColor.RED + "否"));
        lore.add(ChatColor.GRAY + "是否潜行: " + 
                 (player.isSneaking() ? ChatColor.GREEN + "是" : ChatColor.RED + "否"));
        lore.add("");
        lore.add(ChatColor.DARK_GRAY + "点击查看详细信息");
        return lore;
    }
    
    private List<String> getPlayTimeInfo(Player player) {
        List<String> lore = new ArrayList<>();
        
        // 获取游戏内时间（ticks）
        int ticks = player.getStatistic(Statistic.PLAY_ONE_MINUTE);
        int minutes = ticks / 1200; // 1200 ticks = 1分钟
        int hours = minutes / 60;
        minutes = minutes % 60;
        
        lore.add(ChatColor.GRAY + "在线时间: " + ChatColor.GOLD + 
                 hours + "小时 " + minutes + "分钟");
        lore.add(ChatColor.GRAY + "游戏刻: " + ChatColor.GOLD + 
                 ticks + " ticks");
        lore.add(ChatColor.GRAY + "首次加入: " + ChatColor.GOLD + 
                 "已记录");
        lore.add("");
        lore.add(ChatColor.DARK_GRAY + "点击查看详细信息");
        return lore;
    }
    
    private List<String> getWorldInfo(Player player) {
        List<String> lore = new ArrayList<>();
        
        String worldName = player.getWorld().getName();
        String environment = player.getWorld().getEnvironment().toString();
        int time = (int)player.getWorld().getTime();
        
        lore.add(ChatColor.GRAY + "世界名称: " + ChatColor.BLUE + 
                 worldName);
        lore.add(ChatColor.GRAY + "环境类型: " + ChatColor.BLUE + 
                 environment);
        lore.add(ChatColor.GRAY + "游戏时间: " + ChatColor.BLUE + 
                 time + " ticks");
        lore.add("");
        lore.add(ChatColor.DARK_GRAY + "点击查看详细信息");
        return lore;
    }
    
    private List<String> getRefreshLore() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "点击刷新所有信息");
        lore.add(ChatColor.DARK_GRAY + "重新计算当前状态");
        return lore;
    }
    
    private List<String> getCloseLore() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "点击关闭菜单");
        lore.add(ChatColor.DARK_GRAY + "返回游戏");
        return lore;
    }
    
    private List<String> getHelpLore() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "玩家状态信息菜单");
        lore.add(ChatColor.DARK_GRAY + "显示你的各项游戏数据");
        lore.add("");
        lore.add(ChatColor.YELLOW + "使用方法:");
        lore.add(ChatColor.WHITE + "• 点击物品查看详细信息");
        lore.add(ChatColor.WHITE + "• 点击刷新按钮更新数据");
        lore.add(ChatColor.WHITE + "• 命令: /stats");
        lore.add(ChatColor.WHITE + "• 右键物品打开菜单");
        return lore;
    }
    
    // 计算总经验值
    private int getTotalExperience(Player player) {
        int level = player.getLevel();
        float progress = player.getExp();
        int total = 0;
        
        // 计算之前等级的经验
        for (int i = 0; i < level; i++) {
            total += getExpForLevel(i);
        }
        
        // 加上当前等级的经验
        total += Math.round(getExpForLevel(level) * progress);
        
        return total;
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
    
    public void refreshMenu(Player player, Inventory inv) {
        // 清除旧的信息物品
        int[] infoSlots = {11, 13, 15, 20, 22, 24, 29, 31};
        for (int slot : infoSlots) {
            inv.setItem(slot, null);
        }
        
        // 重新添加信息物品
        addInfoItems(inv, player);
    }
}