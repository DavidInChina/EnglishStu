package bdkj.com.englishstu.view.Fragment;


import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.EvaluatorListener;
import com.iflytek.cloud.EvaluatorResult;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechEvaluator;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.orhanobut.logger.Logger;
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
import bdkj.com.englishstu.common.tool.StringUtil;
import bdkj.com.englishstu.common.tool.ToastUtil;
import bdkj.com.englishstu.common.widget.IseDialog;
import bdkj.com.englishstu.view.AnswerExamActivity;
import butterknife.BindView;
import butterknife.OnClick;

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
    @BindView(R.id.tv_translate_internet)
    TextView tvTranslateInternet;
    @BindView(R.id.fl_speck_voice)
    FrameLayout flSpeckVoice;
    @BindView(R.id.fl_remind_voice)
    FrameLayout flRemindVoice;
    // 语音合成对象 科大讯飞合成
    private SpeechSynthesizer mTts;
    // 默认发音人
    private String voicer = "catherine";
    // 缓冲进度
    private int mPercentForBuffering = 0;
    // 播放进度
    private int mPercentForPlaying = 0;
    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private Toast progressToast;


    // 评测语种 科大讯飞评测
    private String language;
    // 评测题型
    private String category;
    // 结果等级
    private String result_level;


    private SpeechEvaluator mIse;
    private IseDialog dialog;

    private Test currentTest;
    private Exam currentExam;
    private int position = 0;//当前单词下标
    private String[] words;//单词列表
    private String[] results;//解析结果列表
    private String type = "0";//当前测试类别，默认考试
    private Handler handler = new Handler() {
        public void handleMessage(Message message) {
            Translate translate = (Translate) message.getData().getSerializable("result");
            tvWord.setText(translate.getQuery());
            tvTranslate.setText(StringUtil.listStr(translate.getTranslations()));
            tvSpeckEn.setText("英式发音：["
                    + translate.getUkPhonetic() + "]");
            tvSpeckUs.setText("美式发音：["
                    + translate.getUsPhonetic() + "]");
            tvTranslateResult.setText(StringUtil.listStr(translate.getExplains()));
            tvTranslateInternet.setText("网络释义：\n" + StringUtil.webMeans(translate.getWebExplains()));
        }
    };

    @OnClick({R.id.fl_speck_voice, R.id.fl_remind_voice, R.id.iv_prev, R.id.iv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.fl_speck_voice:
                beginSpeck();
                flSpeckVoice.setEnabled(false);
                break;
            case R.id.fl_remind_voice:
                beginVoice();
                break;
            case R.id.iv_prev:
                if (position > 0) {
                    changeWord(--position);
                } else {
                    ToastUtil.show(mContext, "已是第一个单词！");
                }
                break;
            case R.id.iv_next:
                if (null == results[position] || "".equals(results[position])) {
                    ToastUtil.show(mContext, "请完成当前单词阅读！");
                } else {
                    if (position < words.length - 1) {
                        changeWord(++position);
                    } else {
                        ToastUtil.show(mContext, "已是最后一个单词！");
                    }
                }
                break;
        }
    }

    /**
     * 根据下标来改变当前显示的单词
     *
     * @param position
     */
    public void changeWord(int position) {
        queryWord(words[position]);
    }

    @Override
    public int getViewLayout() {
        return R.layout.fragment_answer_wrod;
    }

    @Override
    public void initView(ViewGroup parent) {
        initData();
        initVoice();
        initToast();
    }

    public void initToast() {
        progressToast = new Toast(mContext);
        progressToast.setDuration(Toast.LENGTH_SHORT);
        TextView view = new TextView(mContext);
        progressToast.setView(view);
    }

    private void showProgress(final String str) {
        TextView view = (TextView) progressToast.getView();
        view.setText(str);
        progressToast.show();
    }

    /**
     * 开始语音评测阅读
     */
    public void beginSpeck() {
        if (mIse == null) {
            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
            ToastUtil.show(mContext, "创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化");
            return;
        }
        //格式 "[word]\napple\nbanana\norange
        String evaText = "[word]\n" + words[position];
        Logger.d(evaText);
        setParams();
        mIse.startEvaluating(evaText, null, mEvaluatorListener);
    }

    /**
     * 开始语音阅读提示
     */
    public void beginVoice() {
        if (null == mTts) {
            // 创建单例失败，与 21001 错误为同样原因，参考 http://bbs.xfyun.cn/forum.php?mod=viewthread&tid=9688
            ToastUtil.show(mContext, "创建对象失败，请确认 libmsc.so 放置正确，且有调用 createUtility 进行初始化");
            return;
        }
        String text = words[position];
        // 设置参数
        setParam();
        int code = mTts.startSpeaking(text, mTtsListener);
        if (code != ErrorCode.SUCCESS) {
            ToastUtil.show(mContext, "语音合成失败,错误码: " + code);
        }
    }

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            showProgress("提示开始");
        }

        @Override
        public void onSpeakPaused() {
            showProgress("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            showProgress("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
            mPercentForBuffering = percent;
            showProgress(String.format(getString(R.string.tts_toast_format),
                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            mPercentForPlaying = percent;
            showProgress(String.format(getString(R.string.tts_toast_format),
                    mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                showProgress("提示结束");
            } else if (error != null) {
                showProgress(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };

    /**
     * 评测监听接口
     */
    private EvaluatorListener mEvaluatorListener = new EvaluatorListener() {

        @Override
        public void onResult(EvaluatorResult result, boolean isLast) {
            if (isLast) {
                StringBuilder builder = new StringBuilder();
                builder.append(result.getResultString());

                if (!TextUtils.isEmpty(builder)) {
                    results[position] = builder.toString();
                    if (position == words.length - 1) {
                        StringBuilder builder2 = new StringBuilder();
                        for (String result2 : results
                                ) {
                            builder2.append("," + result2);
                        }
                        String lastResult = builder2.toString().substring(1, builder2.length());
                        ((AnswerExamActivity) getActivity()).setWordResult(lastResult);
                    }

                }
                flSpeckVoice.setEnabled(true);
                dialog.hide(0, "");
            } else {
                dialog.hide(1, "");
            }
        }

        @Override
        public void onError(SpeechError error) {
            flSpeckVoice.setEnabled(true);
            if (error != null) {
                dialog.hide(1, error.getErrorDescription());
//                showProgress("error:" + error.getErrorCode() + "," + error.getErrorDescription());
            } else {
            }
        }

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            Logger.d("evaluator begin");
            dialog.show();
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            Logger.d("evaluator stoped");
            dialog.showLoading();

        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            dialog.showChange(volume);
            Logger.d("返回音频数据：" + data.length);

        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }

    };
    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                showProgress("初始化失败，错误码：" + code);
            }
        }
    };
    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            showProgress(error.getPlainDescription(true));
        }

    };

    /**
     * 语音合成参数设置
     *
     * @return
     */
    private void setParam() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, "10");
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, "50");
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, "100");
        } else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");
        }
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");
    }

    /**
     * 语音评测参数设置
     */
    private void setParams() {
        // 设置评测语言
        language = "en_us";
        // 设置需要评测的类型
        category = "read_word";//read_sentence
        // 设置结果等级（中文仅支持complete）
        result_level = "complete";
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        String vad_bos = "3000";
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        String vad_eos = "1800";
        // 语音输入超时时间，即用户最多可以连续说多长时间；
        String speech_timeout = "-1";

        mIse.setParameter(SpeechConstant.LANGUAGE, language);
        mIse.setParameter(SpeechConstant.ISE_CATEGORY, category);
        mIse.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
        mIse.setParameter(SpeechConstant.VAD_BOS, vad_bos);
        mIse.setParameter(SpeechConstant.VAD_EOS, vad_eos);
        mIse.setParameter(SpeechConstant.KEY_SPEECH_TIMEOUT, speech_timeout);
        mIse.setParameter(SpeechConstant.RESULT_LEVEL, result_level);
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIse.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mIse.setParameter(SpeechConstant.ISE_AUDIO_PATH, Environment.getExternalStorageDirectory().getAbsolutePath() + "/msc/ise.wav");
    }

    public void initVoice() {
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(mContext, mTtsInitListener);
        mIse = SpeechEvaluator.createEvaluator(mContext, null);
        dialog = new IseDialog(mContext);
        dialog.setBegin();
    }

    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                ToastUtil.show(mContext, "初始化失败,错误码：" + code);
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

    /**
     * 试题请求的回调
     */
    public void initData() {
        currentTest = ((AnswerExamActivity) getActivity()).getCurrentTest();
        type = ((AnswerExamActivity) getActivity()).getCurrentType();
        if (type.equals("0")) {
            flRemindVoice.setVisibility(View.GONE);
        } else {
            flRemindVoice.setVisibility(View.VISIBLE);
        }
        if (null != currentTest) {
            JsonEntity entity = StuDbUtils.getExamDetail(currentTest.getExamId());
            if (entity.getCode() == 0) {
                currentExam = (Exam) entity.getData();
                if (null != currentExam) {
                    words = currentExam.getWords().split(",");
                    results = new String[words.length];
                    position = 0;//默认第一个单词开始
                    queryWord(words[position]);

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

    /**
     * 单词翻译
     *
     * @param inputWord 单词
     */
    private void queryWord(String inputWord) {
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


}
