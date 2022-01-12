package application.example.photodiary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DiaryDbHelper extends SQLiteOpenHelper {
    public static int versiondb=1;
    public static String dbname="PhotoDiaryData.db";

    public DiaryDbHelper(@Nullable Context context) {
        super(context,dbname,null,versiondb);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE photo_diary_table(id INTEGER PRIMARY KEY AUTOINCREMENT,note TEXT,date TEXT,pic BLOB);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS photo_diary_table;");
        onCreate(sqLiteDatabase);
    }
}
