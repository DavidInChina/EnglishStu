package bdkj.com.englishstu.view.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import bdkj.com.englishstu.R;
import bdkj.com.englishstu.base.JsonEntity;
import bdkj.com.englishstu.base.baseView.BaseFragment;
import bdkj.com.englishstu.common.adapter.TeacherAdapter;
import bdkj.com.englishstu.common.beans.Teacher;
import bdkj.com.englishstu.common.dbinfo.AdmDbUtils;
import bdkj.com.englishstu.common.divider.RecDivider;
import bdkj.com.englishstu.common.tool.IntentUtil;
import bdkj.com.englishstu.common.tool.ToastUtil;
import bdkj.com.englishstu.view.ShowTeacherActivity;
import bdkj.com.englishstu.xrecyclerview.ProgressStyle;
import bdkj.com.englishstu.xrecyclerview.XRecyclerView;
import bdkj.com.englishstu.xrecyclerview.viewholder.RecycleItemClickListener;
import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdTeacherFragment extends BaseFragment implements RecycleItemClickListener {
    @BindView(R.id.recycler_view)
    XRecyclerView recyclerView;

    private List<Teacher> listData;
    private TeacherAdapter mAdapter;

    @Override
    public int getViewLayout() {
        return R.layout.fragment_ad_teacher;
    }

    @Override
    public void initView(ViewGroup parent) {
        initRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();
        recyclerView.setRefreshing(true);
    }

    public void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);//设置纵向列表
        recyclerView.setRefreshProgressStyle(ProgressStyle.Pacman);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        recyclerView.setArrowImageView(R.mipmap.iconfont_downgrey);
        recyclerView.setLoadingMoreEnabled(false);
        listData = new ArrayList<Teacher>();
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                listData.clear();
                JsonEntity entity = AdmDbUtils.teacherList();
                if (entity.getCode() == 0) {
                    List<Teacher> teachers = (List<Teacher>) entity.getData();
                    for (Teacher teacher : teachers) {
                        listData.add(teacher);
                    }
                } else {
                    ToastUtil.show(mContext, "获取教师列表失败！");
                }

                mAdapter.notifyDataSetChanged();
                recyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
            }
        });
        mAdapter = new TeacherAdapter(mContext, (ArrayList<Teacher>) listData);
        mAdapter.setClickListener(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new RecDivider(mContext, RecDivider.VERTICAL_LIST));
        recyclerView.setRefreshing(true);
    }

    @Override
    public void onItemClick(View view, int postion) {
        Teacher teacher = (Teacher) view.getTag();
        Bundle bundle = new Bundle();
        bundle.putSerializable("teacher", teacher);
        IntentUtil.launcher(mContext, ShowTeacherActivity.class, bundle);
    }
}