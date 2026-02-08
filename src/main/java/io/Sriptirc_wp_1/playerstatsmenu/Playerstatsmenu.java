package io.Sriptirc_wp_1.playerstatsmenu;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Playerstatsmenu extends JavaPlugin {
    
    private static Playerstatsmenu instance;
    private ConfigManager configManager;
    private StatsGUI statsGUI;
    private CommandHandler commandHandler;
    private ItemListener itemListener;
    private GUIListener guiListener;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // 初始化配置管理器
        configManager = new ConfigManager(this);
        
        // 初始化GUI管理器
        statsGUI = new StatsGUI(this, configManager);
        
        // 初始化命令处理器
        commandHandler = new CommandHandler(this, configManager, statsGUI);
        
        // 注册命令
        getCommand("stats").setExecutor(commandHandler);
        getCommand("stats").setTabCompleter(commandHandler);
        
        // 注册事件监听器
        itemListener = new ItemListener(this, configManager, statsGUI);
        guiListener = new GUIListener(this, configManager, statsGUI);
        
        Bukkit.getPluginManager().registerEvents(itemListener, this);
        Bukkit.getPluginManager().registerEvents(guiListener, this);
        
        // 保存默认配置
        saveDefaultConfig();
        
        getLogger().info("PlayerStatsMenu 插件已启用！");
        getLogger().info("版本: " + getDescription().getVersion());
        getLogger().info("作者: " + getDescription().getAuthors());
    }
    
    @Override
    public void onDisable() {
        getLogger().info("PlayerStatsMenu 插件已禁用！");
    }
    
    public static Playerstatsmenu getInstance() {
        return instance;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public StatsGUI getStatsGUI() {
        return statsGUI;
    }
}
