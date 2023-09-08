# The script runs on the Monitor (laptop or desktop).
# It controls the three Collectors (either profiling a BLE device or monitoring all devices).
# 这个脚本是在监控程序（笔记本电脑或台式机）上运行的。
# 它控制三个收集器，可以对一个BLE设备进行分析或监控所有设备。

# ——————————————————————————————————————————————————————————————————————————

#!/usr/bin/python
from pwn import *
from Device import Device
from DeviceMonitorThread import DeviceMonitorThread
from CollectorThread import CollectorThread
import sys
import pickle
import os
import queue
import random
import os.path as ospath


c1 = None
c2 = None
c3 = None

device_pkt_queue = {}
profiles = {}
device_mac_list = []
device_thread_list = []
collector_thread_list = []

profile_path = 'profiles'
mac_list_file = 'maclist'
ip_config_file = 'config'

# ——————————————————————————————————————————————————————————————————————————


# 定义calculateTimeout函数，用于计算连接超时时间。
def calculateTimeout(p):
    t = 0
    p.sendline('connect')
    while True:
        rsp = p.recvline()
        print(rsp.strip())
        if 'Connection successful' in rsp:
            t = time.time()
        elif 'Invalid file descriptor' in rsp:
            return int(time.time() - t)
        elif 'Connection refused' in rsp:
            return 0


# 定义connect2collector函数，用于连接到收集器并返回套接字。
def connect2collector(ipaddr):
    sock = remote(ipaddr, 5555)
    print('Connected to ' + ipaddr)
    return sock


# 定义getIp函数，从配置文件中获取收集器的IP地址。
def getIp():
    global ip_config_file

    print("Read collector's IP from " + ip_config_file + " file.")
    with open(ip_config_file, 'r') as infile:
        return infile.readline().strip(), infile.readline().strip(), infile.readline().strip()


# 定义loadProfile函数，用于加载配置文件中的设备配置信息。
def loadProfile():
    global profile_path, profiles, device_mac_list

    print('Load profiles from ' + profile_path + ' folder.')
    for f in device_mac_list:
        fpath = ospath.join(profile_path, f.replace(':', '-'))
        dev = pickle.load(open(fpath, 'r'))
        profiles[f] = dev
    print(str(len(profiles.keys())) + ' profiles loaded.')


# 定义initDeviceThreads函数，初始化设备监控线程。
def initDeviceThreads():
    global profiles, device_thread_list, device_pkt_queue
    for k in profiles.keys():
        device_thread = DeviceMonitorThread()
        device_thread.setDevice(profiles[k])
        device_thread.setQueue(device_pkt_queue[k])
        device_thread_list.append(device_thread)
    print('Device monitoring threads initialization finished.')


# 定义getAdvData函数，获取设备的广告数据。
def getAdvData(mac):
    print('Get advertising data')
    global c3
    c3.sendline('cmd advdata')
    c3.sendline(mac)
    pkt = getPkt(c3)
    return pkt


# 定义getPkt函数，从收集器接收并返回数据包。
def getPkt(collector):
    return pickle.load(collector)


# 定义getInterval函数，获取设备的广告间隔和RSSI。
def getInterval(mac):
    print('Get device advertising interval and rssi.')
    global c1, c2, c3
    c1.sendline('cmd intervalandrssi 37')
    c1.sendline(mac)
    c2.sendline('cmd intervalandrssi 38')
    c2.sendline(mac)
    c3.sendline('cmd intervalandrssi 39')
    c3.sendline(mac)
    c1_line_num = int(c1.recvline(keepends=False))
    c2_line_num = int(c2.recvline(keepends=False))
    c3_line_num = int(c3.recvline(keepends=False))
    c1_list = getLines(c1, c1_line_num)
    c2_list = getLines(c2, c2_line_num)
    c3_list = getLines(c3, c3_line_num)
    interval1 = c1_list[0]
    interval2 = c2_list[0]
    interval3 = c3_list[0]
    if interval1 < interval2 and interval1 < interval3:
        interval = interval1
    elif interval2 < interval3:
        interval = interval2
    else:
        interval = interval3
    return interval


# 定义getLines函数，从收集器接收指定数量的数据行。
def getLines(collector, line_num=1):
    print('Get data in list form from ' + collector.rhost)
    lines = []
    for i in range(line_num):
        lines.append(collector.recvline(keepends=False))
    return lines


# 定义getConnAdvData函数，获取设备连接时的广告数据
def getConnAdvData(mac, timeout):
    print('Get advertising data when device connected.')
    global c1, c2, c3
    c1.sendline('cmd advdatatimeout ' + str(timeout))
    c1.sendline(mac)
    c2.sendline('cmd advdatatimeout ' + str(timeout))
    c2.sendline(mac)
    c3.sendline('cmd advdatatimeout ' + str(timeout))
    c3.sendline(mac)

    c1pkt = getPkt(c1)
    if c1pkt is not None:
        if c1pkt.adv_type == 'ADV_SCAN_IND' or c1pkt.adv_type == 'ADV_NONCONN_IND':
            return c1pkt.adv_type
    c2pkt = getPkt(c2)
    if c2pkt is not None:
        if c2pkt.adv_type == 'ADV_SCAN_IND' or c2pkt.adv_type == 'ADV_NONCONN_IND':
            return c2pkt.adv_type
    c3pkt = getPkt(c3)
    if c3pkt is not None:
        if c3pkt.adv_type == 'ADV_SCAN_IND' or c3pkt.adv_type == 'ADV_NONCONN_IND':
            return c3pkt.adv_type
    return None


# 定义getConnedAdvData函数，获取设备连接时的广告数据。
def getConnedAdvData(dev):
    # get advertising data when connected
    global c1, c2, c3
    if dev.addrType == 'public':
        p = process('/usr/bin/gatttool -I -b ' + dev.mac, shell=True)
    else:
        p = process('/usr/bin/gatttool -t random -I -b ' + dev.mac, shell=True)
    #  timeout = calculateTimeout(p)
    timeout = 4
    dev.setTimeout(timeout)
    if timeout == 0:
        print('Cannot connect to device. Abort')
        exit(0)
    p.sendline('connect')
    while True:
        rsp = p.recvline()
        print(rsp.strip())
        if 'Connection successful' in rsp:
            dev.setConnedData(getConnAdvData(dev.mac, timeout - 1))
            break


# 定义profile函数，对设备进行分析和配置。
def profile(mac):
    global profile_path

    print('Profiling...')
    dev = Device()
    dev.setMac(mac)
    print('MAC: ' + mac)
    adv_pkt = getAdvData(mac)
    # advertising data
    dev.setAdvData(adv_pkt.data)
    print('Advertising data: ' + dev.advData)
    # address type
    dev.setAddressType(adv_pkt.addr_type)
    print('Address type: ' + dev.addrType)
    # advertising interval
    interval = getInterval(mac)
    dev.setAdvInterval(interval)
    print('Advertising interval: ' + str(dev.advInterval))
    # advertising data when connected
    getConnedAdvData(dev)
    print('Advertising when connected: ' + str(dev.connectedData))
    with open(ospath.join(profile_path, mac), 'w') as output:
        pickle.dump(dev, output)
    print('Done. Save to file ' + mac)


# 定义monitor函数，监控设备并收集数据。
def monitor(maclist):
    global c1, c2, c3, device_pkt_queue, device_thread_list, collector_thread_list
    print('Monitoring...')
    # 3 threads to collect packets, split to every device queue
    # and put into that queue
    # Each device thread takes packets from that queue checks status
    # and matches rssi&cfo.
    collector_thread1 = CollectorThread(c1, device_pkt_queue, 1, maclist)
    collector_thread_list.append(collector_thread1)
    collector_thread2 = CollectorThread(c2, device_pkt_queue, 2, maclist)
    collector_thread_list.append(collector_thread2)
    collector_thread3 = CollectorThread(c3, device_pkt_queue, 3, maclist)
    collector_thread_list.append(collector_thread3)
    collector_thread1.start()
    collector_thread2.start()
    collector_thread3.start()

    for t in device_thread_list:
        t.start()

    while True:
        time = random.randint(10, 30)
        c1.sendline('cmd collect 37 ' + str(time))
        c2.sendline('cmd collect 38 ' + str(time))
        c3.sendline('cmd collect 39 ' + str(time))
        sleep(time)

        time = random.randint(10, 30)
        c1.sendline('cmd collect 38 ' + str(time))
        c2.sendline('cmd collect 39 ' + str(time))
        c3.sendline('cmd collect 37 ' + str(time))
        sleep(time)

        time = random.randint(10, 30)
        c1.sendline('cmd collect 39 ' + str(time))
        c2.sendline('cmd collect 37 ' + str(time))
        c3.sendline('cmd collect 38 ' + str(time))
        sleep(time)


# 定义loadMacList函数，加载设备的MAC地址列表。
def loadMacList():
    global device_mac_list, mac_list_file
    print("Load devices' mac address from " + mac_list_file + " file.")
    with open(mac_list_file, 'r') as infile:
        for line in infile:
            device_mac_list.append(line.strip())
    print(str(len(device_mac_list)) + ' addresses loaded.')
    return device_mac_list


# 定义initializeDevicePktQueue函数，初始化设备数据包队列。
def initializeDevicePktQueue():
    global device_pkt_queue, device_mac_list

    print('Initializing device packet queue.')
    # each device has a triple list keeping packets from 3 collectors
    for mac in device_mac_list:
        device_pkt_queue[mac] = {}
        # initialize 37 38 39 channel queue
        device_pkt_queue[mac][1] = Queue.Queue()
        device_pkt_queue[mac][2] = Queue.Queue()
        device_pkt_queue[mac][3] = Queue.Queue()

def main():
    ip1, ip2, ip3 = getIp()
    global c1, c2, c3, device_mac_list
    c1 = connect2collector(ip1)
    c2 = connect2collector(ip2)
    c3 = connect2collector(ip3)

    if len(sys.argv) == 1:
        # default: monitoring
        loadMacList()
        loadProfile()

        initializeDevicePktQueue()
        initDeviceThreads()
        monitor(device_mac_list)
        while True:
            try:
                for t in device_thread_list:
                    if t.isAlive():
                        t.join(1)
                for t in collector_thread_list:
                    if t.isAlive():
                        t.join(1)
            except KeyboardInterrupt:
                for t in device_thread_list:
                    t.exit = True
                for t in collector_thread_list:
                    t.exit = True
                break

    elif sys.argv[1] == 'profile':
        profile(sys.argv[2])


# 定义main函数，根据命令行参数选择执行不同的操作。
if __name__ == "__main__":
    main()


# ——————————————————————————————————————————————————————————————————————————


# 注意：该脚本依赖于其他模块和配置文件
# 如Device模块、DeviceMonitorThread模块和配置文件config、maclist等
# 请确保在运行脚本之前提供所需的模块和配置文件。