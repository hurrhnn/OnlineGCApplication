package app.mobilecontests.onlinegcapplication.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        String query = "CREATE TABLE if not exists google_classroom ("
                + "course blob,"
                + "course_works blob);";
        database.execSQL(query);

        query = "CREATE TABLE if not exists ebs_classroom ("
                + "account_info blob,"
                + "lectures blob);";

        database.execSQL(query);

        query = "CREATE TABLE if not exists school_timetable ("
                + "date text,"
                + "timetable blob);";
        database.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        String query = "DROP TABLE if exists google_classroom";
        database.execSQL(query);

        query = "DROP TABLE if exists ebs_classroom";
        database.execSQL(query);
        onCreate(database);

        query = "DROP TABLE if exists school_timetable";
        database.execSQL(query);
        onCreate(database);
    }
}
