package com.itelesoft.test.app.repositories;

import com.itelesoft.test.app.database.model.TB_SearchHistory;

import java.util.List;

public class MainActivityRepository extends MainRepository {

    // Constants
    private static final String TAG = MainActivityRepository.class.getSimpleName();

    // Objects
    private static MainActivityRepository mInstance;

    public void insertQueryTextToLocalDb(TB_SearchHistory queryText) {

        List<TB_SearchHistory> queryTextList = getRoomDaoSession().searchHistoryDao().getAllQueryTexts();

        if(!checkQueryTextExistInDb(queryTextList, queryText.getQueryText())) {
            if (queryTextList.size() < 6) {
                getRoomDaoSession().searchHistoryDao().insertQueryText(queryText);
            } else {
                getRoomDaoSession().searchHistoryDao().deleteQueryText(queryTextList.get((queryTextList.size() - 1)));
                getRoomDaoSession().searchHistoryDao().insertQueryText(queryText);
            }
        }else{
            //Already search text found in Local db -- Ignore it
        }

    }

    // Prevent to insert same queryText again
    private boolean checkQueryTextExistInDb(List<TB_SearchHistory> queryTextList, String queryText) {

        boolean isFound = false;
        // We are iterating here, Since the list size is small (Alw) else Better to use sqlite where clause query.
        for (TB_SearchHistory searchItem : queryTextList) {
            if (searchItem.getQueryText().equalsIgnoreCase(queryText)) {
                isFound = true;
            }
        }

        return isFound;
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
