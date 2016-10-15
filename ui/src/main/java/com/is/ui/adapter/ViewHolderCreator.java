package com.is.ui.adapter;

public interface ViewHolderCreator<ItemDataType> {
    public ViewHolderBase<ItemDataType> createViewHolder(int position);
}