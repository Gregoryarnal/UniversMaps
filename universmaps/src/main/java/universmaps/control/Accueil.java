package universmaps.control;

import org.apache.jena.query.*;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


import java.text.ParseException;
import java.time.LocalDate;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


@Controller
public class Accueil {

    @GetMapping("/")
    public String accueil(Model model) throws ParseException {
        return "index";
    }
    
    @GetMapping("/popup/{id}")
    public String popup(@PathVariable("id") String id, Model model) throws ParseException {
    	//@{/photos/saturne.png}
    	System.out.println(id);
    	getInfoObjet(id);
    	return "";
    }
    
    
    
    public void getInfoObjet(String id) throws ParseException {


        String name = id;
        String szEndpoint = "http://dbpedia.org/sparql";
        String szQuery = "prefix dct: <http://purl.org/dc/terms/>\n"
        		+ "prefix dbc: <http://dbpedia.org/resource/Category:>\n"
                + "prefix dbp: <http://dbpedia.org/property/>\n" + "prefix dbo: <http://dbpedia.org/ontology/>\n"
                + "Select * where{\n" 
                + "?s dbo:density ?density .\n"
                + "?s dbo:averageSpeed ?averageSpeed .\n" 
                + "?s dbp:atmosphereComposition ?atmosphereComposition .\n" 
                + "?s dbp:name ?name .\n" 
                + "?s dbp:satellites ?satellites .\n" 
                + "?s dbp:surfaceGrav ?surfaceGrav .\n" 
                + "?s dbp:surfacePressure ?surfacePressure .\n" 
                + "?s dbo:abstract ?abstract .\n" 
                + "filter langMatches(lang(?abstract), 'fr') .\n"
                + "filter contains(str(?name), '"+ name +"')}";

        
        Query query = QueryFactory.create(szQuery);
        System.out.println("on va envoyer la query");
        // Create the Execution Factory using the given Endpoint
        QueryExecution qexec = QueryExecutionFactory.sparqlService(szEndpoint, query);

        // Set Timeout
        ((QueryEngineHTTP) qexec).addParam("timeout", "10000");

        // Execute Query
        int iCount = 0;
        ResultSet rs = qexec.execSelect();
        while (rs.hasNext()) {
            // Get Result
            QuerySolution qs = rs.next();

            // Get Variable Names
            Iterator<String> itVars = qs.varNames();

            // Count
            iCount++;
            System.out.println("Result " + iCount + ": ");
            //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            //Date dateNaissance = format.parse(qs.get("dateNaissance").toString());
            //listPersonne.add(new PersonneConnue(qs.get("uri").toString(), qs.get("nom").toString(), dateNaissance,
                   // qs.get("image").toString()));
            // Display Result
            while (itVars.hasNext()) {
                String szVar = itVars.next().toString();
                String szVal = qs.get(szVar).toString();

                System.out.println("[" + szVar + "]: " + szVal);
            }
        }
    }
    
    
}