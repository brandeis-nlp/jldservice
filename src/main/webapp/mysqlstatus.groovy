import groovy.sql.Sql
/**
 * forwards to passed in page
 */
def forward(page, req, res){
  def dis = req.getRequestDispatcher(page);
  dis.forward(req, res);
}
def sql = Sql.newInstance("jdbc:mysql://localhost:3306/words", "words",
        "words", "com.mysql.jdbc.Driver")
   
def uptime = ""
def questions = ""
sql.eachRow("show status"){ status ->
  if(status.variable_name == "Uptime"){
     uptime =  status[1]
     request.setAttribute("uptime", uptime)
  }else if (status.variable_name == "Questions"){
     questions =  status[1]
     request.setAttribute("questions", questions)
  }
}

request.setAttribute("qpm", Integer.valueOf(questions) / 
Integer.valueOf(uptime) )

int insertnum = 0
int selectnum = 0
int updatenum = 0
sql.eachRow("show status like 'Com_%'"){ status ->
  if(status.variable_name == "Com_insert"){
     insertnum =  Integer.valueOf(status[1])
  }else if (status.variable_name == "Com_select"){
     selectnum =  Integer.valueOf(status[1])
  }else if (status.variable_name == "Com_update"){
     updatenum =  Integer.valueOf(status[1])
 }
}

request.setAttribute("qinsert", 100 * (insertnum / Integer.valueOf(uptime)))
request.setAttribute("qselect", 100 * (selectnum / Integer.valueOf(uptime)))
request.setAttribute("qupdate", 100 * (updatenum / Integer.valueOf(uptime)))
forward("mysqlreport.gsp", request, response)
