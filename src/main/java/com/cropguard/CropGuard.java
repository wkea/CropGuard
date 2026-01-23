package com.cropguard;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public class CropGuard extends JavaPlugin {
    
    private static CropGuard instance;
    private ConfigManager configManager;
    private EventListener eventListener;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // 保存默认配置
        saveDefaultConfig();
        
        // 初始化管理器
        configManager = new ConfigManager();
        eventListener = new EventListener(configManager);
        
        // 注册事件监听器
        getServer().getPluginManager().registerEvents(eventListener, this);
        
        // 注册命令
        getCommand("cropguard").setExecutor(new CommandHandler(configManager));
        getCommand("cropguard").setTabCompleter(new CommandHandler(configManager));
        
        getLogger().info("农作物防踩踏插件已启用！");
    }
    
    @Override
    public void onDisable() {
        getLogger().info("农作物防踩踏插件已禁用！");
    }
    
    public static CropGuard getInstance() {
        return instance;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
}