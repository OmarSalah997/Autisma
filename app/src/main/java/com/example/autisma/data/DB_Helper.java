package com.example.autisma.data;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB_Helper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "alarm.db";

    private static final int DATABASE_VERSION = 1;

    public DB_Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a String that contains the SQL statement to create the reminder table
        String SQL_CREATE_ALARM_TABLE =  "CREATE TABLE " + alarm_contract.AlarmReminderEntry.TABLE_NAME + " ("
                + alarm_contract.AlarmReminderEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + alarm_contract.AlarmReminderEntry.KEY_TITLE + " TEXT NOT NULL, "
                + alarm_contract.AlarmReminderEntry.KEY_DATE + " TEXT NOT NULL, "
                + alarm_contract.AlarmReminderEntry.KEY_TIME + " TEXT NOT NULL, "
                + alarm_contract.AlarmReminderEntry.KEY_REPEAT + " TEXT NOT NULL, "
                + alarm_contract.AlarmReminderEntry.KEY_REPEAT_NO + " TEXT NOT NULL, "
                + alarm_contract.AlarmReminderEntry.KEY_REPEAT_TYPE + " TEXT NOT NULL, "
                + alarm_contract.AlarmReminderEntry.KEY_ACTIVE + " TEXT NOT NULL " + " );";

        // Execute the SQL statement
        sqLiteDatabase.execSQL(SQL_CREATE_ALARM_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
