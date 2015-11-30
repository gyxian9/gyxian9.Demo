package com.gyxian9.photobrower2;

import android.support.v4.app.FragmentActivity;

import android.os.Bundle;

import android.util.Log;
import android.view.Window;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.truba.touchgallery.GalleryWidget.FilePagerAdapter;
import ru.truba.touchgallery.GalleryWidget.GalleryViewPager;

/**
 * 实现类似Gallery的功能，全屏显示图片
 */
public class PhotoActivity extends FragmentActivity {

    private GalleryViewPager viewPager;
    private String[] list;
    private int index;
    private List<String> items ;

    public PhotoActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_photo);

        Bundle b = getIntent().getExtras();
        index = b.getInt("imageIndex");
        list = b.getStringArray("images");
        items = new ArrayList<String>();
        Collections.addAll(items,list);
        Log.e("TAG", items.toString());
        init();
    }

    private void init() {
        viewPager = (GalleryViewPager) findViewById(R.id.viewpager);
        FilePagerAdapter adapter = new FilePagerAdapter(this,items);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(index);
    }
}
