package group12.storytime.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenerationRequest {

    private String prompt;
    private String mode;
    private String model;
    private String outputFormat;
    private String negativePrompt;
    private String aspectRatio;
    private Integer seed;
    private Double strength;
    private byte[] image;
}
