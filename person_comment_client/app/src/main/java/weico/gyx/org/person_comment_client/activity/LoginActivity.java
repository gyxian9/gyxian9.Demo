package weico.gyx.org.person_comment_client.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import weico.gyx.org.person_comment_client.R;
import weico.gyx.org.person_comment_client.entity.ResponseObject;
import weico.gyx.org.person_comment_client.entity.User;
import weico.gyx.org.person_comment_client.util.JsonUtils;
import weico.gyx.org.person_comment_client.util.MyUrl;
import weico.gyx.org.person_comment_client.util.MyUtils;
import weico.gyx.org.person_comment_client.util.ShareUtils;

public class LoginActivity extends Activity implements View.OnClickListener,PlatformActionListener{

    @ViewInject(R.id.login_zhuce)
    private TextView zhuce;
    @ViewInject(R.id.weixin_btn)
    private TextView login2WeiXin;
    @ViewInject(R.id.qq_btn)
    private TextView login2QQ;
    @ViewInject(R.id.login_back)
    private ImageView loginBack;
    @ViewInject(R.id.login_username)
    private EditText username;
    @ViewInject(R.id.login_password)
    private EditText password;
    @ViewInject(R.id.login_et_yanzhengma)
    private EditText yanzhengma_et;
    @ViewInject(R.id.login_btn_yanzhengma)
    private Button yanzhengma_btn;
    @ViewInject(R.id.login_btn)
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewUtils.inject(this);
        ShareSDK.initSDK(this);

        yanzhengma_btn.setOnClickListener(this);
        loginBack.setOnClickListener(this);
        login.setOnClickListener(this);
        zhuce.setOnClickListener(this);
        login2WeiXin.setOnClickListener(this);
        login2QQ.setOnClickListener(this);

        //初始化验证码
        setRandomView(yanzhengma_btn);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_back:
                finish();
                break;
            case R.id.login_zhuce:
                startActivity(new Intent(LoginActivity.this,RegisteredActivity.class));
                break;
            case R.id.login_btn_yanzhengma:
                setRandomView(yanzhengma_btn);
                break;
            case R.id.login_btn:
                handleLogin();
                break;
            case R.id.weixin_btn:
                loginByWeChat();
                break;
            case R.id.qq_btn:
                loginByQQ();
                break;

        }
    }

    private void handleLogin() {
        final String uname = username.getText().toString();
        String upassword = password.getText().toString();
        JsonUtils jsonUtils = JsonUtils.getInstance();

        new HttpUtils().send(HttpRequest.HttpMethod.GET, MyUrl.userURL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                Gson gson = new GsonBuilder().create();
                ResponseObject<User> userResponseObject =
                        gson.fromJson(responseInfo.result,
                                new TypeToken<ResponseObject<User>>() {
                                }.getType());
                if (userResponseObject.getState() == 1) {
                    ShareUtils.putUserName(LoginActivity.this, uname);
                    String uname1 = username.getText().toString();
                    loginSuccess(uname1);
                }
                Toast.makeText(LoginActivity.this, userResponseObject.getMsg()
                        , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText(LoginActivity.this, "登陆失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loginSuccess(String uname) {
//        String uname = username.getText().toString();
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("login_name",uname);
        setResult(MyUtils.RequestLoginCode,intent);
        finish();
    }

    private void loginByQQ() {
        Platform platform = ShareSDK.getPlatform(this, QQ.NAME);
        platform.setPlatformActionListener(this);
        if(platform.isValid()){
            String uname = platform.getDb().getUserName();
            String pid = platform.getDb().getUserId();
            loginSuccess(uname);
        }else{
            platform.showUser(null);
        }
    }

    private void loginByWeChat() {
        //1.得到微信的登录平台
        Platform platform = ShareSDK.getPlatform(this, QQ.NAME);
        //2.增加监听事件授权
        platform.setPlatformActionListener(this);
        //3.判断授权是否已经验证（是否正常登录）
        if (platform.isValid()) {
            String uname = platform.getDb().getUserName();//获取三方的显示名称
            //返回我的页面
            loginSuccess(uname);
        }else{
            //如果没有授权登录
            platform.showUser(null);
        }
    }

    private void setRandomView(Button randomView) {
        randomView.setText(MyUtils.getRandom(4));
    }



    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        String uname = platform.getDb().getUserName();
        loginSuccess(uname);
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        Toast.makeText(this, platform.getName()+"授权已失败，请重试", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel(Platform platform, int i) {
        Toast.makeText(this, platform.getName()+"授权已取消", Toast.LENGTH_SHORT).show();
    }
}
