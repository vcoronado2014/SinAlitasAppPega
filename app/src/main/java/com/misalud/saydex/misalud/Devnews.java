package com.misalud.saydex.misalud;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vcoronado on 24-10-2016.
 */
public class Devnews {
    @SerializedName("Id")
    private int Id;

    @SerializedName("RolId")

    private int RolId;

    public int getId() {
        return Id;
    }

    public void SetId(int id) {
        this.Id = id;
    }

    public int getRolId() {
        return RolId;
    }

    public void SetRolId(int rolId) {
        this.RolId = rolId;
    }
}
