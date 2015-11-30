package com.gyxian9.photobrower2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.gyxian9.photobrower2.adapter.AlbumsAdapter;
import com.gyxian9.photobrower2.bean.PhotoAlbum;
import com.gyxian9.photobrower2.utils.PhotoAlbumHelper;

import java.util.List;

/**
 * 这里要实现本地图片所属的文件夹和其第一张图片
 */
public class MainActivity extends Activity {

    private GridView gridView;
    private AlbumsAdapter adapter;
    //应用打开界面默认显示图片文件夹
    private List<PhotoAlbum> photoAlbumList;

    private PhotoAlbumHelper helper;//加载缩略图和扫描本地图片的工具类

    private ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        init();     //初始化布局
        loadData(); //加载数据
        itemClick();
    }

    private void init() {
        back = (ImageView) findViewById(R.id.bottom).findViewById(R.id.back);

        gridView = (GridView) findViewById(R.id.album_gridv);
        adapter = new AlbumsAdapter(MainActivity.this);
        gridView.setAdapter(adapter);
    }

    private void loadData() {
        helper = PhotoAlbumHelper.getIstance();//获取工具类实例
        helper.initContentResolver(MainActivity.this);//初始化ContentResolver对象
        //实现接口方法
        helper.setGetAlbumInterface(new PhotoAlbumHelper.GetAlbumInterface() {
            @Override
            public void getAlbumInterface(List<PhotoAlbum> list) {
                adapter.setArrayList(list);//给适配器提供数据
                adapter.notifyDataSetChanged();//刷新适配器
                MainActivity.this.photoAlbumList = list;//获取数据
            }
        });
        helper.execute(false);//异步线程执行
    }

    private void itemClick(){
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, AlbumActivity.class);
                intent.putExtra("imageList", photoAlbumList.get(position));
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
