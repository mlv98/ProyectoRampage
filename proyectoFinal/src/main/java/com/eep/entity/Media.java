// com/eep/entity/Media.java
package com.eep.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "media")
public class Media {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  public enum Type { IMAGE, VIDEO }
  @Enumerated(EnumType.STRING)
  private Type type;

  private String path;       

  @ManyToOne(optional = false)
  @JoinColumn(name = "post_id")
  private Post post;
  
  
  


public Long getId() {
	return id;
}

public void setId(Long id) {
	this.id = id;
}

public Type getType() {
	return type;
}

public void setType(Type type) {
	this.type = type;
}

public String getPath() {
	return path;
}

public void setPath(String path) {
	this.path = path;
}

public Post getPost() {
	return post;
}

public void setPost(Post post) {
	this.post = post;
}


  
  
}
