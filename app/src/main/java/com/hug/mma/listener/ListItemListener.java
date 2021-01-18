package com.hug.mma.listener;

import com.hug.mma.db.room.entity.Device;

/**
 * Created by Raviteja on 31-08-2017 for HugFit.
 * Copyright (c) 2017 Hug Innovations. All rights reserved.
 */

public interface ListItemListener {
    void onItemClick(int position);
    void onChanged(boolean isChanged);
}
