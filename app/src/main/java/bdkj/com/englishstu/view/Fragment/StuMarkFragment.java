package bdkj.com.englishstu.view.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import bdkj.com.englishstu.common.adapter.MarkAdapter;
import bdkj.com.englishstu.common.beans.Mark;
import bdkj.com.englishstu.common.beans.Student;
import bdkj.com.englishstu.common.dbinfo.StuDbUtils;
import bdkj.com.englishstu.common.divider.RecDivider;
import bdkj.com.englishstu.common.tool.IntentUtil;
import bdkj.com.englishstu.common.tool.ToastUtil;
import bdkj.com.englishstu.view.MarkDetailActivity;
import bdkj.com.englishstu.view.StuMainActivity;
import bdkj.com.englishstu.xrecyclerview.ProgressStyle;
import bdkj.com.englishstu.xrecyclerview.XRecyclerView;
import bdkj.com.englishstu.xrecyclerview.viewholder.RecycleItemClickListener;
import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class StuMarkFragment extends BaseFragment implements RecycleItemClickListener,StuMainActivity.ChangeMarkTypeListener {
    @BindView(R.id.recycler_view)
    XRecyclerView recyclerView;

    private List<Mark> listData;
    private MarkAdapter mAdapter;
    private Student student;
    private String type = "练习";//默认练习

    @Override
    public int getViewLayout() {
        return R.layout.fragment_ad_notice;
    }

    @Override
    public void initView(ViewGroup parent) {
        student = Application.getStudentInfo();
        initRecyclerView();
    }
    @Override
    public void changeType(String type1) {
        type = type1;
        Logger.d("状态改变");
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
        listData = new ArrayList<Mark>();
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                listData.clear();
                JsonEntity entity = StuDbUtils.markList(student.getId(), type);
                if (entity.getCode() == 0) {
                    for (Mark note : (List<Mark>) entity.getData()) {
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
        mAdapter = new MarkAdapter(mContext, (ArrayList<Mark>) listData);
        mAdapter.setClickListener(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new RecDivider(mContext, RecDivider.VERTICAL_LIST));
        recyclerView.setRefreshing(true);
    }

    @Override
    public void onItemClick(View view, int postion) {
        Mark mark = (Mark) view.getTag();
        Bundle bundle = new Bundle();
        bundle.putSerializable("mark", mark);
        IntentUtil.launcher(mContext, MarkDetailActivity.class, bundle);
    }


}
