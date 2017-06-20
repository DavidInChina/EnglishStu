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
import bdkj.com.englishstu.common.beans.Teacher;
import bdkj.com.englishstu.common.dbinfo.AdmDbUtils;
import bdkj.com.englishstu.common.tool.ToastUtil;
import bdkj.com.englishstu.swipeitem.widget.SwipeItemLayout;
import bdkj.com.englishstu.xrecyclerview.viewholder.BaseViewHolder;
import bdkj.com.englishstu.xrecyclerview.viewholder.RecycleItemClickListener;
import bdkj.com.englishstu.xrecyclerview.viewholder.RecycleItemLongClickListener;
import butterknife.BindView;

/**
 * Created by davidinchina on 2017/6/6.
 */

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.ViewHolder> {

    private List<Teacher> noteList = null;
    private Context mContext;
    public RecycleItemClickListener clickListener;
    public RecycleItemLongClickListener longClickListener;

    /**
     * 当前处于打开状态的item
     */
    private List<SwipeItemLayout> mOpenedSil = new ArrayList<>();

    public TeacherAdapter(Context mContext, ArrayList<Teacher> datas) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_teacher_layout, parent, false);
        return new ViewHolder(view, clickListener, longClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Teacher teacher = noteList.get(position);
        SwipeItemLayout swipeRoot = holder.itemContactSwipeRoot;
        Glide.with(mContext).load(teacher.getUserHead()).into(holder.ivLeftImg);
       holder.tvTeacherName.setText(teacher.getUserName());
        holder.tvTeacherNumber.setText("教师编号："+teacher.getNumber());
        holder.tvTeacherPhone.setText("手机号码："+teacher.getPhone());
        holder.tvTeacherEmail.setText("邮箱地址："+teacher.getEmail());
        int classNum =0;
        if (null!=teacher.getClassIds()&&!"".equals(teacher.getClassIds())){
            classNum =  teacher.getClassIds().split(",").length;
        }
        holder.tvTeacherClasses.setText("任课班级："+classNum+"个");
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
        holder.itemContactDelete.setTag(teacher);
        holder.itemContactDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Teacher teacher1 = (Teacher) v.getTag();
                JsonEntity entity =  AdmDbUtils.deleteTeacher(teacher1.getId());
                if (entity.getCode()==0){
                    noteList.remove(teacher1);
                    notifyDataSetChanged();
                }else{
                    ToastUtil.show(mContext,entity.getMsg());
                }
                closeOpenedSwipeItemLayoutWithAnim();
            }
        });
        holder.baseView.setTag(teacher);
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
        TextView tvTeacherName;
        TextView tvTeacherNumber;
        TextView tvTeacherPhone;
        TextView tvTeacherEmail;
        TextView tvTeacherClasses;

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
            tvTeacherName = (TextView) view.findViewById(R.id.tv_teacher_name);
            tvTeacherNumber = (TextView) view.findViewById(R.id.tv_teacher_number);
            tvTeacherPhone = (TextView) view.findViewById(R.id.tv_teacher_phone);
            tvTeacherEmail = (TextView) view.findViewById(R.id.tv_teacher_email);
            tvTeacherClasses = (TextView) view.findViewById(R.id.tv_teacher_classes);
        }
    }
}
