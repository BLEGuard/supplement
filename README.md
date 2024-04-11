# BLEGuard Supplement

As the foremost protocol for low-power communication, Bluetooth Low Energy (BLE) significantly impacts various aspects of our lives, including industry and healthcare. Given BLE’s inherent security limitations and firmware vulnerabilities, spoofing attacks can readily compromise BLE devices and jeopardize privacy data. In this paper, we introduce ***BLEGuard***, a hybrid mechanism for detecting spoofing attacks in BLE networks. We established a physical Bluetooth system to conduct attack simulations and construct a substantial dataset (***BLE-SAD***). BLEGuard integrates pre-detection, reconstruction, and classification models to effectively identify spoofing activities, achieving an impressive preliminary accuracy of 99.01%, with a false alarm rate of 2.05% and an undetection rate of 0.36%.

**Our Github repo contain the following code and data:**


```tex
.
├─dataset               # sample set of our dataset.
│  ├─profiles           # sample data of BLE device.
│  └─BLE-SAD            # large-scale BLE network packets.
│  └─Android_tool_app   # Android app for automatic data collection.
├─src
│  ├─blemonitor         # BLE device monitoring program.
│  ├─machine-learning   # relative code for our learning model.
│  └─ubertooth          # fixed ubertooth code for additional attribution.
├─static                # static resource.
└─README                # the document you are reading now :D
```

## Our Poster for MobiSys 2024

<left>
  <img src = "./static/BLEGuard_poster.png">
</left>

## Reference & License

You can find the projects we've referenced in the machine learning section at this [link](https://github.com/BLEGuard/supplement/blob/master/src/machine-learning/machine-learning.md).

This project is licensed under the MIT license. See the [LICENSE](./LICENSE) file for details.
