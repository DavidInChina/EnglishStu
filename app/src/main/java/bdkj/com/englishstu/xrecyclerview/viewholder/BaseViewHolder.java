package bdkj.com.englishstu.xrecyclerview.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * Created by davidinchina on 2016/10/29.
 */
public abstract class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
    protected RecycleItemClickListener mListener;
    protected RecycleItemLongClickListener mLongClickListener;

    public BaseViewHolder(View view) {
        super(view);
        initViews(view);
    }

    public BaseViewHolder(View rootView, RecycleItemClickListener listener, RecycleItemLongClickListener longClickListener) {
        super(rootView);
        initViews(rootView);
        mListener = listener;
        mLongClickListener = longClickListener;
        rootView.setOnClickListener(this);
        rootView.setOnLongClickListener(this);
    }

    abstract public void initViews(View view);

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onItemClick(v, getPosition());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mLongClickListener != null) {
            mLongClickListener.onItemLongClick(v, getPosition());
        }
        return true;
    }
}