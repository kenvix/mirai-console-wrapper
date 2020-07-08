# (deprecated)
此项目已停止维护，请使用 https://github.com/LXY1226/MiraiOK

# mirai-console-wrapper
[mirai-console](https://github.com/mamoe/mirai-console) 启动器.

**[下载(download)](https://github.com/mamoe/mirai-console-wrapper/releases)**  
请下载最新的 `mirai-console-wrapper-x.x.x.jar`

#### 命令行参数
```
--native / -n                    以图形界面模式启动
                                 
--update [KEEP|STABLE|EA]        版本升级策略. "KEEP" 为停留在当前版本; "STABLE" (默认)
                                 为更新到最新稳定版; "EA" 为更新到最新预览版.
                                 
--console [Graphical|Terminal|Pure]
                                 UI 类型. "GRAPHICAL" 为 JavaFX 图形界面;
                                 "TERMINAL" 为 Unix 终端界面; "PURE" (默认) 为纯命令行.
                                 
--proxy [URL]                    HTTP 代理地址. 不提供时自动从 127.0.0.1:1080 和 127.0.0.1:1088 检测 SS 代理.

--source [URL]                   版本更新源. 需要支持 miria-core-qqandroid 和 mirai-console 后端和前端等相关 jar 包的下载. 
                                 URL 中 {module} 将会被替换为模块名, 如 mirai-core, mirai-console-pure, mirai-console;
                                 {version} 将会被替换为版本号, 如 1.0-RC2.
                                 
                                 
-h, --help                       显示这个帮助
```

这些参数都可以通过环境变量 `mirai.wrapper.*` 设置. 如 `mirai.wrapper.proxy`, `mirai.wrapper.update`

### 使用Docker

``` bash
docker build . -t mirai
# 构建镜像

docker run -it --rm \
    -v $PWD/data:/app/data
    -p 8080:8080 # 如果你需要使用mirai-api-http，请开启端口
    mirai
# 启动mirai-console-wrapper

# 如果你需要使用插件，将你所需要使用的插件放在 data/plugins/ 下, e.g
cd $PWD/data/plugins
curl -LO \
    https://github.com/ryoii/mirai-console-addition/releases/download/v0.2.3/mirai-console-addition-V0.2.3.jar
```
