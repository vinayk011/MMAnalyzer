package com.hug.mma.binding;

import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Raviteja on 20-07-2017 for HugFit
 * Copyright (c) 2017 Hug Innovations. All rights reserved.
 */


public class ViewHolderBinding extends RecyclerView.ViewHolder{
    public final ViewDataBinding binding;

    public ViewHolderBinding(ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}