package cat.company.ppcalc.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by carles on 02/08/14.
 * DayPoint provider metadata definitions.
 */
public class DayPointsProviderMetadata {
    public static final String AUTHORITY = "cat.company.ppcalc.daypointsProvider";
    public static final String DATABASE_NAME = "ppcalc.db";
    public static final int DATABASE_VERSION = 2;
    public static final String POINTS_TABLENAME = "daypoints";

    public DayPointsProviderMetadata() {}

    public static final class DayPointsTableMetadata implements BaseColumns {
        private DayPointsTableMetadata() {}
        public static final String TABLE_NAME = "daypoints";
        //uri and MIME type definitions
        public static final Uri CONTENT_URI =
                Uri.parse("content://" + AUTHORITY + "/daypoints");
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.ppcalc.daypoint";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.ppcalc.daypoint";
        public static final String DEFAULT_SORT_ORDER = "modified DESC";

        public static final String DATE = "date";
        public static final String POINTS = "points";
        public static final String COMMENT="comment";

        public static final String CREATED_DATE = "created";

        public static final String MODIFIED_DATE = "modified"; }
}
