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

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.jack.zhou.floatview.floatutil.FloatUtil;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final int       SDK_PERMISSION_REQUEST = 127;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPersimmions();
        setContentView(R.layout.activity_main);

        FloatUtil.getInstance().createFloatView(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        FloatUtil.getInstance().removeFloatView();
    }

    @TargetApi(23)
    private void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            /***
             * 发出一个意图让用户去设置同意附表权限，申请权限
             */
            // 定位精确位置
            if(!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                startActivity(intent);
                return;
            }
        }
    }
}
