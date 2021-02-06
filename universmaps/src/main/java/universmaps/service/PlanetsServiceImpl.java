package universmaps.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import universmaps.control.ConfigReader;
import universmaps.dto.PlanetsDTO;
import universmaps.model.Planets;
import universmaps.repository.PlanetsRepository;

@Service
@Transactional
public class PlanetsServiceImpl implements PlanetsService{

	@Autowired
	PlanetsRepository planetsrepository;
	
	@Override
	public PlanetsDTO instantiate() {
		// TODO Auto-generated method stub
    	ConfigReader config = new ConfigReader();
    	Properties prop;
    	try {
			 prop = config.read();
			 String[] names = prop.getProperty("planet").split(",");
			 
			 for(int i = 0; i < names.length ; i++) {
				 System.out.println("Instantiate : " + names[i]);
				 planetsrepository.save(new Planets(names[i]));
			 }
			 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
		return null;
	}

	@Override
	public List<PlanetsDTO> getPlanets() {
		// TODO Auto-generated method stub
		return mapTicketToDTO(planetsrepository.findAll()) ;
	}

	 private static List<PlanetsDTO> mapTicketToDTO(Collection<Planets> planets) {
	        return planets.stream().map(a -> buildDTO(a)).collect(Collectors.toList());
	    }
	 
	 private static PlanetsDTO buildDTO(Planets a) {
	        return new PlanetsDTO(a.getName());
	    }
}
