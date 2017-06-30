package bdkj.com.englishstu.common.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import bdkj.com.englishstu.R;
import bdkj.com.englishstu.base.JsonEntity;
import bdkj.com.englishstu.common.beans.Student;
import bdkj.com.englishstu.common.dbinfo.AdmDbUtils;
import bdkj.com.englishstu.common.tool.TimeUtil;
import bdkj.com.englishstu.common.tool.ToastUtil;
import bdkj.com.englishstu.common.widget.CircleImageView;
import bdkj.com.englishstu.swipeitem.widget.SwipeItemLayout;
import bdkj.com.englishstu.xrecyclerview.viewholder.BaseViewHolder;
import bdkj.com.englishstu.xrecyclerview.viewholder.RecycleItemClickListener;
import bdkj.com.englishstu.xrecyclerview.viewholder.RecycleItemLongClickListener;

/**
 * Created by davidinchina on 2017/6/6.
 */

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private List<Student> noteList = null;
    private Context mContext;
    public RecycleItemClickListener clickListener;
    public RecycleItemLongClickListener longClickListener;

    /**
     * 当前处于打开状态的item
     */
    private List<SwipeItemLayout> mOpenedSil = new ArrayList<>();

    public StudentAdapter(Context mContext, ArrayList<Student> datas) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student_layout, parent, false);
        return new ViewHolder(view, clickListener, longClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Student student = noteList.get(position);
        SwipeItemLayout swipeRoot = holder.itemContactSwipeRoot;
        Glide.with(mContext).load(student.getUserHead()).into(holder.ivLeftImg);
        holder.tvStuName.setText(student.getUserName());
        holder.tvStuNumber.setText("学号：" + student.getNumber());
        holder.tvStuSex.setText("性别：" + student.getSex());
        holder.tvStuClass.setText("班级：" + student.getClassIds().split(",")[0]);
        holder.tvStuTime.setText("入学时间：" + TimeUtil.date2String(student.getCreateDate()));
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
        holder.itemContactDelete.setTag(student);
        holder.itemContactDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Student student1 = (Student) v.getTag();
                JsonEntity entity = AdmDbUtils.deleteStudent(student1.getId());
                if (entity.getCode() == 0) {
                    noteList.remove(student1);
                    notifyDataSetChanged();
                } else {
                    ToastUtil.show(mContext, entity.getMsg());
                }
            }
        });
        holder.baseView.setTag(student);
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
        TextView itemContactDelete;
        SwipeItemLayout itemContactSwipeRoot;
        public View baseView;
        CircleImageView ivLeftImg;
        TextView tvStuName;
        TextView tvStuNumber;
        TextView tvStuSex;
        TextView tvStuClass;
        TextView tvStuTime;

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
            ivLeftImg = (CircleImageView) view.findViewById(R.id.iv_left_img);
            tvStuName = (TextView) view.findViewById(R.id.tv_stu_name);
            tvStuNumber = (TextView) view.findViewById(R.id.tv_stu_number);
            tvStuSex = (TextView) view.findViewById(R.id.tv_stu_sex);
            tvStuClass = (TextView) view.findViewById(R.id.tv_stu_class);
            tvStuTime = (TextView) view.findViewById(R.id.tv_stu_time);
        }
    }
}
