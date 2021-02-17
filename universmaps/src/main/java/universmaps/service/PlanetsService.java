package universmaps.service;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import universmaps.dto.PlanetsDTO;

public interface PlanetsService {
	PlanetsDTO instantiate() throws JSONException;
	
	List<PlanetsDTO> getPlanets();
	List<PlanetsDTO> getListPlanets();
	
	JSONObject SearchData(String planetname) throws JSONException;
	
	JSONObject instantiateplanetliste() throws JSONException;
}
