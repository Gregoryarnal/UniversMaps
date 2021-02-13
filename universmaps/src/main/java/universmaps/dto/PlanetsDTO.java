package universmaps.dto;

public class PlanetsDTO {
	private Integer id;	
	private String name;
	private String[] dataType;
	private String[][] data;
	
	public String[] getDataType() {
		return dataType;
	}
	public void setDataType(String[] dataType) {
		this.dataType = dataType;
	}
	public String[][] getData() {
		return data;
	}
	public void setData(String[][] data) {
		this.data = data;
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
	
	public PlanetsDTO(String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
}
  