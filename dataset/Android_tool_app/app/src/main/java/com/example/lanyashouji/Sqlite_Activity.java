package com.example.lanyashouji;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Sqlite_Activity extends Activity implements View.OnClickListener {
    public static ArrayList<Integer> rssi_save1 = new ArrayList<Integer>();//查询的rssi的值要分别存在这些数组中
    public static ArrayList<Integer> rssi_save2 = new ArrayList<Integer>();
    public static ArrayList<Integer> rssi_save3 = new ArrayList<Integer>();
    public static ArrayList<Integer> rssi_save4 = new ArrayList<Integer>();
    public static ArrayList<Integer> rssi_save5 = new ArrayList<Integer>();
    public static ArrayList<Integer> rssi_save6 = new ArrayList<Integer>();
    public static ArrayList<Integer> rssi_save7 = new ArrayList<Integer>();
    public static ArrayList<Integer> rssi_save8 = new ArrayList<Integer>();
    public static ArrayList<Integer> rssi_save9 = new ArrayList<Integer>();
    public static ArrayList<Integer> rssi_save10 = new ArrayList<Integer>();
    public static ArrayList<Integer> rssi_save11 = new ArrayList<Integer>();
    public static ArrayList<Integer> rssi_save12 = new ArrayList<Integer>();
    public static ArrayList<Integer> rssi_save13 = new ArrayList<Integer>();
    public static ArrayList<Integer> rssi_save14 = new ArrayList<Integer>();
    public static ArrayList<Integer> rssi_save15 = new ArrayList<Integer>();

    private Button query_button;
    private Button update_button;
    private Button delete_button;
    private EditText query_edittext;
    private EditText update_edittext;
    private EditText delete_edittext;
    private EditText save_edittext;
    private Button close_sqlite_bn;
    private TextView show_textview;
    private Button save_txt_button;

    private StringBuilder sb;

    private Sqlite_Helper mSqliteHelper;//获取Sqlite类的对象
    private SQLiteDatabase db;//用于数据的存储和更改，调用它的方法

    private String[] db_namelist = new String[]{"RSSI1","RSSI2","RSSI3","RSSI4","RSSI5"
            ,"RSSI6","RSSI7","RSSI8","RSSI9","RSSI10"
            ,"RSSI11","RSSI12","RSSI13","RSSI14","RSSI15"};
    private String[] txt_namelist = new String[]{"/RSSI1.txt","/RSSI2.txt","/RSSI3.txt","/RSSI4.txt","/RSSI5.txt"
            ,"/RSSI6.txt","/RSSI7.txt","/RSSI8.txt","/RSSI9.txt","/RSSI10.txt"
            ,"/RSSI11.txt","/RSSI12.txt","/RSSI13.txt","/RSSI14.txt","/RSSI15.txt"};
    int num_save = 0;
	/*private String[] rssi_save_namelist = new String[]{"rssi_save1","rssi_save2","rssi_save3","rssi_save4","rssi_save5"
													,"rssi_save6","rssi_save7","rssi_save8","rssi_save9"};*/

    int db_int = 0;
    private Cursor cursor;
    private int collect_num = 0;
    String[] values;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sqlite_layout);

        sb = new StringBuilder();

        mSqliteHelper = new Sqlite_Helper(this, "RSSI.db", null, 1);//获取实例
        db = mSqliteHelper.getWritableDatabase();//创建表格,会返回一个SQLiteDatabase对象,用于增减更新数据

        set_contentview();
        set_listener();

    }



    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.query_bn:

                break;
            case R.id.update_bn:

                break;
            case R.id.delete_bn:
                String str = delete_edittext.getText().toString();
                db_int = Integer.parseInt(str);
                //db.execSQL("drop table RSSI6");//删除表格
                db.execSQL("delete from " + db_namelist[db_int-1]);//清空表格
                //db.delete("RSSI6", null, null);
                init_id();
                show(db_namelist[db_int-1] + " has been delete");
                break;
            case R.id.save_txt:
                //save("test");
                save_all();
                break;
            case R.id.close_sqlite:
                finish();
                break;

            default:
                break;
        }
    }

    public void save(String saveString, String txt_name) {
        // TODO Auto-generated method stub
        try {

            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                File sdCardDir = Environment.getExternalStorageDirectory();
                FileWriter fw = new FileWriter(sdCardDir.toString()+txt_name,true);//构造方法FileWriter(File file, boolean append)append是true才是追加模式
                fw.write(saveString);
                fw.write("\r\n");
                fw.close();

//				Log.e("Sqlite_Activity", "sdCardDir..."+sdCardDir.getPath().toString());
                Log.e("Sqlite_Activity", "save_rssi_txt");
				/*File targetFile = new File(sdCardDir,"savemessage.txt");
			out = new FileOutputStream(targetFile);
			out.write(saveString.getBytes());
			out.write("\r\n".getBytes());
			out.close();	*/
            }

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    /***************一直出错的原有难道是，LOG输出了太多的rssi，阻塞线程？？？？？*********************/
    public ArrayList query_db(String dbname_msg){//查询数据库，1参表格名字，2参存入的数组名字
        //dbHelper = new Sqlite_Helper(this, "RSSI.db", null, 1);
        //db = dbHelper.getWritableDatabase();//会返回一个SQLiteDatabase对象，251页，第一行代码

        ArrayList<Integer> rssi_list = new ArrayList<>();
        cursor = db.query(dbname_msg, null, null, null, null, null, null);//query方法会返回一个Cursor对象
        if(cursor.moveToFirst()){
            do {
                //String name = cursor.getString(cursor.getColumnIndex("name"));//获取name列的数据
                //String address = cursor.getString(cursor.getColumnIndex("address"));//获取address列的数据
                int rssi_read = cursor.getInt(cursor.getColumnIndex("RSSI"));//获取RSSI列的数据
                rssi_list.add(rssi_read);
                collect_num++;
                //Log.d("MainActivity", "name is  "+name);
                //Log.d("MainActivity", "address is  "+address);
                //Log.d("MainActivity", "RSSI is  "+rssi);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return rssi_list;
        //Toast.makeText(this, "Query msg succeeed", Toast.LENGTH_SHORT).show();
    }

    private void save_all(){
        String str = save_edittext.getText().toString();
        values = str.split("\\.");  //定义一个数组，把一个字符串分割成字符串数组。按照“|”来分割，格式必须写成"\\|"才能正确分割
        //show(""+values[0]);
        //new Thread(new Runnable() {
        //public void run() {
        /******最好不要在这里执行耗时程序，看起来app卡死********/
        for(int i=0;i<values.length;i++){
            num_save = Integer.parseInt(values[i]);//将获得的string转换成int
            if(num_save == 1){
                //query_db("RSSI1", rssi_save1);
                rssi_save1 = query_db("RSSI1");
                for(int j=0;j<collect_num;j++){
                    String str2 = rssi_save1.get(j) + " ";//int转化成string
                    save(str2, txt_namelist[num_save-1]);
                }
                collect_num = 0;
//		 			   Log.e("Sqlite_Activity", "save_rssi_txt");
                show("RSSI"+num_save+" was save to txt"+"|"+get_currentTime());
            }else if(num_save == 2){
                //query_db("RSSI2", rssi_save2);
                rssi_save2 = query_db("RSSI2");
                for(int j=0;j<collect_num;j++){
                    String str2 = rssi_save2.get(j) + " ";//int转化成string
                    save(str2, txt_namelist[num_save-1]);
                }
                collect_num = 0;
                show("RSSI"+num_save+" was save to txt"+"|"+get_currentTime());
            }else if(num_save == 3){
                //query_db("RSSI3", rssi_save3);
                rssi_save3 = query_db("RSSI3");
                for(int j=0;j<collect_num;j++){
                    String str2 = rssi_save3.get(j) + " ";//int转化成string
                    save(str2, txt_namelist[num_save-1]);
                }
                collect_num = 0;
                show("RSSI"+num_save+" was save to txt"+"|"+get_currentTime());
            }else if(num_save == 4){
                //query_db("RSSI4", rssi_save4);
                rssi_save4 = query_db("RSSI4");
                for(int j=0;j<collect_num;j++){
                    String str2 = rssi_save4.get(j) + " ";//int转化成string
                    save(str2, txt_namelist[num_save-1]);
                }
                collect_num = 0;
                show("RSSI"+num_save+" was save to txt"+"|"+get_currentTime());
            }else if(num_save == 5){
                //query_db("RSSI5", rssi_save5);
                rssi_save5 = query_db("RSSI5");
                for(int j=0;j<collect_num;j++){
                    String str2 = rssi_save5.get(j) + " ";//int转化成string
                    save(str2, txt_namelist[num_save-1]);
                }
                collect_num = 0;
                show("RSSI"+num_save+" was save to txt"+"|"+get_currentTime());
            }else if(num_save == 6){
                //query_db("RSSI6", rssi_save6);
                rssi_save6 = query_db("RSSI6");
                for(int j=0;j<collect_num;j++){
                    String str2 = rssi_save6.get(j) + " ";//int转化成string
                    save(str2, txt_namelist[num_save-1]);
                }
                collect_num = 0;
                show("RSSI"+num_save+" was save to txt"+"|"+get_currentTime());
            }else if(num_save == 7){
                //query_db("RSSI7", rssi_save7);
                rssi_save7 = query_db("RSSI7");
                for(int j=0;j<collect_num;j++){
                    String str2 = rssi_save7.get(j) + " ";//int转化成string
                    save(str2, txt_namelist[num_save-1]);
                }
                collect_num = 0;
                show("RSSI"+num_save+" was save to txt"+"|"+get_currentTime());
            }else if(num_save == 8){
                //query_db("RSSI8", rssi_save8);
                rssi_save8 = query_db("RSSI8");
                for(int j=0;j<collect_num;j++){
                    String str2 = rssi_save8.get(j) + " ";//int转化成string
                    save(str2, txt_namelist[num_save-1]);
                }
                collect_num = 0;
                show("RSSI"+num_save+" was save to txt"+"|"+get_currentTime());
            }else if(num_save == 9){
                //query_db("RSSI9", rssi_save9);
                rssi_save9 = query_db("RSSI9");
                for(int j=0;j<collect_num;j++){
                    String str2 = rssi_save9.get(j) + " ";//int转化成string
                    save(str2, txt_namelist[num_save-1]);
                }
                collect_num = 0;
                show("RSSI"+num_save+" was save to txt"+"|"+get_currentTime());
            }else if(num_save == 10){
                //query_db("RSSI10", rssi_save10);
                rssi_save10 = query_db("RSSI10");
                for(int j=0;j<collect_num;j++){
                    String str2 = rssi_save10.get(j) + " ";//int转化成string
                    save(str2, txt_namelist[num_save-1]);
                }
                collect_num = 0;
                show("RSSI"+num_save+" was save to txt"+"|"+get_currentTime());
            }else if(num_save == 11){
                //query_db("RSSI11", rssi_save11);
                rssi_save11 = query_db("RSSI11");
                for(int j=0;j<collect_num;j++){
                    String str2 = rssi_save11.get(j) + " ";//int转化成string
                    save(str2, txt_namelist[num_save-1]);
                }
                collect_num = 0;
                show("RSSI"+num_save+" was save to txt"+"|"+get_currentTime());
            }else if(num_save == 12){
                //query_db("RSSI12", rssi_save12);
                rssi_save12 = query_db("RSSI12");
                for(int j=0;j<collect_num;j++){
                    String str2 = rssi_save12.get(j) + " ";//int转化成string
                    save(str2, txt_namelist[num_save-1]);
                }
                collect_num = 0;
                show("RSSI"+num_save+" was save to txt"+"|"+get_currentTime());
            }else if(num_save == 13){
                //query_db("RSSI13", rssi_save13);
                rssi_save13 = query_db("RSSI13");
                for(int j=0;j<collect_num;j++){
                    String str2 = rssi_save13.get(j) + " ";//int转化成string
                    save(str2, txt_namelist[num_save-1]);
                }
                collect_num = 0;
                show("RSSI"+num_save+" was save to txt"+"|"+get_currentTime());
            }else if(num_save == 14){
                //query_db("RSSI14", rssi_save14);
                rssi_save14 = query_db("RSSI14");
                for(int j=0;j<collect_num;j++){
                    String str2 = rssi_save14.get(j) + " ";//int转化成string
                    save(str2, txt_namelist[num_save-1]);
                }
                collect_num = 0;
                show("RSSI"+num_save+" was save to txt"+"|"+get_currentTime());
            }else if(num_save == 15){
                //query_db("RSSI15", rssi_save15);
                rssi_save15 = query_db("RSSI15");
                for(int j=0;j<collect_num;j++){
                    String str2 = rssi_save15.get(j) + " ";//int转化成string
                    save(str2, txt_namelist[num_save-1]);
                }
                collect_num = 0;
                show("RSSI"+num_save+" was save to txt"+"|"+get_currentTime());
            }

        }
        //}
        //});
    }



    private void init_id() {
        // TODO Auto-generated method stub
        if(db_int == 1)
            db.execSQL("update sqlite_sequence set seq=0 where name='RSSI1'");//id从0开始自增长
        else if(db_int == 2)
            db.execSQL("update sqlite_sequence set seq=0 where name='RSSI2'");//id从0开始自增长
        else if(db_int == 3)
            db.execSQL("update sqlite_sequence set seq=0 where name='RSSI3'");//id从0开始自增长
        else if(db_int == 4)
            db.execSQL("update sqlite_sequence set seq=0 where name='RSSI4'");//id从0开始自增长
        else if(db_int == 5)
            db.execSQL("update sqlite_sequence set seq=0 where name='RSSI5'");//id从0开始自增长
        else if(db_int == 6)
            db.execSQL("update sqlite_sequence set seq=0 where name='RSSI6'");//id从0开始自增长
        else if(db_int == 7)
            db.execSQL("update sqlite_sequence set seq=0 where name='RSSI7'");//id从0开始自增长
        else if(db_int == 8)
            db.execSQL("update sqlite_sequence set seq=0 where name='RSSI8'");//id从0开始自增长
        else if(db_int == 9)
            db.execSQL("update sqlite_sequence set seq=0 where name='RSSI9'");//id从0开始自增长
        else if(db_int == 10)
            db.execSQL("update sqlite_sequence set seq=0 where name='RSSI10'");//id从0开始自增长
        else if(db_int == 11)
            db.execSQL("update sqlite_sequence set seq=0 where name='RSSI11'");//id从0开始自增长
        else if(db_int == 12)
            db.execSQL("update sqlite_sequence set seq=0 where name='RSSI12'");//id从0开始自增长
        else if(db_int == 13)
            db.execSQL("update sqlite_sequence set seq=0 where name='RSSI13'");//id从0开始自增长
        else if(db_int == 14)
            db.execSQL("update sqlite_sequence set seq=0 where name='RSSI14'");//id从0开始自增长
        else if(db_int == 15)
            db.execSQL("update sqlite_sequence set seq=0 where name='RSSI15'");//id从0开始自增长
    }


    private void set_listener() {
        // TODO Auto-generated method stub
        query_button.setOnClickListener(this);
        update_button.setOnClickListener(this);
        delete_button.setOnClickListener(this);
        close_sqlite_bn.setOnClickListener(this);
        save_txt_button.setOnClickListener(this);
    }

    private void set_contentview() {
        // TODO Auto-generated method stub
        query_button = (Button)findViewById(R.id.query_bn);
        update_button = (Button)findViewById(R.id.update_bn);
        delete_button = (Button)findViewById(R.id.delete_bn);
        save_txt_button = (Button)findViewById(R.id.save_txt);
        close_sqlite_bn = (Button)findViewById(R.id.close_sqlite);
        query_edittext = (EditText)findViewById(R.id.query_et);
        update_edittext = (EditText)findViewById(R.id.update_et);
        delete_edittext = (EditText)findViewById(R.id.delete_et);
        save_edittext = (EditText)findViewById(R.id.save_et);
        show_textview = (TextView)findViewById(R.id.show_tv);


        show_textview.setMovementMethod(ScrollingMovementMethod.getInstance());//当然我们为了让TextView动起来，还需要用到TextView的setMovementMethod方法设置一个滚动实例
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
                show_textview.setText(sb.toString());//Returns the contents of this builder.，更新textview
            }
        });
    }
}
