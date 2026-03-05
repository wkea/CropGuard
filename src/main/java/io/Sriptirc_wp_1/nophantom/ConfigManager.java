package io.Sriptirc_wp_1.nophantom;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConfigManager {
    
    private final JavaPlugin plugin;
    private FileConfiguration config;
    private final Map<String, Boolean> worldSettings = new HashMap<>();
    private final Set<String> bypassPlayers = new HashSet<>();
    private boolean globalEnabled = true;
    private boolean sendWarning = true;
    private String warningMessage;
    
    public ConfigManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    
    public void loadConfig() {
        plugin.saveDefaultConfig();
        config = plugin.getConfig();
        
        // 设置配置版本
        config.set("ScriptIrc-config-version", 1);
        
        // 加载全局设置
        globalEnabled = config.getBoolean("global-enabled", true);
        sendWarning = config.getBoolean("send-warning", true);
        warningMessage = config.getString("warning-message", "&c幻翼生成已被阻止！");
        
        // 加载世界设置
        worldSettings.clear();
        if (config.contains("worlds")) {
            for (String worldName : config.getConfigurationSection("worlds").getKeys(false)) {
                boolean enabled = config.getBoolean("worlds." + worldName, true);
                worldSettings.put(worldName, enabled);
            }
        }
        
        // 加载绕过玩家列表
        bypassPlayers.clear();
        List<String> bypassList = config.getStringList("bypass-players");
        bypassPlayers.addAll(bypassList);
        
        // 保存配置
        saveConfig();
    }
    
    public void saveConfig() {
        try {
            config.save(new File(plugin.getDataFolder(), "config.yml"));
        } catch (IOException e) {
            plugin.getLogger().severe("保存配置文件时出错: " + e.getMessage());
        }
    }
    
    public void reloadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
        loadConfig();
    }
    
    // 检查幻翼生成是否被允许
    public boolean isPhantomAllowed(String worldName, String playerName) {
        // 检查玩家是否有绕过权限
        if (bypassPlayers.contains(playerName)) {
            return true;
        }
        
        // 检查全局设置
        if (!globalEnabled) {
            return false;
        }
        
        // 检查世界特定设置
        if (worldSettings.containsKey(worldName)) {
            return worldSettings.get(worldName);
        }
        
        // 默认允许
        return true;
    }
    
    // 添加绕过玩家
    public void addBypassPlayer(String playerName) {
        bypassPlayers.add(playerName);
        List<String> bypassList = config.getStringList("bypass-players");
        if (!bypassList.contains(playerName)) {
            bypassList.add(playerName);
            config.set("bypass-players", bypassList);
            saveConfig();
        }
    }
    
    // 移除绕过玩家
    public void removeBypassPlayer(String playerName) {
        bypassPlayers.remove(playerName);
        List<String> bypassList = config.getStringList("bypass-players");
        bypassList.remove(playerName);
        config.set("bypass-players", bypassList);
        saveConfig();
    }
    
    // 设置世界规则
    public void setWorldRule(String worldName, boolean enabled) {
        worldSettings.put(worldName, enabled);
        config.set("worlds." + worldName, enabled);
        saveConfig();
    }
    
    // 获取世界规则
    public Boolean getWorldRule(String worldName) {
        return worldSettings.get(worldName);
    }
    
    // 移除世界规则
    public void removeWorldRule(String worldName) {
        worldSettings.remove(worldName);
        config.set("worlds." + worldName, null);
        saveConfig();
    }
    
    // Getters
    public boolean isGlobalEnabled() {
        return globalEnabled;
    }
    
    public void setGlobalEnabled(boolean enabled) {
        this.globalEnabled = enabled;
        config.set("global-enabled", enabled);
        saveConfig();
    }
    
    public boolean shouldSendWarning() {
        return sendWarning;
    }
    
    public void setSendWarning(boolean sendWarning) {
        this.sendWarning = sendWarning;
        config.set("send-warning", sendWarning);
        saveConfig();
    }
    
    public String getWarningMessage() {
        return warningMessage;
    }
    
    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
        config.set("warning-message", warningMessage);
        saveConfig();
    }
    
    public Set<String> getBypassPlayers() {
        return new HashSet<>(bypassPlayers);
    }
    
    public Map<String, Boolean> getWorldSettings() {
        return new HashMap<>(worldSettings);
    }
}