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

//        query = "CREATE TABLE if not exists ebs_classroom ("
//                + "_id integer primary key autoincrement,"
//                + "course blob,"
//                + "course_work blob);";
//
//        database.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        String query = "DROP TABLE if exists google_classroom";

        database.execSQL(query);
        onCreate(database);
    }
}
