package tech.kolektiv.hiddenstory.client.version.domain;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GameVersions {

    @SerializedName("engineVersion")
    @Expose
    private String engineVersion;
    @SerializedName("info")
    @Expose
    private List<Info> info = new ArrayList<>();

    /**
     * @return The engineVersion
     */
    public String getEngineVersion() {
        return engineVersion;
    }

    /**
     * @param engineVersion The engineVersion
     */
    public void setEngineVersion(String engineVersion) {
        this.engineVersion = engineVersion;
    }

    /**
     * @return The info
     */
    public List<Info> getInfo() {
        return info;
    }

    /**
     * @param info The info
     */
    public void setInfo(List<Info> info) {
        this.info = info;
    }

    public Info findStory(String gameName){
        for (Info info : getInfo()) {
            if (info != null && gameName.equals(info.getName())) {
                return info;
            }
        }
        throw new IllegalStateException();
    }

}