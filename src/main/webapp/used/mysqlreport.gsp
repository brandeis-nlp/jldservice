<html><head>
<title>MySql Health Report</title>
</head>
<body>
<table>
<tr>
  <td>Database Uptime:</td><td><%= 
  "${request.getAttribute("uptime")}" %></td>
</tr>
<tr>
  <td>Number of Queries:</td><td><%=
  "${request.getAttribute("questions")}" %></td>
</tr>
<tr>
  <td>Queries per Minute =</td><td><%=
  "${request.getAttribute("qpm")}" %></td>
</tr>
<tr>
  <td>% Queries Inserts =</td><td><%=
  "${request.getAttribute("qinsert")}" %></td>
</tr>
<tr>
  <td>% Queries Selects =</td><td><%=
  "${request.getAttribute("qselect")}" %></td>
</tr>
<tr>
  <td>% Queries Updates =</td><td><%=
  "${request.getAttribute("qupdate")}" %></td>
</tr>
</table>
</body>
</html>
