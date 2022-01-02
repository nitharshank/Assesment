package com.itelesoft.test.app.repositories;


import com.itelesoft.test.app.common.AssessmentApplication;
import com.itelesoft.test.app.database.ProjectDatabase;

public class MainRepository {

    public static ProjectDatabase getRoomDaoSession() {
        return AssessmentApplication.getRoomSession();
    }

}
