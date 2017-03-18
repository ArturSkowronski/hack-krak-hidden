package tech.kolektiv.hiddenstory.client.version.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Info {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("storyVersion")
    @Expose
    private String storyVersion;

    /**
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return The storyVersion
     */
    public String getStoryVersion() {
        return storyVersion;
    }

    /**
     * @param storyVersion The storyVersion
     */
    public void setStoryVersion(String storyVersion) {
        this.storyVersion = storyVersion;
    }

}