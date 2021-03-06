package com.rae.cnblogs.blog.adapter;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rae.cnblogs.blog.R;
import com.rae.cnblogs.blog.holder.CategoryHolder;
import com.rae.cnblogs.sdk.bean.CategoryBean;
import com.rae.cnblogs.widget.drag.DragRecylerViewAdapter;
import com.rae.cnblogs.widget.drag.OnStartDragListener;

/**
 * Created by rae on 2018/6/29.
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
public class CategoryDragAdapter extends DragRecylerViewAdapter<CategoryBean, CategoryHolder> implements View.OnLongClickListener, View.OnClickListener {

    // 是否为编辑模式
    private boolean mIsEditMode;

    private ICategoryItemListener mCategoryItemListener;


    public CategoryDragAdapter(OnStartDragListener dragStartListener, @NonNull ICategoryItemListener listener) {
        super(dragStartListener);
        mCategoryItemListener = listener;
    }

    @Override
    public void onClick(View v) {
        CategoryHolder holder = (CategoryHolder) v.getTag();
        int position = holder.getAdapterPosition();
        CategoryBean item = getDataItem(position);
        if (item == null) return;
        mCategoryItemListener.onItemClick(position, item);
    }

    @Override
    public boolean onLongClick(View v) {
        CategoryHolder holder = (CategoryHolder) v.getTag();
        int position = holder.getAdapterPosition();
        CategoryBean item = getDataItem(position);
        if (item == null) return false;
        mCategoryItemListener.onItemLongClick(position, item);
        return false;
    }


    /**
     * 设置是否为编辑模式
     */
    public void setIsEditMode(boolean isEditMode) {
        mIsEditMode = isEditMode;
    }

    /**
     * 是否为锁定的项目
     * 锁定的分类，不能编辑，不能拖动
     *
     * @param position 索引
     */
    private boolean isLockItem(int position) {
        return position < 2;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CategoryHolder holder = new CategoryHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false));
        holder.itemView.setOnLongClickListener(this);
        holder.itemView.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.itemView.setTag(holder);
        final CategoryBean item = getDataItem(position);
        String itemName = item.getName();
        holder.itemView.setActivated(!TextUtils.equals("推荐", itemName) & !TextUtils.equals("首页", itemName));
        holder.setTitle(item.getName());
        holder.setIsEditMode(mIsEditMode && !isLockItem(position));
        holder.setOnRemoveClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCategoryItemListener.onItemRemoveClick(holder.getAdapterPosition(), item);
            }
        });
    }
}
