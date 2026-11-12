# Neko VoidWarehouse Search（虚空仓库搜索框）

> **1.20.1 Fabric 移植版** · 移植维护：OvOSSK · 原作者：Akirono
> [原项目](https://github.com/YoshinoShizuka/neko-voidwarehouse-search) · [移植说明](PORT-NOTICE.txt)

纯客户端 Fabric mod：在「虚空仓库」的界面（一个 6 行原版大箱子）上叠加一个搜索框，支持**中文 / 拼音全拼 / 拼音首字母 / 注册名**搜索筛选物品。服务端 GUI 一个字节不变，纯原版玩家零感知。

![Side](https://img.shields.io/badge/side-%E7%BA%AF%E5%AE%A2%E6%88%B7%E7%AB%AF-informational) ![Minecraft](https://img.shields.io/badge/Minecraft-1.20.1-2ea043)

## 工作方式

```
打开仓库界面 ──search_hello──▶ 服务端确认是虚空仓库
             ◀─search_catalog─ 仓库物品 id 目录（≈30 KB）
输入 ─▶ 本地匹配（中文名 / 全拼 / 首字母 / 注册名，jpinyin 建索引）
     ──search_apply──▶ 服务端按 Item 身份过滤出页（无匹配 = 空结果；显式清空标记 = 恢复完整列表）
```

- 匹配在客户端做（只有客户端有中文语言文件），服务端只做集合求交；
- 过滤是**会话级**状态：关掉界面即消失，不写存档；页码回第一页；「可兑」虚拟条目同样参与搜索；
- 服务端限流：hello 100ms / apply 50ms，目录上限 4096 种，未知 id 静默忽略。

## 安装

mod id `neko_voidwarehouse_search`，构件名 `neko-voidwarehouse-search`。本 fork 仅发布 Minecraft 1.20.1：

| 客户端版本 | jar | 构建管线 |
| --- | --- | --- |
| 1.20.1 | `neko-voidwarehouse-search-0.1.1-mc1.20.1.jar` | Fabric Loom 1.5.8 + officialMojangMappings（Java 17） |

从 [Releases](https://github.com/OvOSSK/neko-voidwarehouse-search/releases/latest) 下载。

| 位置 | 需要什么 |
| --- | --- |
| 客户端 | 本 mod + Fabric Loader 0.16.14+ + 1.20.1 Fabric API |
| 服务端 | 配套 [Neko 主模组 1.20.1 移植版](https://github.com/OvOSSK/Neko-Carpet-Addition)；本搜索模组仅装客户端 |
| 纯原版客户端 | 什么都不用装，看不到任何变化 |

## 使用

- 打开虚空仓库界面，点击界面上方的搜索框；
- 输入 `铁锭` / `tieding` / `td` / `iron_ingot` 均可命中；清空输入框 = 恢复完整列表；
- 🔴 聚焦搜索框时**所有按键都会被吞掉**（ESC = 退出搜索框，再按一次关界面）——否则数字键 `1-9` 会触发原版「把快捷栏整组存进仓库」的 SWAP 语义。

## 构建

- JDK 17、Gradle 8.5（wrapper 已固定），在仓库根目录执行：

```bat
.\gradlew.bat build
.\gradlew.bat test
```

- Linux/macOS 使用 `./gradlew build`；产物在 `build/libs/`。
- **与服务端 JAR 成对更新**：三个搜索 payload 的字段顺序和语义须与主模组一致。1.20.1 apply 包保留列表前缀并追加显式清空标记；旧服务端不支持修正后的无匹配空结果。
- 11 项搜索索引测试通过；匹配、无匹配、清空恢复及界面输入已实测，见 [验证记录](VALIDATION-2026-11-13.txt)。

## 版本差异（仅两处）

本分支仅适配 1.20.1：

| 适配点 | 1.20.1 实现 |
| --- | --- |
| 网络注册 | Fabric Identifier / PacketByteBuf API 兼容层 |
| 渲染与输入 | 1.20.1 GuiGraphics 接口与容器界面 Mixin；修正重复绘制和搜索框焦点 |
| 重开界面 | 握手节流后延迟重试，避免快速重开缺少搜索框 |

## 版本兼容

| 服务端 \ 客户端 | 装了本 mod | 没装 / 老版本 |
| --- | --- | --- |
| 配套 carpet-Neko-Addition 1.0.17-mc1.20.1 | 搜索框可用 | 无任何影响 |
| 其它版本 / 没装 carpet | 搜索框自动隐藏（`canSend` 判定） | 无任何影响 |

## 许可证

MIT。拼音库 [jpinyin](https://github.com/stuxuhai/jpinyin)（Apache-2.0）以 Jar-in-Jar 打包。
