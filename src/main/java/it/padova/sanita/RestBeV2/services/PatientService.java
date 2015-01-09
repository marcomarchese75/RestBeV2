package it.padova.sanita.restbe.services;

import java.util.List;
 
import it.padova.sanita.restbe.dao.PatientDAO;
import it.padova.sanita.restbe.dto.Patient;

import javax.ejb.Stateless;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/** Example resource class hosted at the URI path "/myresource"
 */
@Path("/rest")
@Stateless
public class PatientService {
    
	private Gson gson = new Gson();
	private PatientDAO patientDAO = new PatientDAO();
	
    /** Method processing HTTP GET requests, producing "text/plain" MIME media
     * type.
     * @return String that will be send back as a response of type "text/plain".
     */
	/*@Path("/patient")
	@GET 
    @Produces("text/plain")
    public String getIt() {
        return "Hello page!";
    }*/

	@GET
	@Path("patient")
	@Produces("application/json")
	public Response getPatients()
	{
		try
		{
			//Get specific values
			List<Patient> _patients = patientDAO.findAll();

			if(_patients != null) {
				return Response.status(200).entity(gson.toJson(_patients)).build(); 
			} else {
				return Response.status(404).entity("NOT FOUND").build();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return Response.status(500).entity("ERROR").build();
		}
	}
	
	@GET
	@Path("patient/{id}")
	@Produces("application/json")
	public Response getPatient(@PathParam("id") Long id)
	{
		try
		{
			//Get specific values
			Patient _patient = patientDAO.findById(id);

			if(_patient != null) {
				return Response.status(200).entity(gson.toJson(_patient)).build(); 
			} else {
				return Response.status(404).entity("NOT FOUND").build();
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
			return Response.status(500).entity("ERROR").build();
		}

	}
	
	@POST
	@Path("/patient")
	public Response createOrUpdatePatient(String payload) {
		Patient patient = gson.fromJson(payload, Patient.class);
		try {
			patient = patientDAO.saveOrUpdate(patient);
			return Response.status(200).entity(null).build();

		} catch (Exception e) {
			return Response.status(500).entity(null).build();
		}
	}
	
	@POST
	@Path("/patientStoredPro")
	public Response InsertOrUpdateViaStoredPro(String payload) {
		Patient patient = gson.fromJson(payload, Patient.class);

		try {
			String ret = patientDAO.InsertOrUpdateViaStoredPro(patient);
			return Response.status(200).entity(ret).build();
		} catch (Exception e) {
			return Response.status(500).entity(null).build();
		}
	}

	@DELETE
	@Path("patient/{id}")
	public Response deletePatient(@PathParam("id") Long id)
	{
		try
		{
			//Get specific values
			Patient _patient = patientDAO.findById(id);

			if(_patient != null) {
				patientDAO.delete(_patient);
				return Response.status(200).entity(gson.toJson(null)).build(); 
			} else {
				return Response.status(404).entity("NOT FOUND").build();
			}

		}
		catch (Exception e)
		{
			e.printStackTrace();
			return Response.status(500).entity("ERROR").build();
		}
	}
	
}
