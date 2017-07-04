package bdkj.com.englishstu.view.Fragment;


import android.support.v4.app.Fragment;
import android.view.ViewGroup;

import bdkj.com.englishstu.R;
import bdkj.com.englishstu.base.baseView.BaseFragment;
import bdkj.com.englishstu.view.AnswerExamActivity;
import butterknife.OnClick;

/**
 * 测试提交页面
 * A simple {@link Fragment} subclass.
 */
public class TestSubFragment extends BaseFragment {

    @Override
    public int getViewLayout() {
        return R.layout.fragment_answer_sub;
    }

    @Override
    public void initView(ViewGroup parent) {
    }


    @OnClick(R.id.btn_save)
    public void onViewClicked() {
        ((AnswerExamActivity) getActivity()).beginSaveMark();
    }
}
