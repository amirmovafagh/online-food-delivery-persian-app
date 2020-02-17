package ir.boojanco.onlinefoodorder.models.stateCity;

import com.google.gson.annotations.SerializedName;

public class AllCitiesList {
    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String name;

    @SerializedName("stateId")
    private long stateId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
