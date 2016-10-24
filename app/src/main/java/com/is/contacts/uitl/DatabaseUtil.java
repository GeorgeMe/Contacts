package com.is.contacts.uitl;

/**
 * Created by Administrator on 2016/10/20 0020.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseUtil {
    private static final String TAG = "DatabaseUtil";

    private static final String DATABASE_NAME = "record_database";

    /**
     * Database Version
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Table Name
     */
    private static final String DATABASE_TABLE = "tb_record";

    /**
     * Table columns
     */
    public static final String KEY_NAME = "time";
    public static final String KEY_COUNT = "count";
    public static final String KEY_ROWID = "_id";

    /**
     * Database creation sql statement
     */
    private static final String CREATE_RECORD_TABLE =
            "create table " + DATABASE_TABLE + "(" + KEY_ROWID + " integer primary key autoincrement,"
                    + KEY_NAME + "text," + KEY_COUNT + "text);";

    /**
     * Context
     */
    private final Context mCtx;

    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Inner private class. Database Helper class for creating and updating database.
     */
    public static class DatabaseHelper extends SQLiteOpenHelper {
        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i(TAG, "Creating DataBase:" + CREATE_RECORD_TABLE);
            db.execSQL(CREATE_RECORD_TABLE);
        }

        /**
         * onUpgrade method is called when database version changes.
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version" + oldVersion + "to"
                    + newVersion);
        }
    }

    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public DatabaseUtil(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * This method is used for creating/opening connection
     *
     * @return instance of DatabaseUtil
     * @throws SQLException
     */
    public DatabaseUtil open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    /**
     * This method is used for closing the connection.
     */
    public void close() {
        mDbHelper.close();
    }

    /**
     * This method is used to create/insert new   record.
     *
     * @return long
     */
    public long insert(String time, String count) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, time);
        initialValues.put(KEY_COUNT, count);
        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * This method will delete  record.
     *
     * @param rowId
     * @return boolean
     */
    public boolean delete(long rowId) {
        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * This method will return Cursor holding all the  records.
     *
     * @return Cursor
     */
    public Cursor fetchAll() {
        return mDb.query(DATABASE_TABLE, null, null, null, null, null, null);
    }

    /**
     * This method will return Cursor holding the specific Student record.
     *
     * @param id
     * @return Cursor
     * @throws SQLException
     */
    public Cursor fetch(long id) throws SQLException {
        Cursor mCursor =
                mDb.query(true, DATABASE_TABLE, new String[]{KEY_ROWID,
                                KEY_NAME, KEY_COUNT}, KEY_ROWID + "=" + id, null,
                        null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    /**
     * This method will update  record.
     *
     * @param id
     * @param name
     * @param standard
     * @return boolean
     */
    public boolean update(int id, String name, String standard) {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        args.put(KEY_COUNT, standard);
        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + id, null) > 0;
    }
}