package com.datepicker;

/**
 * @author puhanhui
 * @version 1.0
 * @date 2016/7/21
 * @since 1.0
 */
public class DateBean {
    private int dateOfMonth;
    private int startWeek;
    private int year;
    private int month;


    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDateOfMonth() {
        return dateOfMonth;
    }

    public void setDateOfMonth(int dateOfMonth) {
        this.dateOfMonth = dateOfMonth;
    }

    public int getStartWeek() {
        return startWeek;
    }

    public void setStartWeek(int startWeek) {
        this.startWeek = startWeek;
    }

    @Override
    public String toString() {
        return "DateBean{" +
                "dateOfMonth=" + dateOfMonth +
                ", startWeek=" + startWeek +
                ", year=" + year +
                ", month=" + month +
                '}';
    }
}
