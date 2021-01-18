package com.hug.mma.viewmodel.request;

/**
 * Created by Raviteja on 02-08-2017 for HugFit.
 * Copyright (c) 2017 Hug Innovations. All rights reserved.
 */

public class SignInRequest {
    private String userName;
    private String password;

    public SignInRequest() {
    }

    public SignInRequest(String mobile, String password) {
        this.userName = mobile;
        this.password = password;
    }

    public String getMobile() {
        return userName;
    }

    public void setMobile(String mobile) {
        this.userName = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
