package com.itelesoft.test.app.dtos.response;

import java.io.Serializable;

public class Source implements Serializable {

    private static final long serialVersionUID = 13234L;

    private String id;
    private String name;

    public Source(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
