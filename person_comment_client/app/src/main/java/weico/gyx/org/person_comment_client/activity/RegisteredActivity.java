package weico.gyx.org.person_comment_client.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import weico.gyx.org.person_comment_client.R;

public class RegisteredActivity extends Activity implements View.OnClickListener{

    @ViewInject(R.id.registered_back)
    private ImageView registeredBack;

    @ViewInject(R.id.registered_btn)
    private Button registeredBtn;

    @ViewInject(R.id.registered_get_yanzhengma)
    private Button registeredYanZhengMa;

    @ViewInject(R.id.registered_phone_num)
    private EditText phoneNum;

    @ViewInject(R.id.registered_yanzhengma)
    private EditText yanZhengMa;

    @ViewInject(R.id.registered_password)
    private EditText password;

    @ViewInject(R.id.registered_checkbox)
    private CheckBox checkBox;

    private CountTimer countTimer;
    private EventHandler eventHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);
        ViewUtils.inject(this);

        countTimer = new CountTimer(60000,1000);

        registeredBtn.setOnClickListener(this);
        registeredBack.setOnClickListener(this);
        registeredYanZhengMa.setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.registered_get_yanzhengma:
                countTimer.start();
                sendSMSRandom();
                break;
            case R.id.registered_back:
                finish();
                break;
            case R.id.registered_btn:
                SMSSDK.submitVerificationCode("86",phoneNum.getText().toString()
                        ,yanZhengMa.getText().toString());
                break;
            default:
                break;
        }
    }

    public void sendSMSRandom(){
        SMSSDK.initSDK(this,"a9ed586c5d7c","b99fe0cc62d90f848521116a114df073");

        eventHandler = new EventHandler(){
            @Override
            public void afterEvent(int event, int result, Object o) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        // 提交验证码成功
                        Log.e("tag","验证码校验成功");
                        System.out.println("验证码校验成功");
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        // 获取验证码成功
                        Log.e("tag","验证码发送成功");
                        System.out.println("验证码发送成功");
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        // 返回支持发送验证码的国家列表
                    }
                } else {
                    ((Throwable) o).printStackTrace();
                }
            }
        };

        SMSSDK.registerEventHandler(eventHandler);

        String number = phoneNum.getText().toString();
        SMSSDK.getVerificationCode("86",number.toString());
    }

    public class CountTimer extends CountDownTimer{

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         *                          时间间隔是多长的时间
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         *                          回调onTick方法，没隔多久执行一次
         *
         */
        public CountTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //间隔时间内执行的操作
        @Override
        public void onTick(long millisUntilFinished) {
            registeredYanZhengMa.setText(millisUntilFinished/1000+"秒后发送");
            registeredYanZhengMa.setBackgroundResource(R.mipmap.btn_light_press9);
            registeredYanZhengMa.setClickable(false);
        }

        //间隔时间结束的时候调用的方法
        @Override
        public void onFinish() {
            registeredYanZhengMa.setText("获取验证码");
            registeredYanZhengMa.setBackgroundResource(R.mipmap.my_register_check_pass);
            registeredYanZhengMa.setClickable(true);
        }
    }

}


