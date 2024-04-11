package com.example.lanyashouji;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Draw_View extends View {
    private Draw_Activity mDraw_Activity = new Draw_Activity();//获取我们活动的实例，这些为需要接受的值
    final ArrayList<Integer> list_rssi1 = mDraw_Activity.rssi_list1;//读取传来的表格中的RSSI数据的List,只能读取一次，再改变不管用
    final ArrayList<Integer> list_rssi2 = mDraw_Activity.rssi_list2;//读取传来的表格中的RSSI数据的List
    final ArrayList<Integer> list_rssi3 = mDraw_Activity.rssi_list3;//读取传来的表格中的RSSI数据的List
    final ArrayList<Integer> list_rssi4 = mDraw_Activity.rssi_list4;//读取传来的表格中的RSSI数据的List
    final ArrayList<Integer> list_rssi5 = mDraw_Activity.rssi_list5;//读取传来的表格中的RSSI数据的List
    final ArrayList<Integer> list_rssi6 = mDraw_Activity.rssi_list6;//读取传来的表格中的RSSI数据的List
    final ArrayList<Integer> list_rssi7 = mDraw_Activity.rssi_list7;//读取传来的表格中的RSSI数据的List
    final ArrayList<Integer> list_rssi8 = mDraw_Activity.rssi_list8;//读取传来的表格中的RSSI数据的List
    final ArrayList<Integer> list_rssi9 = mDraw_Activity.rssi_list9;//读取传来的表格中的RSSI数据的List
    final int draw_time = mDraw_Activity.time_draw;
    //final boolean start_draw = mDraw_Activity.start;

    //public boolean start_draw=false;

    int count_point1=0;
    int count_point2=0;
    int count_point3=0;
    int count_point4=0;
    int count_point5=0;
    int count_point6=0;
    int count_point7=0;
    int count_point8=0;
    int count_point9=0;
    final int s = mDraw_Activity.a;//测试能否获得数据

    private MainActivity mMainActivity = new MainActivity();//获取采集的设备数
    final int number_device = mMainActivity.device_number;

    public int X0=50;//挺哥手机范围，0到700Y轴，0到450X轴,坐标轴的三个点
    public int Y0=50;
    public int X_lengh=600;
    public int Y_lengh=700;
    public int X_scale=10;//50个点
    public int Y_scale=7;//100个点

    Paint p = new Paint();
    Paint p1 = new Paint();
    Paint p2 = new Paint();
    Paint p3 = new Paint();
    Paint p4 = new Paint();
    Paint p5 = new Paint();
    Paint p6 = new Paint();
    Paint p7 = new Paint();
    Paint p8 = new Paint();
    Paint p9 = new Paint();

    private String[] Y_point = new String[Y_lengh/Y_scale];
    private int Max_X_point = X_lengh/X_scale;

    private List<Integer> data1 = new ArrayList<Integer>();
    private List<Integer> data2 = new ArrayList<Integer>();
    private List<Integer> data3 = new ArrayList<Integer>();
    private List<Integer> data4 = new ArrayList<Integer>();
    private List<Integer> data5 = new ArrayList<Integer>();
    private List<Integer> data6 = new ArrayList<Integer>();
    private List<Integer> data7 = new ArrayList<Integer>();
    private List<Integer> data8 = new ArrayList<Integer>();
    private List<Integer> data9 = new ArrayList<Integer>();

    mThread t = new mThread();

    public Draw_View(Context context) {
        super(context);
        // TODO Auto-generated constructor stub

    }

    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            if(msg.what == 1){
                Draw_View.this.invalidate();
                //Log.d("Draw_Activity", "1");
            }
            /*if(msg.arg1 == 2){
            	Draw_View.this.invalidate();
            	t.setStop(true);
            	Log.d("Draw_Activity", "3");
            }*/
        };
    };

    public Draw_View(Context context, AttributeSet set) {
        super(context, set);
        // TODO Auto-generated constructor stub
        for(int i=0; i<Y_point.length; i++){ //获取刻度值
            Y_point[i] = "-" + i + "dBm";
        }

        t.setStop(false);
        t.start();

		/*new Thread(new Runnable() {

            @Override
            public void run() {
                while(start_draw){
                    try {
                        Thread.sleep(draw_time);
                    } catch (InterruptedException e) {
                        //e.printStackTrace();
                    	Log.e("error", "2");
                    }
                    if(data1.size() >= Max_X_point){
                        data1.remove(0);
                    }
                    if(data2.size() >= Max_X_point){
                        data2.remove(0);
                    }
                    if(data3.size() >= Max_X_point){
                        data3.remove(0);
                    }
                    if(data4.size() >= Max_X_point){
                        data4.remove(0);
                    }
                    if(data5.size() >= Max_X_point){
                        data5.remove(0);
                    }
                    if(data6.size() >= Max_X_point){
                        data6.remove(0);
                    }
                    //data1.add(s);
                    //data2.add(s);
                    //data3.add(s);
                    //data4.add(s);
                    //data5.add(s);
                    //data6.add(s);
                    //data.add(new Random().nextInt(100) + 1);
                    draw_one_point();
                    handler.sendEmptyMessage(1);
                }
            }
        }).start(); */


    }


    class mThread extends Thread{

        private boolean isSleep = true;
        private boolean isStop = false;

        @Override
        public void run() {
            while(!isStop){
                try {
                    Thread.sleep(draw_time);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                    Log.e("error", "2");
                }
                if(data1.size() >= Max_X_point){
                    data1.remove(0);
                }
                if(data2.size() >= Max_X_point){
                    data2.remove(0);
                }
                if(data3.size() >= Max_X_point){
                    data3.remove(0);
                }
                if(data4.size() >= Max_X_point){
                    data4.remove(0);
                }
                if(data5.size() >= Max_X_point){
                    data5.remove(0);
                }
                if(data6.size() >= Max_X_point){
                    data6.remove(0);
                }
                if(data7.size() >= Max_X_point){
                    data7.remove(0);
                }
                if(data8.size() >= Max_X_point){
                    data8.remove(0);
                }
                if(data9.size() >= Max_X_point){
                    data9.remove(0);
                }
                //data1.add(s);
                //data2.add(s);
                //data3.add(s);
                //data4.add(s);
                //data5.add(s);
                //data6.add(s);
                //data.add(new Random().nextInt(100) + 1);
                draw_one_point();
                handler.sendEmptyMessage(1);
                /*if(start_draw){
                	Message message = new Message();
    		        message.arg1 = 2;
    		        handler.sendMessage(message);
                }*/

            }
        }

        public void setSleep(boolean sleep) {
            this.isSleep = sleep;
        }
        public void setStop(boolean stop) {
            this.isStop = stop;
        }
    }

    @Override
    public void onDraw(Canvas canvas){
        super.onDraw(canvas);

        chose_color();

        draw_XY(canvas);
        draw_notes(canvas);

        draw_Line(canvas);

        //Log.d("tag", ""+start_draw );//不能单独输出boolean，前面加上" ",就好
    }

    @Override
    protected void onDetachedFromWindow() {//退出view时执行此函数，停止线程
        t.setStop(true);
        // TODO Auto-generated method stub
        super.onDetachedFromWindow();
    }

    public void draw_one_point(){//起始不能为空，会报错，list要有值,加上.size>0,再执行，应该没问题

        if(list_rssi1.size() > 0){
            data1.add(list_rssi1.get(count_point1));//获得List里面的数据
        }
        if(list_rssi2.size() > 0){
            data2.add(list_rssi2.get(count_point2));//获得List里面的数据
        }
        if(list_rssi3.size() > 0){
            data3.add(list_rssi3.get(count_point3));//获得List里面的数据
        }
        if(list_rssi4.size() > 0){
            data4.add(list_rssi4.get(count_point4));//获得List里面的数据
        }
        if(list_rssi5.size() > 0){
            data5.add(list_rssi5.get(count_point5));//获得List里面的数据
        }
        if(list_rssi6.size() > 0){
            data6.add(list_rssi6.get(count_point6));//获得List里面的数据
        }
        if(list_rssi7.size() > 0){
            data7.add(list_rssi7.get(count_point7));//获得List里面的数据
        }
        if(list_rssi8.size() > 0){
            data8.add(list_rssi8.get(count_point8));//获得List里面的数据
        }
        if(list_rssi9.size() > 0){
            data9.add(list_rssi9.get(count_point9));//获得List里面的数据
        }
        count_point1++;
        count_point2++;
        count_point3++;
        count_point4++;
        count_point5++;
        count_point6++;
        count_point7++;
        count_point8++;
        count_point9++;
        if(count_point1 >= list_rssi1.size())
            count_point1=0;
        if(count_point2 >= list_rssi2.size())
            count_point2=0;
        if(count_point3 >= list_rssi3.size())
            count_point3=0;
        if(count_point4 >= list_rssi4.size())
            count_point4=0;
        if(count_point5 >= list_rssi5.size())
            count_point5=0;
        if(count_point6 >= list_rssi6.size())
            count_point6=0;
        if(count_point7 >= list_rssi7.size())
            count_point7=0;
        if(count_point8 >= list_rssi8.size())
            count_point8=0;
        if(count_point9 >= list_rssi9.size())
            count_point9=0;
    }

/*	public void draw_all_point(){
		for(int i=0;i<list_rssi.size();i++)
			data.add(list_rssi.get(i));
	} */

    public void chose_color(){
        p.setColor(Color.WHITE);
        p1.setColor(Color.BLUE);
        p2.setColor(Color.WHITE);
        p3.setColor(Color.RED);
        p4.setColor(Color.GREEN);
        p5.setColor(Color.MAGENTA);
        p6.setColor(Color.GRAY);
        p7.setColor(Color.CYAN);
        p8.setColor(Color.YELLOW);
        p9.setColor(Color.BLACK);
    }

    public void draw_XY(Canvas canvas){
        //画y轴和箭头
        canvas.drawLine(X0, Y0, X0, Y0+Y_lengh, p);
        canvas.drawLine(X0, Y0+Y_lengh, X0-10, Y0+Y_lengh-15, p);
        canvas.drawLine(X0, Y0+Y_lengh, X0+10, Y0+Y_lengh-15, p);
        //画x轴和箭头
        canvas.drawLine(X0, Y0, X0+X_lengh, Y0, p);
        canvas.drawLine(X0+X_lengh, Y0, X0+X_lengh-10, Y0-15, p);
        canvas.drawLine(X0+X_lengh, Y0, X0+X_lengh-10, Y0+15, p);
        //canvas.drawCircle(X0, Y0, 15, p);
        //添加刻度和文字

        p.setTextSize(15);//设置文本大小

        for(int i=0; i * Y_scale < Y_lengh; i=i+10) {
            canvas.drawLine(X0, Y0 + i * Y_scale, X0 + 10, Y0 + i * Y_scale, p);  //刻度

            canvas.drawText(Y_point[i], X0 - 50, Y0 + i * Y_scale, p);//文字
        }
    }

    public void draw_notes(Canvas canvas){//画注释

        p1.setTextSize(30);
        p2.setTextSize(30);
        p3.setTextSize(30);
        p4.setTextSize(30);
        p5.setTextSize(30);
        p6.setTextSize(30);
        p7.setTextSize(30);
        p8.setTextSize(30);
        p9.setTextSize(30);
        canvas.drawText("RSSI1", X0, Y0+Y_lengh+50, p1);//文字
        canvas.drawText("RSSI2", X0, Y0+Y_lengh+80, p2);//文字
        canvas.drawText("RSSI3", X0, Y0+Y_lengh+110, p3);//文字
        canvas.drawText("RSSI4", X0, Y0+Y_lengh+140, p4);//文字
        canvas.drawText("RSSI5", X0+100, Y0+Y_lengh+50, p5);//文字
        canvas.drawText("RSSI6", X0+100, Y0+Y_lengh+80, p6);//文字
        canvas.drawText("RSSI7", X0+100, Y0+Y_lengh+110, p7);//文字
        canvas.drawText("RSSI8", X0+100, Y0+Y_lengh+140, p8);//文字
        canvas.drawText("RSSI9", X0+200, Y0+Y_lengh+50, p9);//文字
    }

    public void draw_Line(Canvas canvas){//起始不能为空，会报错，list要有值，不过有限定条件，.size>1才会执行.get()方法，这个是安全的，没问题

        if(data1.size()>1){//画出折线图的关键之处，取各点在坐标上计算位置
            for(int i=1;i<data1.size();i++){
                canvas.drawLine(X0+(i-1)*X_scale, Y0-data1.get(i-1)*Y_scale, X0+i*X_scale, Y0-data1.get(i)*Y_scale, p1);
            }
        }
        if(data2.size()>1){//画出折线图的关键之处，取各点在坐标上计算位置
            for(int i=1;i<data2.size();i++){
                canvas.drawLine(X0+(i-1)*X_scale, Y0-data2.get(i-1)*Y_scale, X0+i*X_scale, Y0-data2.get(i)*Y_scale, p2);
            }
        }
        if(data3.size()>1){//画出折线图的关键之处，取各点在坐标上计算位置
            for(int i=1;i<data3.size();i++){
                canvas.drawLine(X0+(i-1)*X_scale, Y0-data3.get(i-1)*Y_scale, X0+i*X_scale, Y0-data3.get(i)*Y_scale, p3);
            }
        }

        if(data4.size()>1){//画出折线图的关键之处，取各点在坐标上计算位置
            for(int i=1;i<data4.size();i++){
                canvas.drawLine(X0+(i-1)*X_scale, Y0-data4.get(i-1)*Y_scale, X0+i*X_scale, Y0-data4.get(i)*Y_scale, p4);
            }
        }
        if(data5.size()>1){//画出折线图的关键之处，取各点在坐标上计算位置
            for(int i=1;i<data5.size();i++){
                canvas.drawLine(X0+(i-1)*X_scale, Y0-data5.get(i-1)*Y_scale, X0+i*X_scale, Y0-data5.get(i)*Y_scale, p5);
            }
        }
        if(data6.size()>1){//画出折线图的关键之处，取各点在坐标上计算位置
            for(int i=1;i<data6.size();i++){
                canvas.drawLine(X0+(i-1)*X_scale, Y0-data6.get(i-1)*Y_scale, X0+i*X_scale, Y0-data6.get(i)*Y_scale, p6);
            }
        }
        if(data7.size()>1){//画出折线图的关键之处，取各点在坐标上计算位置
            for(int i=1;i<data7.size();i++){
                canvas.drawLine(X0+(i-1)*X_scale, Y0-data7.get(i-1)*Y_scale, X0+i*X_scale, Y0-data7.get(i)*Y_scale, p7);
            }
        }
        if(data8.size()>1){//画出折线图的关键之处，取各点在坐标上计算位置
            for(int i=1;i<data8.size();i++){
                canvas.drawLine(X0+(i-1)*X_scale, Y0-data8.get(i-1)*Y_scale, X0+i*X_scale, Y0-data8.get(i)*Y_scale, p8);
            }
        }
        if(data9.size()>1){//画出折线图的关键之处，取各点在坐标上计算位置
            for(int i=1;i<data9.size();i++){
                canvas.drawLine(X0+(i-1)*X_scale, Y0-data9.get(i-1)*Y_scale, X0+i*X_scale, Y0-data9.get(i)*Y_scale, p9);
            }
        }
    }
}
