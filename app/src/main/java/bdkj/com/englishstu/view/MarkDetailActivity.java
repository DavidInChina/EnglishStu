package bdkj.com.englishstu.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import bdkj.com.englishstu.R;
import bdkj.com.englishstu.base.baseView.BaseActivity;
import bdkj.com.englishstu.common.adapter.MarkResultAdapter;
import bdkj.com.englishstu.common.beans.Mark;
import bdkj.com.englishstu.common.divider.RecDivider;
import bdkj.com.englishstu.common.tool.IntentUtil;
import bdkj.com.englishstu.common.tool.ToastUtil;
import bdkj.com.englishstu.common.xml.Result;
import bdkj.com.englishstu.common.xml.XmlResultParser;
import bdkj.com.englishstu.xrecyclerview.ProgressStyle;
import bdkj.com.englishstu.xrecyclerview.XRecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

public class MarkDetailActivity extends BaseActivity {
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_confirm)
    TextView tvConfirm;
    @BindView(R.id.recycler_view)
    XRecyclerView recyclerView;

    private List<Result> list;
    MarkResultAdapter adapter;
    private Mark mark;

    @Override
    protected int getViewId() {
        return R.layout.activity_mark_detail;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        tvTopTitle.setText("成绩详情");
        tvConfirm.setText("图表展示");
        tvConfirm.setVisibility(View.VISIBLE);
        mark = (Mark) getIntent().getExtras().getSerializable("mark");
        if (null == mark) {
            ToastUtil.show(mContext, "成绩详情获取失败！");
            finish();
        }
        initRecyclerView();
    }

    public void getResult() {
        if (!"".equals(mark.getWordXml())) {
            XmlResultParser resultParser = new XmlResultParser();
            String word[] = mark.getWordXml().split(",");
            for (String wordXml : word
                    ) {
                Result result = resultParser.parse(wordXml);
                list.add(result);
            }
        }
        if (!"".equals(mark.getSentenceXml())) {
            XmlResultParser resultParser = new XmlResultParser();
            String sentences[] = mark.getSentenceXml().split(",");
            for (String sentenceXml : sentences
                    ) {
                Result result = resultParser.parse(sentenceXml);
                list.add(result);
            }
        }
        Logger.d(list.size());
    }

    public void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);//设置纵向列表
        recyclerView.setRefreshProgressStyle(ProgressStyle.Pacman);
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        recyclerView.setArrowImageView(R.mipmap.iconfont_downgrey);
        recyclerView.setLoadingMoreEnabled(false);
        list = new ArrayList<>();
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                list.clear();
                getResult();
                adapter.notifyDataSetChanged();
                recyclerView.refreshComplete();
            }

            @Override
            public void onLoadMore() {
            }
        });
        adapter = new MarkResultAdapter(mContext, (ArrayList<Result>) list);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new RecDivider(mContext, RecDivider.VERTICAL_LIST));
        recyclerView.setRefreshing(true);
    }

    @OnClick({R.id.iv_back,R.id.tv_confirm})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_confirm:
                Bundle bundle = new Bundle();
                bundle.putSerializable("mark", mark);
                IntentUtil.launcher(mContext, MarkCharActivity.class, bundle);
//                finish();
                break;
        }
    }
}
