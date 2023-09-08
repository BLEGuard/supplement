# BLEGuard

This section provides a list of devices used in the BLEGuard experiment setup, along with corresponding website links.  By following the five steps below, a testbed can be built for collecting Bluetooth in the network environment and detecting simulated attacks.  The testbed includes the components used in the basic deployment, as well as the devices being tested, the platform for simulating attacks, the detection methods, and experimental data.

[TOC]

# Experiment Setup

## Deployment Environment

We deployed BLEGuard by placing the three collectors at selected grid locations within the office. **Table 1** shows all components which are used in testbed. The office provides a typical noisy and challenging indoor environment for evaluating the detection performance of BLEGuard. By recording RF signals within the reception range of the collectors, we discovered significant channel interference from 30 other Bluetooth/BLE-equipped devices (sensors, headsets, smartphones, and laptops), dozens of WiFi access points, and a microwave oven. We also observed that sudden movements of people within the office significantly altered the channel conditions in the monitored environment.

| ID   | Component                 | Description                                                  | Specification                                                |
| ---- | ------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 1    | Physical Machine          | Used for conducting  all experiments. Hardware and software was installed on this machine. | Microsoft Windows 11  Pro x64 bit. Processpr of Intel(R) Core(TM) i5-1035G1 CPU @ 1.00GHz  1.19 GHz, 16Gb of RAM and 64Gb of internal  storage. |
| 2    | CSR8510 Chipset           | Used for cloning BLE  devices and broadcasting fake advertisement packets | Bluetooth v4.0  single-chip radio                            |
| 3    | CC2540 USB Dongle         | Used for  eavesdropping BLE packets                          | Processor Intel Core  M-5Y10C clocked at 1Ghz, 4Gb of RAM and 32Gb of internal storage |
| 4    | Linux Machine             | Used for running  Gattacker tool                             | Linux  5.10.134-13.al8.x86_64. Processor of Intel(R) Xeon(R) Platinum 8369B CPU @  2.70GHz. 4GB of RAM and 40GB of internal storage. |
| 5    | Android Device            | Hardware used for  running applications                      | Huawei P40 with  harmony version 3.0                         |
| 6    | Sniffer                   | Used for broadcasting  cloned advertisement packets          | -                                                            |
| 7    | BLE peripheral  simulator | Used for broadcasting  cloned advertisement packets          | -                                                            |

<center>Table 1. Components used in Testbed</center>

## Device Selection

To exhaustively evaluate BLEGuard, we utilized fifteen different BLE devices which are shown in **Table 2**. These BLE devices cover the mainstream BLE applications (e.g., temperature sensor,  smart lock, and sport band) and popular manufacturers (e.g., Nest, August, Eve, and Xiaomi) with a variety of Bluetooth chips (e.g., DA14580 and nRF51822). We randomly chose nine different locations within the office to place these BLE devices. 

| Device ID | Device Name                                      | Device Type                 | Product <u>Address</u>                                       |
| --------- | ------------------------------------------------ | --------------------------- | ------------------------------------------------------------ |
| 1         | Nest Protect Smoke Detector                      | Smoke Detector              | [Nest   Protect 2nd Gen - Installation and Tech Specs - Google Store](https://store.google.com/us/product/nest_protect_2nd_gen_specs?hl=en-US) |
| 2         | Nest Cam Indoor Camera                           | Camera                      | [Tech   Specs for Nest Cam (indoor, wired) - Google Store](https://store.google.com/us/product/nest_cam_indoor_specs?hl=en-US) |
| 3         | SensorPush Temperature Sensor                    | Temperature Sensor          | [HT1   Temperature and Humidity Smart Sensor — SensorPush](https://www.sensorpush.com/products/p/ht1) |
| 4         | Tahmo Tempi Temperature Sensor                   | Temperature Sensor          | [Tahmo   Tempi Smart Temperature & Humidity Monitor \| Wellbots](https://www.wellbots.com/products/tempi-smart-temperature-humidity-monitor) |
| 5         | August Smart Lock                                | Smart home                  | [August   Smart Lock + Connect \| Products \| August Home](https://august.com/products/august-smart-lock-connect) |
| 6         | Eve Door&Window Sensor                           | Smart home                  | [Amazon.com:   Eve Door & Window - Apple HomeKit Smart Home Wireless Contact Sensor for   Windows & Doors, Automatically Trigger Accessories & Scenes, App   Notifications,White : Automotive](https://www.amazon.com/dp/B08BDM47S2/ref=emc_b_5_t?th=1) |
| 7         | Eve Button Remote Control                        | Smart home                  | [Eve   Button - Apple HomeKit Smart Home Remote To Command Accessories and Scenes -   Amazon.com](https://www.amazon.com/Eve-Button-Connected-accessories-Bluetooth/dp/B0789FGSJ9) |
| 8         | Eve Energy Socket                                | Energy                      | [Eve Energy -   Apple HomeKit Smart Home, Smart Plug & Power Meter with Built-in   Schedules & Switches, App Compatibility, Bluetooth and Thread,White -   Amazon.com](https://www.amazon.com/dp/B08YHPN63H/ref=emc_b_5_t?th=1) |
| 9         | Ilumi Smart  Light Bulb                          | Smart home                  | [Amazon.com:   ilumi Bluetooth Smart LED A19 Light Bulb, 2nd Generation - Smartphone   Controlled Dimmable Multicolored Color Changing Light - Works with iPhone,   iPad, Android Phone and Tablet, Arctic White, 1 Count : Electronics](https://www.amazon.com/ilumi-Bluetooth-Smart-Light-Generation/dp/B017WDRD0W) |
| 10        | Xiaomi Mi Smart Scale                            | Scale                       | [Amazon.co.jp:   Xiaomi Mi Smart Scale 2, Smart Scale 2, Weight Scale, Can be Linked with   Smartphone, Xiaomi Mi Smart Scale 2, Health Management, Health Meter, Diet,   Fitness : Home & Kitchen](https://www.amazon.co.jp/-/en/Xiaomi-Weight-Smartphone-Management-Fitness/dp/B08SBX6NP3) |
| 11        | Xiaomi Mi Band 8                                 | Smart Band                  | [Amazon.com:   Xiaomi Mi Band 8 Smart Bracelet AMOLED Screen Heart Rate Blood Oxygen   Bluetooth Sport Watch Fitness Traker Smart Watch (Chinese Standard Version   Black) : Electronics](https://www.amazon.com/Xiaomi-Bracelet-Bluetooth-Fitness-Standard/dp/B0C3B42Q29) |
| 12        | Xiaomi Mijia Bluetooth  Thermometer Hygrometer 2 | Temperature Humidity Sensor | [Xiaomi   Mijia Bluetooth Thermometer Hygrometer 2 Temperature Humidity Sensor Smart   Digital Lcd Moisture Meter Thermo-hygrometer - Smart Remote Control -   AliExpress](https://www.aliexpress.com/i/3256801149866903.html?gatewayAdapt=4itemAdapt) |
| 13        | iHere Key Finder                                 | Key Finder                  | [iHere smart Key   Finder \| nonda](https://www.nonda.co/products/i-here) |
| 14        | Otbeat                                           | heart rate monitor          | [OTBEAT   BURN \| Shop Orangetheory](https://www.shoporangetheory.com/Product/OT-BURN-20-C-OTbeat_Burn) |
| 15        | Nest Mini                                        | Audio Stream                | [Google   Nest Mini Tech Specs - Google Store](https://store.google.com/us/product/google_nest_mini_specs?hl=en-US) |

<center>Table 2. Table of BLE devices.</center>

## Attack Simulation

To carry out different types of attacks, we utilized four attacker platforms, a Lenovo XiaoXin-15IIL 2020 Laptop, a CSR 4.0 BT dongle, an HM-10 developmental board, and a CYW920735 developmental board, shown in **Table 3**. These platforms were selected because they provide ease of access and programmability, and they utilized different transmit power values. Besides, to thoroughly evaluate the performance of BLEGuard, we launched a variety of spoofing attacks from 12 different locations, some at the center and some at edges of the office. Further, to enrich the evaluation of the effectiveness of the CFO inspection, we utilized different copies of the same BLE device as attackers (in addition to the four attacker platforms). For the unbiased evaluation of the RSSI inspection, we utilized the same BLE device as the benign BLE device and the attacker, and collected its advertising packets by placing it at different locations within the office environment. 

| Attacker  ID | Attacker Name                    | Describe                                                     | MAC Address        | Address                                                      |
| ------------ | -------------------------------- | ------------------------------------------------------------ | ------------------ | ------------------------------------------------------------ |
| 1            | Lenovo XiaoXin-15IIL 2020 Laptop | HCI 10.256 / LMP 10.256 in Bluetooth                         | 04:6c:59:05:9c:8a  | [Amazon.com:   Lenovo New Ideapad 3 15.6" FHD Touch Screen Laptop\|Intel Core i5 11th   Gen \|12GB RAM, 256GB SSD\| HDMI\|Baclit Keyboard\|Arctic Grey Windows 11 :   Electronics](https://www.amazon.com/Lenovo-Ideapad-Screen-Keyboard-Windows/dp/B09KSVJ796?th=1) |
| 2            | CSR 4.0 BT dongle                | Bluetooth Low Energy (BLE) protocol support.  Up to 3 Mbps data transfer rate with Enhanced Data Rate (EDR) support. | 02:42:07\:cd:65:a4 | [Amazon.com:   Bluetooth CSR 4.0 USB Dongle Adapter, EKSEN Bluetooth Transmitter of   Electronic Signals for Windows 10/8/7/Vista - Plug and Play on Win 8 and   Above - White : Everything Else](https://www.amazon.com/Bluetooth-Adapter-EKSEN-Transmitter-Receiver/dp/B078Y81S4S) |
| 3            | HM-10 developmental board        | Bluetooth protocol: Bluetooth Specification V4.0 BLE  ISM band Modulation method: GFSK(Gaussian Frequency Shift Keying) Transmission power: -23dbm, -6dbm, 0dbm, 6dbm, can be modified. | 02:42:13:02:c7:f0  | [Ks0255   keyestudio Bluetooth 4.0 Expansion Shield - Keyestudio Wiki](https://wiki.keyestudio.com/Ks0255_keyestudio_Bluetooth_4.0_Expansion_Shield) |
| 4            | CYW920735                        | The CYW20735B1 integrates a 2.4 GHz transceiver that supports Basic Rate (BR) and Bluetooth® Low Energy, and provides 10-dBm output power for an excellent range. | 00:16:3e:0d:95:65  | [002-23666_WEB   PDF 4 (rs-online.com)](https://docs.rs-online.com/c9b9/0900766b816b6e22.pdf) |

<center>Table 3. Attacker platforms</center>

## Detection Implementation

BLEGuard can be readily implemented using low-cost, off-the-shelf platforms. We implemented the collector using an Ubertooth One radio connected to a Raspberry Pi running Linux (**Table 4.**). The total cost of such a collector is approximately \$100. We note that when a collector is deployed on a custom-designed platform, the unit cost could be less than \$5 for a Bluetooth Low Energy (BLE) module.

The Ubertooth first captures packets on advertising channels. To retrieve the physical features, we modified the Ubertooth firmware to provide carrier frequency offset (CFO) and received signal strength indicator (RSSI) values for each received packet. This customization is feasible because Ubertooth is an open platform for Bluetooth research and development.

Finally, the Raspberry Pi communicates the packets along with their relevant features to the monitor. We implemented the monitor on an Ubuntu 18.04 desktop PC. At the monitor, all processes, including interacting with collectors, parsing the received information from collectors, and runtime inspection mechanisms, were implemented with approximately 3,000 lines of Python code.

| Device        |      | Reference Website                                            |
| ------------- | ---- | ------------------------------------------------------------ |
| Raspberry Pi  |      | [Amazon.com: Raspberry Pi 4 Model B 8GB : Electronics](https://www.amazon.com/Raspberry-Pi-Technical-Compliant-Product/dp/B081YD3VL5) |
| Ubertooth One |      | [Ubertooth One — Ubertooth documentation](https://ubertooth.readthedocs.io/en/latest/ubertooth_one.html) |

<center>Table 4. Detection devices list</center>

## Experimental Data

For each BLE device, benign advertising packets were collected for 48 hours (throughout day and night). For each attacker platform placed at each location, spoofed advertising packets were collected for around 15 minutes. As **Table 5**, we totally collected 902,890 advertising packets which are comprised of 85.2% benign advertising packets and 14.8% spoofed advertising packets. This data was utilized as the ground truth for our evaluation. **Table 6** supports ten sample device basic information, advertising pattern, and low bound of INT.

|   Characteristic    |  Value  |
| :-----------------: | :-----: |
| advertising packets | 902,890 |
|   benign packets    |  85.2%  |
|  malicious packets  | 14.8%.  |

<center>Table 5. Experimental data</center>

| Device ID & Name | MAC Address           | Advertising Data         | Advertising Pattern | Low Bound of INT |
| ---------------- | --------------------- | ------------------------ | ------------------- | ---------------- |
| 1, n075w         | `0x02:42:13:02:c7:f0` | `0x06 09 4e 30 39 37 57` | Intermittent        | 1282 `ms`        |
| 2, b3s97         | `0x08:7f:2a:1c:5e:b3` | `0x0A 18 54 28 6d 81 45` | Continuous          | 1376 `ms`        |
| 3, tfb7a         | `0x0E:2b:65:9f:3d:a7` | `0x0F 3A 73 59 21 4C 60` | Continuous          | 923 `ms`         |
| 4, r672y         | `0x0B:19:0c:7f:8e:2d` | `0x0D 0E 76 45 93 82 1A` | Continuous          | 1149`ms`         |
| 5, o3aqe         | `0x01:98:4d:6a:f7:3c` | `0x04 13 28 6B 94 3E 77` | Intermittent        | 1423 `ms`        |
| 6, 7wzfy         | `0x0C:57:6f:8b:a2:4e` | `0x07 23 58 17 9F 02 3D` | Continuous          | 1445 `ms`        |
| 7, 3v9nl         | `0x0A:aa:32:ef:1d:80` | `0x0B 88 49 2E 76 51 3C` | Intermittent        | 1055 `ms`        |
| 8, 42xpz         | `0x03:ef:1a:58:c4:9d` | `0x05 47 9D 0A 86 2B 1E` | Intermittent        | 1290`ms`         |
| 9, qtpv5         | `0x09:6d:84:23:5f:ca` | `0x0C 6F 3B 0C 98 74 21` | Continuous          | 1178 `ms`        |
| 10, l348y        | `0x04:31:2e:7d:0f:6a` | `0x0E 67 A1 05 9D 3F 7A` | Continuous          | 1264 `ms`        |

<center>Table 6. Sample data of BLE devices recorded during the monitor phase.</center>

