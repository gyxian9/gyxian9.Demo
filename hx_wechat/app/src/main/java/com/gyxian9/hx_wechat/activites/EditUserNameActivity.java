package com.gyxian9.hx_wechat.activites;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyxian9.hx_wechat.Constant;
import com.gyxian9.hx_wechat.R;
import com.gyxian9.hx_wechat.base.BaseActivity;
import com.gyxian9.hx_wechat.common.Utils;
import com.gyxian9.hx_wechat.net.BaseJsonRes;
import com.juns.health.net.loopj.android.http.RequestParams;

public class EditUserNameActivity extends BaseActivity implements View.OnClickListener{

    private TextView txt_title;
    private ImageView img_back;
    private EditText edit_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_name);
    }

    @Override
    protected void initControl() {
        txt_title = (TextView) findViewById(R.id.txt_title);
        txt_title.setText("欢迎");
        img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setVisibility(View.GONE);
        edit_name = (EditText) findViewById(R.id.edit_name);
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
        findViewById(R.id.btn_start).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                Utils.finish(EditUserNameActivity.this);
                break;
            case R.id.btn_start:
                setUserMsg();
                break;
            default:
                break;
        }
    }

    private void setUserMsg() {
        String telphone = Utils.getValue(EditUserNameActivity.this,
                Constant.NAME);
        String name = edit_name.getText().toString();
        if (TextUtils.isEmpty(name)) {
            Utils.showLongToast(EditUserNameActivity.this, "请填写您的昵称！ ");
            return;
        }
        RequestParams params = new RequestParams();
        params.put("username", name);
        params.put("telphone", telphone);
        getLoadingDialog("正在加载...  ").show();
        netClient.post(Constant.UpdateInfoURL, params, new BaseJsonRes() {

            @Override
            public void onMySuccess(String data) {
                Utils.putValue(EditUserNameActivity.this, Constant.USERINFO,
                        data);
                getLoadingDialog("正在加载").dismiss();
                Intent intent = new Intent(EditUserNameActivity.this,
                        MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.push_up_in, R.anim.push_up_out);
                finish();
            }

            @Override
            public void onMyFailure() {
                getLoadingDialog("正在登录").dismiss();
            }
        });
    }
}
