package bdkj.com.englishstu.common.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import bdkj.com.englishstu.R;


/**
 * Created by davidinchina on 2017/7/7.
 * 语音评测可视化提示
 */

public class IseDialog extends Dialog {
    private Context mContext;
    private TextView textView;
    private ImageView ivVoiceType;

    public IseDialog(@NonNull Context context) {
        super(context, R.style.myDialog);
        this.mContext = context;
        init();
    }

    public IseDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        init();
    }

    protected IseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
        init();
    }

    public void init() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_ise_layout, null);
        textView = (TextView) view.findViewById(R.id.tv_title);
        ivVoiceType = (ImageView) view.findViewById(R.id.iv_voice_type);
        addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void hide(int type, String code) {
        //隐藏dialog，正确或者失败
        if (type == 0) {//正确
            textView.setText("阅读结束");
            ivVoiceType.setImageDrawable(getContext().getDrawable(R.drawable.dialog_waiting));
        } else {
            textView.setText("阅读失败 " + code);
            ivVoiceType.setImageDrawable(getContext().getDrawable(R.drawable.dialog_warning));
        }
        ivVoiceType.clearAnimation();
        textView.setText("正在倾听");
        this.cancel();
    }

    public void setBegin() {
        textView.setText("正在倾听");
        this.setCancelable(false);

    }

    public void showLoading() {
        //等待结束
        Animation operatingAnim = AnimationUtils.loadAnimation(mContext, R.anim.circle);
        LinearInterpolator lin = new LinearInterpolator();
        operatingAnim.setInterpolator(lin);
        textView.setText("正在保存");
        ivVoiceType.setImageDrawable(getContext().getDrawable(R.drawable.dialog_waiting));
        ivVoiceType.startAnimation(operatingAnim);
    }

    public void showChange(int volume) {
        //根据音量改变内容
        if (volume > 0) {
            ivVoiceType.setImageDrawable(getContext().getDrawable(R.drawable.dialog_full));
        } else {
            ivVoiceType.setImageDrawable(getContext().getDrawable(R.drawable.dialog_empty));
        }
    }


}
