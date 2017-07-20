package bdkj.com.englishstu.view.Fragment;


import android.os.Bundle;
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
import bdkj.com.englishstu.common.adapter.StuTestAdapter;
import bdkj.com.englishstu.common.beans.Student;
import bdkj.com.englishstu.common.beans.Test;
import bdkj.com.englishstu.common.dbinfo.StuDbUtils;
import bdkj.com.englishstu.common.divider.RecDivider;
import bdkj.com.englishstu.common.tool.IntentUtil;
import bdkj.com.englishstu.common.tool.ToastUtil;
import bdkj.com.englishstu.view.AnswerExamActivity;
import bdkj.com.englishstu.xrecyclerview.ProgressStyle;
import bdkj.com.englishstu.xrecyclerview.XRecyclerView;
import bdkj.com.englishstu.xrecyclerview.viewholder.RecycleItemClickListener;
import butterknife.BindView;

/**
 * 学生练习
 */
public class StuTestFragment extends BaseFragment implements RecycleItemClickListener {
    @BindView(R.id.recycler_view)
    XRecyclerView recyclerView;

    private List<Test> listData;
    private StuTestAdapter mAdapter;
    private Student student;

    @Override
    public int getViewLayout() {
        return R.layout.fragment_ad_notice;
    }

    @Override
    public void initView(ViewGroup parent) {
        student = Application.getStudentInfo();
        Logger.e(student.getClassIds());
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
        listData = new ArrayList<Test>();
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                listData.clear();
                JsonEntity entity = StuDbUtils.testList(student.getId(), student.getClassIds().split(",")[1], "练习");
                if (entity.getCode() == 0) {
                    Logger.d(entity.getData());
                    for (Test note : (List<Test>) entity.getData()) {
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
        mAdapter = new StuTestAdapter(mContext, (ArrayList<Test>) listData);
        mAdapter.setClickListener(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new RecDivider(mContext, RecDivider.VERTICAL_LIST));
        recyclerView.setRefreshing(true);
    }

    @Override
    public void onItemClick(View view, int postion) {
        Test test = (Test) view.getTag();
        Bundle bundle = new Bundle();
        bundle.putSerializable("test", test);
        bundle.putString("type","1");//1是练习
        IntentUtil.launcher(mContext, AnswerExamActivity.class, bundle);
//        getActivity().finish();
    }
}
