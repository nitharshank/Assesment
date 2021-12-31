package com.itelesoft.test.app.database.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

// Entity class model of room database
@Entity
public class TB_SearchHistory {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private Long id;

    private String queryText;

    public TB_SearchHistory() {
    }

    public TB_SearchHistory(String queryText) {
        this.queryText = queryText;
    }

    @NonNull
    public Long getId() {
        return id;
    }

    public void setId(@NonNull Long id) {
        this.id = id;
    }

    public String getQueryText() {
        return queryText;
    }

    public void setQueryText(String queryText) {
        this.queryText = queryText;
    }
}
