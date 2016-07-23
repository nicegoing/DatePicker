package com.datepicker;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayout;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author puhanhui
 * @version 1.0
 * @date 2016/7/21
 * @since 1.0
 */
public class DatePickerDialog extends DialogFragment {

    private MyPageAdapter myPageAdapter;
    /**
     * 前一月，当前月，后一月的数据
     */
    private ArrayList<DateBean> list = new ArrayList<>();
    private Calendar  calendar;
    private int       curYear;
    private int       curMonth;
    private int       curDay;
    private int       curPosition;
    private int       selectYear;
    private int       selectMonth;
    private int       selectDay;
    private TextView  tvYear;
    private TextView  tvDetail;
    private ViewPager viewPager;
    private ImageView ivForward;
    private ImageView ivBack;
    private ViewPager.OnPageChangeListener myPageChangeListener;
    private TextView tvCancel;
    private TextView tvOk;
    private OnDateSetListener onDateSetListener;
    public static DatePickerDialog newInstance() {

        Bundle args = new Bundle();
        DatePickerDialog fragment = new DatePickerDialog();
        fragment.setArguments(args);
        return fragment;
    }

    interface OnDateSetListener{
        void selectDate(int year,int month,int day);
    }

    public void setOnDateSetListener(OnDateSetListener onDateSetListener) {
        this.onDateSetListener = onDateSetListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_date_picker, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        tvYear = (TextView) v.findViewById(R.id.tv_year);
        tvDetail = (TextView) v.findViewById(R.id.tv_detail);
        tvCancel = (TextView) v.findViewById(R.id.tv_cancel);
        tvOk = (TextView) v.findViewById(R.id.tv_ok);
        viewPager = (ViewPager) v.findViewById(R.id.view_pager);
        ivBack = (ImageView) v.findViewById(R.id.iv_back);
        ivForward = (ImageView) v.findViewById(R.id.iv_forward);
        calendar = Calendar.getInstance(Locale.getDefault());
        curYear = calendar.get(Calendar.YEAR);
        curMonth = calendar.get(Calendar.MONTH);
        curDay = calendar.get(Calendar.DAY_OF_MONTH);
        selectYear = curYear;
        selectMonth = curMonth;
        selectDay = curDay;
        setSelectDetail();
        initList();
        myPageAdapter = new MyPageAdapter();
        viewPager.setAdapter(myPageAdapter);
        viewPager.setCurrentItem(curYear);
        curPosition = curYear;
        initEvent();
        return v;
    }

    private void initEvent() {
        //设置当前数据
        myPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.i("onPageSelected","onPageSelected");
                //设置当前数据
                setCurrentData(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        viewPager.addOnPageChangeListener(myPageChangeListener);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCurrentData(curPosition-1);
                viewPager.setCurrentItem(curPosition);
            }
        });
        ivForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCurrentData(curPosition+1);
                viewPager.setCurrentItem(curPosition);
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.this.dismiss();
            }
        });

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onDateSetListener!=null){
                    onDateSetListener.selectDate(selectYear,selectMonth,selectDay);
                    DatePickerDialog.this.dismiss();
                }
            }
        });
    }

    public void setCurrentData(int position){
        if (position==curPosition){
            return;
        }
        DateBean dateBean = list.get(position - curPosition + 1);
        curYear = dateBean.getYear();
        curMonth = dateBean.getMonth();
        if (position > curPosition) {
            list.add(getNextDateBean());
            list.remove(0);
        }
        if (position < curPosition) {
            list.add(0, getPrevDateBean());
            list.remove(3);
        }
        curPosition = position;

    }

    /**
     * 设置选中日期
     */
    private void setSelectDetail() {
        calendar.set(selectYear, selectMonth, selectDay);
        int flags = DateUtils.FORMAT_SHOW_DATE|DateUtils.FORMAT_SHOW_WEEKDAY|DateUtils.FORMAT_ABBREV_WEEKDAY|DateUtils.FORMAT_ABBREV_MONTH|DateUtils.FORMAT_NO_YEAR;
        tvYear.setText(String.valueOf(selectYear));
        tvDetail.setText(DateUtils.formatDateTime(getContext(), calendar.getTimeInMillis(), flags));
    }


    /**
     * 初始化日历数据集合
     */
    private void initList() {
        DateBean dateBean = getDateBean(calendar);
        list.add(getPrevDateBean());
        list.add(dateBean);
        list.add(getNextDateBean());
        Log.i("preDateBean:", getPrevDateBean().toString());
        Log.i("dateBean:", dateBean.toString());
        Log.i("nextDateBean:", getNextDateBean().toString());
    }

    /**
     * 获取当前时间后一个月的数据
     *
     * @return 返回数据实体类
     */
    public DateBean getNextDateBean() {
        if (curMonth == Calendar.DECEMBER) {
            calendar.set(curYear + 1, Calendar.JANUARY, 1);
        } else {
            calendar.set(curYear, curMonth + 1, 1);
        }
        return getDateBean(calendar);
    }

    /**
     * 获取当前时间前一个月的数据
     *
     * @return 返回数据实体类
     */
    public DateBean getPrevDateBean() {
        if (curMonth == Calendar.JANUARY) {
            calendar.set(curYear - 1, Calendar.DECEMBER, 1);
        } else {
            calendar.set(curYear, curMonth - 1, 1);
        }
        return getDateBean(calendar);
    }

    /**
     * 获取当前日历数据
     *
     * @param calendar 当前日历
     * @return 当日日历数据实体类
     */
    public DateBean getDateBean(Calendar calendar) {
        DateBean dateBean = new DateBean();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        dateBean.setYear(calendar.get(Calendar.YEAR));
        dateBean.setMonth(calendar.get(Calendar.MONTH));
        dateBean.setDateOfMonth(calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        dateBean.setStartWeek(calendar.get(Calendar.DAY_OF_WEEK));
        return dateBean;
    }

    class MyPageAdapter extends PagerAdapter {
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            Log.i("instantiateItem:","instantiateItem"+position);
            DateBean dateBean = list.get(position - curPosition + 1);
            float dimension = getContext().getResources().getDimension(R.dimen.item);
            LinearLayout view = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.item_grid, container, false);
            final TextView tvMonth = (TextView) view.findViewById(R.id.tv_month);
            final GridLayout gridLayout = (GridLayout) view.findViewById(R.id.gl_calendar);
            calendar.set(dateBean.getYear(),dateBean.getMonth(),1);
            int flags = DateUtils.FORMAT_NO_MONTH_DAY | DateUtils.FORMAT_SHOW_DATE|DateUtils.FORMAT_SHOW_YEAR;
            tvMonth.setText(DateUtils.formatDateTime(getContext(), calendar.getTimeInMillis(), flags));
            //add weekday
            for (int i = 0; i <7; i++) {
                TextView textView = new TextView(getContext());
                GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(GridLayout.spec(0), GridLayout.spec(i));
                textView.setText(DateUtils.getDayOfWeekString(i+1,DateUtils.LENGTH_SHORTEST));
                textView.setGravity(Gravity.CENTER);
                ViewGroup.LayoutParams params = gridLayout.getLayoutParams();
                layoutParams.width = (int) dimension;
                layoutParams.height = (int) dimension;
                textView.setLayoutParams(params);
                gridLayout.addView(textView, layoutParams);
            }

            int j = dateBean.getStartWeek() - 1;
            boolean flag = false;//是否选中的日期在该月中
            if (dateBean.getYear() == selectYear && dateBean.getMonth() == selectMonth) {
                flag = true;
            }
            for (int i = 0; i < dateBean.getDateOfMonth(); i++) {
                final TextView textView = new TextView(getContext());
                if (flag && i == selectDay - 1) {
                    textView.setBackgroundResource(R.drawable.circle);
                    viewPager.setTag(textView);
                }
                GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(GridLayout.spec(j / 7 + 1), GridLayout.spec(j % 7));
                j++;
                textView.setText(i + 1 + "");
                textView.setTextColor(Color.BLACK);
                textView.setGravity(Gravity.CENTER);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Object v =viewPager.getTag();
                        if (v!=null){
                            ((View)v).setBackgroundDrawable(null);//清空当前页选中
                        }
                        selectYear = curYear;
                        selectMonth = curMonth;
                        selectDay = gridLayout.indexOfChild(view) + 1 - 7;
                        setSelectDetail();
                        textView.setBackgroundResource(R.drawable.circle);
                        gridLayout.requestLayout();
                        viewPager.setTag(textView);
                    }
                });
                ViewGroup.LayoutParams params = gridLayout.getLayoutParams();
                layoutParams.width = (int) dimension;
                layoutParams.height = (int) dimension;
                textView.setLayoutParams(params);
                gridLayout.addView(textView, layoutParams);
            }
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE / 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }


}
