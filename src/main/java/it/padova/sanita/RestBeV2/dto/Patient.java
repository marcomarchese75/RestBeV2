package it.padova.sanita.restbe.dto;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="ASSISTITO", schema="scott")
@SequenceGenerator(name="IdPatientSequenceGen",sequenceName="ASS_IPCA_SEQ",allocationSize=1,initialValue=1)
public class Patient
{	
	public Patient(Long ass_ipca, String ass_cogn, String ass_nome,
			String ass_tel, String ass_email) {
		
		this.ass_ipca = ass_ipca;
		this.ass_cogn = ass_cogn;
		this.ass_nome = ass_nome;
		this.ass_tel = ass_tel;
		this.ass_email = ass_email;
	}
	
	public Patient() {
	}

	private Long ass_ipca;

	private String ass_cogn;

	private String ass_nome;

	private String ass_tel;

	private String ass_email;

	@Id
	@Column(name="ASS_IPCA", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IdPatientSequenceGen")
	public Long getAss_Ipca()
	{
		return ass_ipca;
	}

	public void setAss_Ipca(Long ass_ipca)
	{
		this.ass_ipca = ass_ipca;
	}

	@Column(name="ASS_COGN", nullable = false, length = 100)
	public String getAss_Cogn()
	{
		return ass_cogn;
	}

	public void setAss_Cogn(String ass_cogn)
	{
		this.ass_cogn = ass_cogn;
	}
	
	@Column(name="ASS_NOME", nullable = false, length = 100)
	public String getAss_Nome()
	{
		return ass_nome;
	}

	public void setAss_Nome(String ass_nome)
	{
		this.ass_nome = ass_nome;
	}
	
	@Column(name="ASS_TEL", nullable = false, length = 100)
	public String getAss_Tel()
	{
		return ass_tel;
	}

	public void setAss_Tel(String ass_tel)
	{
		this.ass_tel = ass_tel;
	}
	
	@Column(name="ASS_EMAIL", nullable = false, length = 100)
	public String getAss_Email()
	{
		return ass_email;
	}

	public void setAss_Email(String ass_email)
	{
		this.ass_email = ass_email;
	}
}
