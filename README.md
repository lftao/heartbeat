# heartbeat
端口心跳检测，可适用于集群中返回健康的地址(引入jar) 或者独立运行做服务检测报警
# 特点
集成jsw，可以安装成服务，守护进程保证jvm正常运行，意外关闭自动启动，端口的异常会恢复提醒管理者(邮件) 
# 命令
Usage: heartbeat { console : start : pause : resume : stop : restart : install : remove : status }
# 配置1
List<IPorts> iPorts = new ArrayList<>();
iPorts.add(new IPorts(host, port));
new HBConfig().initNode(iPorts);
# 配置2
[{
		"host" : "127.0.0.1",
		"port" : "8080",
		"callback" : "com.javatao.heartbeat.TestCallback"
	},
	{
		"host" : "127.0.0.1",
		"port" : "80",
	}
]