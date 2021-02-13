package universmaps.service;

import java.text.ParseException;
import java.util.Iterator;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

public class DataServiceImpl implements DataService{

	@Override
	public String[][] SearchData(String planetname) {
		// TODO Auto-generated method stub
    	String[][] data = new String[13][2];
        String name = planetname;
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
                + "OPTIONAL { ?s dbo:abstract ?abstractd .}\n" 
                + "OPTIONAL { ?s dbo:volume ?volume .}\n"
                + "filter langMatches(lang(?abstractd), 'fr') .\n"
                + "filter not exists{ filter contains(str(?abstractd), 'Erde') . }\n"
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
