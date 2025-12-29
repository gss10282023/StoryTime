package group12.storytime.model;

public class VideoGenerationResponse {
    private String id;                  // 生成任务的 ID
    private String state;               // 生成状态，如 "pending", "processing", "succeeded", "failed"
    private String failureReason;       // 失败原因（如果有）
    private String createdAt;           // 创建时间
    private Assets assets;              // 包含生成的视频等资源
    private String version;             // API 版本
    private VideoGenerationRequest request; // 请求对象

    // 内部类，用于表示生成的资源
    public static class Assets {
        private String video; // 视频的 URL

        // Getter 和 Setter
        public String getVideo() {
            return video;
        }

        public void setVideo(String video) {
            this.video = video;
        }

        @Override
        public String toString() {
            return "Assets{" +
                    "video='" + video + '\'' +
                    '}';
        }
    }

    // Getter 和 Setter 方法

    public String getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Assets getAssets() {
        return assets;
    }



    public String getVersion() {
        return version;
    }

    public VideoGenerationRequest getRequest() {
        return request;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setAssets(Assets assets) {
        this.assets = assets;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setRequest(VideoGenerationRequest request) {
        this.request = request;
    }

    /**
     * 获取生成的视频的 URL。
     * @return 视频的 URL，如果未生成成功则返回 null。
     */
    public String getResultUrl() {
        // 确保只有在状态为 "succeeded" 时返回视频 URL
        if ("succeeded".equalsIgnoreCase(state) && assets != null) {
            return assets.getVideo();
        }
        return null;
    }

    /**
     * 重写 toString() 方法，便于调试和日志输出。
     */
    @Override
    public String toString() {
        return "VideoGenerationResponse{" +
                "id='" + id + '\'' +
                ", state='" + state + '\'' +
                ", failureReason='" + failureReason + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", assets=" + assets +
                ", version='" + version + '\'' +
                ", request=" + request +
                '}';
    }
}
