package bdkj.com.englishstu.view.Fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.widget.TextView;

import com.youdao.sdk.app.Language;
import com.youdao.sdk.app.LanguageUtils;
import com.youdao.sdk.ydonlinetranslate.TranslateErrorCode;
import com.youdao.sdk.ydonlinetranslate.TranslateListener;
import com.youdao.sdk.ydonlinetranslate.TranslateParameters;
import com.youdao.sdk.ydonlinetranslate.Translator;
import com.youdao.sdk.ydtranslate.Translate;

import bdkj.com.englishstu.R;
import bdkj.com.englishstu.base.JsonEntity;
import bdkj.com.englishstu.base.baseView.BaseFragment;
import bdkj.com.englishstu.common.beans.Exam;
import bdkj.com.englishstu.common.beans.Test;
import bdkj.com.englishstu.common.dbinfo.StuDbUtils;
import bdkj.com.englishstu.common.eventbus.RequestTest;
import bdkj.com.englishstu.common.eventbus.TheTest;
import bdkj.com.englishstu.common.tool.StringUtil;
import bdkj.com.englishstu.common.tool.ToastUtil;
import butterknife.BindView;
import de.greenrobot.event.EventBus;

/**
 * 单词朗读页面
 * A simple {@link Fragment} subclass.
 */
public class TestWordFragment extends BaseFragment {


    @BindView(R.id.tv_word)
    TextView tvWord;
    @BindView(R.id.tv_speck_en)
    TextView tvSpeckEn;
    @BindView(R.id.tv_speck_us)
    TextView tvSpeckUs;
    @BindView(R.id.tv_translate_result)
    TextView tvTranslateResult;
    @BindView(R.id.tv_translate)
    TextView tvTranslate;
    @BindView(R.id.tv_word2)
    TextView tvWord2;
    @BindView(R.id.tv_translate2)
    TextView tvTranslate2;
    @BindView(R.id.tv_speck_en2)
    TextView tvSpeckEn2;
    @BindView(R.id.tv_speck_us2)
    TextView tvSpeckUs2;
    @BindView(R.id.tv_translate_result2)
    TextView tvTranslateResult2;
    @BindView(R.id.tv_word3)
    TextView tvWord3;
    @BindView(R.id.tv_translate3)
    TextView tvTranslate3;
    @BindView(R.id.tv_speck_en3)
    TextView tvSpeckEn3;
    @BindView(R.id.tv_speck_us3)
    TextView tvSpeckUs3;
    @BindView(R.id.tv_translate_result3)
    TextView tvTranslateResult3;
    private Test currentTest;
    private Exam currentExam;
    private Handler handler = new Handler() {
        public void handleMessage(Message message) {
            Translate translate = (Translate) message.getData().getSerializable("result");
            switch (message.what) {
                case 0:
                    tvWord.setText(translate.getQuery());
                    tvTranslate.setText(StringUtil.listStr(translate.getTranslations()));
                    tvSpeckEn.setText("英式发音：["
                            + translate.getUkPhonetic() + "]");
                    tvSpeckUs.setText("美式发音：["
                            + translate.getUsPhonetic() + "]");
                    tvTranslateResult.setText(StringUtil.listStr(translate.getExplains()));
                    break;
                case 1:

                    tvWord2.setText(translate.getQuery());
                    tvTranslate2.setText(StringUtil.listStr(translate.getTranslations()));
                    tvSpeckEn2.setText("英式发音：["
                            + translate.getUkPhonetic() + "]");
                    tvSpeckUs2.setText("美式发音：["
                            + translate.getUsPhonetic() + "]");
                    tvTranslateResult2.setText(StringUtil.listStr(translate.getExplains()));
                    break;
                case 2:
                    tvWord3.setText(translate.getQuery());
                    tvTranslate3.setText(StringUtil.listStr(translate.getTranslations()));
                    tvSpeckEn3.setText("英式发音：["
                            + translate.getUkPhonetic() + "]");
                    tvSpeckUs3.setText("美式发音：["
                            + translate.getUsPhonetic() + "]");
                    tvTranslateResult3.setText(StringUtil.listStr(translate.getExplains()));
                    break;
            }
        }
    };

    @Override
    public int getViewLayout() {
        return R.layout.fragment_answer_wrod;
    }

    @Override
    public void initView(ViewGroup parent) {
        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new RequestTest());
    }

    public void onEventMainThread(TheTest test) {
        currentTest = test.getTest();
        if (null != currentTest) {
            JsonEntity entity = StuDbUtils.getExamDetail(currentTest.getExamId());
            if (entity.getCode() == 0) {
                currentExam = (Exam) entity.getData();
                if (null != currentExam) {
                    String words[] = currentExam.getWords().split(",");
                    for (int i = 0; i < words.length; i++) {
                        queryWord(words[i], i);
                    }

                } else {
                    ToastUtil.show(mContext, "获取试题失败！");
                    getActivity().finish();
                }
            } else {
                ToastUtil.show(mContext, entity.getMsg());
            }
        } else {
            ToastUtil.show(mContext, "获取试题失败！");
            getActivity().finish();
        }
    }

    private void queryWord(String inputWord, final int what) {
        // 源语言或者目标语言其中之一必须为中文,目前只支持中文与其他几个语种的互译
        Language langFrom = LanguageUtils.getLangByName("英文");
        // 若设置为自动，则查询自动识别源语言，自动识别不能保证完全正确，最好传源语言类型
        // Language langFrosm = LanguageUtils.getLangByName("自动");
        Language langTo = LanguageUtils.getLangByName("中文");
        TranslateParameters tps = new TranslateParameters.Builder()
                .source("youdao").from(langFrom).to(langTo).build();// appkey可以省略
        Translator translator = Translator.getInstance(tps);
        translator.lookup(inputWord, new TranslateListener() {
            @Override
            public void onResult(Translate result, String input) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("result", result);
                Message message = new Message();
                message.what = what;
                message.setData(bundle);
                handler.sendMessage(message);
                //异步翻译结果，需要填充到页面
            }

            @Override
            public void onError(TranslateErrorCode error) {
                ToastUtil.show(mContext, "查询单词错误:" + error.name());
            }
        });
    }


    @Override

    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
