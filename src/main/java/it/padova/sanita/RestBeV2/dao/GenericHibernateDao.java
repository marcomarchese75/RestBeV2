package it.padova.sanita.restbe.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.StringTokenizer;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.hibernate.internal.SessionFactoryImpl;

import it.padova.sanita.restbe.utils.HibernateUtils;

@Transactional
public abstract class GenericHibernateDao<T, ID extends Serializable>
{
	protected Class<T> persistentType;

	public GenericHibernateDao(Class<T> persistentType)
	{
		this.persistentType = persistentType;
	}

	public SessionFactory getSessionFactory()
	{
		return HibernateUtils.getInstance().getSessionFactory();
	}

	//  public void setSessionFactory(SessionFactory sessionFactory)
	//  {
	//    this.sessionFactory = sessionFactory;
	//  }

	public Class<T> getPersistentType()
	{
		return persistentType;
	}

	public Session getSession()
	{
		Session session = HibernateUtils.getInstance().getSession();
		if (!session.getTransaction().isActive())
		{
			session.beginTransaction();
		}
		return session;
	}

	public void detachObject(T obj) throws Exception
	{
		if (obj != null)
		{
			Session session = getSession();
			session.evict(obj);
			flush(session);
		}
	}

	@SuppressWarnings("unchecked")
	public T mergeObject(T obj) throws Exception
	{
		if (obj != null)
		{
			Session session = getSession();
			T mergedObject = (T)session.merge(obj);
			flush(session);
			return mergedObject;
		}
		else
			return null;
	}

	@SuppressWarnings("unchecked")
	public T findById(ID id) throws Exception
	{
		try 
		{
			return (T) getSession().get(getPersistentType(), id);
		}
		catch (HibernateException e)
		{
			throw new Exception(e);
		}    
	}


	public List<T> findAll() throws Exception
	{
		return findByCriteria();
	}

	public Criteria createCriteria() throws Exception
	{
		return getSession().createCriteria(getPersistentType());
	}

	@SuppressWarnings("unchecked")
	public List<T> findByExample(T exampleInstance, String... excludeProperty) throws Exception
	{
		try 
		{
			Criteria crit = getSession().createCriteria(getPersistentType());
			Example example =  Example.create(exampleInstance);
			for (String exclude : excludeProperty)
			{
				example.excludeProperty(exclude);
			}
			crit.add(example);
			return crit.list();
		}
		catch (HibernateException e)
		{
			throw new Exception(e);
		}
	}

	public T saveOrUpdate(T entity) throws Exception
	{
		try
		{
			Session session = getSession();
			session.merge(entity);
			flush(session);
			return entity;
		}
		catch (HibernateException e)
		{
			throw new Exception(e);
		}
	}

	public void delete(T entity) throws Exception
	{
		try
		{
			Session session = getSession();
			session.delete(entity);
			flush(session);
		}
		catch (HibernateException e)
		{
			throw new Exception(e);
		}
	}

	public void flush(Session session) throws Exception
	{
		try 
		{
			session.getTransaction().commit();
			//session.close();
		}
		catch (HibernateException e)
		{
			throw new Exception(e);
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> findByCriteria(Criterion... criterion) throws Exception
	{
		try 
		{
			Criteria crit = getSession().createCriteria(getPersistentType());
			for (Criterion c : criterion)
			{
				crit.add(c);
			}
			return crit.list();
		}
		catch (HibernateException e)
		{
			throw new Exception(e);
		}    
	}

	/** Data una Stringa di item separati da "," formatta la stessa lista 
	 * contornando gli item di apici in moda da renderla compatibile con i token 
	 * SQL di tipo SELECT IN (...).
	 * @param stringList Stringa di input del tipo A,B,C  
	 * @return String nel formato 'A','B','C'
	 * */
	protected String parseItemListForSelectInStatement(String stringList)
	{
		StringTokenizer st = new StringTokenizer(stringList, ",");
		StringBuilder sb = new StringBuilder();
		int i = 0;
		while (st.hasMoreTokens())
		{
			if (i>0)
				sb.append(",");
			sb.append("'");
			sb.append(st.nextToken());
			sb.append("'");
			i++;
		}

		return sb.toString();


	}

	protected Connection getConnection(){
		Session session = getSession();
		Connection connection = null;
		SessionFactoryImpl sessionFactory = (SessionFactoryImpl) session.getSessionFactory();
		try{
			connection = sessionFactory.getConnectionProvider().getConnection();
		}
		catch (HibernateException | SQLException e)
		{
			e.printStackTrace();
		}
		return connection;
	}
}
