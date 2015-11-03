package weico.gyx.org.person_comment_client.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import weico.gyx.org.person_comment_client.R;

/**
 * Created by manager on 2015/8/28.
 */
public class SideBar extends View {

    public static String[] b = { "#","A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

    private OnTouchingLetterChangedListener listener;

    private int choose = -1;

    private Paint mPaint = new Paint();

    private TextView dialog;

    public void setDialog(TextView dialog) {
        this.dialog = dialog;
    }

    public SideBar(Context context) {
        super(context);
    }

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //获取自定义view的高低
        int height = getHeight();
        int width = getWidth();
        //设定每个字幕也所在空间的高度
        int each_height = height / b.length;


        //给每个字母绘制出来
        for(int i = 0; i < b.length ; i++){
            //设置字体颜色为灰色 默认粗体 大小为20sp
            mPaint.setColor(Color.rgb(33, 65, 98));
            mPaint.setTypeface(Typeface.DEFAULT_BOLD);
            mPaint.setAntiAlias(true);
            mPaint.setTextSize(20);

            if(i == choose){
                mPaint.setColor(Color.parseColor("#3399ff"));
                mPaint.setFakeBoldText(true);
            }
            // x坐标等于中间-字符串宽度的一半.
            float x = (width - mPaint.measureText(b[i])) / 2;
            float y = (1 + i) * each_height;
            canvas.drawText(b[i],x,y,mPaint);
            mPaint.reset();
        }

    }


    /**
     * 设置sideBar的点击监听
     * @param event
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        final int action = event.getAction();
        final float y = event.getY();//点击的Y坐标
        final OnTouchingLetterChangedListener changedListener = listener;

        //点击y坐标所占总高度的比例*数组的长度就等于点击b中的个数.
        final int index = (int) (y / getHeight() * b.length);
        switch(action){
            case MotionEvent.ACTION_UP://抬起
                setBackgroundResource(android.R.color.transparent);
                choose = -1;
                invalidate();
                if (dialog != null) {
                    dialog.setVisibility(View.INVISIBLE);
                }
                break;
            default:
                setBackgroundResource(R.drawable.siderbar_bg);
                if (choose != index){
                    if (index > 0 && index < b.length) {
                        if(changedListener != null){
                            changedListener.onTouchingLetterChanged(b[index]);
                        }
                        if (dialog != null) {
                            dialog.setText(b[index]);
                            dialog.setVisibility(View.VISIBLE);
                        }

                        choose = index;
                        invalidate();
                    }
                }
                break;
        }
        return true;
    }



    /**
     * 对外公开方法
     * 设置监听接口
     */
    public interface OnTouchingLetterChangedListener{
        public void onTouchingLetterChanged(String s);

    }
    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener listener){
        this.listener = listener;
    }

}
