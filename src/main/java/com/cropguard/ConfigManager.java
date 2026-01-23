package com.cropguard;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.Material;
import java.util.HashSet;
import java.util.Set;

public class ConfigManager {
    
    private final CropGuard plugin;
    private Set<Material> protectedCrops;
    private Set<String> enabledWorlds;
    private boolean enableMessages;
    private String bypassPermission;
    
    public ConfigManager() {
        this.plugin = CropGuard.getInstance();
        reloadConfig();
    }
    
    public void reloadConfig() {
        plugin.reloadConfig();
        FileConfiguration config = plugin.getConfig();
        
        // 加载保护作物列表
        protectedCrops = new HashSet<>();
        for (String cropName : config.getStringList("protected-crops")) {
            try {
                Material material = Material.valueOf(cropName.toUpperCase());
                protectedCrops.add(material);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("未知的作物类型: " + cropName);
            }
        }
        
        // 加载生效世界
        enabledWorlds = new HashSet<>();
        for (String world : config.getStringList("enabled-worlds")) {
            if (!world.trim().isEmpty()) {
                enabledWorlds.add(world.trim());
            }
        }
        
        // 加载其他配置
        enableMessages = config.getBoolean("enable-messages", true);
        bypassPermission = config.getString("bypass-permission", "cropguard.bypass");
        
        plugin.getLogger().info("配置已重载！保护作物数量: " + protectedCrops.size());
    }
    
    public boolean isProtectedCrop(Material material) {
        return protectedCrops.contains(material);
    }
    
    public boolean isWorldEnabled(String worldName) {
        if (enabledWorlds.isEmpty()) {
            return true; // 如果列表为空，表示所有世界都生效
        }
        return enabledWorlds.contains(worldName);
    }
    
    public boolean isMessagesEnabled() {
        return enableMessages;
    }
    
    public String getBypassPermission() {
        return bypassPermission;
    }
    
    public Set<Material> getProtectedCrops() {
        return new HashSet<>(protectedCrops);
    }
    
    public Set<String> getEnabledWorlds() {
        return new HashSet<>(enabledWorlds);
    }
}