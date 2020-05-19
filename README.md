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
                                 
-h, --help                       显示这个帮助
```

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