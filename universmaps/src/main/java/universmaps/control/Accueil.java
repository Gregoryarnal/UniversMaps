package universmaps.control;

import org.apache.jena.query.*;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import universmaps.service.DataService;
import universmaps.service.PlanetsService;

import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;


@Controller
public class Accueil {
	
	@Autowired
	PlanetsService planetsservice;
	
	
	PositionControl control;

    @GetMapping("/")
    public String accueil(Model model) throws ParseException, IOException {
    	if (planetsservice.getPlanets().size() == 0) {
    		planetsservice.instantiate();
    		control = new PositionControl(planetsservice.getPlanets());
    	}
    	model.addAttribute("planets", planetsservice.getPlanets() );
        return "index";
    }
    
    @GetMapping("/popup/{name}")
    public ResponseEntity<String> dataLoad(@PathVariable("name") String name, Model model) throws ParseException, JSONException {
    	//@{/photos/saturne.png}
    	System.out.println(name);
        //System.out.println("on va envoyer la query");
    	JSONObject data = planetsservice.SearchData(name);
    	System.out.println("search data done");
    	//String[] dataType = planetsservice.getDataType(name);
    	//System.out.println("dataType done : " + dataType[1]);
    	//String nam = planetsservice.getData(name, dataType[1]);
    	
    	
        return new ResponseEntity<>(
        	      data.toString(), 
        	      HttpStatus.OK);
    }
    
    
    @GetMapping("/position")
    public void updatePosition(Model model) throws ParseException {

    	control.updatePosition();

    }
    
    
   
}