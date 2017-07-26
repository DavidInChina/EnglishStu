package bdkj.com.englishstu.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bdkj.com.englishstu.R;
import bdkj.com.englishstu.common.xml.Result;
import bdkj.com.englishstu.swipeitem.widget.SwipeItemLayout;
import bdkj.com.englishstu.xrecyclerview.viewholder.BaseViewHolder;
import bdkj.com.englishstu.xrecyclerview.viewholder.RecycleItemClickListener;
import bdkj.com.englishstu.xrecyclerview.viewholder.RecycleItemLongClickListener;

/**
 * author:davidinchina on 2017/7/20 17:39
 * email:davicdinchina@gmail.com
 * tip:
 */
public class MarkResultAdapter extends RecyclerView.Adapter<MarkResultAdapter.ViewHolder> {
    private List<Result> noteList = null;
    private Context mContext;
    public RecycleItemClickListener clickListener;
    public RecycleItemLongClickListener longClickListener;

    /**
     * 当前处于打开状态的item
     */
    private List<SwipeItemLayout> mOpenedSil = new ArrayList<>();

    public MarkResultAdapter(Context mContext, ArrayList<Result> datas) {
        this.mContext = mContext;
        this.noteList = datas;
    }

    public RecycleItemClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(RecycleItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public RecycleItemLongClickListener getLongClickListener() {
        return longClickListener;
    }

    public void setLongClickListener(RecycleItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    @Override
    public MarkResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mark_detail_layout, parent, false);
        return new MarkResultAdapter.ViewHolder(view, clickListener, longClickListener);
    }

    @Override
    public void onBindViewHolder(MarkResultAdapter.ViewHolder holder, int position) {
        Result result = noteList.get(position);
        holder.tvTestContet.setText("评测内容：" + result.content);
        int score = (int) (result.total_score * 20);
        holder.tvTestSocre.setText("评测得分：" + score);
        holder.tvTestTime.setText("评测类别：" + result.category);
        String isWrong = result.is_rejected ? "是" : "否";
        holder.tvTestWrong.setText("是否乱读：" + isWrong);
        holder.baseView.setTag(result);
    }

    public void closeOpenedSwipeItemLayoutWithAnim() {
        for (SwipeItemLayout sil : mOpenedSil) {
            sil.closeWithAnim();
        }
        mOpenedSil.clear();
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends BaseViewHolder {
        TextView tvTestContet;
        TextView tvTestSocre;
        TextView tvTestTime;
        TextView tvTestWrong;
        public View baseView;

        public ViewHolder(View view) {
            super(view);
        }

        public ViewHolder(View rootView, RecycleItemClickListener listener, RecycleItemLongClickListener longClickListener) {
            super(rootView, listener, longClickListener);
        }

        @Override
        public void initViews(View view) {
            baseView = view;
            tvTestContet = (TextView) view.findViewById(R.id.tv_test_contet);
            tvTestSocre = (TextView) view.findViewById(R.id.tv_test_score);
            tvTestTime = (TextView) view.findViewById(R.id.tv_test_time);
            tvTestWrong = (TextView) view.findViewById(R.id.tv_test_wrong);
        }
    }
}
