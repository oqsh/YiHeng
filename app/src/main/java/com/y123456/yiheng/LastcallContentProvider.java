package com.y123456.yiheng;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.io.File;

public class LastcallContentProvider extends ContentProvider {
    private static final String TAG = "LastCallContentProvider";
    private SQLiteDatabase db;

    public LastcallContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        final String table = "lastcall";
        return db.delete(table, selection, selectionArgs);
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String tel = (String)values.get("tel");
        String start_time = (String)values.get("start_time");
        Integer call_duration = (Integer)values.get("call_duration"); // 通话时长（秒）
        ContentValues cv = new ContentValues();
        cv.put("tel", tel);
        cv.put("start_time", start_time);
        cv.put("call_duration", call_duration);
        db.insert("lastcall", null, cv);
        return uri;
    }

    @Override
    public boolean onCreate() {
        File file = new File(getContext().getFilesDir(), "lastcall.db3");
        DBOpenHandler dbOpenHandler = new DBOpenHandler(getContext(), file.getAbsolutePath(), null, 1);
        db = dbOpenHandler.getReadableDatabase();
        final String create_table_sql = "CREATE TABLE IF NOT EXISTS lastcall(id integer primary key autoincrement, tel varchar(64) not null, start_time timestamp not null, call_duration integer not null)";
        db.execSQL(create_table_sql);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final String table = "lastcall";
        final String groupBy = "";
        final String having = "";
        return db.query(table, projection, selection, selectionArgs, groupBy, having, sortOrder);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        final String table = "lastcall";
        return db.update(table, values, selection, selectionArgs);
    }
}
