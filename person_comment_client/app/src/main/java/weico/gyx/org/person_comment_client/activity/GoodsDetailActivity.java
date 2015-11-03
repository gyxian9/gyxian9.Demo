package weico.gyx.org.person_comment_client.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.picasso.Picasso;

import weico.gyx.org.person_comment_client.R;
import weico.gyx.org.person_comment_client.entity.Goods;

public class GoodsDetailActivity extends Activity {
    @ViewInject(R.id.goods_detail_goback)
    private TextView goBack;
    @ViewInject(R.id.goods_detail_title)
    private TextView title;
    @ViewInject(R.id.goods_detail_content)
    private TextView content;
    @ViewInject(R.id.goods_detail_price)
    private TextView price;
    @ViewInject(R.id.goods_detail_value)
    private TextView value;
    @ViewInject(R.id.goods_detail_shop_title)
     private TextView shop_title;
    @ViewInject(R.id.goods_detail_img)
    private ImageView img;
//    @ViewInject(R.id.goods_detail_layout_webView)
//    private WebView layout;
//    @ViewInject(R.id.goods_detail_warm)
//    private WebView warm;

    private Goods goods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_detail);
        ViewUtils.inject(this);

        value.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        //网页自适应
//        WebSettings settings1 = layout.getSettings();
//        settings1.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        WebSettings settings2 = layout.getSettings();
//        settings2.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            goods = (Goods) bundle.get("goods");
        }

        if(goods != null){
            updateTitleImage();
            updateGoodsInfo();
            updateMoreDetails();
        }
    }
    //点击电话icon
    @OnClick({R.id.goods_detail_shop_call,R.id.goods_detail_goback})
    private void onClick(View view){
        switch (view.getId()){
            case R.id.goods_detail_shop_call:
                Intent callin = new Intent(Intent.ACTION_DIAL);
                callin.setData(Uri.parse("tel:13286365330"));
                startActivity(callin);
                break;
            case R.id.goods_detail_goback:
                finish();
                break;
        }
    }

    //webview加载内容
    private void updateMoreDetails() {

    }
    //更新商品的标题。价格
    private void updateGoodsInfo() {
        title.setText(goods.getTitle());
        shop_title.setText(goods.getTitle());
        content.setText(goods.getSortTitle());
        price.setText("￥"+goods.getPrice());
        value.setText("￥"+goods.getValue());
    }
    //更新商品图片
    private void updateTitleImage() {
        Picasso.with(this)
                .load(goods.getImgUrl())
                .placeholder(R.mipmap.ic_empty_dish)
                .into(img);
    }
}
