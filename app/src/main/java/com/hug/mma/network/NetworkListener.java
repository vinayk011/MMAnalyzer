package com.hug.mma.network;

import java.util.List;
import java.util.Map;

/**
 * Created by Raviteja on 19-07-2017 for HugFit
 * Copyright (c) 2017 Hug Innovations. All rights reserved.
 */

public interface NetworkListener<T> {

    void success(T t);

    void headers(Map<String, String> header);

    void fail(int code, List<NetworkError> networkErrors);

    void failure();
}
