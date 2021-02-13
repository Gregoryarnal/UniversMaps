package universmaps.service;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import universmaps.dto.PlanetsDTO;

public interface PlanetsService {
	PlanetsDTO instantiate();
	
	List<PlanetsDTO> getPlanets();
	
	JSONObject SearchData(String planetname) throws JSONException;

}
