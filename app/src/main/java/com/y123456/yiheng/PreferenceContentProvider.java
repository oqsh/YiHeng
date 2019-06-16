package com.y123456.yiheng;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import java.io.File;

public class PreferenceContentProvider extends ContentProvider {
    private static final String TAG = "PreferenceCP";
    private static final String table = "preference";
        
    private SQLiteDatabase db;

    public PreferenceContentProvider() {}

    @Override
    public boolean onCreate() {
        File file = new File(getContext().getFilesDir(), "contact.db3");
        DBOpenHandler dbOpenHandler = new DBOpenHandler(getContext(), file.getAbsolutePath(), null, 1);
        db = dbOpenHandler.getReadableDatabase();

        // preference 采用 key, value 的方式存储用户设置，方便扩展，来自KQ的idea
        final String create_table_sql = "CREATE TABLE IF NOT EXISTS preference("
                                    + "k varchar(64) unique not null, "
                                    + "v varchar(64) not null )";

        db.execSQL(create_table_sql);
        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String projection[] = new String[]{"k", "v"};
        String selection = "k = ?";
        String selectionArgs[] = new String[] {(String)values.get("k")};
        Cursor cursor = db.query(table, projection, selection, selectionArgs, "", "", "");

        Log.i(TAG, "k = "+values.get("k"));
        Log.i(TAG, "v = "+values.get("v"));

        if(cursor.getCount() > 0) {
            Log.i(TAG, "表中已存在该key，使用update");
            db.update(table, values, selection, selectionArgs);
        }
        else {
            Log.i(TAG, "表中没有该key，使用insert");
            db.insert(table, null, values);
        }

        Log.i(TAG, "插入成功");
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
