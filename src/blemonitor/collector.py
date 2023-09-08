# The script runs on the collector (Raspberry Pi with Ubertooth One).
# It receives and executes commands from the Monitor.
# 充当一个收集器，接收并执行来自监控程序的命令

#!/usr/bin/python
import pickle
from pwn import *
from Advpkt import *
from Device import *

# 定义全局变量
sock = None
p = None
uberpath = '/usr/local/bin/ubertooth-btle'

# ——————————————————————————————————————————————————————————————————————————

# 定义getUbertooth函数，该函数设置并返回一个特定通道和可选MAC地址的Ubertooth进程
# 它使用pwn模块的process函数
def getUbertooth(channel, mac=None, timeout=None):
    global p
    if timeout is None:
        if mac is None:
            p = process(uberpath + ' -n -A ' + channel, shell=True)
        else:
            p = process(uberpath + ' -n -A ' + channel + ' -t' + mac, shell=True)
    else:
        p = process('timeout ' + timeout + ' ' + uberpath + ' -n -A ' + channel, shell=True)
    return p


# 定义collect函数，该函数在指定的通道上收集一段给定时间的蓝牙数据。
# 它使用getUbertooth函数设置Ubertooth进程，并从中接收数据。收集到的数据随后通过套接字连接发送给监控程序。
def collect(channel, time):
    global sock, p
    # clear target
    process(uberpath + ' -t none', shell=True)
    p = getUbertooth(channel, timeout=time)
    while True:
        try:
            data = p.recv()
            sock.send(data)
        except EOFError:
            break


# 定义getInterval函数，该函数从特定MAC地址和通道中检索蓝牙包之间的间隔。
# 它使用getUbertooth函数设置Ubertooth进程，并使用Advpkt模块的getPkts函数获取数据包。计算并返回包之间的间隔。
def getInterval(mac, channel):
    global p
    p = getUbertooth(channel, mac)
    p.recvline()
    pkts = getPkts(p)
    p.kill()
    return getIntervalFromPkts(pkts)


# 定义getRssiFromPkts函数，该函数从数据包列表中计算RSSI（接收信号强度指示）值的众数（出现频率最高的值）。
# 它返回众数的RSSI值。
def getRssiFromPkts(data):
    rssi_map = {}
    for d in data:
        if d.rssi in rssi_map.keys():
            rssi_map[d.rssi] += 1
        else:
            rssi_map[d.rssi] = 1
    mode = data[0].rssi
    times = 1
    for k in rssi_map.keys():
        if rssi_map[k] > times:
            times = rssi_map[k]
            mode = k
    return mode


# 定义getIntervalFromPkts函数，该函数根据数据包的时间戳计算包之间的间隔。
# 它接受数据包列表，获取它们的时间戳，并计算它们之间的最小间隔。返回间隔的毫秒表示。
def getIntervalFromPkts(data):
    pre = data[0].time
    # 5000ms
    interval = 5000
    for i in range(1, len(data)):
        post = data[i].time
        if (post - pre)/1000 < interval:
            interval = (post - pre)/1000
        pre = post
    return interval


# 定义startServer函数，该函数在5555端口上设置套接字服务器并等待连接。它使用pwn模块的server函数。
def startServer():
    sock = server(5555, callback=cmdloop)
    sock.wait()


# 定义cmdloop函数，该函数是处理从监控程序接收到的命令的主循环。
# 它通过套接字连接接收命令，进行处理，并执行相应的操作。
# 支持的命令有'advdata'、'advdatatimeout'、'intervalandrssi'和'collect'。
# 根据接收到的命令调用其他函数，并将数据或数据包发送回监控程序。
def cmdloop(insock):
    global sock, p
    sock = insock
    print('Center connected.\n')
    while True:
        try:
            line = sock.recvline()
            if 'cmd' in line:
            # handle command from center
                cmd = line.strip().split()[1]
                if cmd == 'advdata':
                    print('CMD: advdata')
                    mac = sock.recvline(keepends=False)
                    sendPkt(getAdvertisingData(mac))
                elif cmd == 'advdatatimeout':
                    print('CMD: advdatatimeout')
                    timeout = int(line.strip().split()[2])
                    mac = sock.recvline(keepends=False)
                    sendPkt(getAdvertisingData(mac, timeout))
                elif cmd == 'intervalandrssi':
                    print('CMD: intervalandrssi')
                    channel = line.strip().split()[2]
                    mac = sock.recvline(keepends=False)
                    interval = getInterval(mac, channel)
                    sendData(str(interval))
                elif cmd == 'collect':
                    print('CMD: collect')
                    channel = line.strip().split()[2]
                    time = line.strip().split()[3]
                    collect(channel, time)
        except EOFError:
            print('Center disconnected.')
            sock.close()
            p.kill()
            break


# 定义getAdvertisingData函数，该函数从特定MAC地址获取广告数据。
# 它使用getUbertooth函数设置Ubertooth进程，并使用Advpkt模块的getPkt函数获取数据包。
# 它搜索广告数据包并返回该包。
def getAdvertisingData(mac, timeout=10):
    global p
    p = getUbertooth('37', mac)
    p.readline()
    pkt = getPkt(p, timeout)
    while pkt is not None and not pkt.adv_type == 'ADV_IND':
        pkt = getPkt(p)
    p.kill()
    return pkt


# 定义sendPkts函数，该函数通过套接字连接将一系列数据包发送给监控程序。
# 它先发送数据包的数量，然后使用sendPkt函数逐个发送数据包。
def sendPkts(pkt_list):
    global sock
    sock.sendline(str(len(pkt_list)))
    for pkt in pkt_list:
        sendPkt(pkt)

# 定义sendPkt函数，该函数通过套接字连接将单个数据包发送给监控程序。
# 它使用pickle模块将数据包对象序列化并发送。
def sendPkt(pkt):
    global sock
    pickle.dump(pkt, sock)

# 定义sendData函数，该函数通过套接字连接将一系列数据项发送给监控程序。
# 它先发送数据项的数量，然后逐行发送每个数据项。
def sendData(data_list):
    global sock
    sock.sendline(str(len(data_list)))
    for data in data_list:
        sock.sendline(data)


# 定义main函数，该函数通过调用startServer函数启动服务器。
def main():
    startServer()


# 如果脚本直接执行，则运行main函数。
if __name__ == "__main__":
    main()

# ——————————————————————————————————————————————————————————————————————————

# 注意：该脚本似乎缺少一些重要的导入
# 例如pwn模块的server函数，Advpkt模块的getPkts和getPkt函数，以及Advpkt和Device模块本身。
# 请确保导入这些模块或提供支持脚本中使用的函数所需的代码。