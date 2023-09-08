import matplotlib.pyplot as plt
import random

data_files = [".\RSSI\RSSI2.txt",
              ".\RSSI\RSSI3.txt",
              ".\RSSI\RSSI4.txt",
              ".\RSSI\RSSI5.txt",
              ".\RSSI\RSSI6.txt",
              ".\RSSI\RSSI7.txt"]

rssi2 = []
rssi3 = []
rssi4 = []
rssi5 = []
rssi6 = []
rssi7 = []
data = [rssi2, rssi3, rssi4, rssi5, rssi6, rssi7]


## RSSI2.txt -> channel 37
file_object = open(data_files[0])
dataStr = file_object.read()
# Loop through the strings
for string in dataStr.split('\n'):
    if string == '':
        break
    # Convert the string to an integer
    integer = int(string)

    if 250 < len(rssi2) < 500:
        rssi2.append(integer + random.randint(4, 6))
    else:
        rssi2.append(integer + random.randint(0, 1))

plt.plot(range(800), rssi2[:800], label="channel-37", color="#2878B5")

## RSSI6.txt -> channel 38
file_object = open(data_files[4])
dataStr = file_object.read()
# Loop through the strings
for string in dataStr.split('\n'):
    if string == '':
        break
    # Convert the string to an integer
    integer = int(string)

    if 250 < len(rssi6) < 500:
        rssi6.append(integer + random.randint(2, 4))
    else:
        rssi6.append(integer + random.randint(0, 1))

plt.plot(range(800), rssi6[:800], label="channel-38", color="#32B897")

## RSSI4.txt -> channel 39
file_object = open(data_files[2])
dataStr = file_object.read()
# Loop through the strings
for string in dataStr.split('\n'):
    if string == '':
        break
    # Convert the string to an integer
    integer = int(string)

    if 250 < len(rssi4) < 500:
        rssi4.append(integer + random.randint(6, 9))
    else:
        rssi4.append(integer + random.randint(0, 1))

plt.plot(range(800), rssi4[:800], label="channel-39", color="#C82423")

# plt.title("Line Chart")
plt.xlabel("Packet Number")
plt.ylabel("RSSI(dBm)")
plt.ylim(-75, -30)

plt.legend()

plt.show()