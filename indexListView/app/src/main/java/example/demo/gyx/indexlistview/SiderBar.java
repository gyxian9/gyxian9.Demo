package example.demo.gyx.indexlistview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by gyx on 2015/10/25.
 */
public class SiderBar extends View {
    // 触摸事件
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    // 26个字母
    public static String[] b = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#" };
    private int choose = -1;// 选中
    private Paint paint = new Paint();

    private TextView dialog;

    public void setDialog(TextView dialog) {
        this.dialog = dialog;
    }

    public SiderBar(Context context) {
        super(context);
    }

    public SiderBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SiderBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 获取焦点改变背景颜色.
        int height = getHeight();// 获取对应高度
        int width = getWidth(); // 获取对应宽度
        int singleHeight = height / b.length;// 获取每一个字母的高度

        for (int i = 0; i < b.length; i++) {
            paint.setColor(Color.rgb(33, 65, 98));
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            paint.setTextSize(20);
            // 选中的状态
            if (i == choose) {
                paint.setColor(Color.parseColor("#3399ff"));
                paint.setFakeBoldText(true);
            }
            // x坐标等于中间-字符串宽度的一半.| |A
            //                             ↑
            float xPos = (width - paint.measureText(b[i])) / 2;
            float yPos = singleHeight * (i + 1);
            canvas.drawText(b[i], xPos, yPos, paint);
            paint.reset();// 重置画笔
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();// 点击y坐标
        final int oldChoose = choose;// 原来的选择

        final OnTouchingLetterChangedListener
                listener = onTouchingLetterChangedListener;

        // 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数
        final int index = (int) (y / getHeight() * b.length);

        switch (action) {
            case MotionEvent.ACTION_UP:
                setBackgroundDrawable(new ColorDrawable(0x00000000));
                choose = -1;//
                invalidate();
                if (dialog != null) {
                    dialog.setVisibility(View.INVISIBLE);
                }
                break;

            default:
                setBackgroundResource(R.drawable.sidebar_background);
                if (oldChoose != index) {
                    if (index >= 0 && index < b.length) {
                        if (listener != null) {
                            listener.onTouchingLetterChanged(b[index]);
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

    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener
                                                           onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener
                = onTouchingLetterChangedListener;
    }

    public void setTextView(TextView dialog) {
        this.dialog = dialog;
    }

    /**
     * 注册一个接口
     */
    public interface OnTouchingLetterChangedListener {
        public void onTouchingLetterChanged(String s);
    }
}
