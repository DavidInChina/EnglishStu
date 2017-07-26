package bdkj.com.englishstu.view.Fragment;


import android.os.Bundle;
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
import bdkj.com.englishstu.common.adapter.StuMarkAdapter;
import bdkj.com.englishstu.common.adapter.TeaMarkAdapter;
import bdkj.com.englishstu.common.beans.Mark;
import bdkj.com.englishstu.common.beans.Teacher;
import bdkj.com.englishstu.common.beans.Test;
import bdkj.com.englishstu.common.dbinfo.StuDbUtils;
import bdkj.com.englishstu.common.divider.RecDivider;
import bdkj.com.englishstu.common.eventbus.ClassChoose;
import bdkj.com.englishstu.common.tool.IntentUtil;
import bdkj.com.englishstu.common.tool.ToastUtil;
import bdkj.com.englishstu.view.MarkDetailActivity;
import bdkj.com.englishstu.view.TeaMainActivity;
import bdkj.com.englishstu.xrecyclerview.ProgressStyle;
import bdkj.com.englishstu.xrecyclerview.XRecyclerView;
import bdkj.com.englishstu.xrecyclerview.viewholder.RecycleItemClickListener;
import butterknife.BindView;
import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class TeaMarkFragment extends BaseFragment {
    @BindView(R.id.recycler_view_stu)
    XRecyclerView recyclerViewStu;
    @BindView(R.id.recycler_view_test)
    XRecyclerView recyclerViewStuTest;
    private List<Test> listData;
    private List<Mark> markList;
    private TeaMarkAdapter mAdapter;
    private StuMarkAdapter stuAdapter;
    private String testId = "";
    private String currentClassId = "";
    private Teacher teacher;

    @Override
    public int getViewLayout() {
        return R.layout.fragment_tea_mark;
    }

    @Override
    public void initView(ViewGroup parent) {
        EventBus.getDefault().register(this);
        teacher = Application.getTeacherInfo();
        initRecyclerView();
    }

    public void onEventMainThread(ClassChoose classChoose) {
        recyclerViewStu.setRefreshing(true);
        recyclerViewStuTest.setRefreshing(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        recyclerViewStu.setRefreshing(true);
        recyclerViewStuTest.setRefreshing(true);
    }

    public void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewStu.setLayoutManager(layoutManager);//设置纵向列表
        recyclerViewStu.setRefreshProgressStyle(ProgressStyle.Pacman);
        recyclerViewStu.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        recyclerViewStu.setArrowImageView(R.mipmap.iconfont_downgrey);
        recyclerViewStu.setLoadingMoreEnabled(false);
        markList = new ArrayList<Mark>();
        recyclerViewStu.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                markList.clear();
                currentClassId = ((TeaMainActivity) getActivity()).getClassId();
                JsonEntity entity = StuDbUtils.markList2(currentClassId, testId);
                if (entity.getCode() == 0) {
                    for (Mark mark : (List<Mark>) entity.getData()) {
                        markList.add(mark);
                    }
                } else {
                    ToastUtil.show(mContext, entity.getMsg());
                }
                stuAdapter.notifyDataSetChanged();
                recyclerViewStu.refreshComplete();
            }

            @Override
            public void onLoadMore() {
            }
        });
        stuAdapter = new StuMarkAdapter(mContext, (ArrayList<Mark>) markList);
        stuAdapter.setClickListener(new RecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                Mark mark = (Mark) view.getTag();
                Bundle bundle = new Bundle();
                bundle.putSerializable("mark", mark);
                IntentUtil.launcher(mContext, MarkDetailActivity.class, bundle);
//                getActivity().finish();
            }
        });
        recyclerViewStu.setAdapter(stuAdapter);
        recyclerViewStu.addItemDecoration(new RecDivider(mContext, RecDivider.VERTICAL_LIST));

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(mContext);
        layoutManager2.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewStuTest.setLayoutManager(layoutManager2);//设置纵向列表
        recyclerViewStuTest.setRefreshProgressStyle(ProgressStyle.Pacman);
        recyclerViewStuTest.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        recyclerViewStuTest.setArrowImageView(R.mipmap.iconfont_downgrey);
        recyclerViewStuTest.setLoadingMoreEnabled(false);
        listData = new ArrayList<Test>();
        recyclerViewStuTest.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                listData.clear();
                currentClassId = ((TeaMainActivity) getActivity()).getClassId();
                JsonEntity entity = StuDbUtils.testList(teacher.getId(), currentClassId);
                if (entity.getCode() == 0) {
                    for (Test test : (List<Test>) entity.getData()) {
                        listData.add(test);
                        if ("".equals(testId)) {
                            testId = test.getId();
                            recyclerViewStu.setRefreshing(true);
                        }
                    }
                } else {
                    ToastUtil.show(mContext, entity.getMsg());
                }
                mAdapter.notifyDataSetChanged();
                recyclerViewStuTest.refreshComplete();
            }

            @Override
            public void onLoadMore() {
            }
        });
        mAdapter = new TeaMarkAdapter(mContext, (ArrayList<Test>) listData);
        mAdapter.setClickListener(new RecycleItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                //这里选中了练习
                Test test = (Test) view.getTag();
                testId = test.getId();
                recyclerViewStu.setRefreshing(true);
            }
        });
        recyclerViewStuTest.setAdapter(mAdapter);
        recyclerViewStuTest.addItemDecoration(new RecDivider(mContext, RecDivider.VERTICAL_LIST));
        recyclerViewStuTest.setRefreshing(true);

    }


}
