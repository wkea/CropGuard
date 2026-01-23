# CropGuard - 农作物防踩踏插件

一个保护农作物不被玩家踩踏破坏的 Minecraft 服务器插件。

## 功能特性

- ✅ 完全保护农作物不被踩踏破坏
- ✅ 支持配置保护特定作物类型
- ✅ 可设置生效世界（默认所有世界）
- ✅ 提供绕过权限控制
- ✅ 可配置提示消息
- ✅ 支持命令重载配置

## 安装方法

1. 将编译后的 `.jar` 文件放入服务器的 `plugins` 文件夹
2. 重启服务器或使用 `/reload` 命令
3. 插件会自动生成配置文件

## 配置说明

配置文件位于 `plugins/CropGuard/config.yml`

### 主要配置项

```yaml
# 受保护的农作物列表
protected-crops:
  - "WHEAT"          # 小麦
  - "CARROTS"        # 胡萝卜
  - "POTATOES"       # 马铃薯
  - "BEETROOTS"      # 甜菜根

# 生效世界列表（留空表示所有世界）
enabled-worlds: []

# 是否启用提示消息
enable-messages: true

# 绕过权限节点
bypass-permission: "cropguard.bypass"
```

### 支持的作物类型

- `WHEAT` - 小麦
- `CARROTS` - 胡萝卜  
- `POTATOES` - 马铃薯
- `BEETROOTS` - 甜菜根
- `MELON_STEM` - 西瓜茎
- `PUMPKIN_STEM` - 南瓜茎
- `COCOA` - 可可豆
- `NETHER_WART` - 下界疣

## 命令使用

### 玩家命令
- `/cropguard help` - 显示帮助信息
- `/cropguard info` - 查看插件信息

### 管理员命令
- `/cropguard reload` - 重载插件配置（需要权限 `cropguard.reload`）

## 权限节点

- `cropguard.reload` - 允许重载插件配置
- `cropguard.bypass` - 允许绕过农作物保护（可以踩踏农作物）

## 工作原理

1. 插件监听玩家交互事件（`PlayerInteractEvent`）
2. 当玩家踩踏（物理交互）农作物时，检查是否受保护
3. 如果受保护且玩家没有绕过权限，则取消踩踏事件
4. 同时监听玩家移动事件，提供预警提示

## 注意事项

1. 插件只保护农作物不被踩踏，其他破坏方式（挖掘、爆炸等）不受影响
2. 动物踩踏不受保护（需要其他插件配合）
3. 建议配合领地插件使用，实现更完善的保护

## 版本兼容

- 支持 Minecraft 1.20.x 版本
- 基于 Bukkit/Spigot API 开发

## 问题反馈

如果遇到任何问题或有功能建议，请提交 Issue 或联系开发者。

## 更新日志

### v1.0.0
- 初始版本发布
- 实现基本农作物防踩踏功能
- 支持配置和权限管理