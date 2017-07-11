package bdkj.com.englishstu.view;

import android.os.Bundle;
import android.widget.TextView;

import bdkj.com.englishstu.R;
import bdkj.com.englishstu.base.baseView.BaseActivity;
import bdkj.com.englishstu.common.beans.Mark;
import bdkj.com.englishstu.common.tool.ToastUtil;
import bdkj.com.englishstu.common.xml.Result;
import bdkj.com.englishstu.common.xml.XmlResultParser;
import butterknife.BindView;
import butterknife.OnClick;

public class MarkDetailActivity extends BaseActivity {
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_word_result)
    TextView tvWordResult;
    @BindView(R.id.tv_sentence_result)
    TextView tvSentenceResult;

    @Override
    protected int getViewId() {
        return R.layout.activity_mark_detail;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        tvTopTitle.setText("成绩详情");
        Mark mark = (Mark) getIntent().getExtras().getSerializable("mark");
        if (null == mark) {
            ToastUtil.show(mContext, "成绩详情获取失败！");
            finish();
        }
        if (!"".equals(mark.getWordXml())) {
            StringBuilder wordResult = new StringBuilder();
            XmlResultParser resultParser = new XmlResultParser();
            String word[] = mark.getWordXml().split(",");
            for (String wordXml : word
                    ) {
                Result result = resultParser.parse(wordXml);
                wordResult.append("\n" + result.toString());
            }
            tvWordResult.setText("单词朗读:" + wordResult.toString());
        }
        if (!"".equals(mark.getSentenceXml())) {
            StringBuilder sentenceResult = new StringBuilder();
            XmlResultParser resultParser = new XmlResultParser();
            String sentences[] = mark.getSentenceXml().split(",");
            for (String sentenceXml : sentences
                    ) {
                Result result = resultParser.parse(sentenceXml);
                sentenceResult.append("\n" + result.toString());
            }
            tvSentenceResult.setText("语句朗读:" + sentenceResult.toString());
        }
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
