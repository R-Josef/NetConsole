# NetConsole
树莓派网络控制台

## 目前已实现:
配置文件定时\命令行手动开关设备.<br>
国际化语言文件.

## 许可:
因为某些原因，暂时不能发布许可.

## 用到的库:
[pi4j](https://pi4j.com/1.2/index.html)<br>
[Wiring Pi](http://wiringpi.com/)(树莓派系统自带)<br>
[SnakeYAML](https://bitbucket.org/asomov/snakeyaml)<br>
[JLine](https://github.com/jline/jline3)<br>

## 用法/命令:
开关某个引脚: `switch <引脚名> <on|off>`<br>
定时开关某个引脚: `timing <引脚名> <on|off> <yyyy-MM-dd_hh:mm:ss> [loop]`<br>
查看所有pin口状态: `info [pincache|pin]`<br> 
重载软件: `reload`<br> 
关闭软件: `stop`<br> 
