package bdkj.com.englishstu.view.Fragment;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import bdkj.com.englishstu.R;
import bdkj.com.englishstu.base.baseView.BaseFragment;
import bdkj.com.englishstu.common.adapter.NoticeAdapter;
import bdkj.com.englishstu.common.beans.Note;
import bdkj.com.englishstu.common.divider.RecDivider;
import bdkj.com.englishstu.xrecyclerview.viewholder.RecycleItemClickListener;
import bdkj.com.englishstu.xrecyclerview.ProgressStyle;
import bdkj.com.englishstu.xrecyclerview.XRecyclerView;
import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdNoticeFragment extends BaseFragment implements RecycleItemClickListener {
    @BindView(R.id.recycler_view)
    XRecyclerView recyclerView;

    private List<Note> listData;
    private NoticeAdapter mAdapter;

    @Override
    public int getViewLayout() {
        return R.layout.fragment_ad_notice;
    }

    @Override
    public void initView(ViewGroup parent) {
        initRecyclerView();
    }

    public void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);//设置纵向列表
        recyclerView.setRefreshProgressStyle(ProgressStyle.Pacman);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        recyclerView.setArrowImageView(R.mipmap.iconfont_downgrey);
        recyclerView.setLoadingMoreEnabled(true);
        listData = new ArrayList<Note>();
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                listData.clear();
                recyclerView.setLoadingMoreEnabled(true);
                for (int i = 0; i < 10; i++) {
                    listData.add(new Note());
                }
                mAdapter.notifyDataSetChanged();
                recyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
                if (listData.size() < 40) {
                    for (int i = 0; i < 10; i++) {
                        listData.add(new Note());
                    }
                    mAdapter.notifyDataSetChanged();
                    recyclerView.loadMoreComplete();
                } else {
                    recyclerView.loadMoreComplete();
                    recyclerView.setLoadingMoreEnabled(false);
                }
            }
        });
        mAdapter = new NoticeAdapter(mContext, (ArrayList<Note>) listData);
        mAdapter.setClickListener(this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(new RecDivider(mContext, RecDivider.VERTICAL_LIST));
        recyclerView.setRefreshing(true);
    }

    @Override
    public void onItemClick(View view, int postion) {

    }
}
