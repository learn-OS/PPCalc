package cat.company.ppcalc.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class DayPointsContentProvider extends ContentProvider {
    private static HashMap<String, String> sDayPointsProjectionMap;
    static {
        sDayPointsProjectionMap=new HashMap<String, String>();
        sDayPointsProjectionMap.put(DayPointsProviderMetadata.DayPointsTableMetadata._ID,
                DayPointsProviderMetadata.DayPointsTableMetadata._ID);
        sDayPointsProjectionMap.put(DayPointsProviderMetadata.DayPointsTableMetadata.DATE,
                DayPointsProviderMetadata.DayPointsTableMetadata.DATE);
        sDayPointsProjectionMap.put(DayPointsProviderMetadata.DayPointsTableMetadata.POINTS,
                DayPointsProviderMetadata.DayPointsTableMetadata.POINTS);
        sDayPointsProjectionMap.put(DayPointsProviderMetadata.DayPointsTableMetadata.COMMENT,
                DayPointsProviderMetadata.DayPointsTableMetadata.COMMENT);
        sDayPointsProjectionMap.put(DayPointsProviderMetadata.DayPointsTableMetadata.CREATED_DATE,
                DayPointsProviderMetadata.DayPointsTableMetadata.CREATED_DATE);
        sDayPointsProjectionMap.put(DayPointsProviderMetadata.DayPointsTableMetadata.MODIFIED_DATE,
                DayPointsProviderMetadata.DayPointsTableMetadata.MODIFIED_DATE);
    }

    private static final UriMatcher sUriMatcher;
    private static final int INCOMING_POINT_COLLECTION_URI_INDICATOR = 1;
    private static final int INCOMING_SINGLE_POINT_URI_INDICATOR = 2;
    private static final int INCOMING_DAYS_COLLECTION_URI_INDICATOR = 3;
    private static final int INCOMING_WEEK_COLLECTION_URI_INDICATOR = 4;
    private static final int INCOMING_MONTH_COLLECTION_URI_INDICATOR=5;
    static {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(DayPointsProviderMetadata.AUTHORITY,"daypoints", INCOMING_POINT_COLLECTION_URI_INDICATOR);
        sUriMatcher.addURI(DayPointsProviderMetadata.AUTHORITY,"daypoints/#",INCOMING_SINGLE_POINT_URI_INDICATOR);
        sUriMatcher.addURI(DayPointsProviderMetadata.AUTHORITY,"lastdays/#",INCOMING_DAYS_COLLECTION_URI_INDICATOR);
        sUriMatcher.addURI(DayPointsProviderMetadata.AUTHORITY, "week", INCOMING_WEEK_COLLECTION_URI_INDICATOR);
        sUriMatcher.addURI(DayPointsProviderMetadata.AUTHORITY, "month", INCOMING_MONTH_COLLECTION_URI_INDICATOR);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DayPointsProviderMetadata.DATABASE_NAME, null, DayPointsProviderMetadata.DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + DayPointsProviderMetadata.DayPointsTableMetadata.TABLE_NAME + " ("
                            + DayPointsProviderMetadata.DayPointsTableMetadata._ID + " INTEGER PRIMARY KEY,"
                            + DayPointsProviderMetadata.DayPointsTableMetadata.DATE + " DATETIME DEFAULT (datetime('now','localtime')),"
                            + DayPointsProviderMetadata.DayPointsTableMetadata.POINTS + " INTEGER,"
                            + DayPointsProviderMetadata.DayPointsTableMetadata.COMMENT+" NVARCHAR(100),"
                            + DayPointsProviderMetadata.DayPointsTableMetadata.CREATED_DATE + " DATETIME DEFAULT (datetime('now','localtime')),"
                            + DayPointsProviderMetadata.DayPointsTableMetadata.MODIFIED_DATE + " DATETIME DEFAULT (datetime('now','localtime')))"
            );
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i2) {
            db.execSQL("DROP TABLE IF EXISTS " +
                    DayPointsProviderMetadata.DayPointsTableMetadata.TABLE_NAME);
            onCreate(db);
        }
    }

    private DatabaseHelper mOpenHelper;

    public DayPointsContentProvider(){

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int count;
        switch (sUriMatcher.match(uri)) {
            case INCOMING_POINT_COLLECTION_URI_INDICATOR:
                count = db.delete(DayPointsProviderMetadata.DayPointsTableMetadata.TABLE_NAME, selection, selectionArgs);
                break;
            case INCOMING_SINGLE_POINT_URI_INDICATOR:
                String rowId = uri.getPathSegments().get(1);
                count = db.delete(DayPointsProviderMetadata.DayPointsTableMetadata.TABLE_NAME,
                        DayPointsProviderMetadata.DayPointsTableMetadata._ID + "=" + rowId +
                                (!TextUtils.isEmpty(selection) ? " AND (" + selection + ")" : ""),
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI "+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)){
            case INCOMING_POINT_COLLECTION_URI_INDICATOR:
                return DayPointsProviderMetadata.DayPointsTableMetadata.CONTENT_TYPE;
            case INCOMING_SINGLE_POINT_URI_INDICATOR:
                return DayPointsProviderMetadata.DayPointsTableMetadata.CONTENT_ITEM_TYPE;
            case INCOMING_DAYS_COLLECTION_URI_INDICATOR:
                return DayPointsProviderMetadata.DayPointsTableMetadata.CONTENT_DAYS_TYPE;
            default:
                throw new IllegalArgumentException("Unknown uri "+uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues initialValues) {
        // Validate the requested uri
        if (sUriMatcher.match(uri) != INCOMING_POINT_COLLECTION_URI_INDICATOR)
        {
            throw new IllegalArgumentException("Unknown URI " + uri);
        }
        ContentValues values;
        if (initialValues != null) {
            values = new ContentValues(initialValues);
        } else {
            values = new ContentValues();
        }

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (!values.containsKey(DayPointsProviderMetadata.DayPointsTableMetadata.DATE)) {
            values.put(DayPointsProviderMetadata.DayPointsTableMetadata.DATE,sdf.format(new Date()));
        }

        if (!values.containsKey(DayPointsProviderMetadata.DayPointsTableMetadata.POINTS)) {
            throw new SQLException(
                    "Failed to insert row because points are needed " + uri);
        }

        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        long rowId = db.insert(DayPointsProviderMetadata.DayPointsTableMetadata.TABLE_NAME,
                DayPointsProviderMetadata.DayPointsTableMetadata.POINTS, values);
        if (rowId > 0) {
            Uri insertedBookUri =
                    ContentUris.withAppendedId(DayPointsProviderMetadata.DayPointsTableMetadata.CONTENT_URI, rowId);
            getContext() .getContentResolver()
                    .notifyChange(insertedBookUri, null);
            return insertedBookUri;
        }
        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public boolean onCreate() {
        mOpenHelper=new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        switch (sUriMatcher.match(uri)) {
            case INCOMING_POINT_COLLECTION_URI_INDICATOR:
                qb.setTables(DayPointsProviderMetadata.DayPointsTableMetadata.TABLE_NAME);
                qb.setProjectionMap(sDayPointsProjectionMap);
                break;
            case INCOMING_SINGLE_POINT_URI_INDICATOR:
                qb.setTables(DayPointsProviderMetadata.DayPointsTableMetadata.TABLE_NAME);
                qb.setProjectionMap(sDayPointsProjectionMap);
                qb.appendWhere(DayPointsProviderMetadata.DayPointsTableMetadata._ID + "="
                    + uri.getPathSegments().get(1));
                break;
            case INCOMING_DAYS_COLLECTION_URI_INDICATOR:
                qb.setTables(DayPointsProviderMetadata.DayPointsTableMetadata.TABLE_NAME);
                // TODO: Finish query.
                break;
            case INCOMING_WEEK_COLLECTION_URI_INDICATOR: {
                String query = "SELECT *," +
                        " STRFTIME('%d-%m-%Y'," + DayPointsProviderMetadata.DayPointsTableMetadata.DATE + ") AS DATE," +
                        " SUM(" + DayPointsProviderMetadata.DayPointsTableMetadata.POINTS + ") AS POINTSUM" +
                        " FROM " + DayPointsProviderMetadata.DayPointsTableMetadata.TABLE_NAME +
                        " WHERE STRFTIME('%W-%Y'," + DayPointsProviderMetadata.DayPointsTableMetadata.DATE + ")=STRFTIME('%W-%Y','now')" +
                        " GROUP BY STRFTIME('%d-%m-%Y'," + DayPointsProviderMetadata.DayPointsTableMetadata.DATE + ")" +
                        " ORDER BY " + DayPointsProviderMetadata.DayPointsTableMetadata.DATE;
                SQLiteDatabase db = mOpenHelper.getReadableDatabase();
                Cursor c = db.rawQuery(query, null);

                int i = c.getCount();

                c.setNotificationUri(getContext().getContentResolver(), uri);
                return c;
            }
            case INCOMING_MONTH_COLLECTION_URI_INDICATOR: {
                String query = "SELECT *," +
                        " STRFTIME('%d-%m-%Y'," + DayPointsProviderMetadata.DayPointsTableMetadata.DATE + ") AS DATE," +
                        " SUM(" + DayPointsProviderMetadata.DayPointsTableMetadata.POINTS + ") AS POINTSUM" +
                        " FROM " + DayPointsProviderMetadata.DayPointsTableMetadata.TABLE_NAME +
                        " WHERE STRFTIME('%m-%Y'," + DayPointsProviderMetadata.DayPointsTableMetadata.DATE + ")=STRFTIME('%m-%Y','now')" +
                        " GROUP BY STRFTIME('%d-%m-%Y'," + DayPointsProviderMetadata.DayPointsTableMetadata.DATE + ")" +
                        " ORDER BY " + DayPointsProviderMetadata.DayPointsTableMetadata.DATE;
                SQLiteDatabase db = mOpenHelper.getReadableDatabase();
                Cursor c = db.rawQuery(query, null);

                int i = c.getCount();

                c.setNotificationUri(getContext().getContentResolver(), uri);
                return c;
            }
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        String orderBy;
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = DayPointsProviderMetadata.DayPointsTableMetadata.DEFAULT_SORT_ORDER; }
        else {
            orderBy = sortOrder;
        }

        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection,
                selectionArgs, null, null, orderBy);

        int i = c.getCount();

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase(); int count;
        switch (sUriMatcher.match(uri)) {
            case INCOMING_POINT_COLLECTION_URI_INDICATOR:
                count = db.update(DayPointsProviderMetadata.DayPointsTableMetadata.TABLE_NAME, values, selection, selectionArgs);
                break;
            case INCOMING_SINGLE_POINT_URI_INDICATOR:
                String rowId = uri.getPathSegments().get(1); count = db.update(DayPointsProviderMetadata.DayPointsTableMetadata.TABLE_NAME,
                    values, DayPointsProviderMetadata.DayPointsTableMetadata._ID + "=" + rowId
                            + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""),
                    selectionArgs); break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
