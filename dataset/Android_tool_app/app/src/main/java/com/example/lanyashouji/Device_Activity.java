package com.example.lanyashouji;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Device_Activity extends Activity implements View.OnClickListener {
    /************这段代码实现开始扫描之后，点击按钮实现显示设备名称*********************/
	/*public static final String TAG = "Device_Activity";
	ArrayList<String> dev_list1 = new ArrayList<String>();
	ArrayAdapter<String> dev_array_Adapter1;
	private ListView dev_listview;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_layout);

		//Intent intent = getIntent();
		dev_list1 = (ArrayList<String>)getIntent().getStringArrayListExtra("dev_key");//获取传递过来的device

		dev_listview = (ListView)findViewById(R.id.dev_lv);
		//String str = "we find device like this:";
		//dev_list1.add(str);
		dev_array_Adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dev_list1); //这个加载的布局的是SDK中提供的
		dev_listview.setAdapter(dev_array_Adapter1);//采用了新的ArrayAdapter，其中的数据采用List<String>来存放，.add来添加数据
		Log.d(TAG, "Listview was succeed");

		//String str = "test";
		//dev_list.add(str);
		//String str1 = intent.getStringExtra("1");
		//dev_list1.add(str1);

		dev_array_Adapter1.notifyDataSetChanged();  //自动刷新，如果适配器的内容改变时需要强制调用getView来刷新每个Item的内容
        dev_listview.smoothScrollToPosition(dev_array_Adapter1.getCount() - 1);//加上这句话，自动滚屏，可以的
	}*/

    /*******这段扫描单独设置扫描时间******************/
    public static final String TAG = "Device_Activity";
    private static final int LOCATION = 1;//定义成int，才能startActivityForResult中返回

    private BluetoothAdapter mBluetoothAdapter;   //要重新定义，不能从service中获取，下面有获取按钮
    private static final long SCAN_PERIOD = 2000; //2 seconds

    List<String> dev_list=new ArrayList<String>();
    ArrayAdapter<String> dev_array_Adapter;
    private Button dec_button;
    private ListView dev_listview;

    private boolean mScanning;
    private Handler mHandler;

    private int mPosition = 0;
    private String mAddress = null;
    private int mLocation = 0;

    private String[] data_namelist = new String[]{"data_loc1","data_loc2","data_loc3","data_loc4","data_loc5"
            ,"data_loc6","data_loc7","data_loc8","data_loc9","data_loc10"
            ,"data_loc11","data_loc12","data_loc13","data_loc14","data_loc15"};
    private MainActivity mActivity = new MainActivity();//获取我们活动的实例，这些为需要接受的值
    final int dev_num = mActivity.device_number;
    private int dev_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_layout);
        Log.d(TAG, "Device_Activity onCreate");

        mHandler = new Handler();

        final BluetoothManager mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);//使用getSystemService（）返回BluetoothManager
        mBluetoothAdapter = mBluetoothManager.getAdapter();//然后将其用于获取适配器的一个实例,获取适配器

        dev_listview = (ListView)findViewById(R.id.dev_lv);
        dec_button = (Button)findViewById(R.id.dec_bn);
        dev_listview.setOnItemClickListener(mItemClickListener);//老是忘记设定监听器，有用就有鬼了
        dec_button.setOnClickListener(this);

        showlistview();

        show_last_set_dev_table();

        scanLeDevice(true);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.dec_bn:
                finish();
                break;

            default:
                break;
        }
    }

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub

            mBluetoothAdapter.stopLeScan(mLeScanCallback);//既然点击了扫描到的设备，说明扫描完成，需要停止扫描

            String str = dev_list.get(position);
            String[] values = str.split("\\|");  //定义一个数组，把一个字符串分割成字符串数组。按照“|”来分割，格式必须写成"\\|"才能正确分割
            String address = values[0];  //当初我们定义的“设备地址|rssi”，所以我们需要的地址在数组中的第1个，即0
            mAddress = address;
            Log.e("address", values[0]);

            switch (parent.getId())
            {
                case R.id.dev_lv:
                    whichItemClick(position);//position 代表你点的哪一个
                    break;
                default:
                    break;
            }
        }
    };

    public void whichItemClick(int position){
        Intent setLoc = new Intent(Device_Activity.this, SetLocation_Activity.class);
        mPosition = position;
        startActivityForResult(setLoc, LOCATION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode) {
            case LOCATION:
                if(resultCode == RESULT_OK){
                    String str_loc = data.getStringExtra("loc_key");
                    int int_loc = Integer.parseInt(str_loc);
                    mLocation = int_loc;

                    if(dev_count>=dev_num)	finish();//存储设备地址和位置
                    else{
                        mSharedPreferences_save(mAddress, mLocation, dev_count);
                        dev_count++;//在ondestry中清0
                    }


                    Log.e("test", ""+mAddress+"||"+"position: "+mPosition+"|| location : "+mLocation);

                    String srt1 =mAddress+" | location:  "+mLocation;
                    if(dev_list.indexOf(srt1) == -1)
                        dev_list.add(srt1);
                    dev_array_Adapter.notifyDataSetChanged();  //自动刷新，如果适配器的内容改变时需要强制调用getView来刷新每个Item的内容
                    dev_listview.smoothScrollToPosition(dev_array_Adapter.getCount() - 1);//加上这句话，自动滚屏，可以的
                }
                break;

            default:
                break;
        }
    }

    public void mSharedPreferences_save(String address, int location, int i){

        SharedPreferences.Editor editor = getSharedPreferences(data_namelist[i], MODE_MULTI_PROCESS).edit();
        //editor.putBoolean("mboolean", bl);
        editor.putInt("mint", location);
        editor.putString("mstring", address);
        editor.commit();

        Log.d("save","succeed");
    }
	/*public void mSharedPreferences_get(){
		SharedPreferences pref = getSharedPreferences("data", MODE_MULTI_PROCESS);
		boolean bl = pref.getBoolean("mblooean", false);
		Log.d("save", ""+bl);
	}*/

    /*****这里存储的顺序无所谓，最后插入表格式要查询地址，设定位置的，这里只是存储*******/
    public String mSharedPreferences_get(int i){//SharedPreferences读取，读取地址和location
        SharedPreferences pref = getSharedPreferences(data_namelist[i], MODE_MULTI_PROCESS);
        //boolean bl = pref.getBoolean("mblooean", false);
        int loc = pref.getInt("mint", 0);
        //mLocation = loc;
        String addr = pref.getString("mstring", null);
        //mAddress = addr;
        String msg_show = addr + "|"+ "Table:" +loc;
        return msg_show;
        //Log.d("save", ""+bl);
    }

    private void show_last_set_dev_table(){
        for(int i=0;i<dev_num;i++){//这个是读取保存的SharedPreferences的文件的信息然后显示，如果出错，有可能是文件没有建立或空
            String showmsg = mSharedPreferences_get(i);
            if(dev_list.indexOf(showmsg) == -1)
                dev_list.add(showmsg);
            dev_array_Adapter.notifyDataSetChanged();  //自动刷新，如果适配器的内容改变时需要强制调用getView来刷新每个Item的内容
            dev_listview.smoothScrollToPosition(dev_array_Adapter.getCount() - 1);//加上这句话，自动滚屏，可以的
        }
    }

    private void showlistview(){
        dev_array_Adapter = new ArrayAdapter<String>(this, R.layout.device_list, dev_list); //这个加载的布局的是SDK中提供的
        dev_listview.setAdapter(dev_array_Adapter);//采用了新的ArrayAdapter，其中的数据采用List<String>来存放，.add来添加数据
        Log.d(TAG, "first show dev_listview");

    }

    //扫描设备和显示，10s扫描后停止，扫描函数
	/*因为扫描BLE设备是电源密集型操作，浪费电量，因此要保证以下原则：

    1）扫描到需要的设备后，马上停止扫描；

    2）给扫描一个时间限制*/
    private void scanLeDevice(final boolean enable) {

        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {  //handle的使用
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);//停止10s后才开始扫描扫描
                }
            }, SCAN_PERIOD);

            mScanning = true;   //状态表示，正在扫描
            mBluetoothAdapter.startLeScan(mLeScanCallback); //开始扫描
        } else {
            mScanning = false;  //enable设置为false，不扫描，状态显示false
            mBluetoothAdapter.stopLeScan(mLeScanCallback);//停止扫描
        }

    }
    //扫描时的回调的函数，mLeScanCallback()，回调函数LeScanCallback会自动产生device和rssi
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    String str = device.getAddress()+"|"+rssi;
                                    if (dev_list.indexOf(str) == -1)// 防止地址被重复添加
                                        dev_list.add(str); // 获取设备mac地址  和 Rssi
                                    dev_array_Adapter.notifyDataSetChanged();  //自动刷新，如果适配器的内容改变时需要强制调用getView来刷新每个Item的内容
                                    dev_listview.smoothScrollToPosition(dev_array_Adapter.getCount() - 1);//加上这句话，自动滚屏，可以的
                                }
                            });

                        }
                    });
                }
            };



    @Override
    public void onStart() {
        super.onStart();

        //IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);//连接手机时需要使用的action，这里没有用到
        //filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        // filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
    }

    @Override
    public void onStop() {
        super.onStop();
        mBluetoothAdapter.stopLeScan(mLeScanCallback);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBluetoothAdapter.stopLeScan(mLeScanCallback);
        dev_count = 0;
    }

    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
    }


}
