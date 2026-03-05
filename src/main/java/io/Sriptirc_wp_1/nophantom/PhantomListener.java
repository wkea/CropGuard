package io.Sriptirc_wp_1.nophantom;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class PhantomListener implements Listener {
    
    private final NoPhantom plugin;
    private final ConfigManager configManager;
    
    public PhantomListener(NoPhantom plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }
    
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        // 只处理幻翼生成
        if (event.getEntityType() != EntityType.PHANTOM) {
            return;
        }
        
        // 获取生成原因
        CreatureSpawnEvent.SpawnReason reason = event.getSpawnReason();
        
        // 只处理自然生成的幻翼（排除刷怪蛋、命令等）
        if (reason != CreatureSpawnEvent.SpawnReason.NATURAL && 
            reason != CreatureSpawnEvent.SpawnReason.PATROL) {
            return;
        }
        
        // 获取世界名称
        String worldName = event.getLocation().getWorld().getName();
        
        // 尝试找到最近的玩家（幻翼通常是围绕玩家生成的）
        Player nearestPlayer = findNearestPlayer(event);
        String playerName = nearestPlayer != null ? nearestPlayer.getName() : "Unknown";
        
        // 检查是否允许生成
        boolean allowed = configManager.isPhantomAllowed(worldName, playerName);
        
        if (!allowed) {
            // 取消生成
            event.setCancelled(true);
            
            // 发送警告消息
            if (configManager.shouldSendWarning() && nearestPlayer != null) {
                String message = ChatColor.translateAlternateColorCodes('&', 
                    configManager.getWarningMessage());
                nearestPlayer.sendMessage(message);
            }
            
            // 记录日志
            plugin.getLogger().info("阻止了幻翼生成 - 世界: " + worldName + 
                ", 玩家: " + playerName + ", 位置: " + event.getLocation());
        }
    }
    
    private Player findNearestPlayer(CreatureSpawnEvent event) {
        Player nearestPlayer = null;
        double nearestDistance = Double.MAX_VALUE;
        
        for (Player player : event.getLocation().getWorld().getPlayers()) {
            double distance = player.getLocation().distance(event.getLocation());
            if (distance < nearestDistance) {
                nearestDistance = distance;
                nearestPlayer = player;
            }
        }
        
        return nearestPlayer;
    }
}