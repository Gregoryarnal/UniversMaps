package universmaps.service;

import java.util.List;

import universmaps.dto.PlanetsDTO;

public interface PlanetsService {
	PlanetsDTO instantiate();
	
	List<PlanetsDTO> getPlanets();
}
