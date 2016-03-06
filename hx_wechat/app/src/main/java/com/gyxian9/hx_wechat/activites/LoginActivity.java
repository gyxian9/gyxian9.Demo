package com.gyxian9.hx_wechat.activites;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.gyxian9.hx_wechat.Constant;
import com.gyxian9.hx_wechat.R;
import com.gyxian9.hx_wechat.base.BaseActivity;
import com.gyxian9.hx_wechat.common.DES;
import com.gyxian9.hx_wechat.common.Utils;
import com.gyxian9.hx_wechat.net.BaseJsonRes;
import com.juns.health.net.loopj.android.http.RequestParams;

import org.apache.http.message.BasicNameValuePair;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private TextView txt_title;
    private ImageView img_back;
    private Button btn_login, btn_register;
    private EditText et_usertel, et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void initControl() {
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_title.setText("登陆");
        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setVisibility(View.VISIBLE);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_qtlogin);
        et_usertel = (EditText) findViewById(R.id.et_usertel);
        et_password = (EditText) findViewById(R.id.et_password);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        img_back.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        findViewById(R.id.tv_wenti).setOnClickListener(this);
        et_usertel.addTextChangedListener(new TextChange());
        et_password.addTextChangedListener(new TextChange());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                Utils.finish(LoginActivity.this);
                break;
            case R.id.tv_wenti:
//                Utils.start_Activity(LoginActivity.this, WebViewActivity.class,
//                        new BasicNameValuePair(Constant.TITLE, "帮助"),
//                        new BasicNameValuePair(Constant.URL,
//                                "http://weixin.qq.com/"));
                break;
            case R.id.btn_qtlogin:
//                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                break;
            case R.id.btn_login:
                getLogin();
                break;
            default:
                break;
        }
    }

    private void getLogin() {
        String userName = et_usertel.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        getLoadingDialog("正在登录...").show();
        getLogin(userName, password);
    }

    /**
     * 登录方法
     * @param userName
     * @param password
     */
    private void getLogin(final String userName, final String password) {
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {

            RequestParams params = new RequestParams();
            params.put("username", userName);
            params.put("password", DES.md5Pwd(password));
            getLoadingDialog("正在登录...  ").show();
            //网络请求服务器 http://wechatjuns.sinaapp.com/index.php/user/login
            netClient.post(Constant.Login_URL, params, new BaseJsonRes() {

                @Override
                public void onMySuccess(String data) {
                    //记录用户信息
                    Utils.putValue(LoginActivity.this, Constant.USERINFO, data);
                    //设置用户状态为登录
                    Utils.putBooleanValue(LoginActivity.this,
                            Constant.LOGINSTATE, true);
                    //记录用户名和密码
                    Utils.putValue(LoginActivity.this, Constant.NAME, userName);
                    Utils.putValue(LoginActivity.this, Constant.PASSWD,
                            DES.md5Pwd(password));

                    getChatSerive(userName, DES.md5Pwd(password));
                }

                @Override
                public void onMyFailure() {
                    getLoadingDialog("正在登录").dismiss();
                }
            });
        } else {
            Utils.showLongToast(LoginActivity.this, "请填写账号或密码！");
        }
    }

    /**
     * 虽然把数据保存到服务器，不过还是要通过环信保存用户列表
     * @param userName
     * @param password
     */
    private void getChatSerive(final String userName,final String password) {
        EMChatManager.getInstance().login(userName, password, new EMCallBack() {// 回调
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Utils.putBooleanValue(LoginActivity.this,
                                Constant.LOGINSTATE, true);
                        Utils.putValue(LoginActivity.this,
                                Constant.USERID, userName);
                        Utils.putValue(LoginActivity.this,
                                Constant.PASSWD, password);

                        Log.d("main", "登陆聊天服务器成功！");
                        // 加载群组和会话
                        EMGroupManager.getInstance().loadAllGroups();
                        EMChatManager.getInstance()
                                .loadAllConversations();
                        getLoadingDialog("正在登录...").dismiss();
//                        Intent intent = new Intent(LoginActivity.this,
//                                MainActivity.class);
//                        startActivity(intent);
                        overridePendingTransition(R.anim.push_up_in,
                                R.anim.push_up_out);
                        finish();
                    }
                });
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.d("main", "登陆聊天服务器失败！");
                runOnUiThread(new Runnable() {
                    public void run() {
                        getLoadingDialog("正在登录...").dismiss();
                        Utils.showLongToast(LoginActivity.this, "登陆失败！");
                    }
                });
            }
        });
    }

    class TextChange implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            boolean Sign2 = et_usertel.getText().length() > 0;
            boolean Sign3 = et_password.getText().length() > 4;
            if (Sign2 & Sign3) {
                btn_login.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.btn_bg_green));
                btn_login.setEnabled(true);
            } else {
                btn_login.setBackgroundDrawable(getResources().getDrawable(
                        R.drawable.btn_enable_green));
                btn_login.setTextColor(0xFFD0EFC6);
                btn_login.setEnabled(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }
}
