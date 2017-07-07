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
import bdkj.com.englishstu.common.beans.Classes;
import bdkj.com.englishstu.common.dbinfo.AdmDbUtils;
import bdkj.com.englishstu.common.tool.TimeUtil;
import bdkj.com.englishstu.common.tool.ToastUtil;
import bdkj.com.englishstu.swipeitem.widget.SwipeItemLayout;
import bdkj.com.englishstu.xrecyclerview.viewholder.BaseViewHolder;
import bdkj.com.englishstu.xrecyclerview.viewholder.RecycleItemClickListener;
import bdkj.com.englishstu.xrecyclerview.viewholder.RecycleItemLongClickListener;

/**
 * Created by davidinchina on 2017/6/6.
 */

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> {
    private List<Classes> noteList = null;
    private Context mContext;
    public RecycleItemClickListener clickListener;
    public RecycleItemLongClickListener longClickListener;

    /**
     * 当前处于打开状态的item
     */
    private List<SwipeItemLayout> mOpenedSil = new ArrayList<>();

    public ClassAdapter(Context mContext, ArrayList<Classes> datas) {
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class_layout, parent, false);
        return new ViewHolder(view, clickListener, longClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Classes classes = noteList.get(position);
        holder.tvClassName.setText(classes.getName());
        Glide.with(mContext).load(classes.getLogo()).into(holder.ivLeftImg);
        holder.tvCreateTime.setText("创建时间："+TimeUtil.date2String(classes.getCreateDate()));
        holder.tvClassNumber.setText("班级人数："+classes.getClassNumber()+"人");
        holder.tvClassTeachers.setText("任课教师："+classes.getTeacherNumber()+"人");
        holder.tvTestNumber.setText("近期考试："+classes.getTestNumber()+"场");
        holder.tvRightStatus.setVisibility(View.GONE);
        holder.tvRightStatus2.setVisibility(View.GONE);
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
        holder.itemContactDelete.setTag(classes);
        holder.itemContactDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Classes classes1 = (Classes) v.getTag();
                JsonEntity entity =  AdmDbUtils.deleteClasses(classes1.getId());
                if (entity.getCode()==0){
                    noteList.remove(classes1);
                    notifyDataSetChanged();
                }else{
                    ToastUtil.show(mContext,entity.getMsg());
                }
                closeOpenedSwipeItemLayoutWithAnim();
            }
        });
        holder.baseView.setTag(classes);
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
        ImageView ivLeftImg;
        TextView tvClassName;
        TextView tvClassNumber;
        TextView tvClassTeachers;
        TextView tvCreateTime;
        TextView tvTestNumber;
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
            tvClassName = (TextView) view.findViewById(R.id.tv_class_name);
            tvClassNumber = (TextView) view.findViewById(R.id.tv_class_number);
            tvClassTeachers = (TextView) view.findViewById(R.id.tv_class_teachers);
            tvCreateTime = (TextView) view.findViewById(R.id.tv_create_time);
            tvTestNumber = (TextView) view.findViewById(R.id.tv_test_number);
            tvRightStatus = (TextView) view.findViewById(R.id.tv_right_status);
            tvRightStatus2 = (TextView) view.findViewById(R.id.tv_right_status2);
        }
    }
}
