/*
 *         Copyright (C) 2016-2017 宙斯
 *         All rights reserved
 *
 *        filename :Class4
 *        description :
 *
 *         created by jackzhous at  11/07/2016 12:12:12
 *         http://blog.csdn.net/jackzhouyu
 */

package com.jack.zhou.floatview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jack.zhou.floatview.floatutil.FloatUtil;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatUtil.getInstance().createFloatView(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        FloatUtil.getInstance().removeFloatView();
    }
}
