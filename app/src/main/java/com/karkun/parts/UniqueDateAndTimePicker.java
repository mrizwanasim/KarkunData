package com.karkun.parts;

import android.content.Context;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

public class UniqueDateAndTimePicker {

    private DatePicker datePicker;
    private TimePicker timePicker;
    private Context context;

    public UniqueDateAndTimePicker(Context context) {
        this.context = context;
    }

    public long uniqueDateAndTimePicker() {
        datePicker = new DatePicker(context);
        timePicker = new TimePicker(context);

        Calendar calendar = Calendar.getInstance();
        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),
                timePicker.getCurrentHour(), timePicker.getCurrentMinute(), 0);
        long startTime = calendar.getTimeInMillis();
        return startTime;
    }
}
