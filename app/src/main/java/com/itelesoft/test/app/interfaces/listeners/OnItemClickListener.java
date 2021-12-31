package com.itelesoft.test.app.interfaces.listeners;

public interface OnItemClickListener<T> {

    void onItemClick(int position, T data);

}