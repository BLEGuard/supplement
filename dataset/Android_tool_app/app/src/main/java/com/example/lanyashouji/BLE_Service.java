package com.example.lanyashouji;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.UUID;

public class BLE_Service extends Service {

    private final static String TAG=BLE_Service.class.getSimpleName();

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final  static String ACTION_GATT_CONNECTED= "com.example.bt_ble.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.example.bt_ble.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED =
            "com.example.bt_ble.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE =
            "com.example.bt_ble.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA =
            "com.example.bt_ble.EXTRA_DATA";
    public final static String DEVICE_DOES_NOT_SUPPORT_UART =
            "com.example.bt_ble.DEVICE_DOES_NOT_SUPPORT_UART";


    public static final UUID TX_POWER_UUID = UUID.fromString("00001804-0000-1000-8000-00805f9b34fb");
    public static final UUID TX_POWER_LEVEL_UUID = UUID.fromString("00002a07-0000-1000-8000-00805f9b34fb");
    public static final UUID CCCD = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    public static final UUID FIRMWARE_REVISON_UUID = UUID.fromString("00002a26-0000-1000-8000-00805f9b34fb");
    public static final UUID DIS_UUID = UUID.fromString("0000180a-0000-1000-8000-00805f9b34fb");
    public static final UUID RX_SERVICE_UUID = UUID.fromString("0000fff0-0000-1000-8000-00805f9b34fb");
    //这个service的uuid代表了一系列characteristic的总服务，它包括了下列的服务

    //注意，这里的RX是对于CC254x来说的接收，也就是0xfff1
    public static final UUID RX_CHAR_UUID = UUID.fromString("0000fff1-0000-1000-8000-00805f9b34fb");
    //注意，这里的TX是对于CC254x来说的发送，也就是0xfff4，notify方式。
    public static final UUID TX_CHAR_UUID = UUID.fromString("0000fff4-0000-1000-8000-00805f9b34fb");


    private BluetoothManager mBluetoothManager;
    private BluetoothAdapter mBluetoothAdapter;
    private String mBluetoothDeviceAddress;
    private BluetoothGatt mBluetoothGatt;
    private int mConnectionState = STATE_DISCONNECTED;


    //自定义IBinder类
    public class LocalBinder extends Binder {
        BLE_Service getService() {
            return BLE_Service.this;
        }
    }
    //创建自定义IBinder类类的实例
    private final IBinder mBinder = new LocalBinder();

    @Nullable
    @Override    //返回这个实例，服务的绑定有关
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return mBinder;
    }
    @Override  //service被断开连接时回调该方法
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        close();
        return super.onUnbind(intent);
    }

    //连接成功，返回BluetoothGatt对象，BluetoothGattCallback用于传递一些连接状态及结果.通过BLE API的不同类型的一系列回调方法
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {//mBluetoothGatt定义在此
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {//当连接状态发生改变，回调方法
            String intentAction;

            if (newState == BluetoothProfile.STATE_CONNECTED) {//当蓝牙设备已经连接
                intentAction = ACTION_GATT_CONNECTED;
                mConnectionState = STATE_CONNECTED;
                broadcastUpdate(intentAction);
                Log.i(TAG, "Connected to GATT server.");
                // Attempts to discover services after successful connection.
                Log.i(TAG, "Attempting to start service discovery:" +
                        mBluetoothGatt.discoverServices());

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {//当设备无法连接
                intentAction = ACTION_GATT_DISCONNECTED;
                mConnectionState = STATE_DISCONNECTED;
                Log.i(TAG, "Disconnected from GATT server.");
                broadcastUpdate(intentAction);
            }
        }

        // 发现新服务端,discoverService方式的回调方法是onServiceDiscovered。返回动作：服务端可行
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.w(TAG, "mBluetoothGatt = " + mBluetoothGatt );

                broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        // 读写特性的回调方法,这个特性是和读写数据有关的，Tx和Rx，返回动作：属性可行，也就是可以通信传输数据
        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
            }
        }

        //属性改变的回调方法,如果对一个特性启用通知,notification的方式,当远程蓝牙设备特性发送变化，回调函数onCharacteristicChanged( ))被触发
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
        }

    };

    //发送广播，
    private void broadcastUpdate(final String action) {
        // TODO Auto-generated method stub
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    //发送广播，intent携带我们通讯传输要用的charicteristic中的数据，key为EXTRA_DATA，
    private void broadcastUpdate(final String action,
                                 final BluetoothGattCharacteristic characteristic) {
        // TODO Auto-generated method stub
        final Intent intent = new Intent(action);
        if (TX_CHAR_UUID.equals(characteristic.getUuid())) {    //蓝牙设备的发送对应的uuid
            intent.putExtra(EXTRA_DATA, characteristic.getValue());
        } else {

        }
        sendBroadcast(intent);
    }

    //初始化蓝牙适配器
    public boolean initBLE(){
        if (mBluetoothManager == null) {

            mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);//使用getSystemService（）返回BluetoothManager

            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to initialize BluetoothManager.");
                return false;
            }
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();//然后将其用于获取适配器的一个实例,获取适配器

        if (mBluetoothAdapter == null) {
            Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
            return false;
        }

        return true;
    }

    //先判断蓝牙现在的状态，然后再获取address硬件地址下的设备，再设备connectgatt
    public boolean connect(final String address) {
        if (mBluetoothAdapter == null || address == null) {//蓝牙适配器没有启动成功
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }

        // Previously connected device.  Try to reconnect.  先前连过的设备，试图重连
        if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }
        //获取存在address里的硬件地址的设备
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);

        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);//连接到GATT Server,returns a BluetoothGatt instance,Whether to directly connect to the remote device (false) or to automatically connect as soon as the remote device becomes available (true).
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = address;
        mConnectionState = STATE_CONNECTING;
        return true;
    }

    /**
     * Disconnects an existing connection or cancel a pending connection. The disconnection result
     * is reported asynchronously through the
     * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
     * callback.
     */
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
        // mBluetoothGatt.close();
    }

    /**
     * After using a given BLE device, the app must call this method to ensure resources are
     * released properly.
     */
    public void close() {
        if (mBluetoothGatt == null) {
            return;
        }
        Log.w(TAG, "mBluetoothGatt closed");
        mBluetoothDeviceAddress = null;
        mBluetoothGatt.close();
        mBluetoothGatt = null;
    }

    //读某个Characteristic
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }

    //如果设备主动给手机发信息，则可以通过notification的方式，这种方式不用手机去轮询地读设备上的数据
    //如果notificaiton方式对于某个Characteristic是enable的，那么当设备上的这个Characteristic改变时，手机上的onCharacteristicChanged() 回调就会被促发
    //http://blog.csdn.net/woshasanguo/article/details/41082395     https://my.oschina.net/tingzi/blog/215008
    public void enableTXNotification()//手机读取蓝牙设备发来的消息
    {

        if (mBluetoothGatt == null) {
            showMessage("mBluetoothGatt null" + mBluetoothGatt);
            broadcastUpdate(DEVICE_DOES_NOT_SUPPORT_UART);
            return;
        }
        //第一步   得到某个service的对象
        BluetoothGattService readandwriteService = mBluetoothGatt.getService(RX_SERVICE_UUID);//这个uuid的服务中包括了TX和RX

        if (readandwriteService == null) {
            showMessage("readService not found!");
            broadcastUpdate(DEVICE_DOES_NOT_SUPPORT_UART);
            return;
        }
        //第二步   获取此服务结点下的某个Characteristic对象
        BluetoothGattCharacteristic readChar = readandwriteService.getCharacteristic(TX_CHAR_UUID);//TX_CHAR_UUID的uuid对应蓝牙设备的发送

        if (readChar == null) {
            showMessage("readChar not found!");
            broadcastUpdate(DEVICE_DOES_NOT_SUPPORT_UART);
            return;
        }
        //第三步
        mBluetoothGatt.setCharacteristicNotification(readChar,true);

        BluetoothGattDescriptor descriptor = readChar.getDescriptor(CCCD);
        descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
        mBluetoothGatt.writeDescriptor(descriptor);

    }

    //http://blog.csdn.net/woshasanguo/article/details/41082395
    public void writeRXCharacteristic(byte[] value)//手机向蓝牙设备发送数据
    {
        //第一步  得到某个service的对象
        BluetoothGattService readandwriteService = mBluetoothGatt.getService(RX_SERVICE_UUID);
        showMessage("mBluetoothGatt null"+ mBluetoothGatt);

        if (readandwriteService == null) {
            showMessage("readandwriteService not found!");
            broadcastUpdate(DEVICE_DOES_NOT_SUPPORT_UART);
            return;
        }
        //第二步   获取此服务结点下的某个Characteristic对象
        BluetoothGattCharacteristic writeChar = readandwriteService.getCharacteristic(RX_CHAR_UUID);

        if (writeChar == null) {
            showMessage("Rx charateristic not found!");
            broadcastUpdate(DEVICE_DOES_NOT_SUPPORT_UART);
            return;
        }
        //第三步  设置要写的值   这里的values是一个byte数组
        //This function modifies the locally stored cached value of this characteristic. To send the value to the remote device, call writeCharacteristic(BluetoothGattCharacteristic) to send the value to the remote device.
        writeChar.setValue(value);
        boolean status = mBluetoothGatt.writeCharacteristic(writeChar);//写数据，status如果为true，表示写操作已经成功执行，BluetoothGattCallback抽象类的一个方法会被执行

        Log.d(TAG, "write writeChar - status=" + status);
    }



    private void showMessage(String string) {
        // TODO Auto-generated method stub
        Log.e(TAG,string);
    }
}
