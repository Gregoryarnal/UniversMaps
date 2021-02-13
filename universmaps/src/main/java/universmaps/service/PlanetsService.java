package universmaps.service;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import universmaps.dto.PlanetsDTO;

public interface PlanetsService {
	PlanetsDTO instantiate();
	
	List<PlanetsDTO> getPlanets();
	
	void setData( String name, String[][] data);
	
	String getData( String name, String dataType);
	String[] getDataType(String name);
	
	JSONObject SearchData(String planetname) throws JSONException;

	void setDataType(String name, String[] type);

}
