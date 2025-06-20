package com.eep.model;

import java.util.List;

public class Game {
    private Long id;
    private String name;
    private String backgroundImage;
    private String slug;
    private List<String> platforms;
    private String released;

    /**
     * Constructor para categorías (sin fecha de lanzamiento).
     */
    public Game(Long id,
                String name,
                String backgroundImage,
                String slug,
                List<String> platforms) {
        this.id = id;
        this.name = name;
        this.backgroundImage = backgroundImage;
        this.slug = slug;
        this.platforms = platforms;
        this.released = null;
    }

    /**
     * Constructor para últimos lanzamientos (con fecha de lanzamiento).
     */
    public Game(Long id,
                String name,
                String backgroundImage,
                String slug,
                List<String> platforms,
                String released) {
        this(id, name, backgroundImage, slug, platforms);
        this.released = released;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBackgroundImage() {
		return backgroundImage;
	}

	public void setBackgroundImage(String backgroundImage) {
		this.backgroundImage = backgroundImage;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public List<String> getPlatforms() {
		return platforms;
	}

	public void setPlatforms(List<String> platforms) {
		this.platforms = platforms;
	}

	public String getReleased() {
		return released;
	}

	public void setReleased(String released) {
		this.released = released;
	}

    
}
