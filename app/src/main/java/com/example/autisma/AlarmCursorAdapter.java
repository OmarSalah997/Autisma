package com.example.autisma;

import android.annotation.SuppressLint;
import android.widget.CursorAdapter;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.autisma.data.alarm_contract;
public class AlarmCursorAdapter extends CursorAdapter {
    private TextView mTitleText, mDateAndTimeText, mRepeatInfoText;
    private ImageView mActiveImage , mThumbnailImage;
    private String currentLangCode;

    public AlarmCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
        currentLangCode =context.getResources().getConfiguration().locale.getLanguage();
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.alarm_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        mTitleText = (TextView) view.findViewById(R.id.alarm_message);
        mDateAndTimeText = (TextView) view.findViewById(R.id.alarm_date_time);
        mRepeatInfoText = (TextView) view.findViewById(R.id.repeat_info);
        mActiveImage = (ImageView) view.findViewById(R.id.alarm_on_off);


        int titleColumnIndex = cursor.getColumnIndex(alarm_contract.AlarmReminderEntry.KEY_TITLE);
        int dateColumnIndex = cursor.getColumnIndex(alarm_contract.AlarmReminderEntry.KEY_DATE);
        int timeColumnIndex = cursor.getColumnIndex(alarm_contract.AlarmReminderEntry.KEY_TIME);
        int repeatColumnIndex = cursor.getColumnIndex(alarm_contract.AlarmReminderEntry.KEY_REPEAT);
        int repeatNoColumnIndex = cursor.getColumnIndex(alarm_contract.AlarmReminderEntry.KEY_REPEAT_NO);
        int repeatTypeColumnIndex = cursor.getColumnIndex(alarm_contract.AlarmReminderEntry.KEY_REPEAT_TYPE);
        int activeColumnIndex = cursor.getColumnIndex(alarm_contract.AlarmReminderEntry.KEY_ACTIVE);

        String title = cursor.getString(titleColumnIndex);
        String date = cursor.getString(dateColumnIndex);
        String time = cursor.getString(timeColumnIndex);
        String repeat = cursor.getString(repeatColumnIndex);
        String repeatNo = cursor.getString(repeatNoColumnIndex);
        String repeatType = cursor.getString(repeatTypeColumnIndex);
        String active = cursor.getString(activeColumnIndex);
        if(date !=null)
            {
                String dateTime = date + " " + time;
                setReminderDateTime(dateTime);

            }else{
            mDateAndTimeText.setText(R.string.datenotset);
        }

        if (repeat !=null)
        {
            setReminderRepeatInfo(repeat, repeatNo, repeatType);
        }else{
            mRepeatInfoText.setText(R.string.repeatnotset);
        }

        setReminderTitle(title);

if(active !=null)
        setActiveImage(active);
else{
    mActiveImage.setImageResource(R.drawable.ic_baseline_notifications_off_24);
}



    }

    // Set reminder title view
    public void setReminderTitle(String title) {
        mTitleText.setText(title);
    }

    // Set date and time views
    public void setReminderDateTime(String datetime) {
        mDateAndTimeText.setText(datetime);
    }

    // Set repeat views
    @SuppressLint("SetTextI18n")
    public void setReminderRepeatInfo(String repeat, String repeatNo, String repeatType) {
        if(repeat.equals("true")){
            if(currentLangCode.equals("en"))
            mRepeatInfoText.setText("repeat after"+ repeatNo + " " + repeatType );
            else
                mRepeatInfoText.setText("تكرار كل"+ repeatNo + " " + repeatType );
        }else if (repeat.equals("false")) {
            mRepeatInfoText.setText(R.string.repeat_off);
        }
    }

    // Set active image as on or off
    public void setActiveImage(String active){
        if(active.equals("true")){
            mActiveImage.setImageResource(R.drawable.ic_baseline_notifications_on_24);
        }else if (active.equals("false")) {
            mActiveImage.setImageResource(R.drawable.ic_baseline_notifications_off_24);
        }
    }
}
