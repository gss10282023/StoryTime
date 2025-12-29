// FairytaleRequest.java
package group12.storytime.model;

public class FairytaleRequest {
    private String ageGroup;
    private String gender;
    private String name;
    private String characteristic;
    private boolean generateVideo; // 新增字段，表示是否生成视频

    // 构造函数、Getter 和 Setter 方法

    public FairytaleRequest() {
    }

    public FairytaleRequest(String ageGroup, String gender, String name, String characteristic, boolean generateVideo) {
        this.ageGroup = ageGroup;
        this.gender = gender;
        this.name = name;
        this.characteristic = characteristic;
        this.generateVideo = generateVideo;
    }

    // Getter 和 Setter 方法

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(String ageGroup) {
        this.ageGroup = ageGroup;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCharacteristic() {
        return characteristic;
    }

    public void setCharacteristic(String characteristic) {
        this.characteristic = characteristic;
    }

    public boolean isGenerateVideo() {
        return true;
    }

    public void setGenerateVideo(boolean generateVideo) {
        this.generateVideo = generateVideo;
    }
}