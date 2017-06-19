package bdkj.com.englishstu.view;

import android.os.Bundle;
import android.widget.TextView;

import bdkj.com.englishstu.R;
import bdkj.com.englishstu.base.baseView.BaseActivity;
import bdkj.com.englishstu.common.beans.Note;
import bdkj.com.englishstu.common.tool.TimeUtil;
import bdkj.com.englishstu.common.tool.ToastUtil;
import butterknife.BindView;
import butterknife.OnClick;

public class NoteDetailActivity extends BaseActivity {


    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_note_title)
    TextView tvNoteTitle;
    @BindView(R.id.tv_note_author)
    TextView tvNoteAuthor;
    @BindView(R.id.tv_note_time)
    TextView tvNoteTime;
    @BindView(R.id.tv_note_content)
    TextView tvNoteContent;

    @Override
    protected int getViewId() {
        return R.layout.activity_note_detail;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        tvTopTitle.setText("公告详情");
        Note note = (Note) getIntent().getExtras().getSerializable("note");
        if (null == note){
            ToastUtil.show(mContext,"公告内容获取失败！");
            finish();
        }
        tvNoteTitle.setText(note.getTitle());
        tvNoteContent.setText(note.getContent());
        tvNoteAuthor.setText("发布人："+note.getAuthorName());
        tvNoteTime.setText("发布日期："+ TimeUtil.date2String(note.getUpdateDate()));
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
