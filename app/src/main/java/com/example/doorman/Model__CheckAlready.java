package com.example.doorman;

import androidx.annotation.NonNull;

public class Model__CheckAlready {
    private boolean isRight = false;
    private String phone;
    private String message;

    public Model__CheckAlready(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isRight() {
        return isRight;
    }

    public void setRight(boolean right) {
        isRight = right;
    }

    @NonNull
    @Override
    public String toString() {
        return "RepoCheckAlready{" +
                "phone='" + phone + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}