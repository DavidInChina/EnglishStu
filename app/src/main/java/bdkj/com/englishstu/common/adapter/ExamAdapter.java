package bdkj.com.englishstu.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import bdkj.com.englishstu.R;
import bdkj.com.englishstu.base.JsonEntity;
import bdkj.com.englishstu.common.beans.Exam;
import bdkj.com.englishstu.common.dbinfo.TeaDbUtils;
import bdkj.com.englishstu.common.tool.TimeUtil;
import bdkj.com.englishstu.common.tool.ToastUtil;
import bdkj.com.englishstu.swipeitem.widget.SwipeItemLayout;
import bdkj.com.englishstu.xrecyclerview.viewholder.BaseViewHolder;
import bdkj.com.englishstu.xrecyclerview.viewholder.RecycleItemClickListener;
import bdkj.com.englishstu.xrecyclerview.viewholder.RecycleItemLongClickListener;

/**
 * Created by davidinchina on 2017/6/6.
 */

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ViewHolder> {
    private List<Exam> examList = null;
    private Context mContext;
    public RecycleItemClickListener clickListener;
    public RecycleItemLongClickListener longClickListener;

    /**
     * 当前处于打开状态的item
     */
    private List<SwipeItemLayout> mOpenedSil = new ArrayList<>();

    public ExamAdapter(Context mContext, ArrayList<Exam> datas) {
        this.mContext = mContext;
        this.examList = datas;
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exam_layout, parent, false);
        return new ViewHolder(view, clickListener, longClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Exam exam = examList.get(position);
        holder.tvExamName.setText(exam.getName());
        holder.tvExamLevel.setText("试题难度：" + exam.getLevel());
        Glide.with(mContext).load(exam.getLogo()).into(holder.ivLeftImg);
        holder.tvExamStatus.setText("试题权限：" + exam.getType());
        holder.tvExamTime.setText("入库日期：" + TimeUtil.date2String(exam.getUpdateDate()));
        holder.tvExamContent.setText("试题内容：" + exam.getAbout());
        SwipeItemLayout swipeRoot = holder.itemContactSwipeRoot;
        swipeRoot.setDelegate(new SwipeItemLayout.SwipeItemLayoutDelegate() {
            @Override
            public void onSwipeItemLayoutOpened(SwipeItemLayout swipeItemLayout) {
                closeOpenedSwipeItemLayoutWithAnim();
                mOpenedSil.add(swipeItemLayout);
            }

            @Override
            public void onSwipeItemLayoutClosed(SwipeItemLayout swipeItemLayout) {
                mOpenedSil.remove(swipeItemLayout);
            }

            @Override
            public void onSwipeItemLayoutStartOpen(SwipeItemLayout swipeItemLayout) {
                closeOpenedSwipeItemLayoutWithAnim();
            }
        });
        holder.itemContactDelete.setTag(exam);
        holder.itemContactDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Exam exam1 = (Exam) v.getTag();
                JsonEntity entity = TeaDbUtils.deleteExam(exam1.getId());
                if (entity.getCode() == 0) {
                    examList.remove(exam1);
                    notifyDataSetChanged();
                } else {
                    ToastUtil.show(mContext, entity.getMsg());
                }
                closeOpenedSwipeItemLayoutWithAnim();
            }
        });
        holder.baseView.setTag(exam);
    }

    public void closeOpenedSwipeItemLayoutWithAnim() {
        for (SwipeItemLayout sil : mOpenedSil) {
            sil.closeWithAnim();
        }
        mOpenedSil.clear();
    }

    @Override
    public int getItemCount() {
        return examList.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends BaseViewHolder {
        ImageView ivLeftImg;
        TextView tvExamName;
        TextView tvExamLevel;
        TextView tvExamStatus;
        TextView tvExamTime;
        TextView tvExamContent;
        TextView tvRightStatus;
        TextView tvRightStatus2;

        TextView itemContactDelete;
        SwipeItemLayout itemContactSwipeRoot;
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
            itemContactDelete = (TextView) view.findViewById(R.id.item_contact_delete);
            itemContactSwipeRoot = (SwipeItemLayout) view.findViewById(R.id.item_contact_swipe_root);
            ivLeftImg = (ImageView) view.findViewById(R.id.iv_left_img);
            tvExamName = (TextView) view.findViewById(R.id.tv_exam_name);
            tvExamLevel = (TextView) view.findViewById(R.id.tv_exam_level);
            tvExamStatus = (TextView) view.findViewById(R.id.tv_exam_status);
            tvExamTime = (TextView) view.findViewById(R.id.tv_exam_time);
            tvExamContent = (TextView) view.findViewById(R.id.tv_exam_content);

            tvRightStatus = (TextView) view.findViewById(R.id.tv_right_status);
            tvRightStatus2 = (TextView) view.findViewById(R.id.tv_right_status2);
        }
    }
}
