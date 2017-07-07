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
import bdkj.com.englishstu.common.beans.Note;
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

public class StuNoticeAdapter extends RecyclerView.Adapter<StuNoticeAdapter.ViewHolder> {
    private List<Note> noteList = null;
    private Context mContext;
    public RecycleItemClickListener clickListener;
    public RecycleItemLongClickListener longClickListener;

    /**
     * 当前处于打开状态的item
     */
    private List<SwipeItemLayout> mOpenedSil = new ArrayList<>();

    public StuNoticeAdapter(Context mContext, ArrayList<Note> datas) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice_layout, parent, false);
        return new ViewHolder(view, clickListener, longClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Note note = noteList.get(position);
        holder.tvTitle.setText(note.getTitle());
        String content = note.getContent();
        if (note.getContent().length() > 40) {
            content = note.getContent().substring(0, 39) + "...";//截短通知
        }
        holder.tvContent.setText(content);
        Glide.with(mContext).load(note.getImg()).into(holder.ivLeftImg);
        holder.tvAuthor.setText("发布人：" + note.getAuthorName());
        holder.tvTime.setText("发布日期：" + TimeUtil.date2String(note.getUpdateDate()));
        if (note.getStatus() == 0) {//未读
            holder.tvRightStatus.setVisibility(View.VISIBLE);
            holder.tvRightStatus2.setVisibility(View.INVISIBLE);
        } else {
            holder.tvRightStatus.setVisibility(View.INVISIBLE);
            holder.tvRightStatus2.setVisibility(View.VISIBLE);
        }
        SwipeItemLayout swipeRoot = holder.itemContactSwipeRoot;
        swipeRoot.setSwipeAble(false);
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
        holder.itemContactDelete.setTag(note);
        holder.itemContactDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Note note1 = (Note) v.getTag();
                JsonEntity entity = AdmDbUtils.deleteNote(note1.getId());
                if (entity.getCode() == 0) {
                    noteList.remove(note1);
                    notifyDataSetChanged();
                } else {
                    ToastUtil.show(mContext, entity.getMsg());
                }
                closeOpenedSwipeItemLayoutWithAnim();
            }
        });
        holder.baseView.setTag(note);
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
        TextView tvTitle;
        TextView tvContent;
        TextView tvTime;
        TextView tvAuthor;
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
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvContent = (TextView) view.findViewById(R.id.tv_content);
            tvTime = (TextView) view.findViewById(R.id.tv_time);
            tvAuthor = (TextView) view.findViewById(R.id.tv_author);
            tvRightStatus = (TextView) view.findViewById(R.id.tv_right_status);
            tvRightStatus2 = (TextView) view.findViewById(R.id.tv_right_status2);
        }
    }
}
