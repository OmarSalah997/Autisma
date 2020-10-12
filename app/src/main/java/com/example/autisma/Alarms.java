package com.example.autisma;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;


import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;


import android.annotation.SuppressLint;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.autisma.data.alarm_contract;

import java.util.Calendar;

public class Alarms extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    ListView alarms_list;
    AlarmCursorAdapter CursorAdapter;
    TextView edit_alarm_text;
    private String mTime;
    private String mDate;
    private String mRepeat;
    private String mRepeatNo;
    private String mRepeatType;
    private String mActive;

    private static final int VEHICLE_LOADER = 0;
    private String title="";
    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarms);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar !=null)
            actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.toolbar_shape));

        Calendar mCalendar = Calendar.getInstance();
        int mHour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int mMinute = mCalendar.get(Calendar.MINUTE);
        int mYear = mCalendar.get(Calendar.YEAR);
        int mMonth = mCalendar.get(Calendar.MONTH) + 1;
        int mDay = mCalendar.get(Calendar.DATE);

        mDate = mDay + "/" + mMonth + "/" + mYear;
        mTime = mHour + ":" + mMinute;
        alarms_list=findViewById(R.id.alarm_list);
        edit_alarm_text=findViewById(R.id.alarms_top_text);
        View emptyView=findViewById(R.id.no_alarms_text);
        alarms_list.setEmptyView(emptyView);
        CursorAdapter = new AlarmCursorAdapter(this, null);
        alarms_list.setAdapter(CursorAdapter);
        alarms_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(Alarms.this, Add_alarm.class);

                Uri currentVehicleUri = ContentUris.withAppendedId(alarm_contract.AlarmReminderEntry.CONTENT_URI, id);
                // Set the URI on the data field of the intent
                intent.setData(currentVehicleUri);

                startActivity(intent);

            }
        });
        final FloatingActionButton add_alarm = findViewById(R.id.add_alarm_btn);
        add_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Intent intent=new Intent(view.getContext(),Add_alarm.class);
                //startActivity(intent);
                AddTitle();
            }
        });

        getLoaderManager().initLoader(VEHICLE_LOADER, null,  this);

    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {
                alarm_contract.AlarmReminderEntry._ID,
                alarm_contract.AlarmReminderEntry.KEY_TITLE,
                alarm_contract.AlarmReminderEntry.KEY_DATE,
                alarm_contract.AlarmReminderEntry.KEY_TIME,
                alarm_contract.AlarmReminderEntry.KEY_REPEAT,
                alarm_contract.AlarmReminderEntry.KEY_REPEAT_NO,
                alarm_contract.AlarmReminderEntry.KEY_REPEAT_TYPE,
                alarm_contract.AlarmReminderEntry.KEY_ACTIVE

        };

        return new CursorLoader(this,   // Parent activity context
                alarm_contract.AlarmReminderEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order

    }




    @Override
    public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor cursor){
        CursorAdapter.swapCursor(cursor);
        if(cursor.getCount()>0)
            edit_alarm_text.setVisibility(View.VISIBLE);
        else
            edit_alarm_text.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
        CursorAdapter.swapCursor(null);
    }
    public void AddTitle(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.add_title);
        final EditText input=new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(input.getText().toString().isEmpty())
                    return;
                title=input.getText().toString();
                ContentValues values = new ContentValues();
                values.put(alarm_contract.AlarmReminderEntry.KEY_TITLE, title);
                values.put(alarm_contract.AlarmReminderEntry.KEY_DATE, mDate);
                values.put(alarm_contract.AlarmReminderEntry.KEY_TIME, mTime);
                values.put(alarm_contract.AlarmReminderEntry.KEY_REPEAT, true);
                values.put(alarm_contract.AlarmReminderEntry.KEY_REPEAT_NO, "1");
                values.put(alarm_contract.AlarmReminderEntry.KEY_REPEAT_TYPE, "Hour");
                values.put(alarm_contract.AlarmReminderEntry.KEY_ACTIVE, true);
                Uri newuri=getContentResolver().insert(alarm_contract.AlarmReminderEntry.CONTENT_URI, values);
                restartLoader();

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }
    public void restartLoader(){
        getLoaderManager().restartLoader(VEHICLE_LOADER, null,  this);
    }
}