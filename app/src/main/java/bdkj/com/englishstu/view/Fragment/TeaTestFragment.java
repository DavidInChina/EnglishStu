package bdkj.com.englishstu.view.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import bdkj.com.englishstu.R;
import bdkj.com.englishstu.base.Application;
import bdkj.com.englishstu.base.JsonEntity;
import bdkj.com.englishstu.base.baseView.BaseFragment;
import bdkj.com.englishstu.common.adapter.TestAdapter;
import bdkj.com.englishstu.common.beans.Teacher;
import bdkj.com.englishstu.common.beans.Test;
import bdkj.com.englishstu.common.dbinfo.TeaDbUtils;
import bdkj.com.englishstu.common.divider.RecDivider;
import bdkj.com.englishstu.common.tool.IntentUtil;
import bdkj.com.englishstu.common.tool.ToastUtil;
import bdkj.com.englishstu.view.ShowTestActivity;
import bdkj.com.englishstu.view.TeaMainActivity;
import bdkj.com.englishstu.xrecyclerview.ProgressStyle;
import bdkj.com.englishstu.xrecyclerview.XRecyclerView;
import bdkj.com.englishstu.xrecyclerview.viewholder.RecycleItemClickListener;
import butterknife.BindView;

/**
 * 考试练习
 */
public class TeaTestFragment extends BaseFragment implements RecycleItemClickListener {
    @BindView(R.id.recycler_view)
    XRecyclerView recyclerView;

    private List<Test> listData;
    private TestAdapter mAdapter;
    private Teacher teacher;
    private String currentClassId = "";


    @Override
    public int getViewLayout() {
        return R.layout.fragment_ad_notice;
    }

    @Override
    public void initView(ViewGroup parent) {
        teacher = Application.getTeacherInfo();
        initRecyclerView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setRefreshing(true);
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
        listData = new ArrayList<Test>();
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                listData.clear();
                JsonEntity entity = TeaDbUtils.testList(teacher.getId(), currentClassId);
                if (entity.getCode() == 0) {
                    Logger.d(entity.getData());
                    for (Test exam : (List<Test>) entity.getData()) {
                        listData.add(exam);
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
        mAdapter = new TestAdapter(mContext, (ArrayList<Test>) listData);
        mAdapter.setClickListener(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new RecDivider(mContext, RecDivider.VERTICAL_LIST));
        currentClassId = ((TeaMainActivity) getActivity()).getClassId();
        recyclerView.setRefreshing(true);
    }

    @Override
    public void onItemClick(View view, int postion) {
        Test test = (Test) view.getTag();
        Bundle bundle = new Bundle();
        bundle.putSerializable("test", test);
        IntentUtil.launcher(mContext, ShowTestActivity.class,bundle);
    }
}
