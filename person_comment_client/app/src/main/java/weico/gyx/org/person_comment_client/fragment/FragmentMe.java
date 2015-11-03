package weico.gyx.org.person_comment_client.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import weico.gyx.org.person_comment_client.R;
import weico.gyx.org.person_comment_client.activity.LoginActivity;
import weico.gyx.org.person_comment_client.util.MyUtils;


public class FragmentMe extends Fragment implements View.OnClickListener{

    @ViewInject(R.id.my_index_login_text)
    private TextView loginText;
    @ViewInject(R.id.my_index_user_image)
    private ImageView userImg;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        ViewUtils.inject(this,view);
        loginText.setOnClickListener(this);
        userImg.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.my_index_login_text:
//                break;
            case R.id.my_index_user_image:
                login();
                break;
            default:
                break;
        }
    }

    private void login() {
        startActivityForResult(new Intent(getActivity()
                , LoginActivity.class)
                , MyUtils.RequestLoginCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == MyUtils.RequestLoginCode
                && resultCode == MyUtils.RequestLoginCode){
            loginText.setText(data.getStringExtra("login_name"));
            userImg.setImageResource(R.mipmap.profile_default);
        }

    }
}
