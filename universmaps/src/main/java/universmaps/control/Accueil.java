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
    public String accueil(Model model) throws ParseException, IOException, JSONException {
    	if (planetsservice.getPlanets().size() == 0) {
    		planetsservice.instantiate();
    		planetsservice.instantiateplanetliste();
    	}
    	model.addAttribute("planets", planetsservice.getPlanets() );
    	model.addAttribute("planetsList", planetsservice.getListPlanets() );
    	model.addAttribute("allplanets", planetsservice.getAllPlanets() );
        return "index";
    }
    
    @GetMapping("/popup/{name}")
    public ResponseEntity<String> dataLoad(@PathVariable("name") String name, Model model) throws ParseException, JSONException {
    	System.out.println(name);

    	JSONObject data = planetsservice.SearchData(name);
    	System.out.println("search data done");

    	System.out.println(data.toString());
        return new ResponseEntity<>(
        	      data.toString(), 
        	      HttpStatus.OK);
    }
    
    
   
}