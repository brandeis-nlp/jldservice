/**
 * Tests VM version from environment- note, even 1.5 will
 * cause an assertion error.
 */
def testVMVersion(){
  println "<h3>JVM Version Check: </h3>"
  def vers = System.getProperty("java.version")
  println "<p>JVM version: ${vers} </p>"
}

/**
 * Attempts to create an instance of a hibernate session. If this
 * works we have a connection to a database; additionally, we 
 * have a properly configured hibernate instance.
 */
def testHibernate(){
  println "<h3>Hibernate Configuration Check: </h3>"
  try{
    def sessFactory = DefaultHibernateSessionFactory.getInstance()
    def session = sessFactory.getHibernateSession()
    println "<p>Hibernate configuration check was successful</p>"
  }catch(Throwable tr){
    println """
    <p>Unable to create hibernate session. Exception type is: <br/>
    <i>${tr.toString()} </i><br/>		
    </p>
    """
  }   
}

println """
<html>
  <head>
    <title>Diagnostics Check</title>
  </head>
  <body>
"""
testVMVersion()
testHibernate()
println """
  </body>
</html>
"""
