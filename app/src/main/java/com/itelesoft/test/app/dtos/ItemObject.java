package com.itelesoft.test.app.dtos;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemObject implements Parcelable {

    private int id, itemPkId;
    private long itemObjectPkIdNew;
    private String name;

    public ItemObject() {
    }

    public ItemObject(int id, int itemPkId, long itemObjectPkIdNew, String name) {
        this.id = id;
        this.itemPkId = itemPkId;
        this.itemObjectPkIdNew = itemObjectPkIdNew;
        this.name = name;
    }

    protected ItemObject(Parcel in) {
        id = in.readInt();
        itemPkId = in.readInt();
        itemObjectPkIdNew = in.readLong();
        name = in.readString();
    }

    public static final Creator<ItemObject> CREATOR = new Creator<ItemObject>() {
        @Override
        public ItemObject createFromParcel(Parcel in) {
            return new ItemObject(in);
        }

        @Override
        public ItemObject[] newArray(int size) {
            return new ItemObject[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItemPkId() {
        return itemPkId;
    }

    public void setItemPkId(int itemPkId) {
        this.itemPkId = itemPkId;
    }

    public long getItemObjectPkIdNew() {
        return itemObjectPkIdNew;
    }

    public void setItemObjectPkIdNew(long itemObjectPkIdNew) {
        this.itemObjectPkIdNew = itemObjectPkIdNew;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(itemPkId);
        dest.writeLong(itemObjectPkIdNew);
        dest.writeString(name);
    }
}
