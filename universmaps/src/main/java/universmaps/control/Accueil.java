package universmaps.control;

import org.apache.jena.query.*;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
    public ResponseEntity<String[][]> dataLoad(@PathVariable("name") String name, Model model) throws ParseException {
    	//@{/photos/saturne.png}
    	System.out.println(name);
        //System.out.println("on va envoyer la query");
    	String[][] data = getInfoObjet(name);
        System.out.println("Reçu");
    	
        return new ResponseEntity<>(
        	      data, 
        	      HttpStatus.OK);
    }
    
    
    @GetMapping("/position")
    public void updatePosition(Model model) throws ParseException {

    	control.updatePosition();

    }
    
    
    public String[][] getInfoObjet(String id) throws ParseException {
    	String[][] data = new String[13][2];
        String name = id;
        String uriname = name;
        if (name.equals("Mercury")) {
        	uriname += "_(planet)";
        }
        System.out.println("[name pour requete]: " + name + "   " + uriname);
        String szEndpoint = "https://dbpedia.org/sparql";
        String szQuery = "prefix dct: <http://purl.org/dc/terms/>\n"
        		+ "prefix dbc: <http://dbpedia.org/resource/Category:>\n"
                + "prefix dbp: <http://dbpedia.org/property/>\n" + "prefix dbo: <http://dbpedia.org/ontology/>\n"
                + "Select * where{\n" 
                + "OPTIONAL { ?s dbo:density ?density .}\n"
                + "OPTIONAL { ?s dbo:averageSpeed ?averageSpeed .}\n" 
                + "OPTIONAL { ?s dbp:atmosphereComposition ?atmosphereComposition .}\n" 
				+ "OPTIONAL { ?s dbo:formerName ?formerName .}\n" 
				+ "OPTIONAL { ?s dbo:orbitalPeriod ?orbitalPeriod .}\n" 
				+ "OPTIONAL { ?s dbo:periapsis ?periapsis .}\n" 
				+ "OPTIONAL { ?s dbp:satellites ?satellites .}\n"
                + "?s dbp:name ?name .\n" 
				+ "OPTIONAL { ?s dbp:surfaceGrav ?surfaceGrav .}\n" 
				+ "OPTIONAL { ?s dbo:escapeVelocity ?escapeVelocity .}\n" 
				+ "OPTIONAL { ?s dbo:apoapsis ?apoapsis .}\n"
                + "OPTIONAL { ?s dbo:abstract ?abstract .}\n" 
                + "OPTIONAL { ?s dbo:volume ?volume .}\n"
                + "filter langMatches(lang(?abstract), 'fr') .\n"
                + "filter not exists{ filter contains(str(?abstract), 'Erde') . }\n"
                + "filter (str(?name) = '"+ name +"') .\n"
                + "filter STRENDS(str(?s),'"+ uriname +"')}";

        //                + "?s dbp:surfacePressure ?surfacePressure .\n" 

        Query query = QueryFactory.create(szQuery);

        // Create the Execution Factory using the given Endpoint
        QueryExecution qexec = QueryExecutionFactory.sparqlService(szEndpoint, query);

        // Set Timeout
        ((QueryEngineHTTP) qexec).addParam("timeout", "10000");

        // Execute Query
        int iCount = 0;
        ResultSet rs = qexec.execSelect();
        //System.out.println("get result ");
        while (rs.hasNext()) {
            // Get Result
            QuerySolution qs = rs.next ();

            // Get Variable Names
            Iterator<String> itVars = qs.varNames();

            // Count
            iCount++;
            //System.out.println("Result " + iCount + ": ");

            // Display Result
            int i = 0;
            while (itVars.hasNext()) {
                String szVar = itVars.next().toString();
                String szVal = qs.get(szVar).toString();

            	data[i][0] = szVar;
                data[i][1] = szVal;

                i++;
            }
        }
		return data;
    }
    
    
}