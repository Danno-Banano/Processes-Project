package processes_project.lootandrun;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by trevor on 4/17/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    private static final String TABLE_NAME = "lnr_db";
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
            "ID         INTEGER PRIMARY KEY NOT NULL," +
            "TYPE       TEXT    NOT NULL," +
            "NAME       TEXT," +
            "HEALTH     INTEGER," +
            "MAX_HEALTH INTEGER," +
            "INVENTORY  TEXT," +
            "LATITUDE   REAL," +
            "LONGITUDE  REAL," +
            "POWER      INTEGER);";

    DatabaseHelper(Context context) {
        super(context, "db", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
