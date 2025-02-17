package eu.gregstr.peoplecounter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public final class DeviceLibraryContract {

    private DeviceLibraryContract() {}

    public static class DeviceEntry implements BaseColumns {
        public static final String TABLE_NAME = "Devices";
        public static final String COLUMN_NAME_DEVICE_NAME = "DeviceName";
        public static final String COLUMN_NAME_DEVICE_ADDRESS = "DeviceAddress";
    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DeviceEntry.TABLE_NAME + " (" +
                    DeviceEntry.COLUMN_NAME_DEVICE_NAME + " TEXT ," +
                    DeviceEntry.COLUMN_NAME_DEVICE_ADDRESS + " TEXT PRIMARY KEY)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DeviceEntry.TABLE_NAME;

    public static class DeviceLibraryDbHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "DeviceLibrary.db";

        public DeviceLibraryDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // upgrade policy is to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }
}
