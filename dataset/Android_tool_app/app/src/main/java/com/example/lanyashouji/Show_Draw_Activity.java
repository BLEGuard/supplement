package com.example.lanyashouji;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Show_Draw_Activity extends Activity implements View.OnClickListener {
    private Button close_show_bn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_draw_layout);

        close_show_bn = (Button)findViewById(R.id.close_show);
        close_show_bn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.close_show:
                finish();
                break;

            default:
                break;
        }
    }

}
