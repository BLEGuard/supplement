package com.example.lanyashouji;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.core.app.NotificationCompat;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends Activity implements View.OnClickListener {

    public static final String TAG = "MainActivity";
    //	private static long SCAN_PERIOD = 1000; // 1 seconds
    private static long CYCLING_PERIOD = 300; // 这里用作采集中的延时，默认300，没有延时，不开启delay_flag不会有用
    // public static final int MAX_DB = 6;//最大的表格数

    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothDevice mdevice = null;
    private int My_Rssi;
    private BLE_Service mService = null;
    private boolean mScanning;
    private Handler mHandler, timeHandler, timeHandler1;
    InputMethodManager imm;

    private EditText num_edittext;
    private EditText loc_edittext;
    private EditText dx_edittext;
    private EditText dy_edittext;
    private EditText time_edittext;
    private EditText time_stop_et;
    private EditText time_cycling_et;
    private Button start_button;
    private Button scan_cancel_button;
    private Button show_draw_button;
    private Button show_dev_button;
    private Button time_button;
    private Button num_button;
    private Button loc_button;
    private Button dx_button;
    private Button dy_button;
    private Button time_stop_bn;
    private Button time_cycling_bn;
    private Button set_last_set_bn;
    private Button sqlite_button;
    private TextView textview1;
    private TextView count_textview;
    private StringBuilder sb;

    ArrayList<String> dev_list = new ArrayList<String>();
    ArrayAdapter<String> dev_array_Adapter;

    // private int scan_flag=0;
    // private final Timer timer = new Timer();
    // private TimerTask task;

    private TimerHelper mTimerHelper = null;// 自定义的抽象类，获取对象
    private TimerHelper mTimerHelper1 = null;// 自定义的抽象类，获取对象

    private Sqlite_Helper mSqliteHelper;// 获取Sqlite类的对象
    private SQLiteDatabase db;// 用于数据的存储和更改，调用它的方法
    private ContentValues values_add;// 用于存储数据的地方

    private String[] Address_list = new String[] { "D0:39:72:CC:C0:4B",      //所有实验蓝牙的地址库
            "D0:39:72:CC:85:E1", "D0:39:72:CC:B9:FC", "C4:BE:84:1A:CD:1E",
            "D0:39:72:CC:8A:BE", "D0:39:72:CC:E8:ED", "D0:39:72:CC:86:B9",
            "98:7B:F3:5C:21:1E", "B4:99:4C:5D:0D:04", "D0:39:72:CC:8A:B5",
            "D0:39:72:CC:BE:76", "D0:39:72:CC:BE:79", "D0:39:72:CC:89:C2",
            "D0:39:72:CC:8A:87", "D0:39:72:CD:04:93", "D0:39:72:CC:F3:64",
            "D0:39:72:CC:89:BF", "D0:39:72:CC:ED:11", "D0:39:72:CC:B9:E1",
            "C4:BE:84:1A:CD:1E", "D0:39:72:CC:80:92", "D0:39:72:CD:01:33",
            "D0:39:72:CC:8A:E1", "D0:39:72:CC:86:C3", "D0:39:72:CC:F3:0D",
            "D0:39:72:CD:03:DB", "D0:39:72:CC:E6:F1", "D0:39:72:CC:88:B1",
            "80:30:DC:DB:A6:24", "80:30:DC:E8:DF:D2", "38:D2:69:FD:9E:85",
            "38:D2:69:FD:9A:A9", "38:D2:69:FD:9E:CE", "38:D2:69:FD:A1:F3",
            "9C:1D:58:14:AB:A2", "9C:1D:58:14:95:C8",
            "3C:A3:08:01:52:7B", "C8:FD:19:4E:F9:F9", "3C:A3:08:01:47:B4",
            "3C:A3:08:01:4E:FD", "3C:A3:08:01:4E:87", "3C:A3:08:01:4A:DE",
            "3C:A3:08:01:52:3A", "3C:A3:08:01:45:59", "38:D2:69:FD:A5:55",
            "3C:A3:08:01:4E:FE", "3C:A3:08:01:55:10"};
    private ArrayList<String> Scaned_Address_list = new ArrayList<String>();// 用于自动保存我扫到的设备
    private static int device_update_number = 0;// 用于自动更新我扫到的设备数
    private int stop_flag = 0;
    private String[] data_namelist = new String[] { "data_loc1", "data_loc2",
            "data_loc3", "data_loc4", "data_loc5", "data_loc6", "data_loc7",
            "data_loc8", "data_loc9", "data_loc10", "data_loc11", "data_loc12"
            , "data_loc13", "data_loc14", "data_loc15"};
    private String[] db_namelist = new String[] { "RSSI1", "RSSI2", "RSSI3",
            "RSSI4", "RSSI5", "RSSI6", "RSSI7", "RSSI8", "RSSI9", "RSSI10",
            "RSSI11", "RSSI12", "RSSI13", "RSSI14", "RSSI15" };

    public static int db_name = 0;// 可以传递
    private int count = 0;// 采集stoptime的计数数，新程序表示采集的轮数
    private int times_count = 0;// 文本显示一共采集了多少个
    private int device_count = 0;// 用于判断采集到几个islock的设备，=device_number，停止扫描
    public static int device_number = 0;// 采集的设备数，可以传递
    private int nofind_count = 0;// 这个记录的是任何设备都没有找到
    private MediaPlayer mediaPlayer = new MediaPlayer();// 系统的音乐播放器

    private int read_count = 0;
    private String mAddress = null;
    private int mLocation = 0;
    private int mStopTimes = 20;// 停止采集的个数,新程序是每一次循环时需要采集的各节点的次数
    private int mCyclingTimes = 50;//循环的次数，默认50次

    private int loc_int = 1;
    private int dx_int = 10;
    private int dy_int = 10;
    private int mSCANtime = 0;
    private ArrayList<Integer> save_last_set = new ArrayList<Integer>();

    //蓝牙权限的常量声明，没有看懂
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION = 1;
    //模糊定位权限，安卓6以上需要
    private int count_r1=0;
    private int count_r2=0;
    private int count_r3=0;
    private int count_r4=0;
    private int count_r5=0;
    private int count_r6=0;
    private int count_r7=0;
    private int count_r8=0;
    private int count_r9=0;
    private int count_r10=0;
    private int count_r11=0;
    private int count_r12=0;
    private int count_r13=0;
    private int count_r14=0;
    private int count_r15=0;
    private int not_add_to_db_flag=0;
    private int delay_flag=0;//延时计时器开始标志，默认0不延时连续采集（一开一关），置1延时一开一关
    public static long Experiment_begin_ms_time;//实验开始的时间
    private long one_cycling_ms_time;//打开开关，连续不停地扫描，都完成次数要求后，停止开关的时间
    private ArrayList<Long> EveryCycling_ms_list = new ArrayList<Long>();// 用于保存每次循环花费的毫秒数
    private long last_one_cycling_ms_time=0;
    private long delay_begin_mstime=0;
    private long delay_end_mstime=0;
    private long cha_delay=0;


    //数据库保存位置的类的声明
    DatabaseContext dbContext = new DatabaseContext(this);
    //SdCardDBHelper dbHelper = new SdCardDBHelper(dbContext);

    // private ListView dev_listview;

    /************* 界面显示不出来，一般是oncreat有错，仔细看看 **************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //判断是否有权限
        // Here, thisActivity is the current activity
//		if (ContextCompat.checkSelfPermission(MainActivity.this,
//		                Manifest.permission.ACCESS_COARSE_LOCATION)
//		        != PackageManager.PERMISSION_GRANTED)
//
//		//请求权限
//		{ActivityCompat.requestPermissions(MainActivity.this,
//		                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
//		                MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION);
//		//判断是否需要 向用户解释，为什么要申请该权限
//		ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
//		                Manifest.permission.READ_CONTACTS);}
//		//权限申请结果
//		onRequestPermissionsResult(int requestCode,
//		        String permissions[], int[] grantResults);


        setContentView(R.layout.activity_main);

        mHandler = new Handler();//获取各种对象的实例
        sb = new StringBuilder();

        final BluetoothManager mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);//使用getSystemService（）返回BluetoothManager
        mBluetoothAdapter = mBluetoothManager.getAdapter();//然后将其用于获取适配器的一个实例,获取适配器

        //mSqliteHelper = new Sqlite_Activity(this, "RSSI.db", null, 1);//获取实例
        //db = mSqliteHelper.getWritableDatabase();//创建表格,会返回一个SQLiteDatabase对象,用于增减更新数据

        get_contentView();//先获取textview1，因为我们的show方法用了这个id
        creat_db();
        set_listener();
        service_init();//绑定服务，动态注册，相当于启动服务，多了访问者与服务的连接
        start_button.setEnabled(false);//避免程序崩溃，必须输入设备后才能使用
        show_dev_button.setEnabled(false);//避免程序崩溃，必须输入设备后才能使用
        set_last_set_bn.setEnabled(false);  //为了避免不小心打开延时，所以暂时不开，等我保存和再设弄好了再开
        mSharedPreferences_get_LastSet();//获得上次采集时我的设置

        show("Default collect times : "+ mStopTimes+"|"+"Default delay:"+CYCLING_PERIOD
                +"|"+"Default cycling times:"+mCyclingTimes+"|"+"Delay Timer is not active"
                +"|"+"Default need address in 1-5 sharepreferences");
//		show("默认采集的点的个数5，需要程序修改");
//		show("这手机设置应用设置权限，蓝牙，写txt，需要手动勾选权限");
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);//收起虚拟键盘
        //show("everything is ok");//show这个方法必须在get_contentView方法后面，因为要先获取textview1的id

        show_last_set();//换到在onresume中来显示，dialog形态只启动onresume，和onpause

//		mTimerHelper = new TimerHelper() {//自定义的抽象类，这里相当于下面task的功能,采集时间
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				Message message = new Message();
//		        message.what = 1;
//		        timeHandler.sendMessage(message);
//				//Log.e("1000", "相当于task的执行");
//			}
//		};
//
        mTimerHelper1 = new TimerHelper() {//自定义的抽象类，这里相当于下面task的功能，判断采集个数
            @Override
            public void run() {
                // TODO Auto-generated method stub
                Message message = new Message();
                message.arg1 = 1;
                timeHandler1.sendMessage(message);
                //Log.e("300", "相当于task的执行");
            }
        };
        //定时器到时间需要完成的任务
		/*task = new TimerTask() {  //task获取实例，必须放在oncreat之类中
		    @Override
		    public void run() {
		        // TODO Auto-generated method stub
		        Message message = new Message();
		        message.what = 1;
		        timeHandler.sendMessage(message);
		    }
		};*/

//		timeHandler = new Handler(){//1s计时器处理函数
//			@Override
//			public void handleMessage(Message msg){
//				switch (msg.what) {
//				case 1:
//					//scanLeDevice(true);
//					mBluetoothAdapter.startLeScan(mLeScanCallback);
//					mTimerHelper1.start((SCAN_PERIOD-100),(SCAN_PERIOD-100));//相当于下面的功能，延迟和周期，不延迟直接开始，周期SCAN_PERIOD
//					Log.e("1000", "1s Handler");
//					break;
//				default:
//					break;
//				}
//				super.handleMessage(msg);
//			}
//		};
//
        timeHandler1 = new Handler(){//300ms计时器处理函数
            @Override
            public void handleMessage(Message msg){
                switch (msg.arg1) {
                    case 1:
//					device_update_number = Scaned_Address_list.size();
//					stop_flag = 1;
                        mTimerHelper1.stop();//延迟CYCLING_PERIOD之后，立马关掉自己
                        Log.e("Timer", "TimerHandle1 is execute");
                        delay_end_mstime=get_ms_currentTime();
                        cha_delay=delay_end_mstime-delay_begin_mstime;
                        save_ms_txt("第"+(count+1)+"次循环之后延迟："+cha_delay+"ms");
                        cycling_and_count();   //定时器里面执行循环判断和计数程序,里边有count++
//					Log.e("500", "stop_flag ="+stop_flag);
//					Stop_Scan();
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };

        if(savedInstanceState != null){//取回被销毁之前的数据
            String tempdata = savedInstanceState.getString("data_key");
            Log.d(TAG, tempdata);
        }
    }

    @Override
    // 用于保存这个活动被销毁之前的数据
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String tempData = "something was saved before this activity was destroyed";
        outState.putString("data_key", tempData);
    }

    // 定义一个ServiceConnection对象
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        // 访问者与service连接成功，将调用ServiceConnection对象的onServiceConnected的回调方法
        public void onServiceConnected(ComponentName className,
                                       IBinder rawBinder) {
            mService = ((BLE_Service.LocalBinder) rawBinder).getService();// 获取我们编写的服务？看看怎么用的，
            Log.d(TAG, "onServiceConnected mService= " + mService);
            if (!mService.initBLE()) {// 调用我们服务中的方法，初始化BLE的方法，返回boolean值，true成功获得本地蓝牙适配器
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
        }

        // 访问者与service连接失败，将调用ServiceConnection对象的onServiceDisconnected的回调方法
        public void onServiceDisconnected(ComponentName classname) {
            // // mService.disconnect(mDevice);
            mService = null;
        }
    };

    // 服务初始化，我们采用的是绑定服务，这样可以传输数据 *******绑定服务*******
    private void service_init() {// public boolean bindService (Intent service,
        // ServiceConnection conn, int flags)
        Intent bindIntent = new Intent(MainActivity.this, BLE_Service.class);
        bindService(bindIntent, mServiceConnection, Context.BIND_AUTO_CREATE);// 绑定指定的service
        // ,
        // mServiceConnection是ServiceConnection对象，后面的是标志key
        // 动态注册广播能接收的动作 //360页，第一行代码,BIND_AUTO_CREATE表示活动和服务绑定后自动创建服务
        /*
         * mActivityReceive = new ActivityReceive();
         * registerReceiver(mActivityReceive,
         * intentfilter());//intentfilter()返回IntentFilter值，符合方法的参数
         */}

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.start:
//			cycling_flag=0;//每次开始默认一直循环轮数，停止实验会置1，只能在开始实验置0
//			mTimerHelper.start(0, SCAN_PERIOD);// 相当于下面的功能，延迟和周期，不延迟直接开始，周期SCAN_PERIOD
//			mTimerHelper1.start((SCAN_PERIOD-100), (SCAN_PERIOD-100));// 相当于下面的功能，延迟和周期，不延迟直接开始，周期SCAN_PERIOD
                mBluetoothAdapter.startLeScan(mLeScanCallback);
                Log.e("start","StartLeScan"+ get_currentTime());
                show("Start collect RSSI's value " + "|" + get_currentTime());
                Experiment_begin_ms_time=get_ms_currentTime();  //记录开始的毫秒时间
                save_ms_txt("\r\n");
                save_ms_txt("开始实验的毫秒时间："+String.valueOf(Experiment_begin_ms_time)+"|"+get_currentTime());
                Log.e("start_ms", "Present mstime:"+get_ms_currentTime());
                mSharedPreferences_save_LastSet();// 保存我上次所有的设置
                // timer.schedule(task, SCAN_PERIOD,
                // SCAN_PERIOD);//SCAN_PERIOD后开始，SCAN_PERIOD秒为一个周期
                // scanLeDevice(true);
                // Toast.makeText(this, "button1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.stop:
//			mTimerHelper.stop();// 相当于下面的功能，停止计时器，不过可以重新开始
//			mTimerHelper1.stop();// 相当于下面的功能，停止计时器，不过可以重新开始
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                // mediaPlayer.pause();//暂停
                // mediaPlayer.reset();//从新加载
                // count = 0;//这个是stoptime的计数次数
                show("Stop collect RSSI's value" + "|" + get_currentTime());
                // timer.cancel();//取消之后没有办法再重新开始了
                // scanLeDevice(false);
                // if(mScanning == false)
                // scanLeDevice(true);//false状态下，按键显示scan，点击开始扫描
                // else scanLeDevice(false);//否则显示cancel，点击关闭
                // Toast.makeText(this, "button2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.draw:
                Intent intent = new Intent(MainActivity.this, Draw_Activity.class);
                startActivity(intent);
                // Toast.makeText(this, "button3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.show_dev:
                Intent dev_intent = new Intent(MainActivity.this,
                        Device_Activity.class);
                start_button.setEnabled(true);
                // String str1 = "test";
                // sendMsg.putExtra("1", str1);
                // sendMsg.putStringArrayListExtra("dev_key",
                // dev_list);//这段代码对应开始扫描之后显示设备，传递List给下一个活动
                startActivity(dev_intent);
                // dev_array_Adapter = new ArrayAdapter<String>(this,
                // android.R.layout.simple_list_item_1, dev_list);
                // //这个加载的布局的是SDK中提供的
                // dev_listview.setAdapter(dev_array_Adapter);
                // creat_db();
                // Toast.makeText(this, "button3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.set_last_set:
                Set_last_set();// 设备对应的应该放在那个表格，已经存在文件里了，不用再重新读取
                show("Last Set was setted" + "|" + get_currentTime());
                if (loc_int > device_number) {
                    start_button.setEnabled(false);
                    show("Location must less or equal than Device number");
                } else
                    start_button.setEnabled(true);
                show_dev_button.setEnabled(true);

                // imm.hideSoftInputFromWindow(time_stop_et.getWindowToken(), 0);
                // //收起键盘
                // Toast.makeText(this, "button3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sqlite_bn:
                Intent intent1 = new Intent(MainActivity.this,
                        Sqlite_Activity.class);
                startActivity(intent1);
                // imm.hideSoftInputFromWindow(time_stop_et.getWindowToken(), 0);
                // //收起键盘
                // Toast.makeText(this, "button3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.change_time://现在是修改延时时间的按键
                delay_flag=1;//这里就打开延时定时器，之后的采集会有延时
                String str = time_edittext.getText().toString();
                CYCLING_PERIOD = Long.parseLong(str);//改成了循环的时间间隔，原先是SCAN_PERIOD
                show("Activate the delay Timer:"+CYCLING_PERIOD+"ms"+"|"+ "delay_flag: "+delay_flag);
                mSCANtime = Integer.parseInt(str);//变成int之后才能存到SharedPreferences
//			show("Time changed succeed, time: " + str + "ms");
                imm.hideSoftInputFromWindow(time_edittext.getWindowToken(), 0); // 收起键盘
                // Toast.makeText(this, "button3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.change_num:
                String str1 = num_edittext.getText().toString();
                device_number = Integer.parseInt(str1);// 将获得的string转换成int
                if (device_number > 0) {
                    // start_button.setEnabled(true);
                    show_dev_button.setEnabled(true);
                    // start_button.setOnClickListener(this);
                }
                show("device's number changed succeed, number: " + str1);
                imm.hideSoftInputFromWindow(num_edittext.getWindowToken(), 0);// 收起键盘
                // Toast.makeText(this, "button3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.change_loc:
                // String str2 = loc_edittext.getText().toString();
                String loc_str = loc_edittext.getText().toString();
                loc_int = Integer.parseInt(loc_str);
                if (loc_int >= device_number)
                    start_button.setEnabled(true);
                show("Face device changed succeed : " + loc_str);
                imm.hideSoftInputFromWindow(loc_edittext.getWindowToken(), 0); // 收起键盘
                // Toast.makeText(this, "button3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.change_dx:
                String str3 = dx_edittext.getText().toString();
                dx_int = Integer.parseInt(str3);
                show("main dx changed succeed : " + str3);
                imm.hideSoftInputFromWindow(dx_edittext.getWindowToken(), 0); // 收起键盘
                // Toast.makeText(this, "button3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.change_dy:
                String str4 = dy_edittext.getText().toString();
                dy_int = Integer.parseInt(str4);
                // mNotification();
                show("main dy changed succeed : " + str4);
                imm.hideSoftInputFromWindow(dy_edittext.getWindowToken(), 0); // 收起键盘
                // Toast.makeText(this, "button3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.stop_time_bn://现在是修改每一次循环各点需要采集次数的按键
                String str5 = time_stop_et.getText().toString();
                mStopTimes = Integer.parseInt(str5);
                show(mStopTimes + "  times will start next cycling" + "|" + get_currentTime());
                imm.hideSoftInputFromWindow(time_stop_et.getWindowToken(), 0); // 收起键盘
                // Toast.makeText(this, "button3", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cycling_time_bn:
                String str6 = time_cycling_et.getText().toString();
                mCyclingTimes = Integer.parseInt(str6);
                show(mCyclingTimes + "  cyclings will be End" + "|" + get_currentTime());
                imm.hideSoftInputFromWindow(time_stop_et.getWindowToken(), 0); // 收起键盘
                break;
            default:
                break;
        }
    }

    /**************** 这个扫描函数没有用到,换成mBluetoothAdapter.startLeScan(mLeScanCallback); *************************/
    // 扫描设备和显示，10s扫描后停止，扫描函数
    /*
     * 因为扫描BLE设备是电源密集型操作，浪费电量，因此要保证以下原则：
     *
     * 1）扫描到需要的设备后，马上停止扫描；
     *
     * 2）给扫描一个时间限制
     */
    /*
     * private void scanLeDevice(final boolean enable) { if (enable) { // Stops
     * scanning after a pre-defined scan period. mHandler.postDelayed(new
     * Runnable() { //handle的使用
     *
     * @Override public void run() { mScanning = false;
     * mBluetoothAdapter.stopLeScan(mLeScanCallback);//停止10s后才开始扫描扫描
     *
     * scan_cancel_button.setText(R.string.scan);
     * //因为没有扫描，所以按钮要设置显示为scan，到底需要用final？
     *
     * } }, SCAN_PERIOD);
     *
     * mScanning = true; //状态表示，正在扫描
     * mBluetoothAdapter.startLeScan(mLeScanCallback); //开始扫描
     * scan_cancel_button.setText(R.string.cancel);//正在扫描，所以按钮要显示cancel
     *
     * count++;//在destroy中清了0 count_textview.setText("采集个数为： "+count);
     * //单单写个count，会报错，必须写成“”+count才可以显示 } else { mScanning = false;
     * //enable设置为false，不扫描，状态显示false
     * mBluetoothAdapter.stopLeScan(mLeScanCallback);//停止扫描
     * scan_cancel_button.setText(R.string.scan);//按钮设置显示scan }
     *
     * }
     */
    // 扫描时的回调的函数，mLeScanCallback()，回调函数LeScanCallback会自动产生device和rssi
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi,
                             byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {// 这个扫描，一次扫出来几个，run执行了几遍
                            mdevice = device;
                            Log.e("scaned", "Scanning");
                            if (islock()) {
                                My_Rssi = rssi;
                                // Add_to_DB();
                                // String str = device.getAddress()+"|"+rssi;
                                // textview1.setText(str);
                                // show("存储成功");
                                update_address();
                                // device_count++;//假如找到2个，我就要2个，下面语句停止扫描
                                // /count_textview.setText(device_count+"");
                                Log.e("save", "Save OK");

                            }
                        }

                    });

                }
            });
        }
    };

    //	private void Stop_Scan() {
//		/***** 设备数一定要设对，不然扫描不进行 *******/
//		if (stop_flag == 1) {
//			stop_flag = 0;
//			Log.e("stopflag", "stop执行");
//			if (device_update_number == 0) {// 防止没有扫到任何的设备，还在计数
//				mBluetoothAdapter.stopLeScan(mLeScanCallback);
//				// mTimerHelper.stop();//相当于下面的功能，停止计时器，不过可以重新开始
//				nofind_count++;
//				device_count = 0;
//				Scaned_Address_list.clear();
//				if (nofind_count == 10) {// 连续10次没有扫到设备才会停止
//					mTimerHelper.stop();// 相当于下面的功能，停止计时器，不过可以重新开始
//					nofind_count = 0;
//					show("10 times Never find any device, had been stopped "
//							+ "|" + get_currentTime());
//					// initMediaPlayer();//播放音乐提醒出现问题
//				}
//				Log.e("stop_count", "emerge");
//			} else if (device_count == device_update_number) {// 需要我们在采集时设置，方便停止
//				device_count = 0;
//				device_update_number = 0;
//				Scaned_Address_list.clear();
//				/**** 这句停止很重要 *****/
//				mBluetoothAdapter.stopLeScan(mLeScanCallback);
//				times_count++;// 采集次数统计，在destroy中清了0
//				count++;//
//				count_textview.setText("采集个数为： " + times_count); // 单单写个count，会报错，必须写成“”+count才可以显示
//				nofind_count = 0;// 避免中间有一次没有扫到设备，从而累加停止
//				if (count >= mStopTimes) {
//					mTimerHelper.stop();// 相当于下面的功能，停止计时器，不过可以重新开始
//					mBluetoothAdapter.stopLeScan(mLeScanCallback);
//					show("Collect was done , times: " + count + "|"
//							+ get_currentTime());
//					count = 0;// 采集多少个停止的计数
//					mNotification();
//
//				}
//			}
//		}
//	}
    /*
     * 这个停止扫描程序一旦出现，意味着我采集够了我需要的R值，每次实验只会出现一次
     * 对比上面的原始的stop，原始的是每次定时时间到了就停止，采多少次，stop出现多少次
     * */
    private void new_stop_scan(){		//位于存进表格和分组计数的程序后面，此时都已经记录完成
        Log.e("stopflag", "stop执行");
        if(Scaned_Address_list.size()==0){
            nofind_count++;
            if (nofind_count == 10) {// 连续10次没有扫到设备才会停止
                nofind_count = 0;
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                show("10 times continuous Never find any device, had been stopped "
                        + "|" + get_currentTime());
                // initMediaPlayer();//播放音乐提醒出现问题
            }
            Log.e("stop_error", "10 times continuous not find any devices");
        }else if(is_every_node_done()){  //默认值采集5个，如果需要采集6个，需要添加；所有的节点都采集够了，才会进入到这个停止程序
            count_r1=0;
            count_r2=0;  //这些count_r1只是代表了Scaned_Address_list表格里的顺序，和外界节点位置没有任何关系，谁先进谁就是r1，依次类推
            count_r3=0;
            count_r4=0;
            count_r5=0;
            count_r6=0;
            count_r7=0;
            count_r8=0;
            count_r9=0;
            count_r10=0;
            count_r11=0;
            count_r12=0;
            count_r13=0;
            count_r14=0;
            count_r15=0;
            nofind_count = 0;// 避免中间有一次没有扫到设备，从而累加停止
            Scaned_Address_list.clear();//停止实验，所以清空记录个数的表格
            mBluetoothAdapter.stopLeScan(mLeScanCallback);

            /*记录每次循环花费的毫秒时间，存到txt，存到ArrayList里*/
            one_cycling_ms_time=get_ms_currentTime(); //每次循环所有点都采够了的时候，记录一下时间
            if(count==0){
                long cha_one_cycling=one_cycling_ms_time-Experiment_begin_ms_time;
                String cha_str = String.valueOf(cha_one_cycling);
                save_ms_txt("第1次循环采集花费时间："+cha_str+"ms");//花费毫秒时间记到TXT
                EveryCycling_ms_list.add(cha_one_cycling);//记录每次循环花费的毫秒时间，实验结束记得清空List
            }else{
                long cha_one_cycling1=one_cycling_ms_time-last_one_cycling_ms_time-cha_delay;
                String cha_str1 = String.valueOf(cha_one_cycling1);
                save_ms_txt("第"+(count+1)+"次循环采集花费时间："+cha_str1+"ms");//花费毫秒时间记到TXT
                EveryCycling_ms_list.add(cha_one_cycling1);//记录每次循环花费的毫秒时间，实验结束记得清空List
            }
            last_one_cycling_ms_time=one_cycling_ms_time;
            Log.e("One cycling", "One cycling mstime:"+one_cycling_ms_time);

            if(delay_flag==0){//默认是0，不延时
                cycling_and_count();//这里面才有count++的语句
            }
//			show("Collect was done , times: " + mStopTimes + "|"	+ get_currentTime());
//			mNotification();//通知栏提醒，震动
//			Log.e("Stop_scan", "Stop this experiment");

            if(delay_flag==1){//点击Done就会变成1，开启延时
                mTimerHelper1.start(CYCLING_PERIOD, CYCLING_PERIOD);// 定时器延迟和周期，延迟再开始，周期CYCLING_PERIOD
                delay_begin_mstime=get_ms_currentTime();
            }
        }
    }

    private boolean is_every_node_done(){
        boolean flag=false;
        switch (Scaned_Address_list.size()) {  //谁先进来表格，谁就是r1，再进来就是r2
            case 1:
                flag=count_r1>=mStopTimes;
                break;
            case 2:
                flag=count_r1>=mStopTimes&count_r2>=mStopTimes;
                break;
            case 3:
                flag=count_r1>=mStopTimes&count_r2>=mStopTimes&count_r3>=mStopTimes;
                break;
            case 4:
                flag=count_r1>=mStopTimes&count_r2>=mStopTimes&count_r3>=mStopTimes&count_r4>=mStopTimes;
                break;
            case 5:
                flag=count_r1>=mStopTimes&count_r2>=mStopTimes&count_r3>=mStopTimes&count_r4>=mStopTimes&count_r5>=mStopTimes;
                break;
            case 6:
                flag=count_r1>=mStopTimes&count_r2>=mStopTimes&count_r3>=mStopTimes&count_r4>=mStopTimes
                        &count_r5>=mStopTimes&count_r6>=mStopTimes;
                break;
            case 7:
                flag=count_r1>=mStopTimes&count_r2>=mStopTimes&count_r3>=mStopTimes&count_r4>=mStopTimes
                        &count_r5>=mStopTimes&count_r6>=mStopTimes&count_r7>=mStopTimes;
                break;
            case 8:
                flag=count_r1>=mStopTimes&count_r2>=mStopTimes&count_r3>=mStopTimes&count_r4>=mStopTimes
                        &count_r5>=mStopTimes&count_r6>=mStopTimes&count_r7>=mStopTimes&count_r8>=mStopTimes;
                break;
            case 9:
                flag=count_r1>=mStopTimes&count_r2>=mStopTimes&count_r3>=mStopTimes&count_r4>=mStopTimes
                        &count_r5>=mStopTimes&count_r6>=mStopTimes&count_r7>=mStopTimes&count_r8>=mStopTimes&count_r9>=mStopTimes;
                break;
            case 10:
                flag=count_r1>=mStopTimes&count_r2>=mStopTimes&count_r3>=mStopTimes&count_r4>=mStopTimes
                        &count_r5>=mStopTimes&count_r6>=mStopTimes&count_r7>=mStopTimes&count_r8>=mStopTimes&count_r9>=mStopTimes
                        &count_r10>=mStopTimes;
                break;
            case 11:
                flag=count_r1>=mStopTimes&count_r2>=mStopTimes&count_r3>=mStopTimes&count_r4>=mStopTimes
                        &count_r5>=mStopTimes&count_r6>=mStopTimes&count_r7>=mStopTimes&count_r8>=mStopTimes&count_r9>=mStopTimes
                        &count_r10>=mStopTimes&count_r11>=mStopTimes;
                break;
            case 12:
                flag=count_r1>=mStopTimes&count_r2>=mStopTimes&count_r3>=mStopTimes&count_r4>=mStopTimes
                        &count_r5>=mStopTimes&count_r6>=mStopTimes&count_r7>=mStopTimes&count_r8>=mStopTimes&count_r9>=mStopTimes
                        &count_r10>=mStopTimes&count_r11>=mStopTimes&count_r12>=mStopTimes;
                break;
            case 13:
                flag=count_r1>=mStopTimes&count_r2>=mStopTimes&count_r3>=mStopTimes&count_r4>=mStopTimes
                        &count_r5>=mStopTimes&count_r6>=mStopTimes&count_r7>=mStopTimes&count_r8>=mStopTimes&count_r9>=mStopTimes
                        &count_r10>=mStopTimes&count_r11>=mStopTimes&count_r12>=mStopTimes&count_r13>=mStopTimes;
                break;
            case 14:
                flag=count_r1>=mStopTimes&count_r2>=mStopTimes&count_r3>=mStopTimes&count_r4>=mStopTimes
                        &count_r5>=mStopTimes&count_r6>=mStopTimes&count_r7>=mStopTimes&count_r8>=mStopTimes&count_r9>=mStopTimes
                        &count_r10>=mStopTimes&count_r11>=mStopTimes&count_r12>=mStopTimes&count_r13>=mStopTimes&count_r14>=mStopTimes;
                break;
            case 15:
                flag=count_r1>=mStopTimes&count_r2>=mStopTimes&count_r3>=mStopTimes&count_r4>=mStopTimes
                        &count_r5>=mStopTimes&count_r6>=mStopTimes&count_r7>=mStopTimes&count_r8>=mStopTimes&count_r9>=mStopTimes
                        &count_r10>=mStopTimes&count_r11>=mStopTimes&count_r12>=mStopTimes&count_r13>=mStopTimes&count_r14>=mStopTimes
                        &count_r15>=mStopTimes;
                break;

            default:
                break;
        }
        /*Xiaomi 手机特有程序,每次只扫到1个，没有计数*/
//		if(Scaned_Address_list.size()==device_number /*&count_r1==0&count_r2==0&count_r3==0&count_r4==0&count_r5==0*/){
//			flag=true;
//			show("Xiaomi is collect over");
//		}
        return flag;
    }

    private void cycling_and_count(){
        if(count>=(mCyclingTimes-1)){   //记录的循环的次数，循环够了，就停止扫描，不够继续扫描，默认总停止的时候关闭延时定时器,count=0的时候就相当于循环了一次，所以减1
            count=0;//循环次数清0
            delay_flag=0;//总停止延时标志置0，默认不开延时，需要延时自己手动再设
//			show("Delay_flag:"+delay_flag+"|"+"need to reDone the delay");
            mBluetoothAdapter.stopLeScan(mLeScanCallback);  //总停止程序
            show(mCyclingTimes+" Cycling is done"+ "|"	+ get_currentTime()+"|"+"Delay_flag:"+delay_flag+"|"+"need to reDone the delay");
            mNotification();//通知栏提醒，震动

            /*记录实验结束的毫秒时间，实验总共花费的时间*/
            long Experiment_end_ms_time=get_ms_currentTime();
            long cha_whole=Experiment_end_ms_time-Experiment_begin_ms_time;
            save_ms_txt("实验结束毫秒时间："+Experiment_end_ms_time+"|"+get_currentTime());
            save_ms_txt("实验总共花费时间（包括延迟）："+cha_whole+"ms");
            long cycling_alltime=0;
            for(int i=0;i<EveryCycling_ms_list.size();i++){
                cycling_alltime=cycling_alltime+EveryCycling_ms_list.get(i);
            }
            save_ms_txt("实验总共花费时间（没有延迟）："+cycling_alltime+"ms");
            EveryCycling_ms_list.clear();
            cha_delay=0;
        }else{
            mBluetoothAdapter.startLeScan(mLeScanCallback);  //循环开始程序
            count++;//循环次数
//			show("Present Collect Cycling: " + count + "|"	+ get_currentTime());
            Log.e("Cycling start", "Present cycling:"+count+ "|"	+ get_currentTime());
        }
    }

    private void update_address() {//每次蓝牙采到一个值，update一下，保存，计数，显示一下
        // TODO Auto-generated method stub
        String temp_dev_address = mdevice.getAddress();
//		if (isExist(temp_dev_address)) {// 自动添加地址，采集到的保存，有几个自动会停止
//			Scaned_Address_list.add(temp_dev_address);
        if(isNeed(temp_dev_address)){	//是我们最开始设置的硬件地址，我们才添加List，并且计数，不是的话不添加不计数不存表
            group_and_count(temp_dev_address);
            Log.e("Scan_list", "Scaned_Address_list number:"+Scaned_Address_list.size());
            if(not_add_to_db_flag==0){
                Add_to_DB();
            }else if(not_add_to_db_flag==1){//每次只采到一个设备，超过了规定个数，不在记录，清除标志，下次默认还是要记录，除非又超了
                not_add_to_db_flag=0;
            }
//			device_count++;
            db_name = 0;
            new_stop_scan();
            count_textview.setText("采集个数为： " + count_r1+"|"+ count_r2+"|"+ count_r3+"|"
                    + count_r4+"|"+ count_r5+"|"+count_r6+"|"+ count_r7+"|"+get_currentTime()
                    +"|"+"Cycling:"+(count+1)); // 单单写个count，会报错，必须写成“”+count才可以显示
        }
//		}
    }


    private void group_and_count(String str) {
        boolean flag = false;
        int count_flag=16;//默认一个值，保证不会执行
        if(Scaned_Address_list.size()==0){
            Scaned_Address_list.add(str);
        }else{
            for (int i = 0; i < Scaned_Address_list.size(); i++) {
                flag = str.equals(Scaned_Address_list.get(i));
                if (flag) {
                    count_flag=i;	//存在，就不List添加，就要计数，所以赋个计数标志
                    break;
                }
            }
            if(!flag){
                Scaned_Address_list.add(str);
                Log.e("Scan_add", "Add a new noExist address");
                count_flag=16;//往list里边添加，就不计数，所以给8,添加不计数，紧接着就是存到表格
            }
            switch (count_flag) {//有重复，就计数，不停的在计数，只有实验做完了，才会清0；超过实验要求，就不再存表格，但是还在计数
                case 0:
                    count_r1++;
                    isEnough(count_r1);
                    flag=false;
                    break;
                case 1:
                    count_r2++;
                    isEnough(count_r2);
                    flag=false;
                    break;
                case 2:
                    count_r3++;
                    isEnough(count_r3);
                    flag=false;
                    break;
                case 3:
                    count_r4++;
                    isEnough(count_r4);
                    flag=false;
                    break;
                case 4:
                    count_r5++;
                    isEnough(count_r5);
                    flag=false;
                    break;
                case 5:
                    count_r6++;
                    isEnough(count_r6);
                    flag=false;
                    break;
                case 6:
                    count_r7++;
                    isEnough(count_r7);
                    flag=false;
                    break;
                case 7:
                    count_r8++;
                    isEnough(count_r8);
                    flag=false;
                    break;
                case 8:
                    count_r9++;
                    isEnough(count_r9);
                    flag=false;
                    break;
                case 9:
                    count_r10++;
                    isEnough(count_r10);
                    flag=false;
                    break;
                case 10:
                    count_r11++;
                    isEnough(count_r11);
                    flag=false;
                    break;
                case 11:
                    count_r12++;
                    isEnough(count_r12);
                    flag=false;
                    break;
                case 12:
                    count_r13++;
                    isEnough(count_r13);
                    flag=false;
                    break;
                case 13:
                    count_r14++;
                    isEnough(count_r14);
                    flag=false;
                    break;
                case 14:
                    count_r15++;
                    isEnough(count_r15);
                    flag=false;
                    break;

                default:
                    break;
            }
        }
    }

    /*每次都是采集一个值，这个值是不是已经采够了，够了的话就不再保存，不够继续保存*/
    private void isEnough(int count){
        if(count>=mStopTimes){//存到List里边其实就算一次了，之后的计数都是比mStoptime少1的,但是到了mStopTimes这次，是不再往表格里面记录的
            not_add_to_db_flag=1;
            Log.e("Enough", "Someone is enough,not add DB:"+count);
        }
    }


    // 动态注册<intent-filter>，这些动作是此activity的broadcastreceiver能接收到的广播
    private static IntentFilter intentfilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BLE_Service.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BLE_Service.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BLE_Service.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BLE_Service.ACTION_DATA_AVAILABLE);
        intentFilter.addAction(BLE_Service.DEVICE_DOES_NOT_SUPPORT_UART);
        return intentFilter; // 返回值为IntentFilter值
    }

    public void creat_db() {// 创建对应于蓝牙设备的表格数
        // int version;
        // String str = num_edittext.getText().toString();
        // version = Integer.parseInt(str);//将获得的string转换成int

        // if(version>1){
//		mSqliteHelper = new Sqlite_Helper(this, "RSSI.db", null, 1);// 获取实例，存到根目录
        mSqliteHelper = new Sqlite_Helper(this, "RSSI.db", null, 1);// 获取实例,用自己写的dbContext类，数据库存到sd卡中
        db = mSqliteHelper.getWritableDatabase();// 创建表格,会返回一个SQLiteDatabase对象,用于增减更新数据
        values_add = new ContentValues();// 获取实例
        show("now database's number : 10" + " |save msg in OperationMsg.txt|");
        // }
    }

    public void Add_to_DB() {// 向表格中添加数据,要想成功运行，必须有db =
        // mSqliteHelper.getWritableDatabase();不然报错
        // int which_db;
        // String str = loc_edittext.getText().toString();
        // which_db = Integer.parseInt(str);//将获得的string转换成int

        values_add.put("device", get_localdevice());
        values_add.put("curtime", get_currentTime());
        values_add.put("dx", dx_int + "dm");// 单个测试，有数据，测5个，没有数据
        values_add.put("dy", dy_int + "dm");
        // values_add.put("location", loc_edittext.getText().toString());
        // values_add.put("dx", dx_edittext.getText().toString()+"dm");
        // values_add.put("dy", dy_edittext.getText().toString()+"dm");
        values_add.put("name", mdevice.getName());
        values_add.put("address", mdevice.getAddress());
        values_add.put("RSSI", My_Rssi);
        /*
         * if(db_name == 0){
         *
         * }else if(db_name == 1){ db.insert("RSSI1", null, values_add); }else
         * if(db_name == 2){ db.insert("RSSI2", null, values_add); }else
         * if(db_name == 3){ db.insert("RSSI3", null, values_add); }
         */
        // if(db_name > 0)
        // db.insert(db_namelist[db_name-1], null,
        // values_add);//你传完你的，我再传我的，不能重复传
        deal_dxdxloc();// 不仅处理dxdyloc，同时插入数据库函数也在这里面
    }

    public boolean islock() {// 是否为我们需要的设备
        boolean flag = false;
        for (int i = 0; i < Address_list.length; i++) {
            if (mdevice.getAddress().equals(Address_list[i])) {
                flag = true;
                String str = mdevice.getAddress();
                if (dev_list.indexOf(str) == -1) // 防止地址被重复添加
                    dev_list.add(str);

                /*
                 * if(read_count>=device_number){} else{ db_name =
                 * mSharedPreferences_get(read_count); read_count++; }
                 */

                for (read_count = 0; read_count < device_number; read_count++) {
                    mSharedPreferences_get(read_count);
                    if (mdevice.getAddress().equals(mAddress)) {
                        db_name = mLocation;
                    }
                }

                // db_name =
                // addressMapDatabase((mdevice.getAddress()));//匹配表格名称，人为设置
                // dev_array_Adapter = new ArrayAdapter<String>(this,
                // android.R.layout.simple_list_item_1, dev_list);
                // //这个加载的布局的是SDK中提供的
                // dev_listview.setAdapter(dev_array_Adapter);
                // show(str);
                break;
            } else {
                if ((i + 1) == Address_list.length)
                    flag = false;
            }
        }
        return flag;
    }

    /*判断是不是我们需要采集的设备，我们设了5个设备，实际存的share文件就是1-5，这里面有我们需要设备的地址*/
    private boolean isNeed(String str){
        boolean flag=false;
        for(int i=0;i<device_number;i++){
            mSharedPreferences_get(i);
            flag=str.equals(mAddress);
            if(flag){
                break;
            }
        }
        return flag;
    }

//	private boolean isExist(String str) {
//		boolean flag = true;
//		for (int i = 0; i < Scaned_Address_list.size(); i++) {
//			flag = str.equals(Scaned_Address_list.get(i));
//			if (flag) {
//				flag = !flag;
//				break;
//			}
//			flag = !flag;
//		}
//		return flag;
//	}

    public void deal_dxdxloc() {// 处理dxdy，按照坐标轴，左-右+
        /*
         * String loc_str = loc_edittext.getText().toString(); loc_int =
         * Integer.parseInt(loc_str);
         */
        if (db_name > 0 && db_name <= device_number) {
            if (db_name == loc_int) {
                values_add.put("location", 0);
                db.insert(db_namelist[db_name - 1], null, values_add);
            } else if (db_name < loc_int) {
                /*
                 * if(db_name == (loc_int - 1)) values_add.put("location",
                 * loc_edittext.getText().toString());
                 */
                for (int i = 0; i < (loc_int - 1); i++) {
                    if (db_name == (loc_int - i - 1)) {
                        values_add.put("location", (db_name - loc_int));
                        db.insert(db_namelist[db_name - 1], null, values_add);
                    }
                }
            } else if (db_name > loc_int) {// 这条语句是为了单个测试，我只采集一个，但是存在大于1的表格里
                for (int i = 0; i < (device_number - loc_int); i++) {
                    if (db_name == (loc_int + i + 1)) {
                        values_add.put("location", (db_name - loc_int));
                        db.insert(db_namelist[db_name - 1], null, values_add);
                    }
                }
            }
        } else if (db_name > device_number) {
            values_add.put("location", loc_int);
            db.insert(db_namelist[db_name - 1], null, values_add);// 你传完你的，我再传我的，不能重复传
        } else if (db_name == 0) {

        }
    }

    /*
     * private int addressMapDatabase(String address) { // TODO Auto-generated
     * method stub int filename = 0; int local_filename = 0;
     *
     *
     * if(address.equalsIgnoreCase("D0:39:72:CC:85:E1")){ local_filename =1; }
     * if(address .equalsIgnoreCase("D0:39:72:CC:ED:11")){ local_filename =2; }
     * if(address.equalsIgnoreCase( "D0:39:72:CC:C0:4B")){ local_filename =3; }
     * if(address .equalsIgnoreCase("D0:39:72:CC:B9:FC")){ local_filename ="4" +
     * ".txt"; } if(address .equalsIgnoreCase("D0:39:72:CC:C0:4B")){
     * local_filename ="5" + ".txt"; }
     * if(address.equalsIgnoreCase("D0:39:72:CC:F3:64")){ local_filename ="5" +
     * ".txt"; } if(address.equalsIgnoreCase( "D0:39:72:CD:01:33")){
     * local_filename ="6" + ".txt"; }
     * if(address.equalsIgnoreCase("D0:39:72:CC:B9:E1")){ local_filename ="7" +
     * ".txt"; } if(address .equalsIgnoreCase("D0:39:72:CC:C0:00")){
     * local_filename ="9" + scene + ".txt"; } if(address.equalsIgnoreCase(
     * "D0:39:72:CC:86:B9")){ local_filename ="8" + scene + ".txt"; } filename
     * =local_filename; return filename; }
     */

    public void mSharedPreferences_get(int i) {// SharedPreferences读取，读取地址和location
        SharedPreferences pref = getSharedPreferences(data_namelist[i],
                MODE_MULTI_PROCESS);
        // boolean bl = pref.getBoolean("mblooean", false);
        int loc = pref.getInt("mint", 0);
        mLocation = loc;
        String addr = pref.getString("mstring", null);
        mAddress = addr;
        // return loc;
        // Log.d("save", ""+bl);
    }

    public void mNotification() {// 通知栏的编写
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Notification mNotification = new Notification(R.drawable.ic_launcher,
        // "RSSI collect finish", System.currentTimeMillis());
        // mNotification.setLatestEventInfo(MainActivity.this,
        // "RSSI collect finish", "Master, touch me, control me", null);

        Intent notify_intent = new Intent(MainActivity.this,
                Sqlite_Activity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, notify_intent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this);
        mBuilder.setContentTitle("RSSI collect finish")// 设置通知栏标题
                .setContentText("Master, touch me, control me") // 设置通知栏显示内容</span>
                .setContentIntent(pi) // 设置通知栏点击意图
                // .setNumber(number) //设置通知集合的数量
                .setTicker("RSSI collect finish") // 通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                // .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                .setAutoCancel(true)// 设置这个标志当用户单击面板就可以让通知将自动取消
                // .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setDefaults(Notification.DEFAULT_VIBRATE)// 向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                // Notification.DEFAULT_ALL Notification.DEFAULT_SOUND 添加声音
                // Notification.DEFAULT_VIBRATE添加震动// requires VIBRATE
                // permission
                .setSmallIcon(R.drawable.ic_launcher);// 设置通知小ICON
        manager.notify(1, mBuilder.build());

    }

    public String get_currentTime() {// 获取当前的时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = format.format(curDate);
        return str;
    }

    public long get_ms_currentTime(){//获取当前的毫秒时间
        Date dt= new Date();
        long time= dt.getTime();
        return time;
    }

    public String get_localdevice() {// 获取采集的手机设备
        Build bd = new Build();
        String localdev = bd.MODEL;
        return localdev;
    }

    public void show(String msg) {
        sb.append(msg + "\n"); // 向StringBuilder中添加字符串
        runOnUiThread(new Runnable() {// 新线程中更新UI的方法

            @Override
            public void run() {
                // TODO Auto-generated method stub
                textview1.setText(sb.toString());// Returns the contents of this
                // builder.，更新textview
            }
        });
        save_txt(msg);// 储存所有的show的信息，便于查看
    }

    public void save_txt(String saveString) {
        // TODO Auto-generated method stub
        try {

            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                File sdCardDir = Environment.getExternalStorageDirectory();
                FileWriter fw = new FileWriter(sdCardDir.toString()
                        + "/OperationMsg.txt", true);// 构造方法FileWriter(File
                // file, boolean
                // append)append是true才是追加模式
                fw.write(saveString);
                fw.write("\r\n");
                fw.close();
                /*
                 * File targetFile = new File(sdCardDir,"savemessage.txt"); out
                 * = new FileOutputStream(targetFile);
                 * out.write(saveString.getBytes());
                 * out.write("\r\n".getBytes()); out.close();
                 */
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public void save_ms_txt(String saveString) {
        // TODO Auto-generated method stub
        try {

            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                File sdCardDir = Environment.getExternalStorageDirectory();
                FileWriter fw = new FileWriter(sdCardDir.toString()
                        + "/ms_time.txt", true);// 构造方法FileWriter(File
                // file, boolean
                // append)append是true才是追加模式
                fw.write(saveString);
                fw.write("\r\n");
                fw.close();
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    private void initMediaPlayer() {
        try {
            File file = new File(Environment.getExternalStorageDirectory(),
                    "song1.mp3");
            mediaPlayer.setDataSource(file.getPath());
            mediaPlayer.prepare();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        mediaPlayer.start();
    }

    public void get_contentView() {
        num_edittext = (EditText) findViewById(R.id.nu_et);
        loc_edittext = (EditText) findViewById(R.id.loc_et);
        dx_edittext = (EditText) findViewById(R.id.dx_et);
        dy_edittext = (EditText) findViewById(R.id.dy_et);
        time_edittext = (EditText) findViewById(R.id.time_et);
        time_stop_et = (EditText) findViewById(R.id.stoptime_et);
        time_cycling_et = (EditText) findViewById(R.id.cyclingtime_et);

        time_button = (Button) findViewById(R.id.change_time);
        num_button = (Button) findViewById(R.id.change_num);
        loc_button = (Button) findViewById(R.id.change_loc);
        dx_button = (Button) findViewById(R.id.change_dx);
        dy_button = (Button) findViewById(R.id.change_dy);
        time_stop_bn = (Button) findViewById(R.id.stop_time_bn);
        time_cycling_bn = (Button) findViewById(R.id.cycling_time_bn);
        set_last_set_bn = (Button) findViewById(R.id.set_last_set);
        sqlite_button = (Button) findViewById(R.id.sqlite_bn);

        start_button = (Button) findViewById(R.id.start);
        scan_cancel_button = (Button) findViewById(R.id.stop);
        show_draw_button = (Button) findViewById(R.id.draw);
        show_dev_button = (Button) findViewById(R.id.show_dev);
        textview1 = (TextView) findViewById(R.id.textView1);
        count_textview = (TextView) findViewById(R.id.count_tv);

        textview1.setMovementMethod(ScrollingMovementMethod.getInstance());// 当然我们为了让TextView动起来，还需要用到TextView的setMovementMethod方法设置一个滚动实例
        count_textview.setMovementMethod(ScrollingMovementMethod.getInstance());
        // dev_listview = (ListView)findViewById(R.id.dev_lv1);

    }

    public void set_listener() {
        start_button.setOnClickListener(this);
        scan_cancel_button.setOnClickListener(this);// scan和cancel共用按键的点击监听
        show_draw_button.setOnClickListener(this);
        show_dev_button.setOnClickListener(this);
        time_button.setOnClickListener(this);
        num_button.setOnClickListener(this);
        loc_button.setOnClickListener(this);
        dx_button.setOnClickListener(this);
        dy_button.setOnClickListener(this);
        time_stop_bn.setOnClickListener(this);
        time_cycling_bn.setOnClickListener(this);
        set_last_set_bn.setOnClickListener(this);
        sqlite_button.setOnClickListener(this);
    }

    public void mSharedPreferences_save_LastSet() {

        SharedPreferences.Editor editor1 = getSharedPreferences(
                "Last_set_data", MODE_MULTI_PROCESS).edit();
        // editor.putBoolean("mboolean", bl);
        editor1.putInt("mdevicenumber", device_number);
        editor1.putInt("mfacewho", loc_int);
        editor1.putInt("mdx", dx_int);
        editor1.putInt("mdy", dy_int);
        editor1.putInt("mscantime", mSCANtime);
        editor1.putInt("mstoptime", mStopTimes);
        /***** 每个设备对应什么位置，都在device_activity中存在了文件了，直接采集就好 ******/
        // editor1.putString("mstring", address);
        editor1.commit();

        Log.d("save last set", "succeed");
    }

    public void mSharedPreferences_get_LastSet() {// SharedPreferences读取，读取地址和location
        SharedPreferences pref1 = getSharedPreferences("Last_set_data",
                MODE_MULTI_PROCESS);
        int mdevicenumber = pref1.getInt("mdevicenumber", 0);
        int mfacewho = pref1.getInt("mfacewho", 0);
        int mdx = pref1.getInt("mdx", 0);
        int mdy = pref1.getInt("mdy", 0);
        int mscantime = pref1.getInt("mscantime", 0);
        int mstoptime = pref1.getInt("mstoptime", 0);
        save_last_set.add(mdevicenumber);
        save_last_set.add(mfacewho);
        save_last_set.add(mdx);
        save_last_set.add(mdy);
        save_last_set.add(mscantime);
        save_last_set.add(mstoptime);
        // Log.d("save", ""+bl);
    }

    /*
     * public void show_last_set(){
     * show("---------"+get_currentTime()+"---------");
     * show("Last number :"+save_last_set.get(0));
     * show("Last location :"+save_last_set.get(1));
     * show("Last dx :"+save_last_set.get(2));
     * show("Last dy :"+save_last_set.get(3));
     * show("Last time :"+save_last_set.get(4));
     * show("Last stoptimes :"+save_last_set.get(5));
     * show("Device_Location was set before"); }
     */
    public void show_last_set() {
        num_edittext.setText("" + save_last_set.get(0));
        loc_edittext.setText("" + save_last_set.get(1));
        dx_edittext.setText("" + save_last_set.get(2));
        dy_edittext.setText("" + save_last_set.get(3));
        time_edittext.setText("" + save_last_set.get(4));
        time_stop_et.setText("" + save_last_set.get(5));
    }

    public void Set_last_set() {
        device_number = save_last_set.get(0);
        loc_int = save_last_set.get(1);
        dx_int = save_last_set.get(2);
        dy_int = save_last_set.get(3);
//		SCAN_PERIOD = save_last_set.get(4);// int自动转化成long
        CYCLING_PERIOD = save_last_set.get(4);// int自动转化成long
//		mSCANtime = (int) SCAN_PERIOD;
        mSCANtime = (int) CYCLING_PERIOD;
        mStopTimes = save_last_set.get(5);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        count = 0;
        times_count = 0;
        // Log.d(TAG, "CommunicationActivity onDestroy()");

        /*
         * try { unregisterReceiver(mActivityReceive); } catch (Exception
         * ignore) { Log.e(TAG, ignore.toString()); }
         */
        unbindService(mServiceConnection);
        // mService.stopSelf();
        mService = null;
//		mTimerHelper.stop();// 相当于下面的功能，停止计时器，不过可以重新开始
//		mTimerHelper1.stop();
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause");
        mSharedPreferences_get_LastSet();
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart");
    }

    @Override
    public void onResume() {
        super.onResume();
        mSharedPreferences_get_LastSet();
        // show_last_set();//加上的话显示太多
        Log.d(TAG, "onResume");

        /*
         * if (!mBtAdapter.isEnabled()) { Log.i(TAG,
         * "onResume - BT not enabled yet"); Intent enableIntent = new
         * Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
         * startActivityForResult(enableIntent, REQUEST_ENABLE_BT); }
         */
    }

    /*
     * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
     * menu; this adds items to the action bar if it is present.
     * getMenuInflater().inflate(R.menu.main, menu); return true; }
     *
     * @Override public boolean onOptionsItemSelected(MenuItem item) { // Handle
     * action bar item clicks here. The action bar will // automatically handle
     * clicks on the Home/Up button, so long // as you specify a parent activity
     * in AndroidManifest.xml. int id = item.getItemId(); if (id ==
     * R.id.action_settings) { return true; } return
     * super.onOptionsItemSelected(item); }
     */

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_COARSE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // TODO request success
                }
                break;
        }
    }


}