package universmaps.service;
import java.util.Iterator; 
import java.util.Map; 
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.json.JSONArray;
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
	public PlanetsDTO instantiate() throws JSONException {
		// TODO Auto-generated method stub
    	ConfigReader config = new ConfigReader();
    	Properties prop;
    	try {
			 prop = config.read();
			 String[] names = prop.getProperty("planet").split(",");
			 JSONObject json = this.instantiateplanetliste();
			 for(int i = 0; i < names.length ; i++) {
				 planetsrepository.save(new Planets(names[i], true));
			 }
       
			String[] planetslist = (String[]) json.get("itemLabel"); 
			
			for (int i = 0 ; i < planetslist.length-1 ; i++) {
				planetsrepository.save(new Planets(planetslist[i].substring(0, planetslist[i].length() -1 ), false));
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
		return mapTicketToDTO(planetsrepository.findByAffichage(true)) ;
	}

	 private static List<PlanetsDTO> mapTicketToDTO(Collection<Planets> planets) {
        return planets.stream().map(a -> buildDTO(a)).collect(Collectors.toList());
    }
	 
	 private static PlanetsDTO buildDTO(Planets a) {
        return new PlanetsDTO(a.getName());
    }

	public JSONObject instantiateplanetliste() throws JSONException {
		JSONObject json = new JSONObject();
		this.SearchListDatawiki(json);
		return json;
	}
	
	public JSONObject SearchData(String planetname) throws JSONException {

        JSONObject json = new JSONObject();
        
        this.searchDatadbpedia(planetname, json);
        this.SearchDatawiki(planetname, json);
        this.cleanData(json);
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
            //System.out.println("Result " + iCount + ": ");

            // Display Result
            while (itVars.hasNext()) {
                String szVar = itVars.next().toString();
                String szVal = qs.get(szVar).toString();

                if (!szVar.equals("s")) {
                	String val = szVal.split("http")[0];
                	val = val.substring(0,val.length()-2);
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
                + "SELECT  distinct ?childLabel ?date ?dist ?perimeter ?area ?diameter ?period ?mass ?discovererLabel where {\n" 
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
	
	
	public void SearchListDatawiki(JSONObject json) throws JSONException {
		
		String szEndpoint = "http://query.wikidata.org/sparql";
        String szQuery = "PREFIX bd: <http://www.bigdata.com/rdf#>\n"
        		+ "PREFIX wikibase: <http://wikiba.se/ontology#>\n"
                + "PREFIX wdt: <http://www.wikidata.org/prop/direct/>\n" + "PREFIX wd: <http://www.wikidata.org/entity/>\n"
                + "SELECT  ?itemLabel \n" 
                + "WHERE {  \n"
                + "?item wdt:P31 ?sub0 . ?sub0 (wdt:P279)* wd:Q634  \n"
                + "SERVICE wikibase:label { bd:serviceParam wikibase:language \"[AUTO_LANGUAGE],en\". }\n"
                + "} limit 100" ;
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
					+ "OPTIONAL { ?s dbp:satellites ?satellites .}\n"
	                + "?s dbp:name ?name .\n" 
					+ "OPTIONAL { ?s dbp:surfaceGrav ?surfaceGrav .}\n" 
					+ "OPTIONAL { ?s dbo:escapeVelocity ?escapeVelocity .}\n" 
	                + "OPTIONAL { ?s dbo:abstract ?abstractd .}\n" 
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


	public void cleanData(JSONObject json) throws JSONException {
		Iterator keys = json.keys();
		while(keys.hasNext()) {
			String key = (String) keys.next();
			String[] data;
			try {
    			data = (String[]) json.get(key);
    		}catch(Exception e){
    			data = new String[1];
    			data[0]= (String) json.get(key);
    		}
			if (key.equals("dist") && data.length >= 2) {
				int a;
				int b;
				if(Integer.parseInt(data[0])>Integer.parseInt(data[1])) {
					a = 0;
					b = 1;
				}else {
					a = 1;
					b = 0;
				}
				data[a]= "Distance de la terre à l'apogée : " + data[a] +" km.\n";
				data[b]= "Distance de la terre au périgée : " + data[b] +" km.";
				
			}else if (key.equals("mass")) {
				data[0] = "La masse est de : " + data[0] + " yottagram.";
			}else if (key.equals("satellites")) {
				data[0] = "Possède " + data[0] + " satellites.";
			}else if (key.equals("volume")) {
				data[0] = "Fait " + data[0] + " m^3";
			}else if (key.equals("averageSpeed")) {
				data[0] = "Vitesse moyenne " + data[0] + " km/h.";
			}else if (key.equals("area")) {
				data[0] = "L'aire est de " + data[0] + " km^2.";
			}else if (key.equals("period")) {
				data[0] = "Met " + data[0] + " jours pour faire le tour du soleil.";
			}else if (key.equals("density")) {
				data[0] = "Fait " + data[0] + " kg par m^3.";
			}else if (key.equals("surfaceGrav")) {
				data[0] = "La gravité est de " + data[0];
			}else if (key.equals("escapeVelocity")) {
				data[0] = "Vitesse de libération " + data[0];
			}else if (key.equals("date")) {
				data[0] = "Découvert le " + data[0];
			}else if (key.equals("discoverer")) {
				data[0] = "Découvert par " + data[0];
			}else if (key.equals("childLabel")) {
				data[0] = "Les objets suivants orbitent autour : " + data[0];
			}else if (key.equals("perimeter")) {
				data[0] = "Le perimetre de l'astre est de : " + data[0] + " km.";
			}else if (key.equals("diameter")) {
				data[0] = "Le diameter de l'astre est de : " + data[0] + " km.";
			}
			
			json.put(key, data);
		}
	}

	@Override
	public List<PlanetsDTO> getListPlanets() {
		// TODO Auto-generated method stub
		return mapTicketToDTO(planetsrepository.findByAffichage(false)) ;
	}
}
