package com.itelesoft.test.app.repositories;

import com.itelesoft.test.app.database.model.TB_SearchHistory;

import java.util.ArrayList;
import java.util.List;

public class MainActivityRepository extends MainRepository {

    // Constants
    private static final String TAG = MainActivityRepository.class.getSimpleName();

    // Objects
    private static MainActivityRepository mInstance;

    public void insertQueryTextToLocalDb(TB_SearchHistory queryText) {
        getRoomDaoSession().searchHistoryDao().insertQueryText(queryText);
    }

    public List<TB_SearchHistory> getAllQueryTextFromLocalDb() {
        return getRoomDaoSession().searchHistoryDao().getAllQueryTexts();
    }

    public static MainActivityRepository getInstance() {
        if (mInstance == null) {
            mInstance = new MainActivityRepository();
        }
        return mInstance;
    }

    //Async task to add note
    /*public class AddNote extends AsyncTask<Note, Void, Void> {
        @Override
        protected Void doInBackground(Note... notes) {
            getRoomDaoSession().noteDao().insertNote(notes[0]);
            return null;
        }
    }*/

}
