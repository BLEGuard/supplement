# BLEGuard Supplement

This repo is the supplemental material for the paper: [BLEGuard: Hybrid Detection Mechanism for Spoofing Attacks in Bluetooth Low Energy Networks (Student Abstract)]()

BLEGuard is a hybrid detection mechanism based on cyber-physical features judgments and machine learning techniques, which can well identify advanced spoofing attacks through offline training and online analysis. 

```markdown
.
├─code				# relative code includes: LSTM, TextCNN etc.
└─dataset			# partly test data used.
   └─RSSI
```

## Deployment

```shell
python BLEGuard.py
BLEGuard device -c 	# device collect
BLEGuard device -l 	# device list
```

Profile a device with MAC address 00:11:22:33:44:55. 

```shell
BLEGuard profile 00:11:22:33:44:55
```

Start/Stop monitor all devices in the maclist file.

```shell
BLEGuard monitor start
BLEGuard monitor stop
```



