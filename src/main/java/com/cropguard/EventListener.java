package com.cropguard;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.block.Block;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

public class EventListener implements Listener {
    
    private final ConfigManager configManager;
    
    public EventListener(ConfigManager configManager) {
        this.configManager = configManager;
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        // 只处理踩踏（物理交互）
        if (event.getAction() != Action.PHYSICAL) {
            return;
        }
        
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        
        if (block == null) {
            return;
        }
        
        // 检查玩家是否有绕过权限
        if (player.hasPermission(configManager.getBypassPermission())) {
            return;
        }
        
        // 检查世界是否启用保护
        if (!configManager.isWorldEnabled(player.getWorld().getName())) {
            return;
        }
        
        // 检查是否为受保护的农作物
        Material blockType = block.getType();
        if (configManager.isProtectedCrop(blockType)) {
            // 取消事件，防止踩踏破坏
            event.setCancelled(true);
            
            // 发送提示消息
            if (configManager.isMessagesEnabled()) {
                player.sendMessage("§a农作物受到保护，无法踩踏！");
            }
        }
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        // 检查玩家是否移动到了新的方块
        if (event.getFrom().getBlock().equals(event.getTo().getBlock())) {
            return;
        }
        
        Player player = event.getPlayer();
        
        // 检查玩家是否有绕过权限
        if (player.hasPermission(configManager.getBypassPermission())) {
            return;
        }
        
        // 检查世界是否启用保护
        if (!configManager.isWorldEnabled(player.getWorld().getName())) {
            return;
        }
        
        // 获取玩家脚下的方块
        Block block = event.getTo().getBlock().getRelative(0, -1, 0);
        Material blockType = block.getType();
        
        // 检查是否为受保护的农作物
        if (configManager.isProtectedCrop(blockType)) {
            // 这里不能直接取消移动事件，但可以尝试将玩家弹回
            // 或者通过其他方式防止踩踏
            // 实际上，PlayerInteractEvent 已经处理了踩踏
            
            // 发送提示消息
            if (configManager.isMessagesEnabled()) {
                player.sendMessage("§c小心脚下！这里有受保护的农作物。");
            }
        }
    }
}