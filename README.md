# BLEGuard Supplement

This repo is the supplemental material for the paper: [BLEGuard: Hybrid Detection Mechanism for Spoofing Attacks in Bluetooth Low Energy Networks (Student Abstract)]()

BLEGuard is a hybrid detection mechanism based on cyber-physical features judgments and machine learning techniques, which can well identify advanced spoofing attacks through offline training and online analysis. 




```yaml
.  
├─dataset
│  ├─package			# wireshark packets.
│  ├─RSSI-dataset		# partly RSSI feature data recorded.
│  └─src				# data visulization source code.
├─src
│  ├─attacker			# attacker.
│  ├─blemonitor			# BLE device monitor code.
│  ├─machine-learning	# relative code includes: LSTM, TextCNN etc.
│  │  ├─LSTM
│  │  └─TCN
│  ├─test				# partly test data used.
│  └─ubertooth			# fixed ubertooth code for additional attribution.
└─static				# static resource.
    ├─deviceList
    └─image
```



More information in [Supplement Report](./supplement.md).
