package com.gyxian9.hx_wechat.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;

import com.gyxian9.hx_wechat.App;
import com.gyxian9.hx_wechat.common.Utils;
import com.gyxian9.hx_wechat.dialog.FlippingLoadingDialog;
import com.gyxian9.hx_wechat.net.NetClient;

import org.apache.http.message.BasicNameValuePair;

/**
 * Created by gyxian9 on 2016/3/6.
 */
public abstract class BaseActivity extends Activity {
    protected Activity context;
    protected NetClient netClient;
    protected FlippingLoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        App.getInstance().addActivity(this);
        netClient = new NetClient(this);
        initControl();
        initView();
        initData();
        setListener();
    }

    /**
     * 设置back键事件
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Utils.finish(this);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 绑定控件id
     */
    protected abstract void initControl();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 设置监听
     */
    protected abstract void setListener();

    /**
     * 打开 Activity
     *
     * @param activity
     * @param cls
     * @param name
     */
    public void start_Activity(Activity activity, Class<?> cls,
                               BasicNameValuePair... name) {
        Utils.start_Activity(activity, cls, name);
    }

    /**
     * 关闭 Activity
     *
     * @param activity
     */
    public void finish(Activity activity) {
        Utils.finish(activity);
    }

    /**
     * 判断是否有网络连接
     */
    public boolean isNetworkAvailable(Context context) {
        return Utils.isNetworkAvailable(context);
    }

    public FlippingLoadingDialog getLoadingDialog(String msg) {
        if (mLoadingDialog == null)
            mLoadingDialog = new FlippingLoadingDialog(this, msg);
        return mLoadingDialog;
    }
}
