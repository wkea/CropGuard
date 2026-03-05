# NoPhantom - 幻翼生成限制插件

一个用于禁止或控制幻翼（Phantom）生成的 Minecraft 服务器插件。

## 功能特性

- ✅ **完全禁止幻翼生成** - 阻止幻翼自然生成
- ✅ **按世界配置** - 不同世界可以有不同的规则
- ✅ **按玩家权限配置** - 特定玩家可以绕过限制
- ✅ **生成前警告** - 可配置的警告消息
- ✅ **详细日志记录** - 记录所有生成尝试
- ✅ **完整命令系统** - 方便的管理命令

## 安装

1. 将编译后的 `.jar` 文件放入服务器的 `plugins` 文件夹
2. 重启服务器或使用 `/reload` 命令
3. 插件会自动生成配置文件

## 权限节点

- `nophantom.use` - 使用插件基本命令（默认给所有玩家）
- `nophantom.admin` - 管理插件配置（默认给 OP）
- `nophantom.bypass` - 绕过幻翼生成限制

## 命令

### 基本命令
- `/nophantom help` - 显示帮助信息
- `/nophantom status` - 显示插件状态

### 管理命令（需要 nophantom.admin 权限）
- `/nophantom reload` - 重新加载配置文件
- `/nophantom bypass <add|remove|list> [玩家名]` - 管理绕过玩家
- `/nophantom world <enable|disable|list> [世界名]` - 管理世界设置
- `/nophantom global <enable|disable>` - 全局开关
- `/nophantom warning <enable|disable|message> [消息]` - 管理警告设置

### 命令别名
- `/np` - `/nophantom` 的简写

## 配置文件

配置文件位于 `plugins/NoPhantom/config.yml`

```yaml
# 全局设置
global-enabled: true  # 是否全局启用幻翼生成限制
send-warning: true    # 是否发送警告消息
warning-message: "&c幻翼生成已被阻止！"

# 世界特定设置
worlds:
  world: false        # 主世界禁止幻翼
  world_nether: true  # 下界允许幻翼
  world_the_end: true # 末地允许幻翼

# 绕过玩家列表
bypass-players: []
```

## 工作原理

插件通过监听 `CreatureSpawnEvent` 事件来检测幻翼生成。当检测到幻翼尝试生成时：

1. 检查全局设置是否启用
2. 检查当前世界是否允许生成
3. 检查玩家是否有绕过权限
4. 如果被禁止，取消生成事件并发送警告消息

## 兼容性

- **Minecraft 版本**: 1.20.x
- **服务器类型**: Bukkit, Spigot, Paper
- **依赖插件**: 无

## 常见问题

### Q: 插件会影响刷怪蛋生成的幻翼吗？
A: 不会，插件只阻止自然生成的幻翼（NATURAL 和 PATROL 生成原因）。

### Q: 如何让某个玩家不受限制？
A: 使用命令 `/nophantom bypass add <玩家名>` 或直接在配置文件中添加玩家名。

### Q: 插件会影响服务器性能吗？
A: 影响极小，只监听幻翼生成事件，不会造成性能问题。

## 更新日志

### v1.0.0
- 初始版本发布
- 支持按世界配置幻翼生成
- 支持玩家绕过权限
- 完整的命令系统
- 可配置的警告消息

## 开发者

由 ScriptIrc Engine 开发

## 许可证

MIT License