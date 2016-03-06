package com.gyxian9.hx_wechat.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.gyxian9.hx_wechat.Constant;
import com.gyxian9.hx_wechat.R;
import com.gyxian9.hx_wechat.common.Utils;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int runCount = Utils.getIntValue(this, Constant.RUNCOUNT);
        //是否第一次运行
        if (runCount == 0) {
            // TODO 引导页面
        } else {
            Utils.putIntValue(this, Constant.RUNCOUNT, runCount++);
        }

        Boolean isLogin = Utils.getBooleanValue(SplashActivity.this, Constant.LOGINSTATE);
        if (isLogin) {
            getLogin();
        }else{
            mHandler.sendEmptyMessage(0);
        }
    }

    /**
     * 如果已经登陆过就直接获取用户名和密码直接登录
     */
    private void getLogin() {
        String name = Utils.getValue(this, Constant.USERID);
        String pwd = Utils.getValue(this, Constant.PASSWD);
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(name))
            getChatSerive(name, pwd);//检测服务器是否有对应的account
        else {
            //如果没有名和密码则判定为没登录过
            Utils.RemoveValue(SplashActivity.this, Constant.LOGINSTATE);
            mHandler.sendEmptyMessageDelayed(0, 600);
        }
    }

    private void getChatSerive(final String userName, final String password) {
        EMChatManager.getInstance().login(userName, password, new EMCallBack() {
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // TODO 保存用户信息
                        Utils.putBooleanValue(SplashActivity.this,
                                Constant.LOGINSTATE, true);
                        Utils.putValue(SplashActivity.this,
                                Constant.USERID, userName);
                        Utils.putValue(SplashActivity.this,
                                Constant.PASSWD, password);
                        Log.e("Token", EMChatManager.getInstance()
                                .getAccessToken());
                        Log.d("main", "登陆聊天服务器成功！");
                        // 加载群组和会话
                        EMGroupManager.getInstance().loadAllGroups();;
                        EMChatManager.getInstance().loadAllConversations();;
                        mHandler.sendEmptyMessage(0);
                    }
                });
            }

            @Override
            public void onError(int i, String s) {

            }

            @Override
            public void onProgress(int i, String s) {
                Log.d("main", "登陆聊天服务器失败！");
            }
        });
    }

    /**
     * 处理是否登录过的行为
     */
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Boolean isLogin = Utils.getBooleanValue(SplashActivity.this,
                    Constant.LOGINSTATE);
            Intent intent = new Intent();
            //如果已经登陆过，直接跳转到主界面
            if (isLogin){
//                intent.setClass(SplashActivity.this,MainActivity.class);
            }else{
//                intent.setClass(SplashActivity.this, LoginActivity.class);
            }
//            startActivity(intent);
            overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
            finish();
        }
    };
}
