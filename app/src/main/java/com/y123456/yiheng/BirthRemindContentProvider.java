package com.y123456.yiheng;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import java.io.File;

public class BirthRemindContentProvider extends ContentProvider {
    private static final String TAG = "BirthRemindContentProvider";
    private static final String table = "birthRemind";

    private SQLiteDatabase db;

    public BirthRemindContentProvider() {}

    @Override
    public boolean onCreate() {
        File file = new File(getContext().getFilesDir(), "contact.db3");
        DBOpenHandler dbOpenHandler = new DBOpenHandler(getContext(), file.getAbsolutePath(), null, 1);
        db = dbOpenHandler.getReadableDatabase();
        final String create_table_sql = "CREATE TABLE IF NOT EXISTS birthRemind("
                                    + "id integer primary key autoincrement,"
                                    + "user_id integer unique not null, "
                                    + "name varchar(64), "
                                    + "birthday varchar(64) default '0000-00-00')";

        db.execSQL(create_table_sql);
        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        db.insert(table, null, values);
        return uri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return db.delete(table, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        return db.update(table, values, selection, selectionArgs);
    }
    
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        final String groupBy = "";
        final String having = "";
        return db.query(table, projection, selection, selectionArgs,
                        groupBy, having, sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
