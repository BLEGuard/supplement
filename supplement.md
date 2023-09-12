# BLEGuard: Hybrid Detection Mechanism for Spoofing Attacks in Bluetooth Low Energy Networks (Student Abstract)

**Supplement Report for AAAI**

Corresponding E-mail: hanlin.cai@ieee.org, zzxu@fzu.edu.cn

---

[TOC]

# 1 BLE Network Basics

In this section, we elaborate on the process of building our BLE network testbed, as well as the specific information of the deployment environment and related devices. To ensure the reliability and reproducibility of our experiments, all hardware devices and software platforms used are openly available on the Internet.



## 1.1 Overview



## 1.2 Overview



# 2 Experiment Setup

In this section, we elaborate on the process of building our BLE network testbed, as well as the specific information of the deployment environment and related devices. To ensure the reliability and reproducibility of our experiments, all hardware devices and software platforms utilized are openly available on the Internet.

## 2.1 Overview

In a word, the testbed environment can be divided into four parts: (i) BLE devices, (ii) user devices, (iii) attacker platforms, and (iv) network sniffers. **Table 1** illustrates all components used in BLEGuard testbed.

<center>Table 1: Components of BLEGuard testbed</center>

| **Component**      | **Description**                              | **Devices Example**                                                                                                                                                            |
| ------------------ | -------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| BLE devices        | Used to build the BLE cyberspace environment | nRF51822 & DA14580 chips                                                                                                                                                       |
| User devices       | Used to connect and simulate usage scenarios | Apple laptop & Android phone                                                                                                                                                   |
| Attacker platforms | Used to launch advanced spoofing attacks     | CSR dongle & Dell laptop                                                                                                                                                       |
| Network sniffers   | Used to capture network advertising package  | [Raspberry Pi](https://www.amazon.com/Raspberry-Pi-Technical-Compliant-Product/dp/B081YD3VL5) & [Ubertooth One](https://ubertooth.readthedocs.io/en/latest/ubertooth_one.html) |



## 2.2 Deployment Environment

We deployed the testbed in a physical environment: a $15m \times 15m$ ofﬁce hosting several students in 18 cubicles. We divided the ofﬁce space into grids of $1m \times 1m$. The ofﬁce presents a typically complicated and noisy indoor environment for determining the detection performance of **BLEGuard**. While recording RF signals within the sniffers\' reception range, we observed substantial channel interference originating from 40 other Bluetooth or BLE-equipped devices (including headsets, sensors, smartphones, and laptops), dozens of Wi-Fi access points, and two microwave devices. Furthermore, we noted that sudden movements of individuals within the office had a significant impact on channel conditions within the monitored environment.

## 2.3 BLE Devices

For the implementation of our network testbed, we employed sixteen widely used BLE devices, covering a variety of Bluetooth chips (such as nRF51822 and DA14580), as shown in **Table 2**. Eventually, we selected twelve of these devices, which exhibited relative stability, to gather our advertising datasets. Furthermore, these BLE devices represent a diverse array of mainstream BLE applications (e.g., temperature sensors, locks, and smoke detectors) from reputable manufacturers including Eve, Tahmo, Nest, Ilumi, Apple and Xiaomi.

<center>Table 2: BLE devices used in BLEGuard testbed</center>

| Sr.  | Device Name                              | Manufacturers | Device Type        | Link for Details                                             |
| :--: | ---------------------------------------- | ------------- | ------------------ | ------------------------------------------------------------ |
|  1   | Smoke detector                           | Nest          | Sensor             | [Link1](https://store.google.com/us/product/nest_protect_2nd_gen_specs?hl=en-US) |
|  2   | Indoor Camera                            | Nest          | Camera             | [Link2](https://store.google.com/us/product/nest_cam_indoor_specs?hl=en-US) |
|  3   | Nest Mini                                | Nest          | Audio Stream       | [Link3](https://store.google.com/us/product/google_nest_mini_specs?hl=en-US) |
|  4   | Temperature Sensor                       | SensorPush    | Sensor             | [Link4](https://www.sensorpush.com/products/p/ht1)           |
|  5   | Tempi Temperature Sensor                 | Tahmo         | Sensor             | [Link5](https://www.wellbots.com/products/tempi-smart-temperature-humidity-monitor) |
|  6   | Smart Lock                               | August        | Smart Home         | [Link6](https://august.com/products/august-smart-lock-connect) |
|  7   | Door&Window Sensor                       | Eve           | Smart Home         | [Link7](https://www.amazon.com/dp/B08BDM47S2/ref=emc_b_5_t?th=1) |
|  8   | Button Remote Control                    | Eve           | Smart Home         | [Link8](https://www.amazon.com/Eve-Button-Connected-accessories-Bluetooth/dp/B0789FGSJ9) |
|  9   | Energy Socket                            | Eve           | Energy             | [Link9](https://www.amazon.com/dp/B08YHPN63H/ref=emc_b_5_t?th=1) |
|  10  | Smart Light Bulb                         | Ilumi         | Smart Home         | [Link10](https://www.amazon.com/ilumi-Bluetooth-Smart-Light-Generation/dp/B017WDRD0W) |
|  11  | Mi Smart Scale                           | Xiaomi        | Scale              | [Link11](https://www.amazon.co.jp/-/en/Xiaomi-Weight-Smartphone-Management-Fitness/dp/B08SBX6NP3) |
|  12  | Mi Band 8                                | Xiaomi        | Smart Band         | [Link12](https://www.amazon.com/Xiaomi-Bracelet-Bluetooth-Fitness-Standard/dp/B0C3B42Q29) |
|  13  | Mijia Bluetooth Thermometer Hygrometer 2 | Xiaomi        | Sensor             | [Link13](https://www.aliexpress.com/i/3256801149866903.html?gatewayAdapt=4itemAdapt) |
|  14  | Key Finder                               | iHere         | Locator            | [Link14](https://www.nonda.co/products/i-here)               |
|  15  | Otbeat Sport Band                        | Otbeat        | Heart Rate Monitor | [Link15](https://www.shoporangetheory.com/Product/OT-BURN-20-C-OTbeat_Burn) |
|  16  |                                          | Apple         | Audio Stream       | Link16                                                       |



## 2.4 User Devices

The user devices are used to connect with BLE devices to simulate network scenarios under normal use, such as Bluetooth speakers playing music, smart thermometers collecting data, and so on. **Table 3** illustrates the user devices utilized in our network environment. It was mentioned above that we have 40 other network devices in our deployment office, but we only collect the advertising datasets from the BLE devices we deployed, and we do not record any data from other unknown devices.

<center>Table 3: User devices used in BLEGuard testbed</center>

| Component                                                                                                       | Description                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         | MAC Address           |
| --------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | --------------------- |
| [MacBook Pro (13-inch, M1, 2020)](https://support.apple.com/kb/SP824?locale=zh_CN)                              | The MacBook Pro, as a flagship product within Apple's Mac series, holds a dominant position in the laptop market, boasting a vast user base and widespread usage. Apple's Mac series enjoys significant market share and a dedicated user following. This ubiquity and popularity make the MacBook Pro a fitting choice for various testing and experimental purposes. Its compatibility with a wide array of peripherals and technologies, including Bluetooth, ensures that it serves as a representative device for a diverse user demographic. Additionally, Apple's commitment to stringent hardware and software standards further establishes the MacBook Pro as a reliable and consistent testing component, making it an ideal choice for conducting Bluetooth-related experiments and studies.                                            | `0x0F:2E:4D:1A:8C:5B` |
| [Lenovo V15-IIL 15.6" Notebook](https://www.amazon.com/Lenovo-V15-IIL-82C500L3US-15-6-Notebook/dp/B08FFLGLG4)   | The Lenovo V15-IIL Notebook, within the Lenovo brand, represents an excellent choice for testing and experimental purposes, owing to its combination of affordability and widespread usage. Lenovo has garnered a significant user base with its range of cost-effective laptops, and the V15-IIL is no exception. Its competitive pricing makes it a popular choice among a diverse demographic of users. This affordability, coupled with Lenovo's reputation for producing reliable hardware, positions the V15-IIL as a suitable testing component. Its broad user adoption ensures that it can effectively represent a substantial portion of the laptop market, making it an apt choice for conducting various experiments and studies, including those related to Bluetooth technology, within a budget-friendly context.                    | `0x0D:76:9A:3F:E7:0B` |
| [Google Pixel 7](https://store.google.com/us/product/pixel_7)                                                   | The Google Pixel 7 smartphone is a pivotal element within the Android ecosystem, highlighting Google's profound influence on both Android and Bluetooth technology. Google's leadership in the Android ecosystem shapes the direction of the operating system, with Bluetooth integration being a crucial aspect. Their support for Bluetooth audio, device connectivity, and Bluetooth Low Energy (BLE) has fostered a thriving market for Bluetooth accessories and IoT devices, benefiting Pixel users and the broader Android community. Google's commitment to security and adherence to Bluetooth standards ensures a consistent, secure, and user-friendly Bluetooth experience across various Android devices. Consequently, the Google Pixel 7 is an ideal choice for Bluetooth-related security experiments within the Android ecosystem. | `0x08:5B:3C:2F:A1:6D` |
| [iPhone 13](https://www.apple.com/iphone-13/specs/)                                                             | Apple's embrace of Bluetooth Low Energy (BLE) in its iPhones was instrumental in driving the development of IoT devices such as fitness trackers, smart home gadgets, and health monitoring devices. Furthermore, Apple's commitment to strict adherence to Bluetooth standards in its products has promoted interoperability and encouraged other manufacturers to maintain compatibility, ensuring a seamless user experience across different devices. This extensive influence underscores why the iPhone 13 is a logical choice for inclusion in BLE security experiments, as it represents a significant and widely-used user device within the Bluetooth ecosystem.                                                                                                                                                                          | `0x0A:9F:7E:2D:6B:8F` |
| [Surface Laptop 5](https://www.microsoft.com/en-ca/d/surface-laptop-5/8xn49v61s1bn?activetab=pivot:overviewtab) |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     | `0x06:3D:1F:7E:A8:4C` |
| [iPad Air](https://www.apple.com.cn/ipad-air)                                                                   |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     | `0x0B:4A:5E:2C:9F:7D` |

​                                                         

​                                                         

​                                                         

## 2.5 Attacker Platforms

To generate multiple spoofing attacks, we employed four distinct types of attacker platforms, each consisting of three identical samples placed at various locations. **Table 4** presents the details of the attacker platforms utilized in our testbed. We selected these platforms for their accessibility, programmability, and their use of different transmit power values.

<center>Table 4: Attacker platform used in BLEGuard testbed</center>

| **Device Name**             | **Description** | **MAC Address**     | **Link for Details**                                                                                     |
| --------------------------- | --------------- | ------------------- | -------------------------------------------------------------------------------------------------------- |
| Lenovo 15IIL laptop         |                 | 04:6c:59:05:9c:8a   | [Link1](https://www.amazon.com/Lenovo-Ideapad-Screen-Keyboard-Windows/dp/B09KSVJ796?th=1)                |
| CSR 4.0 BT dongle           |                 | 02:42:07\:cd\:65:a4 | [Link2](https://www.amazon.com/Bluetooth-Adapter-EKSEN-Transmitter-Receiver/dp/B078Y81S4S)               |
| HM-10 development board     |                 | 02:42:13:02:c7:f0   | [Link3](https://www.amazon.com/DSD-TECH-Bluetooth-iBeacon-Arduino/dp/B06WGZB2N4)                         |
| CYW920735 development board |                 | 00:16:3e:0d:95:65   | [Link4](https://www.mouser.com/datasheet/2/100/CYW920735Q60EVB-01_Evaluation_Kit_User_Guide-1398056.pdf) |

​                                                  

## 2.6 Network Sniffers

We positioned three network sniffers at specific grid locations within the office, with each location having an identical copy of the sniffer. These sniffers were built using Raspberry Pi equipped with Ubertooth One, an open platform suitable for secondary development. The Ubertooth One was utilized for capturing network packets and cyber-physical feature information, while the Raspberry Pi managed the transmission of datasets to the user (monitor) devices. The total cost for establishing such a sniffer configuration is approximately \$100. 

Additionally, we plan to incorporate other types of sniffers for advertising dataset collection, aiming to reduce the deployment costs of BLEGuard system and minimize the data errors as much as possible. **Table 5** provides several available combinations of devices to implement a BLE network sniffer. We note that the user laptops can also be used to capture network information using nRF Connect Software without additional overhead.

<center>Table 5: Several combinations of devices to implement the network
    sniffer</center>

| **Communication platform**                                                                                                                                                                                                                                                                                                                                                                               | **Network capture  platform**                                                                                                                                                                                                  | **Total Cost** |
| -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ | -------------- |
| [Raspberry Pi](https://www.amazon.com/Raspberry-Pi-Technical-Compliant-Product/dp/B081YD3VL5) (Linux 5.4)                                                                                                                                                                                                                                                                                                | [Ubertooth One](https://ubertooth.readthedocs.io/en/latest/ubertooth_one.html)                                                                                                                                                 | $100           |
| [Raspberry Pi](https://www.amazon.com/Raspberry-Pi-Technical-Compliant-Product/dp/B081YD3VL5) (Linux 5.4)                                                                                                                                                                                                                                                                                                | [Acxico 1Pcs USB CC2531](https://www.amazon.com/Acxico-Bluetooth-Wireless-Analyzer-External/dp/B081WRGL31/ref=sr_1_2?keywords=bluetooth+sniffer&qid=1694423571&sr=8-2)                                                         | $75            |
| [Raspberry Pi Pico](https://www.amazon.com/seeed-studio-Raspberry-Microcontroller-Dual-core/dp/B08TQSDP28/ref=pd_bxgy_img_sccl_1/143-1833023-7493734?pd_rd_w=Pmbvo&content-id=amzn1.sym.26a5c67f-1a30-486b-bb90-b523ad38d5a0&pf_rd_p=26a5c67f-1a30-486b-bb90-b523ad38d5a0&pf_rd_r=12MV4HGYFP0XBW2S7655&pd_rd_wg=vZH14&pd_rd_r=8f453468-30f3-4dd7-a22e-905b848ef708&pd_rd_i=B08TQSDP28&th=1) (Linux 4.14) | [MDBT42Q-DB-32](https://www.amazon.com/Raytac-MDBT42Q-512KV2-Bluetooth-Development-Pre-Certified/dp/B081GX1FK7/ref=sr_1_7?crid=1F766B4OYDWCY&keywords=nRF52840+DK&qid=1694425028&sprefix=bluetooth+sniffer%2Caps%2C987&sr=8-7) | $20            |
| Lenovo Laptop (Windows 10)                                                                                                                                                                                                                                                                                                                                                                               | nRF Connect Software                                                                                                                                                                                                           | (User device)  |
| Apple Laptop (macOS 13)                                                                                                                                                                                                                                                                                                                                                                                  | nRF Connect Software                                                                                                                                                                                                           | (User device)  |



# 3 Testbed Implementation & Attack Simulation

In this section

## Overview

## Overview

## Overview



# 4 Advertising Datasets





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



# 5 Detection Mechanism



# 6 References
