package weico.gyx.org.person_comment_client.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import weico.gyx.org.person_comment_client.R;
import weico.gyx.org.person_comment_client.util.ShareUtils;

public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if(ShareUtils.getWelcomeBoolean(getBaseContext())){
                    //不是第一次打开
                    startActivity(new Intent(getBaseContext(),MainActivity.class));
                }else{
                    //第一次打开，必须注释TRUE到Share
                    startActivity(new Intent(getBaseContext(),WelcomeGuideActivity.class));
                    ShareUtils.putWelcomeBoolean(getBaseContext(),true);
                }

                finish();
                return false;
            }
        }).sendEmptyMessageDelayed(0,3000);
    }


}
