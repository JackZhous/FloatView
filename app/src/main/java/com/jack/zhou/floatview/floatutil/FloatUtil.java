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

package com.jack.zhou.floatview.floatutil;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.jack.zhou.floatview.R;

/***********
 * author: jackzhous
 * file: FloatUtil.java
 * create date: 2016/8/12 9:47
 * desc:
 *      浮点工具类，创建  显示  生成
 ************/
public class FloatUtil {

    private static final String TAG = "jackzhous";
    private static FloatUtil instance;
    private WindowManager mMansger;
    private WindowManager.LayoutParams mParams;
    private Context mContext;

    private int float_x = 0;
    private int float_y = 0;
    private int screen_widht;
    private int screen_height;

    private LinearLayout mFloatLayout;

    private ImageView  floatImage;
    private TextView   account_left;
    private TextView   account_right;
    private FloatUtil(){}

    public static FloatUtil getInstance(){
        if(null == instance){
            instance = new FloatUtil();
        }

        return instance;
    }

    public void createFloatView(Context context){
        if(null != floatImage){
            Log.i(TAG, "float is already created!");
            return;
        }
        mContext = context;
        mMansger = (WindowManager)context.getApplicationContext().getSystemService(Activity.WINDOW_SERVICE);
        mParams = new WindowManager.LayoutParams();
        screen_widht = mMansger.getDefaultDisplay().getWidth();
        screen_height = mMansger.getDefaultDisplay().getHeight();

        mParams.format = PixelFormat.RGBA_8888;                                                     //图片格式为透明
        mParams.type = WindowManager.LayoutParams.TYPE_PHONE;                                       //处于所有应用顶端，状态栏之下
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;                              //不获取焦点
        mParams.gravity = Gravity.LEFT | Gravity.TOP;                                               //左上对齐
        mParams.x = float_x;
        mParams.y = float_y;                                                                        //相对于上面的原点
        mParams.width = LayoutParams.WRAP_CONTENT;
        mParams.height = LayoutParams.WRAP_CONTENT;

        mFloatLayout = (LinearLayout)LayoutInflater.from(context).inflate(R.layout.float_window, null);
        mMansger.addView(mFloatLayout, mParams);
        floatImage = (ImageView)mFloatLayout.findViewById(R.id.float_image);
        floatImage.setImageDrawable(context.getDrawable(R.drawable.xy_icon));
        account_left = (TextView)mFloatLayout.findViewById(R.id.account_left);
        account_right = (TextView)mFloatLayout.findViewById(R.id.account_right);

        /**
         * 触摸监听，主要完成浮标的位置移动
         */
        floatImage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                isMove = true;
                if(event.getAction() == MotionEvent.ACTION_DOWN){

                    return false;
                }

                //滑动操作
                if(mFloatLayout != null){
                    float_x = (int)event.getRawX();
                    float_y = (int)event.getRawY();
                    int width = mFloatLayout.getWidth();
                    int height = mFloatLayout.getHeight();
                    if(float_x + width > screen_widht){
                        float_x = screen_height - width;
                    }

                    if(float_y + height > screen_height){
                        float_y = screen_height - height;
                    }

                    //作画默认是从view的左上角画，导致在移动过程中摸不着view  所以要向上和左位置前移就能摸到了
                    mParams.x = (float_x - width/2) > 0 ? float_x - width/2 : 0;
                    mParams.y = (float_y - height/2) > 0 ? float_y - height/2 : 0;
                    mMansger.updateViewLayout(mFloatLayout, mParams);
                }

                /**
                 * 抬起默认滑动结束
                 */
                if(event.getAction() == MotionEvent.ACTION_UP){
                    isMove = false;
                    task_restore.postDelayed(retoreFloatView, 3000);                                //3s内无操作进行隐藏工作
                }

                return false;
            }
        });

        /**
         * 点击监听
         */
        floatImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //展开右边窗口
                if (float_x < screen_widht / 2) {
                    account_right.setVisibility(View.VISIBLE);
                    account_left.setVisibility(View.GONE);
                } else {
                    account_left.setVisibility(View.VISIBLE);
                    account_right.setVisibility(View.GONE);
                }
                mMansger.updateViewLayout(mFloatLayout, mParams);
                task_restore.postDelayed(retoreFloatView, 3000);
            }
        });


    }


    public void removeFloatView(){
        if(mFloatLayout == null){
            return;
        }
        mMansger.removeView(mFloatLayout);
        mMansger = null;
        mParams = null;
    }

    private boolean isMove = false;                     //判断浮标是否在操作中，操作中不允许进行掩藏任务
    private Handler task_restore = new Handler();
    /**
     * 无操作时自动掩藏浮标  使其靠边
     */
    private Runnable retoreFloatView = new Runnable() {
        @Override
        public void run() {

            account_left.setVisibility(View.GONE);
            account_right.setVisibility(View.GONE);
            if(float_x > screen_widht / 2){
                floatImage.setImageDrawable(mContext.getDrawable(R.drawable.xy_icon));                                    //设置右边浮标icon
                float_x = screen_widht - mFloatLayout.getWidth();
            }else{
                floatImage.setImageDrawable(mContext.getDrawable(R.drawable.xy_icon));
                float_x = 0;
            }
            mParams.x = float_x;
            if(isMove){
                return;
            }
            mMansger.updateViewLayout(mFloatLayout, mParams);
        }
    };

}
