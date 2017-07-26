package bdkj.com.englishstu.view;

import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bdkj.com.englishstu.R;
import bdkj.com.englishstu.base.JsonEntity;
import bdkj.com.englishstu.base.baseView.BaseActivity;
import bdkj.com.englishstu.common.beans.Mark;
import bdkj.com.englishstu.common.beans.Test;
import bdkj.com.englishstu.common.dbinfo.TeaDbUtils;
import bdkj.com.englishstu.common.tool.ToastUtil;
import butterknife.BindView;
import butterknife.OnClick;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ViewportChangeListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PreviewColumnChartView;

public class MarkStudentCharActivity extends BaseActivity {
    @BindView(R.id.chart)
    ColumnChartView chart;
    @BindView(R.id.chart_preview)
    PreviewColumnChartView previewChart;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;

    private List<Mark> list;
    private Test test;
    private ColumnChartData data;
    /**
     * Deep copy of data.
     */
    private ColumnChartData previewData;

    @Override
    protected int getViewId() {
        return R.layout.activity_mark_char_layout;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        tvTopTitle.setText("图表分析");
        test = (Test) getIntent().getExtras().getSerializable("test");
        if (null == test) {
            ToastUtil.show(mContext, "学生成绩列表获取失败！");
            finish();
        }
        getResult();
        initCharView();
    }

    public void getResult() {
        if (null == list) {
            list = new ArrayList<>();
        } else {
            list.clear();
        }
        JsonEntity<List<Mark>> entity = TeaDbUtils.markList(test.getId());
        list = entity.getData();
    }

    public void initCharView() {
        generateDefaultData();
        // Disable zoom/scroll for previewed chart, visible chart ranges depends on preview chart viewport so
        // zoom/scroll is unnecessary.
        chart.setZoomEnabled(false);
        chart.setScrollEnabled(false);
        previewChart.setColumnChartData(previewData);
        previewChart.setViewportChangeListener(new ViewportListener());
        int color = ChartUtils.pickColor();
        previewChart.setPreviewColor(color);
        previewX(false);
    }

    private void generateDefaultData() {
        int numSubcolumns = 1;
        int numColumns = list.size();
        List<Column> columns = new ArrayList<Column>();
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<SubcolumnValue>();
            String content = list.get(i).getStuName();
            int score = Integer.parseInt(list.get(i).getScore());
            for (int j = 0; j < numSubcolumns; ++j) {
                SubcolumnValue subcolumnValue = new SubcolumnValue(score, ChartUtils.pickColor());
                subcolumnValue.setLabel(score + "");
                values.add(subcolumnValue);
            }
            axisValues.add(new AxisValue(i).setLabel(content));
            columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
        }
        data = new ColumnChartData(columns);
        Axis axisX = new Axis(axisValues).setHasLines(true);
        Axis axisY = new Axis().setHasLines(true);
        axisX.setName("学生姓名");
        axisY.setName("得分");
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);
        chart.setColumnChartData(data);
        // prepare preview data, is better to use separate deep copy for preview chart.
        // set color to grey to make preview area more visible.
        previewData = new ColumnChartData(data);//深拷贝，修改部分内容
        previewData.setAxisXBottom(new Axis());
        previewData.setAxisYLeft(new Axis().setHasLines(true));
        for (Column column : previewData.getColumns()) {
            for (SubcolumnValue value : column.getValues()) {
                value.setColor(ChartUtils.DEFAULT_DARKEN_COLOR);
            }
        }
    }

    private void previewX(boolean animate) {
        Viewport tempViewport = new Viewport(chart.getMaximumViewport());
        float dx = tempViewport.width() / 6;
        tempViewport.inset(dx, 0);
        if (animate) {
            previewChart.setCurrentViewportWithAnimation(tempViewport);
        } else {
            previewChart.setCurrentViewport(tempViewport);
        }
        previewChart.setZoomType(ZoomType.HORIZONTAL);
    }

    /**
     * Viewport listener for preview chart(lower one). in {@link #onViewportChanged(Viewport)} method change
     * viewport of upper chart.
     */
    private class ViewportListener implements ViewportChangeListener {

        @Override
        public void onViewportChanged(Viewport newViewport) {
            // don't use animation, it is unnecessary when using preview chart because usually viewport changes
            // happens to often.
            chart.setCurrentViewport(newViewport);
        }

    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
