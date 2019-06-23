package com.y123456.yiheng;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import java.io.File;

public class ContactContentProvider extends ContentProvider {
    private static final String TAG = "ContactContentProvider";
    private static final String table = "contact";
    
    private SQLiteDatabase db;

    public ContactContentProvider() {}

    @Override
    public boolean onCreate() {
        File file = new File(getContext().getFilesDir(), "contact.db3");
        DBOpenHandler dbOpenHandler = new DBOpenHandler(getContext(), file.getAbsolutePath(), null, 1);
        db = dbOpenHandler.getReadableDatabase();
        final String create_table_sql = "CREATE TABLE IF NOT EXISTS contact("
                                    + "id integer primary key autoincrement, "
                                    + "name varchar(64), " 
                                    + "firstChars varchar(64), "
                                    + "tel varchar(64) unique not null, "
                                    + "birthday varchar(10) default '0000-00-00', "
                                    + "in_whitelist integer default 0, "
                                    + "in_birthRemind integer default 0)";

        db.execSQL(create_table_sql);
        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        values.put("firstChars", HanziToPinyin.getInstance().getFirstChars(values.getAsString("name")));
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
        values.put("firstChars", HanziToPinyin.getInstance().getFirstChars(values.getAsString("name")));
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
