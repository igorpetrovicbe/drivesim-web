package com.be.drivesim.dto;

public class GeneratorRequestDTO {
    private Integer userId;
    private int framesToGenerate;
    private float angle;
    private float speed;
    private String modelSize;
    private boolean cropImage;
    private String imageBase64; // Add this field for the image

    public GeneratorRequestDTO() {}

    public GeneratorRequestDTO(Integer userId, int framesToGenerate, float angle, float speed, String imageBase64, String modelSize, boolean cropImage) {
        this.userId = userId;
        this.framesToGenerate = framesToGenerate;
        this.angle = angle;
        this.speed = speed;
        this.imageBase64 = imageBase64;
        this.modelSize = modelSize;
        this.cropImage = cropImage;
    }

    // Getters and Setters for all fields
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public int getFramesToGenerate() {
        return framesToGenerate;
    }

    public void setFramesToGenerate(int framesToGenerate) {
        this.framesToGenerate = framesToGenerate;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public String getImageBase64() {
        return imageBase64;
    }

    public void setImageBase64(String imageBase64) {
        this.imageBase64 = imageBase64;
    }

	public String getModelSize() {
		return modelSize;
	}

	public void setModelSize(String modelSize) {
		this.modelSize = modelSize;
	}

	public boolean isCropImage() {
		return cropImage;
	}

	public void setCropImage(boolean cropImage) {
		this.cropImage = cropImage;
	}
}
