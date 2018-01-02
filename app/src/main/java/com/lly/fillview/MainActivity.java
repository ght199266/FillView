package com.lly.fillview;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.lly.mylibrary.FillView;


/**
 * @author lenovo
 */
public class MainActivity extends AppCompatActivity {

    private FillView fill_iv_home, fill_iv_find;

    private TextView tv_home, tv_find;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fill_iv_home = (FillView) findViewById(R.id.iv_home);
        fill_iv_find = (FillView) findViewById(R.id.iv_find);

        tv_home = (TextView) findViewById(R.id.tv_home);
        tv_find = (TextView) findViewById(R.id.tv_find);


//        fill_iv_home.postDelayed(new Runnable() {
//            @Override
//            public void run() {
                fill_iv_home.check(true);
                tv_home.setTextColor(Color.parseColor("#06C1AE"));
//            }
//        }, 200);

        fill_iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fill_iv_home.check(true);
                fill_iv_find.check(false);

                tv_home.setTextColor(Color.parseColor("#06C1AE"));
                tv_find.setTextColor(Color.parseColor("#858585"));
            }
        });

        fill_iv_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fill_iv_home.check(false);
                fill_iv_find.check(true);


                tv_home.setTextColor(Color.parseColor("#858585"));
                tv_find.setTextColor(Color.parseColor("#06C1AE"));

            }
        });
    }
}
