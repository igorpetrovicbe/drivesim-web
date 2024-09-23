package com.be.drivesim.model;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;

@Entity
@Table(name = "generation")
public class Generation {
    @Id
    @SequenceGenerator(name = "generation_sequence_generator", sequenceName = "generation_sequence", initialValue = 100)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator="generation_sequence_generator")
    protected Integer id;
    
    private LocalDateTime dateTime;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    private String modelSize;
    
    private int framesGenerated;
    private float generationTime;

    public Generation() {}

    public Generation(LocalDateTime dateTime, User user, String modelSize, int framesGenerated, float generationTime) {
        this.dateTime = dateTime;
        this.user = user;
        this.modelSize = modelSize;
        this.framesGenerated = framesGenerated;
        this.generationTime = generationTime;
    }

    public Integer getId() {
        return id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

	public String getModelSize() {
		return modelSize;
	}

	public void setModelSize(String modelSize) {
		this.modelSize = modelSize;
	}

	public float getGenerationTime() {
		return generationTime;
	}

	public void setGenerationTime(float generationTime) {
		this.generationTime = generationTime;
	}

	public int getFramesGenerated() {
		return framesGenerated;
	}

	public void setFramesGenerated(int framesGenerated) {
		this.framesGenerated = framesGenerated;
	}
}
