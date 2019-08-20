# NetConsole
树莓派网络控制台

目前已实现: 配置文件定时, 命令行手动开关设备; 国际化语言文件.

许可:
因为某些原因，暂时不能发布许可.

用到的库:
pi4j: https://pi4j.com/1.2/index.html
Wiring Pi(树莓派系统自带): http://wiringpi.com/
SnakeYAML: https://bitbucket.org/asomov/snakeyaml
JLine: https://github.com/jline/jline3

用法/命令:
开关某个引脚: switch <引脚名> <on|off>
定时开关某个引脚: timing <引脚名> <on|off> <yyyy-MM-dd_hh:mm:ss> [loop]
查看所有pin口状态: info [pincache|pin]
重载软件: reload
关闭软件: stop
