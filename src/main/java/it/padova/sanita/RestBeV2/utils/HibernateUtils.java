package it.padova.sanita.restbe.utils;

import javax.annotation.PostConstruct;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class HibernateUtils
{
  private static HibernateUtils instance = null;

  private SessionFactory sessionFactory;

  private ServiceRegistry serviceRegistry;

  protected HibernateUtils()
  {
    //Build session factory
    Configuration configuration = new Configuration();
    configuration.configure();
    serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
    setSessionFactory(configuration.buildSessionFactory(serviceRegistry));
  }
  
  public static HibernateUtils getInstance()
  {
    if(instance == null)
    {
       instance = new HibernateUtils();
    }
    return instance;
 }

  public SessionFactory getSessionFactory() {
    return sessionFactory;
  }

  public void setSessionFactory(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }
  
  @PostConstruct
  public void postConstruct()
  {
    System.out.println("HibernateUtils] - postConstruct()");
  }
  
  public Session getSession()
  {
    if (sessionFactory == null)
      throw new IllegalStateException("SessionFactory has not been set on DAO before usage");
    if (sessionFactory.getCurrentSession() == null)
      throw new IllegalStateException("Session has not been set on DAO before usage");

    if (!sessionFactory.getCurrentSession().isOpen())
    {
      return sessionFactory.openSession();
    }
    return sessionFactory.getCurrentSession();
  }
}
