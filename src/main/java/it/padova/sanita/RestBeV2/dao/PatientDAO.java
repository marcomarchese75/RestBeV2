package it.padova.sanita.restbe.dao;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import oracle.jdbc.internal.OracleTypes;

import org.hibernate.HibernateException;
import org.hibernate.Query;

import com.google.gson.Gson;

import it.padova.sanita.restbe.dto.Patient;

@Transactional()
public class PatientDAO extends GenericHibernateDao<Patient, Long> {

	public PatientDAO(){
		super(Patient.class);
	}

	@SuppressWarnings("unchecked")
	public List<Patient> findByName(String name) throws Exception
	{
		try
		{
			StringBuilder strQuery = new StringBuilder("from Assistito p where p.ass_name = :name");
			Query hqlQuery = getSession().createQuery(strQuery.toString());
			hqlQuery.setString("name", name);

			return (ArrayList<Patient>) hqlQuery.list(); 
		}
		catch (HibernateException ex)
		{
			throw new Exception(ex); 
		}
	}
	
	@SuppressWarnings("unchecked")
	public String InsertOrUpdateViaStoredPro(Patient patient) throws Exception
	{
		//http://stackoverflow.com/questions/13015749/getting-a-result-back-from-a-stored-procedure-in-java
		String ret = "";
		CallableStatement callableStatement = null;
		try{
			callableStatement = getConnection().prepareCall("call INSUPDPATIENT(?,?,?,?,?,?)");
			callableStatement.setLong(1,patient.getAss_Ipca());
			callableStatement.setString(2,patient.getAss_Nome());
			callableStatement.setString(3,patient.getAss_Cogn());
			callableStatement.setString(4,patient.getAss_Tel());
			callableStatement.setString(5,patient.getAss_Email());

			callableStatement.registerOutParameter(6, java.sql.Types.VARCHAR);

			callableStatement.execute();
			
			ret = callableStatement.getString(6);
			//System.out.println(result);
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return ret;
	}
}
