package universmaps.model;

import javax.persistence.*;

@Entity
public class Planets {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;	
	
	private String name;
	private Boolean affichage;
	
	
	public Boolean getAffichage() {
		return affichage;
	}
	public void setAffichage(Boolean affichage) {
		this.affichage = affichage;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Planets(String name, Boolean affichage) {
		super();
		this.name = name;
		this.affichage = affichage;
	}
	
	Planets(){
		
	}
	
	
	
}
