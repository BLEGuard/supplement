package com.example.lanyashouji;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Draw_Activity extends Activity implements View.OnClickListener {
    public static int a = -30;//声明成static，然后才能传给View中,这些为需要传过去的数
    public static ArrayList<Integer> rssi_list1 = new ArrayList<Integer>();//查询的rssi的值要分别存在这些数组中
    public static ArrayList<Integer> rssi_list2 = new ArrayList<Integer>();
    public static ArrayList<Integer> rssi_list3 = new ArrayList<Integer>();
    public static ArrayList<Integer> rssi_list4 = new ArrayList<Integer>();
    public static ArrayList<Integer> rssi_list5 = new ArrayList<Integer>();
    public static ArrayList<Integer> rssi_list6 = new ArrayList<Integer>();
    public static ArrayList<Integer> rssi_list7 = new ArrayList<Integer>();
    public static ArrayList<Integer> rssi_list8 = new ArrayList<Integer>();
    public static ArrayList<Integer> rssi_list9 = new ArrayList<Integer>();
    public static int time_draw = 50, num_draw = 0;/*****有些数必须先初始化，要不然加载不出来布局**********/
    public static boolean start = false;

    private Sqlite_Helper dbHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private EditText time_et_drawet;
    private EditText num_et_drawet;
    private Button time_cg_drawbn;
    private Button num_cg_drawbn;
    private TextView draw_textview;
    private Button start_drawbn;
    private Button close_draw_bn;
	/*private Button stop_drawbn;
	private Button clear_drawbn;
	private Button drawall_drawbn;*/


    private TimerHelper mTimerHelper = null;//自定义的抽象类，获取对象
    private Handler timeHandler;
    private StringBuilder sb;
    InputMethodManager imm;

    private String[] rssi_list_namelist = new String[]{"rssi_list1","rssi_list2","rssi_list3","rssi_list4","rssi_list5"
            ,"rssi_list6","rssi_list7","rssi_list8","rssi_list9"};
    private String[] db_namelist = new String[]{"RSSI1","RSSI2","RSSI3","RSSI4","RSSI5"
            ,"RSSI6","RSSI7","RSSI8","RSSI9"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.draw_layout);
        get_contentView();
        set_listener();
        sb = new StringBuilder();
        start_drawbn.setEnabled(false);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);//收起虚拟键盘
        show("Now speed:"+time_draw+"ms"+"|"+get_currentTime());
        show("No Databases"+"|"+get_currentTime());

        /**************初始要给值，不然崩溃***************/
		/*String[] first = new String[]{"1","2","3"};

		for(int i=0;i<(first.length);i++){
 		   num_draw = Integer.parseInt(first[i]);//将获得的string转换成int
 		   if(num_draw == 1){
 			    quary_db("RSSI1", rssi_list1);
 			}else if(num_draw == 2){
 				quary_db("RSSI2", rssi_list2);
 			}else if(num_draw == 3){
 				quary_db("RSSI3", rssi_list3);}
 			}else if(num_draw ==4){
 				quary_db("RSSI4", rssi_list4);
 			}else if(num_draw ==5){
 				quary_db("RSSI5", rssi_list5);
 			}else if(num_draw ==6){
 				quary_db("RSSI6", rssi_list6);
 			}
		   }
		*/

		/*mTimerHelper = new TimerHelper() {//自定义的抽象类，这里相当于下面task的功能
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Message message = new Message();
		        message.what = 12;
		        timeHandler.sendMessage(message);
				//Log.d(TAG, "相当于task的执行");
			}
		};

		timeHandler = new Handler(){
			@Override
			public void handleMessage(Message msg){
				switch (msg.what) {
				case 12:
					//scanLeDevice(true);
					//draw.invalidate();
					//mBluetoothAdapter.startLeScan(mLeScanCallback);
					//Draw_View mview = new Draw_View(Draw_Activity.this);
					//setContentView(mview);
					//mview.draw_one_point();
			       // mview.invalidate();

					//mview.invalidate();
					//Log.d("Draw_Activity", "0");
					break;

				default:
					break;
				}
				super.handleMessage(msg);
			}
		};*/

    }
    /***************一直出错的原有难道是，LOG输出了太多的rssi，阻塞线程？？？？？*********************/
    public void query_db(String dbname_msg, ArrayList<Integer> rssi_list){//查询数据库，1参表格名字，2参存入的数组名字
        dbHelper = new Sqlite_Helper(this, "RSSI.db", null, 1);
        db = dbHelper.getWritableDatabase();//会返回一个SQLiteDatabase对象，251页，第一行代码
        cursor = db.query(dbname_msg, null, null, null, null, null, null);//query方法会返回一个Cursor对象
        if(cursor.moveToFirst()){
            do {
                //String name = cursor.getString(cursor.getColumnIndex("name"));//获取name列的数据
                //String address = cursor.getString(cursor.getColumnIndex("address"));//获取address列的数据
                int rssi_read = cursor.getInt(cursor.getColumnIndex("RSSI"));//获取RSSI列的数据
                rssi_list.add(rssi_read);
                //Log.d("MainActivity", "name is  "+name);
                //Log.d("MainActivity", "address is  "+address);
                //Log.d("MainActivity", "RSSI is  "+rssi);
            } while (cursor.moveToNext());
        }
        cursor.close();
        //Toast.makeText(this, "Query msg succeeed", Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.time_cg_draw:
                String str = time_et_drawet.getText().toString();
                time_draw = Integer.parseInt(str);//将获得的string转换成int
                show("Figure speed:"+time_draw+"ms"+"|"+get_currentTime());
                imm.hideSoftInputFromWindow(time_et_drawet.getWindowToken(), 0); //收起键盘
                //Toast.makeText(this, "Time changed succeeed : "+time_draw, Toast.LENGTH_SHORT).show();
                break;
            case R.id.num_cg_draw:
                init_queryDB_to_rssilist();
                start_drawbn.setEnabled(true);
                imm.hideSoftInputFromWindow(num_et_drawet.getWindowToken(), 0); //收起键盘
                //chose_db();
			/*for(int i=0;i<(chose_db().length);i++){
    		   num_draw = Integer.parseInt(chose_db()[i]);//将获得的string转换成int
    		   if(num_draw == 1){
    			   quary_db("RSSI1", rssi_list1);
    			}else if(num_draw ==2){
    				quary_db("RSSI2", rssi_list2);
    			}else if(num_draw ==3){
    				quary_db("RSSI3", rssi_list3);}
    			}else if(num_draw ==4){
    				quary_db("RSSI4", rssi_list4);
    			}else if(num_draw ==5){
    				quary_db("RSSI5", rssi_list5);
    			}else if(num_draw ==6){
    				quary_db("RSSI6", rssi_list6);
    			}
		   }*/
                //Toast.makeText(this, "Chose db succeeed", Toast.LENGTH_SHORT).show();
                break;
            case R.id.start_draw:
                Intent show_intent = new Intent(Draw_Activity.this, Show_Draw_Activity.class);
                startActivity(show_intent);
                //set_view();
                break;
            case R.id.close_draw:
                finish();
                break;
		/*case R.id.stop_draw:
			//Draw_View mview = new Draw_View(this);
			//setContentView(mview);
			//mview.t.setStop(false);
			//mview.t.start();
			//start = true;
			//mview.start_draw = true;
			//Log.d("TAG","33"+start);
			//mSharedPreferences_save(start);
			Toast.makeText(this, "stop", Toast.LENGTH_SHORT).show();
			//draw.invalidate();
			//mTimerHelper.start(0,50);
			break;
		case R.id.clear_draw:
			//mTimerHelper.stop();
			break;
		case R.id.drawall_draw:

			break;*/

            default:
                break;
        }
    }

    public String[] chose_db(){//将我输入的要显示的表格字符串，转成string数组，并返回
        String str1 = num_et_drawet.getText().toString();
        String[] values = str1.split("\\.");  //定义一个数组，把一个字符串分割成字符串数组。按照“|”来分割，格式必须写成"\\|"才能正确分割
        //String address = values[0];  //当初我们定义的“设备地址|rssi”，所以我们需要的地址在数组中的第1个，即0
        //Log.e("address", values[0]);
        //Log.e("address", values[1]);
        return values;
    }

    public void init_queryDB_to_rssilist(){

        /**************初始要给值，不然崩溃***************/
        //String[] first = new String[]{"1","2","3"};
        String[] mNamelist = chose_db();
        //Log.e("address", mNamelist[0]);
        clear_all_list();
        for(int i=0;i<mNamelist.length;i++){
            num_draw = Integer.parseInt(mNamelist[i]);//将获得的string转换成int
            if(num_draw == 1){
                query_db("RSSI1", rssi_list1);
                show("RSSI"+num_draw+"was query"+"|"+get_currentTime());
            }else if(num_draw == 2){
                query_db("RSSI2", rssi_list2);
                show("RSSI"+num_draw+"was query"+"|"+get_currentTime());
            }else if(num_draw == 3){
                query_db("RSSI3", rssi_list3);
                show("RSSI"+num_draw+"was query"+"|"+get_currentTime());
            }else if(num_draw == 4){
                query_db("RSSI4", rssi_list4);
                show("RSSI"+num_draw+"was query"+"|"+get_currentTime());
            }else if(num_draw == 5){
                query_db("RSSI5", rssi_list5);
                show("RSSI"+num_draw+"was query"+"|"+get_currentTime());
            }else if(num_draw == 6){
                query_db("RSSI6", rssi_list6);
                show("RSSI"+num_draw+"was query"+"|"+get_currentTime());
            }else if(num_draw == 7){
                query_db("RSSI7", rssi_list7);
                show("RSSI"+num_draw+"was query"+"|"+get_currentTime());
            }else if(num_draw == 8){
                query_db("RSSI8", rssi_list8);
                show("RSSI"+num_draw+"was query"+"|"+get_currentTime());
            }else if(num_draw == 9){
                query_db("RSSI9", rssi_list9);
            }
        }
    }

    public void clear_all_list(){
        rssi_list1.clear();
        rssi_list2.clear();
        rssi_list3.clear();
        rssi_list4.clear();
        rssi_list5.clear();
        rssi_list6.clear();
        rssi_list7.clear();
        rssi_list8.clear();
        rssi_list9.clear();
    }

	/*public void mSharedPreferences_save(boolean bl){
		SharedPreferences.Editor editor = getSharedPreferences("data", MODE_MULTI_PROCESS).edit();
		editor.putBoolean("mboolean", bl);
		//editor.putInt("mint", integer);
		//editor.putString("mstring", str);
		editor.commit();
		Log.d("save", "succeed");
	}
	public void mSharedPreferences_get(){
		SharedPreferences pref = getSharedPreferences("data", MODE_MULTI_PROCESS);
		boolean bl = pref.getBoolean("mblooean", false);
		Log.d("save", ""+bl);
	}*/

    public void get_contentView(){
        time_et_drawet = (EditText)findViewById(R.id.time_et_draw);
        num_et_drawet = (EditText)findViewById(R.id.num_et_draw);
        time_cg_drawbn = (Button)findViewById(R.id.time_cg_draw);
        num_cg_drawbn = (Button)findViewById(R.id.num_cg_draw);
        draw_textview = (TextView)findViewById(R.id.draw_tv);

        start_drawbn = (Button)findViewById(R.id.start_draw);
        close_draw_bn = (Button)findViewById(R.id.close_draw);
		/*stop_drawbn = (Button)findViewById(R.id.stop_draw);
		clear_drawbn = (Button)findViewById(R.id.clear_draw);
		drawall_drawbn = (Button)findViewById(R.id.drawall_draw);*/

        draw_textview.setMovementMethod(ScrollingMovementMethod.getInstance());//当然我们为了让TextView动起来，还需要用到TextView的setMovementMethod方法设置一个滚动实例
    }

    public void set_listener(){
        time_cg_drawbn.setOnClickListener(this);
        num_cg_drawbn.setOnClickListener(this);//scan和cancel共用按键的点击监听
        start_drawbn.setOnClickListener(this);
        close_draw_bn.setOnClickListener(this);
		/*stop_drawbn.setOnClickListener(this);
		clear_drawbn.setOnClickListener(this);
		drawall_drawbn.setOnClickListener(this);*/

    }

    public String get_currentTime(){//获取当前的时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = format.format(curDate);
        return str;
    }

    public void show(String msg){
        sb.append(msg+"\n"); //向StringBuilder中添加字符串
        runOnUiThread(new Runnable() {//新线程中更新UI的方法

            @Override
            public void run() {
                // TODO Auto-generated method stub
                draw_textview.setText(sb.toString());//Returns the contents of this builder.，更新textview
            }
        });
    }

	/*public void set_view(){
		LinearLayout root = (LinearLayout)findViewById(R.id.root);
		final Draw_View draw = new Draw_View(this);
		draw.setMinimumHeight(800);
		draw.setMinimumWidth(800);
		root.addView(draw);

	}*/

}
