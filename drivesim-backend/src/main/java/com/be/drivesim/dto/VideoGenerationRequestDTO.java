package com.be.drivesim.dto;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class VideoGenerationRequestDTO {
	
	@NotNull(message = "Image cannot be null")
	private String image;

    @Min(value = 1, message = "Frames to generate must be at least 1")
    @Max(value = 100, message = "Frames to generate must not be larger than 100")
    private int framesToGenerate;

    @NotNull(message = "Angle cannot be null")
    @DecimalMin (value = "-482.4", message = "Angle cannot be smaller than -482.4")
    @DecimalMax (value = "394.3", message = "Angle cannot be smaller than 394.3")
    private Float angle;

    @NotNull(message = "Speed cannot be null")
    @DecimalMin (value = "-0.6471078", message = "Speed cannot be smaller than -0.6471078")
    @DecimalMax (value = "46.590874", message = "Speed cannot be smaller than 46.590874")
    private Float speed;

    @NotNull(message = "Crop Image flag cannot be null")
    private Boolean cropImage;

    @NotNull(message = "Model size cannot be null")
    private String modelSize;

    public VideoGenerationRequestDTO() {}

    public VideoGenerationRequestDTO(int framesToGenerate, float angle, float speed, boolean cropImage, String modelSize) {
        this.framesToGenerate = framesToGenerate;
        this.angle = angle;
        this.speed = speed;
        this.cropImage = cropImage;
        this.modelSize = modelSize;
    }

    public int getFramesToGenerate() {
        return framesToGenerate;
    }

    public float getAngle() {
        return angle;
    }

    public float getSpeed() {
        return speed;
    }

    public boolean getCropImage() {
        return cropImage;
    }

    public String getModelSize() {
        return modelSize;
    }

    public void setFramesToGenerate(int framesToGenerate) {
        this.framesToGenerate = framesToGenerate;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setCropImage(boolean cropImage) {
        this.cropImage = cropImage;
    }

    public void setModelSize(String modelSize) {
        this.modelSize = modelSize;
    }

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
}
