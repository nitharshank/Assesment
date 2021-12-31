package com.itelesoft.test.app.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.itelesoft.test.app.database.dao.TB_SearchHistoryDao;
import com.itelesoft.test.app.database.model.TB_SearchHistory;


// Room database class
@Database(entities = {TB_SearchHistory.class}, version = 1, exportSchema = false)
public abstract class ProjectDatabase extends RoomDatabase {

    //define static instance
    private static ProjectDatabase mInstance;

    //method to get room database
    public static ProjectDatabase getDatabase(Context context) {

        if (mInstance == null)
            mInstance = Room.databaseBuilder(context,
                    ProjectDatabase.class, "project_db")
                    .build();

        return mInstance;
    }

    //method to remove instance
    public static void closeDatabase() {
        mInstance = null;
    }

    //define SearchHistory dao ( data access object )
    public abstract TB_SearchHistoryDao searchHistoryDao();


}
