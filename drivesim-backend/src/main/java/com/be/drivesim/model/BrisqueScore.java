package com.be.drivesim.model;

import javax.persistence.*;

@Entity
@Table(name = "brisque_score")
public class BrisqueScore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private float value;

    private int index;
    
    @ManyToOne
    @JoinColumn(name = "generation_id", nullable = false)
    private Generation generation;

    public BrisqueScore() {}

    public BrisqueScore(float value, Generation generation, int index) {
        this.value = value;
        this.generation = generation;
        this.index = index;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public Generation getGeneration() {
        return generation;
    }

    public void setGeneration(Generation generation) {
        this.generation = generation;
    }

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
}
