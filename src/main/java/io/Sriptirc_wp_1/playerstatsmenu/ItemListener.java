package io.Sriptirc_wp_1.playerstatsmenu;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemListener implements Listener {
    private final PlayerStatsMenu plugin;
    private final ConfigManager configManager;
    private final StatsGUI statsGUI;
    
    public ItemListener(PlayerStatsMenu plugin, ConfigManager configManager, StatsGUI statsGUI) {
        this.plugin = plugin;
        this.configManager = configManager;
        this.statsGUI = statsGUI;
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!configManager.isItemEnabled()) {
            return;
        }
        
        Player player = event.getPlayer();
        
        // 检查是否有使用权限
        if (!player.hasPermission("playerstatsmenu.use")) {
            return;
        }
        
        // 检查是否是右键动作
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        
        // 检查手中的物品
        ItemStack item = event.getItem();
        if (item == null || item.getType() == Material.AIR) {
            return;
        }
        
        // 检查物品是否是状态查看器
        if (!isStatsItem(item)) {
            return;
        }
        
        // 取消事件（防止放置方块等）
        event.setCancelled(true);
        
        // 打开菜单
        player.openInventory(statsGUI.createStatsMenu(player));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', 
            configManager.getMessage("menu-opened")));
    }
    
    private boolean isStatsItem(ItemStack item) {
        if (!item.hasItemMeta()) {
            return false;
        }
        
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasDisplayName()) {
            return false;
        }
        
        String expectedName = ChatColor.translateAlternateColorCodes('&', 
            configManager.getItemDisplayName());
        return meta.getDisplayName().equals(expectedName);
    }
}