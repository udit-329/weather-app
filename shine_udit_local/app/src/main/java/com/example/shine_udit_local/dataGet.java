package com.example.shine_udit_local;

import com.google.gson.annotations.SerializedName;

public class dataGet {
    @SerializedName("main")
    Main main;

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }
}
