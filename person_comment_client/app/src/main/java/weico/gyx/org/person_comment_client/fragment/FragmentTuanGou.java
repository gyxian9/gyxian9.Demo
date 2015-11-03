package weico.gyx.org.person_comment_client.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnItemClick;
import com.squareup.picasso.Picasso;

import java.util.List;

import weico.gyx.org.person_comment_client.R;
import weico.gyx.org.person_comment_client.activity.GoodsDetailActivity;
import weico.gyx.org.person_comment_client.entity.Goods;
import weico.gyx.org.person_comment_client.entity.ResponseObject;
import weico.gyx.org.person_comment_client.util.MyUrl;


public class FragmentTuanGou extends Fragment {

    private int page,size = 5,count;
    @ViewInject(R.id.index_listGoods)
    private PullToRefreshListView listGoods;
    private MyAdapter adapter;
    private List<Goods> goods;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tuan_gou,null);
        ViewUtils.inject(this, view);
        //设置商品列表信息
        listGoods.setMode(PullToRefreshBase.Mode.BOTH);//支持上下拉
        listGoods.setScrollingWhileRefreshingEnabled(true);
        listGoods.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                //下拉刷新y<0
                loadDatas(listGoods.getScrollY() < 0);
            }
        });
        //首次要自动加载数据
        new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                listGoods.setRefreshing();
                return true;
            }
        }).sendEmptyMessageDelayed(0,300);

        return view;
    }

    //请求数据
    public void loadDatas(final boolean isFirst){
        if(isFirst){
            page = 1;
        }else{
            page++;
        }
        RequestParams params = new RequestParams();
        params.addQueryStringParameter("page", page + "");
        params.addQueryStringParameter("size", size + "");
        new HttpUtils().send(HttpRequest.HttpMethod.GET, MyUrl.goodsURL,
                params, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        listGoods.onRefreshComplete();
                        Gson gson = new Gson();
                        ResponseObject<List<Goods>> list = gson.fromJson(responseInfo.result,
                                new TypeToken<ResponseObject<List<Goods>>>() {
                                }.getType());

                        page = list.getPage();
                        size = list.getSize();
                        count = list.getCount();
                        Log.e("TAG", "page " + page + " ; size " + size + " ; count" + count);

                        //判断上拉还是下拉
                        if (isFirst) {//下拉
                            goods = list.getData();
                            adapter = new MyAdapter();
                            listGoods.setAdapter(adapter);
                        } else {//上啦
                            goods.addAll(list.getData());
                            adapter.notifyDataSetChanged();
                        }

                        if (count == page) {
                            listGoods.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        listGoods.onRefreshComplete();//停止刷新
                        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @OnItemClick(R.id.index_listGoods)
    private void onItemClick(AdapterView<?> parent, View view,
                             int position, long id){
        Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
        intent.putExtra("goods",goods.get(position - 1));
        startActivity(intent);
    }



    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return goods.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyHolder holder = null;

            if(convertView == null){

                holder = new MyHolder();
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tuan_gou_list_item,null);
                ViewUtils.inject(holder,convertView);
                convertView.setTag(holder);

            }else{
                holder = (MyHolder) convertView.getTag();
            }

            Goods g = goods.get(position);

            Picasso.with(parent.getContext())
                    .load(g.getImgUrl())
                    .placeholder(R.mipmap.ic_empty_dish)
                    .into(holder.image);


            StringBuffer sb = new StringBuffer("￥"+g.getValue());
            //添加中划线
            SpannableString spannable = new SpannableString(sb);
            spannable.setSpan(new StrikethroughSpan(), 0, sb.length(),
                    Spanned.SPAN_INCLUSIVE_INCLUSIVE);


            holder.title.setText(g.getTitle());
            holder.content.setText(g.getSortTitle());
            holder.price.setText("￥"+g.getPrice());
            holder.value.setText(spannable);
            holder.count.setText(g.getDetail());

            return convertView;
        }
    }
    class MyHolder{
        @ViewInject(R.id.index_goods_item_title)
        TextView title;
        @ViewInject(R.id.index_goods_item_content)
        TextView content;
        @ViewInject(R.id.index_goods_item_price)
        TextView price;
        @ViewInject(R.id.index_goods_item_value)
        TextView value;
        @ViewInject(R.id.index_goods_item_count)
        TextView count;
        @ViewInject(R.id.index_goods_item_image)
        ImageView image;
    }


}
