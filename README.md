# Neko Client

本分支由 **OvOSSK** 移植到 **Minecraft 1.20.1 Fabric**，同步上游 0.5.9。原作者 Akirono 的署名和 MIT 许可保留。
[![许可证](https://img.shields.io/badge/license-MIT-6B7280?style=flat-square)](LICENSE) [![Minecraft](https://img.shields.io/badge/Minecraft-1.20.1-2ea043?style=flat-square&logo=minecraft&logoColor=white)](https://github.com/OvOSSK/neko-voidwarehouse-search) [![侧别](https://img.shields.io/badge/side-%E7%BA%AF%E5%AE%A2%E6%88%B7%E7%AB%AF-2563EB?style=flat-square)](https://github.com/OvOSSK/neko-voidwarehouse-search)

面向 Fabric 的客户端增强，把「虚空仓库」那面 6 行原版大箱子变成能搜索、能整格取货、能记配方、能投影填充的工作台。纯客户端实现，服务端 GUI 一个字节都不改，纯原版客户端零感知。

## 功能

| 功能 | 说明 |
| --- | --- |
| 搜索框 | 中文名 / 拼音全拼 / 首字母 / 注册名筛选；匹配在本地做，服务端只做集合求交。 |
| 取满手势 | 按住组合键点一格，取出到背包放不下为止；常规给散货、潜影盒模式给满盒，容量由服务端现算。 |
| 配方本 | 图标 + 连续滚动 + 搜索（含模糊拼音），按匹配度置顶；逐条删除（点两次确认，全服共用）。 |
| 投影容器填充 | 装了 Litematica 时右键投影里的容器，按投影内容逐格填充；缺料自动向仓库取货，余料自动退回。 |
| 容器填充物合计 | 一张表：整张原理图所有容器要什么 × 背包 + 仓库还缺多少。入口在材料列表工具栏。 |
| 按键打开仓库 | 自己绑一个热键，游戏内按一下服务端就开界面；已在界面时不重置分页。 |
| 快速丢弃 | 仓库界面里 `Q` 丢 1 个、`Ctrl+Q` 丢一组（原版语义）、`Shift+Q` 丢整格（≤4096 件）。 |
| 聊天静音 | 一键隐藏虚空仓库发来的全部聊天提示，只影响本机显示。 |

## 依赖

<table>
  <thead>
    <tr><th>名称</th><th>类型</th><th>链接</th><th>说明</th></tr>
  </thead>
  <tbody>
    <tr><td nowrap>Fabric API</td><td nowrap>必需</td><td nowrap><a href="https://modrinth.com/mod/fabric-api">Modrinth</a></td><td>Fabric 模组运行所需的 API。</td></tr>
    <tr><td nowrap>MaLiLib</td><td nowrap>必需</td><td nowrap><a href="https://modrinth.com/mod/malilib">Modrinth</a>&nbsp;|&nbsp;<a href="https://github.com/maruohon/malilib">GitHub</a></td><td>配置界面与热键体系；装过 MiniHUD / Litematica 的玩家已有。</td></tr>
    <tr><td nowrap>Litematica</td><td nowrap>可选</td><td nowrap><a href="https://modrinth.com/mod/litematica">Modrinth</a>&nbsp;|&nbsp;<a href="https://github.com/maruohon/litematica">GitHub</a></td><td>启用投影容器填充与容器填充物合计。</td></tr>
    <tr><td nowrap>carpet-Neko-Addition</td><td nowrap>可选</td><td nowrap><a href="https://github.com/OvOSSK/Neko-Carpet-Addition">GitHub</a></td><td>服务端规则与通道来源；没装时搜索框自动隐藏，其余功能自行关闭。</td></tr>
  </tbody>
</table>

## 版本支持

仅支持 **Minecraft 1.20.1 Fabric**。需要 Java 17、Fabric Loader 0.16.14 或更新版本，以及 1.20.1 对应的 Fabric API。

MaLiLib 使用 0.16.3；投影相关功能另需 Litematica 0.15.4。仓库功能请搭配本 fork 的 Neko 主模组 1.0.18-mc1.20.1。

## 热键

| 名称 | 默认键 | 说明 |
| --- | --- | --- |
| 搜索框 / `searchBox` | 未绑定 | 切搜索框，界面开着时立刻生效 |
| 取满手势 / `fillGesture` | 未绑定 | 切取满开关 |
| 静音聊天 / `silenceChat` | 未绑定 | 切聊天静音 |
| 取满组合键 / `fillCombo` | `LEFT_CONTROL,LEFT_ALT,BUTTON_2` | 手势本身：按住它再点格子；清空 = 关掉手势 |
| 打开配置界面 / `openGui` | 未绑定 | 打开 malilib 配置界面 |
| 打开配方本 / `openRecipes` | 未绑定 | 随地可用，不必开着仓库 |
| 容器填充物合计 / `openContainerMaterials` | 未绑定 | 备用入口；主入口是材料列表工具栏的「容器材料」 |
| 打开虚空仓库 / `openWarehouse` | 未绑定 | 非界面状态按一下，服务端开界面 |

- 快速丢弃的 `Shift+Q` 是**固定键**，不在配置里；`Q` / `Ctrl+Q` 走原版，不接管。
- 热键默认留空是为了不与其他模组撞键；配置落 `config/neko-client.json`，手改后重进一次世界生效。
- 绑定入口：Mod Menu 的「Neko Client」配置按钮；也可在 `config/neko-client.json` 中设置 `keyOpenGui`。

## 安装

1. 装 Fabric Loader + Fabric API + **MaLiLib**（同版本）。
2. 将 `neko-voidwarehouse-search-0.5.9-mc1.20.1.jar` 放进 `mods/`，删除旧的 0.1.1 搜索附加模组 JAR，不能同时安装。模组名称已随上游改为 Neko Client，mod id 为 `neko_client`。
3. 需要搜索 / 取满 / 配方本 / 快速丢弃时，服务端装 `carpet-Neko-Addition` 并打开相关规则。

## 构建

使用 **JDK 17、Gradle 8.5**（已提供 wrapper），在仓库根目录执行：

```bat
.\gradlew.bat build
```

Linux/macOS 使用 `./gradlew build`。产物位于 `build/libs/`，安装不带 `sources` 的 JAR。修改说明见 [PORT-NOTICE.txt](PORT-NOTICE.txt)，测试范围见 [验证记录](VALIDATION-2026-11-22.txt)。

## 兼容性

仓库功能请与主模组 1.0.18-mc1.20.1 一起更新；同时使用 Hana 时更新到 hana-neko.2。没装服务端主模组时，仓库相关功能自动隐藏。

客户端必须安装 MaLiLib。Litematica 为可选依赖；1.20.1 没有合成器方块，相应操作不适用。旧版 Litematica 没有上游新版的 Servux 容器缓存接口，多人容器数据未知时不按空库存处理。

## 许可证

[MIT](LICENSE)。拼音库 [jpinyin](https://github.com/stuxuhai/jpinyin)（Apache-2.0）以 Jar-in-Jar 打包。
