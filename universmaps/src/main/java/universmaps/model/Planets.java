package universmaps.model;

import javax.persistence.*;

@Entity
public class Planets {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;	
	
	private String name;
	
	private String[] dataType;
	private String density;
	private String averageSpeed;
	private String atmosphereComposition;
	private String formerName;
	private String orbitalPeriod;
	private String periapsis;
	private String satellites;
	private String surfaceGrav;
	private String escapeVelocity;
	private String apoapsis;
	private String abstractd;
	private String volume;
	public String getDensity() {
		return density;
	}

	public void setDensity(String density) {
		this.density = density;
	}

	public String getAverageSpeed() {
		return averageSpeed;
	}

	public void setAverageSpeed(String averageSpeed) {
		this.averageSpeed = averageSpeed;
	}

	public String getAtmosphereComposition() {
		return atmosphereComposition;
	}

	public void setAtmosphereComposition(String atmosphereComposition) {
		this.atmosphereComposition = atmosphereComposition;
	}

	public String getFormerName() {
		return formerName;
	}

	public void setFormerName(String formerName) {
		this.formerName = formerName;
	}

	public String getOrbitalPeriod() {
		return orbitalPeriod;
	}

	public void setOrbitalPeriod(String orbitalPeriod) {
		this.orbitalPeriod = orbitalPeriod;
	}

	public String getPeriapsis() {
		return periapsis;
	}

	public void setPeriapsis(String periapsis) {
		this.periapsis = periapsis;
	}

	public String getSatellites() {
		return satellites;
	}

	public void setSatellites(String satellites) {
		this.satellites = satellites;
	}

	public String getSurfaceGrav() {
		return surfaceGrav;
	}

	public void setSurfaceGrav(String surfaceGrav) {
		this.surfaceGrav = surfaceGrav;
	}

	public String getEscapeVelocity() {
		return escapeVelocity;
	}

	public void setEscapeVelocity(String escapeVelocity) {
		this.escapeVelocity = escapeVelocity;
	}

	public String getApoapsis() {
		return apoapsis;
	}

	public void setApoapsis(String apoapsis) {
		this.apoapsis = apoapsis;
	}

	public String getAbstractd() {
		return abstractd;
	}

	public void setAbstractd(String abstractd) {
		this.abstractd = abstractd;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}
	

	public String[] getDataType() {
		return dataType;
	}
	public void setDataType(String[] dataType) {
		this.dataType = dataType;
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
	public Planets(String name) {
		super();
		this.name = name;
	}
	
	Planets(){
		
	}
	
	
	
}
