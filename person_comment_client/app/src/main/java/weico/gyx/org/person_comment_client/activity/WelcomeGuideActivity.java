package weico.gyx.org.person_comment_client.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.List;

import weico.gyx.org.person_comment_client.R;

public class WelcomeGuideActivity extends Activity {
    @ViewInject(R.id.welcome_guide_btn)
    private Button gudie_btn;
    @ViewInject(R.id.welcome_wiewPager)
    private ViewPager viewPager;

    private List<View> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_guide);
        ViewUtils.inject(this);
        initViewPager();
    }


    @OnClick(R.id.welcome_guide_btn)
    public void click(View view){
        startActivity(new Intent(getBaseContext(),MainActivity.class));
        finish();
    }


    private void initViewPager() {
        list = new ArrayList<View>();

        ImageView iv1 = new ImageView(this);
        iv1.setImageResource(R.mipmap.guide_01);
        list.add(iv1);

        ImageView iv2 = new ImageView(this);
        iv2.setImageResource(R.mipmap.guide_02);
        list.add(iv2);

        ImageView iv3 = new ImageView(this);
        iv3.setImageResource(R.mipmap.guide_03);
        list.add(iv3);

        viewPager.setAdapter(new MyPagerAdapter());

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            //滑动触发事件
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            //页卡被选中事件
            @Override
            public void onPageSelected(int position) {
                if(position == 2){
                    gudie_btn.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(
                            WelcomeGuideActivity.this,R.anim.welcome_btn_anim);
                    gudie_btn.startAnimation(animation);

                }else{
                    //没选中继续隐藏
                    gudie_btn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class MyPagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        //初始化item    container相当于ViewPager本身
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }
        //item销毁
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));
        }
    }



}
