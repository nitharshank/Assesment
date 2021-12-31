package com.itelesoft.test.app.database.dao;

import static androidx.room.OnConflictStrategy.IGNORE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.itelesoft.test.app.database.model.TB_SearchHistory;

import java.util.List;

@Dao
public interface TB_SearchHistoryDao {

    // Dao method to get all queryTexts
    @Query("SELECT * FROM TB_SearchHistory ORDER BY id DESC")
    List<TB_SearchHistory> getAllQueryTexts();

    @Query("SELECT * FROM TB_SearchHistory where queryText = :text")
    List<TB_SearchHistory> loadFromUser(String text);


    // Dao method to insert queryTexts
    @Insert(onConflict = IGNORE)
    void insertQueryText(TB_SearchHistory queryText);

    // Dao method to delete queryText
    @Delete
    void deleteQueryText(TB_SearchHistory queryText);

}
