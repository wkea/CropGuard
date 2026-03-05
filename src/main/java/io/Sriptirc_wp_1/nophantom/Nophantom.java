package io.Sriptirc_wp_1.nophantom;

import org.bukkit.plugin.java.JavaPlugin;

public final class Nophantom extends JavaPlugin {
    
    private static Nophantom instance;
    private ConfigManager configManager;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // 初始化配置管理器
        configManager = new ConfigManager(this);
        configManager.loadConfig();
        
        // 注册事件监听器
        getServer().getPluginManager().registerEvents(new PhantomListener(this, configManager), this);
        
        // 注册命令处理器
        CommandHandler commandHandler = new CommandHandler(this, configManager);
        getCommand("nophantom").setExecutor(commandHandler);
        getCommand("nophantom").setTabCompleter(commandHandler);
        
        getLogger().info("NoPhantom 插件已启用！");
        getLogger().info("幻翼生成限制: " + (configManager.isGlobalEnabled() ? "启用" : "禁用"));
    }
    
    @Override
    public void onDisable() {
        getLogger().info("NoPhantom 插件已禁用！");
    }
    
    public static Nophantom getInstance() {
        return instance;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
}
