package com.character.microblogapp.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ArrayAdapter<T> extends Adapter<ViewHolder> {

    public Context mContext;
    public List<T> mItems = new ArrayList<>();

    public ArrayAdapter(Context context, ArrayList<T> items) {
        super();

        mContext = context;
        mItems = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void add(T o) {
        mItems.add(o);
    }

    public void insert(int index, T o) {
        mItems.add(index, o);
    }

    public List getItems() {
        return mItems;
    }

    public void addAll(Collection<T> c) {
        mItems.addAll(c);
    }

    public boolean remove(T o) {
        return mItems.remove(o);
    }

    public void removeLastObject() {
        if (getItemCount() == 0)
            return;

        mItems.remove(getItemCount() - 1);
    }

    public void remove(int o) {
        mItems.remove(o);
    }

    public boolean removeAll(Collection<T> c) {
        return mItems.removeAll(c);
    }

    public void clear() {
        mItems.clear();
    }

    boolean contains(T o) {
        return mItems.contains(o);
    }

    public @Nullable
    T getItem(int position) {
        return mItems.get(position);
    }

    public T set(int index, T element) {
        return mItems.set(index, element);
    }
}
