package universmaps.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.json.JSONException;
import org.json.JSONObject;
import javax.transaction.Transactional;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
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

	
	public JSONObject SearchData(String planetname) throws JSONException {

        JSONObject json = new JSONObject();
        
        this.searchDatadbpedia(planetname, json);
        this.SearchDatawiki(planetname, json);

		return json;
    }
	
	public void doRequest(String szEndpoint, String szQuery,JSONObject json ) throws JSONException {
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
            System.out.println("Result " + iCount + ": ");

            // Display Result
            while (itVars.hasNext()) {
                String szVar = itVars.next().toString();
                String szVal = qs.get(szVar).toString();
                System.out.println(szVar + "  : " + szVal);
                if (!szVar.equals("s")) {
                	String val = szVal.split("http")[0];
                	String[] data;
                	if (json.has(szVar)) {
                		try {
                			data = (String[]) json.get(szVar);
                		}catch(Exception e){
                			data = new String[1];
                			data[0]= (String) json.get(szVar);
                		}
                			
                		if (!Arrays.asList(data).contains(val)) {
                			String[] result = new String[data.length + 1];
	                   		 for (int i = 0; i <= data.length -1 ;i++) {
	                   			 result[i] = data[i];
	                   		 }
	                   		 result[result.length-1] = val;
	                   		 json.put(szVar, result);
                		}
                		
                	}else {
                		json.put(szVar, val);
                		
                	}
                   
                }
            }
            
        }
        qexec.close();
	}
	
	public void SearchDatawiki(String planetname, JSONObject json) throws JSONException {
		// TODO Auto-generated method stub
		
        String name = planetname;

        String szEndpoint = "http://query.wikidata.org/sparql";
        String szQuery = "PREFIX bd: <http://www.bigdata.com/rdf#>\n"
        		+ "PREFIX wikibase: <http://wikiba.se/ontology#>\n"
                + "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n" + "PREFIX wd: <http://www.wikidata.org/entity/>\n"
                + "SELECT  distinct ?childLabel ?date ?dist ?perimeter ?area ?diameter ?period ?mass ?discovererlabel where {\n" 
                + "?planet ?planetLabel '"+ name +"' . \n"
                + "?planet wdt:P361 ?Q7879772 . \n"
                + "OPTIONAL { ?planet wdt:P398 ?child . }\n" 
                + "OPTIONAL { ?planet wdt:P2583 ?dist . }\n" 
				+ "OPTIONAL { ?planet wdt:P2046 ?area . }\n" 
				+ "OPTIONAL { ?planet wdt:P2547 ?perimeter . }\n" 
				+ "OPTIONAL { ?planet wdt:P2286 ?diameter . }\n" 
				+ "OPTIONAL { ?planet wdt:P2146 ?period . }\n"
				+ "OPTIONAL { ?planet wdt:P575 ?date . }\n"
				+ "OPTIONAL { ?planet wdt:P61 ?discoverer . }\n"
                + "OPTIONAL { ?planet wdt:P2067 ?mass . }\n" 
				+ "SERVICE wikibase:label {\n" 
				+ "bd:serviceParam wikibase:language \"[AUTO_LANGUAGE],fr,ar,be,bg,bn,ca,cs,da,de,el,en,es,et,fa,fi,he,hi,hu,hy,id,it,ja,jv,ko,nb,nl,eo,pa,pl,pt,ro,ru,sh,sk,sr,sv,sw,te,th,tr,uk,yue,vec,vi,zh\"\n" 
				+ " }\n"
                + "}"; 
        this.doRequest(szEndpoint,szQuery, json);
    }
	
	public void searchDatadbpedia(String planetname, JSONObject json) throws JSONException {
		// TODO Auto-generated method stub
		
		 String name = planetname;
	        String uriname = name;
	        if (name.equals("Mercury")) {
	        	uriname += "_(planet)";
	        }
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
	                + "OPTIONAL { ?s dbo:abstract ?abstractd .}\n" 
	                + "OPTIONAL { ?s dbo:volume ?volume .}\n"
	                + "filter langMatches(lang(?abstractd), 'fr') .\n"
	                + "filter not exists{ filter contains(str(?abstractd), 'Erde') . }\n"
	                + "filter (str(?name) = '"+ name +"') .\n"
	                + "filter STRENDS(str(?s),'"+ uriname +"')}";

        this.doRequest(szEndpoint,szQuery, json);
    }
	
	public int sizeOf(String[][] data) {
		int size = 0;
		for (int i = 0; i<data.length-1; i++) {
			if ( ! data[i][0].isEmpty()) {
				size++;
			}
		}
		return size;
	}


}
