package weico.gyx.org.person_comment_client.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import weico.gyx.org.person_comment_client.R;
import weico.gyx.org.person_comment_client.fragment.FragmentHome;
import weico.gyx.org.person_comment_client.fragment.FragmentMe;
import weico.gyx.org.person_comment_client.fragment.FragmentTuanGou;

public class MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener{
    @ViewInject(R.id.main_bottom_tabs)
    private RadioGroup group;
    @ViewInject(R.id.main_home)
    private RadioButton main_home;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);

        fragmentManager = getSupportFragmentManager();
        //设置默认选中
        main_home.setChecked(true);
        group.setOnCheckedChangeListener(this);
        //切换不同的fragment,让他默认显示HOME
        changeFragment(new FragmentHome(),false);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.main_home:
                changeFragment(new FragmentHome(),true);
                break;
            case R.id.main_tuangou:
                changeFragment(new FragmentTuanGou(),true);
                break;
//            case R.id.main_search:
//                changeFragment(new FragmentSearch(),true);
//                break;
            case R.id.main_me:
                changeFragment(new FragmentMe(),true);
                break;
        }
    }
    //切换不同的fragment

    /**
     *
     * @param fragment 传入要切换的fragment
     * @param isInit   默认为FALSE就是在没选中的情况下让他加入回退栈,
	 *官方的说法是取决于你是否要在回退的时候显示上一个Fragment。
     */
    public void changeFragment(Fragment fragment,boolean isInit){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main_content,fragment);
        if(!isInit){
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }
}
