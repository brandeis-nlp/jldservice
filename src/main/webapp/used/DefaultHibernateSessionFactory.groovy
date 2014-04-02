class DefaultHibernateSessionFactory{
  DefaultHibernateSessionFactory getInstance(){
    return new DefaultHibernateSessionFactory()
  }
  
  String getHibernateSession(){
    throw new Exception("Boo!")
  }
}