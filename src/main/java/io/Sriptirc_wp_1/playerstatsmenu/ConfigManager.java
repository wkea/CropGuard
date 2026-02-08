package io.Sriptirc_wp_1.playerstatsmenu;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {
    private final JavaPlugin plugin;
    private FileConfiguration config;
    private File configFile;
    
    // 默认配置
    private final Map<String, Object> defaults = new HashMap<>();
    
    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
        setupDefaults();
        loadConfig();
    }
    
    private void setupDefaults() {
        // GUI相关配置
        defaults.put("gui.title", "&6玩家状态信息");
        defaults.put("gui.rows", 4);
        
        // 物品配置
        defaults.put("items.health.material", "RED_STAINED_GLASS_PANE");
        defaults.put("items.health.displayname", "&c❤ 血量");
        
        defaults.put("items.armor.material", "IRON_CHESTPLATE");
        defaults.put("items.armor.displayname", "&b🛡 护甲");
        
        defaults.put("items.speed.material", "FEATHER");
        defaults.put("items.speed.displayname", "&a⚡ 移动速度");
        
        defaults.put("items.hunger.material", "COOKED_BEEF");
        defaults.put("items.hunger.displayname", "&e🍖 饥饿值");
        
        defaults.put("items.exp.material", "EXPERIENCE_BOTTLE");
        defaults.put("items.exp.displayname", "&d✨ 经验等级");
        
        defaults.put("items.gamemode.material", "GRASS_BLOCK");
        defaults.put("items.gamemode.displayname", "&2🌍 游戏模式");
        
        defaults.put("items.playtime.material", "CLOCK");
        defaults.put("items.playtime.displayname", "&6⏰ 在线时间");
        
        defaults.put("items.world.material", "COMPASS");
        defaults.put("items.world.displayname", "&9🧭 当前世界");
        
        defaults.put("items.refresh.material", "EMERALD");
        defaults.put("items.refresh.displayname", "&a🔄 刷新信息");
        
        defaults.put("items.close.material", "BARRIER");
        defaults.put("items.close.displayname", "&c✖ 关闭菜单");
        
        defaults.put("items.help.material", "BOOK");
        defaults.put("items.help.displayname", "&e📖 帮助信息");
        
        // 功能配置
        defaults.put("features.command.enabled", true);
        defaults.put("features.item.enabled", true);
        defaults.put("features.item.material", "COMPASS");
        defaults.put("features.item.displayname", "&6玩家状态查看器");
        defaults.put("features.item.lore", "&7右键打开状态菜单");
        
        // 消息配置
        defaults.put("messages.no-permission", "&c你没有权限使用此功能！");
        defaults.put("messages.only-player", "&c只有玩家才能使用此命令！");
        defaults.put("messages.menu-opened", "&a已打开状态信息菜单");
        defaults.put("messages.info-refreshed", "&a信息已刷新");
        defaults.put("messages.item-given", "&a你获得了状态查看器！");
        defaults.put("messages.item-already-have", "&c你已经有状态查看器了！");
    }
    
    private void loadConfig() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        
        configFile = new File(plugin.getDataFolder(), "config.yml");
        
        if (!configFile.exists()) {
            plugin.saveResource("config.yml", false);
        }
        
        config = YamlConfiguration.loadConfiguration(configFile);
        
        // 设置默认值
        for (Map.Entry<String, Object> entry : defaults.entrySet()) {
            if (!config.contains(entry.getKey())) {
                config.set(entry.getKey(), entry.getValue());
            }
        }
        
        saveConfig();
    }
    
    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("无法保存配置文件: " + e.getMessage());
        }
    }
    
    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }
    
    public FileConfiguration getConfig() {
        return config;
    }
    
    // 获取配置的便捷方法
    public String getGuiTitle() {
        return config.getString("gui.title", "&6玩家状态信息");
    }
    
    public int getGuiRows() {
        return config.getInt("gui.rows", 4);
    }
    
    public String getItemMaterial(String itemType) {
        return config.getString("items." + itemType + ".material", "STONE");
    }
    
    public String getItemDisplayName(String itemType) {
        return config.getString("items." + itemType + ".displayname", "&f物品");
    }
    
    public boolean isCommandEnabled() {
        return config.getBoolean("features.command.enabled", true);
    }
    
    public boolean isItemEnabled() {
        return config.getBoolean("features.item.enabled", true);
    }
    
    public String getItemMaterial() {
        return config.getString("features.item.material", "COMPASS");
    }
    
    public String getItemDisplayName() {
        return config.getString("features.item.displayname", "&6玩家状态查看器");
    }
    
    public String getItemLore() {
        return config.getString("features.item.lore", "&7右键打开状态菜单");
    }
    
    public String getMessage(String key) {
        return config.getString("messages." + key, "&c消息未配置");
    }
}