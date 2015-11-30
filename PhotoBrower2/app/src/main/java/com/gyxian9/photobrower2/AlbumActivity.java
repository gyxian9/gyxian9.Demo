package com.gyxian9.photobrower2;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyxian9.photobrower2.adapter.AlbumsItemAdapter;
import com.gyxian9.photobrower2.bean.Photo;
import com.gyxian9.photobrower2.bean.PhotoAlbum;

import java.util.List;

/**
 * 这里是某个相册内的所有图片
 */
public class AlbumActivity extends Activity{

    private GridView gridView;
    private TextView title;
    private ImageView back,image2;

    private AlbumsItemAdapter adapter;

    private List<Photo> imagesList;

    private PhotoAlbum photoAlbum;

    String[] imgPath;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            String titleName = msg.obj.toString();
            title.setText(titleName);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_album);
        init();
        setlistener();//设置监听
    }

    private void setlistener() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(AlbumActivity.this,PhotoActivity.class);
                Bundle b = new Bundle();
                b.putInt("imageIndex", position);
                b.putStringArray("images",imgPath);
                intent.putExtras(b);
                startActivity(intent);
            }
        });


    }

    private void init() {
        gridView = (GridView) findViewById(R.id.album_item_gridv);
        back = (ImageView) findViewById(R.id.back);
        title = (TextView) findViewById(R.id.head_bar);


        Intent intent = getIntent();
        photoAlbum = (PhotoAlbum) intent.getSerializableExtra("imageList");
        imagesList = photoAlbum.getImageList();
        adapter = new AlbumsItemAdapter(AlbumActivity.this,photoAlbum.getImageList());
        gridView.setAdapter(adapter);

        imgPath = new String[imagesList.size()];
        for (int i = 0; i < imagesList.size(); i++) {
            String path = imagesList.get(i).getImagePath();
            imgPath[i] = path;
        }

        if (photoAlbum != null){
            Message msg = new Message();
            msg.obj = photoAlbum.getDirName();
            mHandler.sendMessage(msg);
        }

    }


}
