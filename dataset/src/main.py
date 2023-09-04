import matplotlib.pyplot as plt

# data_files = ["X:\BLEGuard\supplement\dataset\RSSI-dataset\RSSI1.txt",
#               "X:\BLEGuard\supplement\dataset\RSSI-dataset\RSSI2.txt",
#               "X:\BLEGuard\supplement\dataset\RSSI-dataset\RSSI3.txt",
#               "X:\BLEGuard\supplement\dataset\RSSI-dataset\RSSI4.txt",
#               "X:\BLEGuard\supplement\dataset\RSSI-dataset\RSSI5.txt",
#               "X:\BLEGuard\supplement\dataset\RSSI-dataset\RSSI6.txt",
#               "X:\BLEGuard\supplement\dataset\RSSI-dataset\RSSI7.txt"]

data_files = ["X:\BLEGuard\supplement\dataset\RSSI-dataset\RSSI2.txt",
              "X:\BLEGuard\supplement\dataset\RSSI-dataset\RSSI3.txt",
              "X:\BLEGuard\supplement\dataset\RSSI-dataset\RSSI4.txt",
              "X:\BLEGuard\supplement\dataset\RSSI-dataset\RSSI5.txt",
              "X:\BLEGuard\supplement\dataset\RSSI-dataset\RSSI6.txt",
              "X:\BLEGuard\supplement\dataset\RSSI-dataset\RSSI7.txt"]



for data_file in data_files:
    file_object = open(data_file)
    dataStr = file_object.read()  # Read data

    data = []

    # Loop through the strings
    for string in dataStr.split('\n'):
        if string == '':
            continue
        # Convert the string to an integer
        integer = int(string)

        # Append the integer to the list of integers
        data.append(integer)

    # Set the color of the line
    if data_file == data_files[0]:
        color = "blue"
    elif data_file == data_files[1]:
        color = "red"
    elif data_file == data_files[2]:
        color = "orange"
    elif data_file == data_files[3]:
        color = "olive"
    elif data_file == data_files[4]:
        color = "green"
    elif data_file == data_files[5]:
        color = "pink"
    # elif data_file == data_files[6]:
    #     color = "cyan"

    # Plot the line
    # plt.plot(range(len(data)), data, color=color)
    plt.plot(range(1500), data[:1500], color=color)

# plt.title("Line Chart")
plt.xlabel("Packet Number")
plt.ylabel("RSSI(dBm)")

plt.show()


# import matplotlib.pyplot as plt
#
# data_files = ["X:\BLEGuard\supplement\dataset\RSSI-dataset\RSSI2.txt",
#               "X:\BLEGuard\supplement\dataset\RSSI-dataset\RSSI3.txt",
#               "X:\BLEGuard\supplement\dataset\RSSI-dataset\RSSI4.txt",
#               "X:\BLEGuard\supplement\dataset\RSSI-dataset\RSSI5.txt",
#               "X:\BLEGuard\supplement\dataset\RSSI-dataset\RSSI6.txt",
#               "X:\BLEGuard\supplement\dataset\RSSI-dataset\RSSI7.txt"]
#
# rssi2 = []
# rssi3 = []
# rssi4 = []
# rssi5 = []
# rssi6 = []
# rssi7 = []
# data = [rssi2, rssi3, rssi4, rssi5, rssi6, rssi7]
#
# file_object0 = open(data_files[0])
# dataStr0 = file_object0.read()  # Read data
#
# count = 0
#
# # Loop through the strings
# for string in dataStr0.split('\n'):
#     if string == '':
#         break
#     # Convert the string to an integer
#     integer = int(string)
#
#     # 增加计数器
#     count += 1
#
#     # 将数据限制在 1500 个点
#     if count < 1500:
#         rssi2.append(integer)
#
# file_object1 = open(data_files[1])
# dataStr1 = file_object1.read()  # Read data
#
# count = 0
#
# # Loop through the strings
# for string in dataStr1.split('\n'):
#     if string == '':
#         break
#     # Convert the string to an integer
#     integer = int(string)
#
#     # 增加计数器
#     count += 1
#
#     # 将数据限制在 1500 个点
#     if count < 1500:
#         rssi2.append(integer)
#
#
#
#
# for dataLine in data:
#     # Set the color of the line
#     if data_file == data_files[0]:
#         color = "blue"
#     elif data_file == data_files[1]:
#         color = "red"
#     elif data_file == data_files[2]:
#         color = "orange"
#     elif data_file == data_files[3]:
#         color = "olive"
#     elif data_file == data_files[4]:
#         color = "green"
#     elif data_file == data_files[5]:
#         color = "pink"
#
#     # Plot the line
#     plt.plot(range(1500), dataLine, color=color)
#
#
# # plt.title("Line Chart")
# plt.xlabel("Packet Number")
# plt.ylabel("RSSI(dBm)")
#
# plt.show()
#
