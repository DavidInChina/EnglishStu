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
import bdkj.com.englishstu.common.adapter.StudentAdapter;
import bdkj.com.englishstu.common.beans.Student;
import bdkj.com.englishstu.common.dbinfo.AdmDbUtils;
import bdkj.com.englishstu.common.divider.RecDivider;
import bdkj.com.englishstu.common.tool.IntentUtil;
import bdkj.com.englishstu.common.tool.ToastUtil;
import bdkj.com.englishstu.view.ShowStudentActivity;
import bdkj.com.englishstu.xrecyclerview.ProgressStyle;
import bdkj.com.englishstu.xrecyclerview.XRecyclerView;
import bdkj.com.englishstu.xrecyclerview.viewholder.RecycleItemClickListener;
import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdStudentFragment extends BaseFragment implements RecycleItemClickListener {
    @BindView(R.id.recycler_view)
    XRecyclerView recyclerView;

    private List<Student> listData;
    private StudentAdapter mAdapter;

    @Override
    public int getViewLayout() {
        return R.layout.fragment_ad_student;
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
        listData = new ArrayList<Student>();
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                listData.clear();
                JsonEntity jsonEntity = AdmDbUtils.studentList();
                if (jsonEntity.getCode() == 0) {
                    List<Student> students = (List<Student>) jsonEntity.getData();
                    for (Student student : students) {
                        listData.add(student);
                    }
                } else {
                    ToastUtil.show(mContext, jsonEntity.getMsg());
                }

                mAdapter.notifyDataSetChanged();
                recyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
            }
        });
        mAdapter = new StudentAdapter(mContext, (ArrayList<Student>) listData);
        mAdapter.setClickListener(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new RecDivider(mContext, RecDivider.VERTICAL_LIST));
        recyclerView.setRefreshing(true);
    }

    @Override
    public void onItemClick(View view, int postion) {
//        ToastUtil.show(mContext, "点击了第" + postion + "项");
        Student student = (Student) view.getTag();
        Bundle bundle = new Bundle();
        bundle.putSerializable("student", student);
        IntentUtil.launcher(mContext, ShowStudentActivity.class, bundle);
    }
}