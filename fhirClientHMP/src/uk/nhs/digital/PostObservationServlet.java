package uk.nhs.digital;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hl7.fhir.dstu3.model.Bundle;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.ContactPoint.ContactPointUse;
import org.hl7.fhir.dstu3.model.Enumerations.AdministrativeGender;
import org.hl7.fhir.dstu3.model.HumanName;
import org.hl7.fhir.dstu3.model.Identifier;
import org.hl7.fhir.dstu3.model.Patient;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;

/**
 * Servlet implementation class GetIdServlet
 */
@WebServlet("/postObservation")
public class PostObservationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	FhirContext ctx = FhirContext.forDstu3();
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String serverBase = request.getParameter("serverURL");
		String identifier = request.getParameter("identifier");

		IGenericClient client = ctx.newRestfulGenericClient(serverBase);

		// Perform a search
		Bundle results = client.search().forResource(Patient.class)
				.where(Patient.IDENTIFIER.exactly().systemAndCode("https://fhir.nhs.uk/Id/nhs-number", identifier))
				.returnBundle(Bundle.class).execute();

		String encoded = ctx.newXmlParser().setPrettyPrint(true).encodeResourceToString(results);
		System.out.println("results= " + encoded);

		response.getWriter().println("Found " + results.getTotal() + " patients");
		response.getWriter().println(encoded);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		if (request.getParameter("method").equals("delete")){
			doDelete(request, response);
			return;
		}
		
		System.out.println("Using Observation Servlet");
		String serverBase = request.getParameter("serverURL");
		String family = request.getParameter("postFamilyName");
		String given = request.getParameter("postGivenName");
		String postIdentifier = request.getParameter("postIdentifier");
		String postDOB = request.getParameter("postDOB");
		Date dob = null;
		
		try {
			dob = new SimpleDateFormat("yyyy-MM-dd").parse(postDOB);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		IGenericClient client = ctx.newRestfulGenericClient(serverBase);

		Patient newpat = new Patient();

		HumanName name = newpat.addName();
		name.addGiven(given).setFamily(family);

		Identifier identifier = newpat.addIdentifier();
		identifier.setValue(postIdentifier);

		List<ContactPoint> contact = new ArrayList<ContactPoint>();
		contact.add(new ContactPoint().setUse(ContactPointUse.HOME).setValue("555-444"));

		newpat.setTelecom(contact);
		newpat.setGender(AdministrativeGender.MALE);
		newpat.setBirthDate(dob);
		MethodOutcome outcome = client.create().resource(newpat).execute();
		response.getWriter().println("id= " + outcome.getId() + "Created: " + outcome.getCreated());
	}
	
	@Override
	protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String serverBase = req.getParameter("serverURL");
		String identifier = req.getParameter("identifier");
		
		IGenericClient client = ctx.newRestfulGenericClient(serverBase);
		
		client.delete()
        .resourceConditionalByType("Patient")
        .where(Patient.IDENTIFIER.exactly().systemAndIdentifier("value", identifier))
        .execute();
		
		resp.getWriter().println("id= " + identifier + " deleted: ");

		
	}

}
