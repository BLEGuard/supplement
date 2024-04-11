package com.example.lanyashouji;

import java.util.Timer;
import java.util.TimerTask;

/*由于Timer和TimerTask类的对象只能用一次，所以如果想多次调度同一样的任务，
必须重新实例化。所以，封装了一个TimerHelper类
http://www.cnblogs.com/tianjun9/p/4213113.html*/
public abstract class TimerHelper {
    private Timer mTimer = null;

    public void start(long delay, long period){//相当于实现schedule的功能
        stop();
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {//这里的run相当于实现Timetask的功能
                // TODO Auto-generated method stub
                TimerHelper.this.run();
            }
        },delay,period);
    }

    public void stop(){//相当于实现.cancel的功能
        if(mTimer != null){
            mTimer.cancel();
            mTimer = null;
        }
    }

    public TimerHelper(){
        mTimer = null;
    }

    public abstract void run();
}