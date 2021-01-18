package com.hug.mma.network;

/**
 * Created by Raviteja on 19-07-2017 for HugFit
 * Copyright (c) 2017 Hug Innovations. All rights reserved.
 */

public class NetworkError {

    private String description;
    private String recommendation;

    public NetworkError(String description, String recommendation) {
        this.description = description;
        this.recommendation = recommendation;
    }

    public String getDescription() {
        return description;
    }

    public String getRecommendation() {
        return recommendation;
    }
}
