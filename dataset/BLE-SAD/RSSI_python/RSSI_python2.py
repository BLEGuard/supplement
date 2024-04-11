
import xlrd
import numpy as np
import sys, os
sys.path.append(os.pardir)
import numpy as np
from keras import models#需要搭TensorFlow和keras环境,成功后出现>>> from keras import models >>>Using TensorFlow backend.
from keras import layers
from keras import optimizers
import tensorflow as tf
#from two_layer_net import TwoLayerNet

def excel2m(path):#从excel中读取数据并存成矩阵
    data = xlrd.open_workbook(path)
    table = data.sheets()[0]
    nrows = table.nrows  # 行数
    ncols = table.ncols  # 列数
    c1 = np.arange(0, nrows, 1)
    datamatrix = np.zeros((nrows, ncols))
    for x in range(ncols):
        cols = table.col_values(x)
        minVals = min(cols)
        maxVals = max(cols)
        cols1 = np.matrix(cols)  # 把list转换为矩阵进行矩阵操作
        # ranges = maxVals - minVals
        # b = cols1 - minVals
        # normcols = b / ranges  # 数据进行归一化处理
        datamatrix[:, x] = cols1 # 把数据进行存储
    return datamatrix
    
def excel2m_Normalized(path):
    data = xlrd.open_workbook(path)
    table = data.sheets()[0]
    nrows = table.nrows  # 行数
    ncols = table.ncols  # 列数
    c1 = np.arange(0, nrows, 1)
    datamatrix = np.zeros((nrows, ncols))
    for x in range(ncols):
        cols = table.col_values(x)
        minVals = min(cols)
        maxVals = max(cols)
        cols1 = np.matrix(cols)  # 把list转换为矩阵进行矩阵操作
        ranges = maxVals - minVals
        b = cols1 - minVals
        normcols = b / ranges  # 数据进行归一化处理
        datamatrix[:, x] = normcols # 把数据进行存储
    return datamatrix
    
    
def shuffle_dataset(x, t):
    """打乱数据集

    Parameters
    ----------
    x : 训练数据
    t : 监督数据

    Returns
    -------
    x, t : 打乱的训练数据和监督数据
    example:
    >>> A
array([[ 1,  2,  3,  4,  5],
       [ 6,  7,  8,  9, 10],
       [10, 12, 13, 14, 15]])
>>> x,y=shuffle_dataset(A,A)
>>> x
array([[10, 12, 13, 14, 15],
       [ 6,  7,  8,  9, 10],
       [ 1,  2,  3,  4,  5]])
>>> y
array([[10, 12, 13, 14, 15],
       [ 6,  7,  8,  9, 10],
       [ 1,  2,  3,  4,  5]])
    """
    permutation = np.random.permutation(x.shape[0])
    x = x[permutation,:] if x.ndim == 2 else x[permutation,:,:,:]
    t = t[permutation]
    return x, t

wb=excel2m("10.4.xlsx")#读取相应dx和dy的数据集
x_train=wb[90:2970,0:4]#x_train.shape=(2880, 7)和FaceME+一样，去头去尾
t_train=wb[90:2970,1:2]#t_train.shape=(2880, 1)取最近节点的RSSI值
x_train,t_train=shuffle_dataset(x_train,t_train)#将数据随机排序
x_train_temp=x_train.astype(int)
t_train_temp=t_train.astype(int)
x_train=x_train_temp[0:2350,:]#2350*7 将3000组数据分成2400作为训练集，600作为测试集
t_train=t_train_temp[0:2350,:]
x_test=x_train_temp[2350:,:]#530*7
t_test=t_train_temp[2350:,:]


def build_model():
    # Because we will need to instantiate the same model multiple times,（因为需要将同一个模型多次实例化，）
    # we use a function to construct it.（所以用一个函数来构建模型）
    model = models.Sequential()
    model.add(layers.Dense(5, activation='relu',
                           input_shape=(x_train.shape[1],)))
    model.add(layers.Dense(5, activation='relu'))
    model.add(layers.Dense(5, activation='relu'))
    model.add(layers.Dense(5, activation='relu'))
    model.add(layers.Dense(5, activation='relu'))
    model.add(layers.Dense(1))
    #model.compile(optimizer=optimizers.RMSprop(lr=0.001), loss='mse', metrics=['mae'])#mse为均方误差,mae为平均绝对误差
    model.compile(optimizer=optimizers.RMSprop(lr=0.001), loss='mse', metrics=['mae'])
    return model

    
k = 4#k折交叉验证
num_val_samples = len(x_train) // k
num_epochs = 200 #200
all_scores = []

with tf.device('/cpu:0'):#使用CPU训练
    for i in range(k):
        print('processing fold #', i)
        # Prepare the validation data: data from partition # k（准备验证数据：第 k 个分区的数据）
        val_data = x_train[i * num_val_samples: (i + 1) * num_val_samples]
        val_targets = t_train[i * num_val_samples: (i + 1) * num_val_samples]
        # Prepare the training data: data from all other partitions（准备训练数据：其他所有分区的数据）
        partial_train_data = np.concatenate(
            [x_train[:i * num_val_samples],
             x_train[(i + 1) * num_val_samples:]],
            axis=0)
        partial_train_targets = np.concatenate(
            [t_train[:i * num_val_samples],
             t_train[(i + 1) * num_val_samples:]],
            axis=0)
        # Build the Keras model (already compiled)（构建 Keras 模型（已编译））
        model = build_model()
        # Train the model (in silent mode, verbose=0)（训练模型（静默模式，）
        model.fit(partial_train_data, partial_train_targets,
                  epochs=num_epochs, batch_size=1, verbose=0)
        # Evaluate the model on the validation data（在验证数据上评估模型）
        val_mse, val_mae = model.evaluate(val_data, val_targets, verbose=0)
        all_scores.append(val_mae)
    
    
    
predict=model.predict(x_test)#val_data.shape=(600, 7)

count=0
count1=0
count2=0
count3=0
count4=0
for i1 in range (0,530):
    pre1=abs(predict[i1,0]-x_test[i1,0])
    pre2=abs(predict[i1,0]-x_test[i1,1])
    pre3=abs(predict[i1,0]-x_test[i1,2])
    pre4=abs(predict[i1,0]-x_test[i1,3])
   # pre5=abs(predict[i1,0]-x_test[i1,4])
    #pre6=abs(predict[i1,0]-x_test[i1,5])
   # pre7=abs(predict[i1,0]-x_test[i1,6])
    min1=min(pre1,pre2,pre3,pre4)
    if (pre2<=pre1) & (pre2<=pre3) & (pre2<=pre4) :   
        count=count+1
        #pre=np.array([val_data[i1,0],val_data[i1,1],val_data[i1,2],val_data[i1,3],val_data[i1,4],val_data[i1,5],val_data[i1,6]])
        pre=x_test[i1,:]
        pre1=list(pre)#把数组转换为List
        k_num=pre1.count(x_test[i1,3])#在预测出最近节点的情况下，统计相同RSSI的个数，作为剩余的节点
        if k_num==1:
            count1+=1
        elif k_num==2:
            count2+=1
        elif k_num==3:
            count3+=1
        elif k_num==4:
            count4+=1
        if k_num==2:
            print("val_data2=",x_test[i1,:])
            print("predict2=",predict[i1,0])
        elif k_num==3:
            print("val_data3=",x_test[i1,:])
            print("predict3=",predict[i1,0])
        elif k_num==4:
            print("val_data4=",x_test[i1,:])
            print("predict4=",predict[i1,0])
    else:
        print("val_data=",x_test[i1,:])
        print("predict=",predict[i1,0])
        
    
Success_rate=count/530*100
count1_rate=count1/count*100
count2_rate=count2/count*100
count3_rate=count3/count*100
count4_rate=count4/count*100
print("成功率为："+str(Success_rate)+"%")
print("剩余1个节点："+str(count1_rate)+"%")
print("剩余2个节点："+str(count2_rate)+"%")
print("剩余3个节点："+str(count3_rate)+"%")
print("剩余4个节点："+str(count4_rate)+"%")
