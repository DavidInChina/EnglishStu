package bdkj.com.englishstu.view.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import bdkj.com.englishstu.R;
import bdkj.com.englishstu.base.Application;
import bdkj.com.englishstu.base.JsonEntity;
import bdkj.com.englishstu.base.baseView.BaseFragment;
import bdkj.com.englishstu.common.adapter.NoticeAdapter;
import bdkj.com.englishstu.common.beans.Note;
import bdkj.com.englishstu.common.beans.Teacher;
import bdkj.com.englishstu.common.dbinfo.TeaDbUtils;
import bdkj.com.englishstu.common.divider.RecDivider;
import bdkj.com.englishstu.common.tool.IntentUtil;
import bdkj.com.englishstu.common.tool.ToastUtil;
import bdkj.com.englishstu.view.NoteDetailActivity;
import bdkj.com.englishstu.view.TeaMainActivity;
import bdkj.com.englishstu.xrecyclerview.ProgressStyle;
import bdkj.com.englishstu.xrecyclerview.XRecyclerView;
import bdkj.com.englishstu.xrecyclerview.viewholder.RecycleItemClickListener;
import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeNoticeFragment extends BaseFragment implements RecycleItemClickListener {
    @BindView(R.id.recycler_view)
    XRecyclerView recyclerView;

    private List<Note> listData;
    private NoticeAdapter mAdapter;
    private Teacher teacher;
    private String currentClassId = "";


    @Override
    public int getViewLayout() {
        return R.layout.fragment_ad_notice;
    }

    @Override
    public void initView(ViewGroup parent) {
//        EventBus.getDefault().register(this);
        teacher = Application.getTeacherInfo();
        initRecyclerView();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setRefreshing(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);//设置纵向列表
        recyclerView.setRefreshProgressStyle(ProgressStyle.Pacman);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        recyclerView.setArrowImageView(R.mipmap.iconfont_downgrey);
        recyclerView.setLoadingMoreEnabled(false);
        listData = new ArrayList<Note>();
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                listData.clear();
                JsonEntity entity = TeaDbUtils.noteList(teacher.getId(), currentClassId);
                if (entity.getCode() == 0) {
                    for (Note note : (List<Note>) entity.getData()) {
                        listData.add(note);
                    }
                } else {
                    ToastUtil.show(mContext, entity.getMsg());
                }
                mAdapter.notifyDataSetChanged();
                recyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
            }
        });
        mAdapter = new NoticeAdapter(mContext, (ArrayList<Note>) listData);
        mAdapter.setClickListener(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new RecDivider(mContext, RecDivider.VERTICAL_LIST));
        currentClassId = ((TeaMainActivity) getActivity()).getClassId();
        recyclerView.setRefreshing(true);
    }
    @Override
    public void onResume() {
        super.onResume();
        recyclerView.setRefreshing(true);
    }
    @Override
    public void onItemClick(View view, int postion) {
        Note note = (Note) view.getTag();
        Bundle bundle = new Bundle();
        bundle.putSerializable("note", note);
        IntentUtil.launcher(mContext, NoteDetailActivity.class, bundle);
    }
}
