# Neko VoidWarehouse Search — Fabric 1.20.1

由 **OvOSSK** 维护的 [neko-voidwarehouse-search](https://github.com/YoshinoShizuka/neko-voidwarehouse-search) 社区移植版。原作者 Akirono、原版权声明及 MIT 许可证保持不变。本分支只构建 1.20.1。

## 安装与使用

从 Releases 下载不含 sources 的 JAR，安装于 Fabric 1.20.1 客户端。需要 Fabric Loader 0.16.14+、1.20.1 Fabric API；服务端配套安装 [Neko 主模组移植版](https://github.com/OvOSSK/Neko-Carpet-Addition)。

搜索支持中文、拼音及物品 ID。无匹配输入显示空结果，清空搜索恢复全部物品。请同时更新主模组和搜索模组，旧服务端不支持修正后的空结果语义。

## 构建与验证

Java 17，运行 `./gradlew build`（Windows：`gradlew.bat build`），Gradle 8.5。

11 项搜索索引测试通过；实机验证了仓库界面、匹配搜索、无匹配结果和清空恢复。修复了 1.20.1 界面绘制、输入焦点、过滤协议和快速重开握手重试。多人服务器尚未充分验证，见 [验证记录](VALIDATION-2026-11-13.txt)。

## 许可

原项目与本次修改按 [MIT](LICENSE) 分发。OvOSSK 仅署名移植维护贡献，不替代原作者。本版本不是上游官方发布。见 [移植说明](PORT-NOTICE.txt)。
