package uk.nhs.digital;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.Patient;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;

/**
 * Servlet implementation class GetIdServlet
 */
@WebServlet("/getPatientNameServlet")
public class GetPatientNameServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	FhirContext ctx = FhirContext.forDstu3();
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String serverBase = request.getParameter("serverURL");
		String searchFamily = request.getParameter("familyName");
		String searchGiven = request.getParameter("givenName");
		String searchGender = "male";
		
		IGenericClient client = ctx.newRestfulGenericClient(serverBase);
		
		// Perform a search
		Bundle results = client
		      .search()
		      .forResource(Patient.class)
		      .where(Patient.FAMILY.matches().value(searchFamily))
		      .where(Patient.GIVEN.matches().value(searchGiven))
		      .where(Patient.GENDER.exactly().code(searchGender))
		      .returnBundle(Bundle.class)
		      .execute();
		
		String encoded = ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(results);
		System.out.println("results= "+encoded);
		
		response.getWriter().println("Found " + results.getTotal() + " patients");
		response.getWriter().println(encoded);
		
		}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
