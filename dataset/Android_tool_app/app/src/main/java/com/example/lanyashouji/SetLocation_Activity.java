package com.example.lanyashouji;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SetLocation_Activity   extends Activity implements View.OnClickListener {
    private EditText loc_edittext;
    private Button set_loc_bn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_loc_layout);

        loc_edittext = (EditText)findViewById(R.id.edittext11);
        set_loc_bn = (Button)findViewById(R.id.set_loc);

        set_loc_bn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.set_loc:
                String loc = loc_edittext.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("loc_key", loc);
                setResult(RESULT_OK, intent);
                Log.d("SetLocation_Activity", "location was return");
                finish();
                break;

            default:
                break;
        }
    }
}
