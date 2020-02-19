package ir.boojanco.onlinefoodorder.models.map;

import com.google.gson.annotations.SerializedName;

public class Result {
    @SerializedName("code")
    private long code;
    @SerializedName("id")
    private long id;
    @SerializedName("title")
    private String title;
    @SerializedName("type")
    private long type;

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getType() {
        return type;
    }

    public void setType(long type) {
        this.type = type;
    }
}
