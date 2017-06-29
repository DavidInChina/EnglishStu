package bdkj.com.englishstu.common.wheelpicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Calendar;

import bdkj.com.englishstu.R;
import bdkj.com.englishstu.common.wheel.LoopView;
import bdkj.com.englishstu.common.wheel.OnItemSelectedListener;

/**
 * Created by younminx on 2017/3/17.
 * 日期选择控件
 */

public class DatePicker
        extends RelativeLayout {
    int startYear = 1970;
    Calendar calendar;
    //月数据
    LoopView monthView;
    ArrayList<String> monthList;
    //天数据
    LoopView dayView;
    ArrayList<String> dayList;
    //年数据
    LoopView yearView;
    ArrayList<String> yearList;
    //时数据
    LoopView hourView;
    ArrayList<String> hourList;
    //分数据
    LoopView minuteView;
    ArrayList<String> minuteList;

    public DatePicker(Context context) {
        super(context);
        init();
    }

    private void init() {
        calendar = Calendar.getInstance();
        LayoutInflater.from(getContext()).inflate(R.layout.layout_date_picker, this);
        //月数据
        monthView = (LoopView) findViewById(R.id.monthview);
        monthList = new ArrayList<>();
        //天数据
        dayView = (LoopView) findViewById(R.id.dayview);
        dayList = new ArrayList<>();
        //年数据
        yearView = (LoopView) findViewById(R.id.yearview);
        yearList = new ArrayList<>();
        //时数据
        hourView = (LoopView) findViewById(R.id.hourview);
        hourList = new ArrayList<>();
        //分数据
        minuteView = (LoopView) findViewById(R.id.minuteview);
        minuteList = new ArrayList<>();
        for (int i = startYear; i <= calendar.get(Calendar.YEAR) + 10; i++) {
            yearList.add(i + "");
        }
        //滚动监听
        yearView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                initDayView();
            }
        });
        //设置数据
        yearView.setItems(yearList);
        yearView.setCurrentPosition(calendar.get(Calendar.YEAR) - startYear);

        for (int i = 1; i <= 12; i++) {
            monthList.add(i + "");
        }
        monthView.setItems(monthList);
        monthView.setCurrentPosition(calendar.get(Calendar.MONTH));
        //滚动监听
        monthView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                //初始化填天数据
                initDayView();
            }
        });
        initDayView();
        //初始化小时数据
        for (int i = 0; i < 24; i++) {
            hourList.add((i < 10 ? "0" : "") + i);
        }
        hourView.setItems(hourList);
        hourView.setCurrentPosition(calendar.get(Calendar.HOUR_OF_DAY));
        //初始化分钟数据
        for (int i = 0; i < 60; i++) {
            minuteList.add((i < 10 ? "0" : "") + i);
        }
        minuteView.setItems(minuteList);
        minuteView.setCurrentPosition(calendar.get(Calendar.MINUTE));

    }

    private void initDayView() {
        Calendar dayCalendar = Calendar.getInstance();
        dayCalendar.set(yearView.getSelectedItem() + startYear, monthView.getSelectedItem(), 1);
        dayList.clear();
        for (int i = 1; i <= dayCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            dayList.add(i + "");
        }
        dayView.setItems(dayList);
        dayView.setCurrentPosition(calendar.get(Calendar.DATE));
    }

    public String getDate() {
        String date = yearList.get(yearView.getSelectedItem()) + "-" + monthList.get(monthView.getSelectedItem()) + "-" + dayList.get(dayView.getSelectedItem());
        String time = hourList.get(hourView.getSelectedItem()) + ":" + minuteList.get(minuteView.getSelectedItem());
        return date + " " + time;
    }
}
