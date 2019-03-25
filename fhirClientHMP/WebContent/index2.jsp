<html>
<head>
<title>FHIR Client Dashboard</title>
</head>
<body>

<h2>Patient: </h2><input type = "hidden" value="John Doe"/>
<form action="getIdServlet" method="get">
		<h2>Search Using ID</h2>
		URL: <input type="text" name="serverURL"
			value="http://cdwebintl01.corp.internal:8085/hapi-fhir-jpaserver-example/baseDstu3" size="50px"><br />

		<p>
			Patient NHS Number: <input type="text" name="identifier" value="" size="50px"><br />
		</p>
		
		<input type="submit" value="Go" />
	</form>
	
	<form action="getPatientNameServlet" method="get">
		<h2>Search Patient - Using Name</h2>
		URL: <input type="text" name="serverURL"
			value="http://cdwebintl01.corp.internal:8085/hapi-fhir-jpaserver-example/baseDstu3" size="50px"><br />

		<p>
			Family Name: <input type="text" name="familyName" value="" size="50px"><br />
		</p>
		<p>
			Given Name: <input type="text" name="givenName" value="" size="50px"><br />
		</p>
		<input type="submit" value="Go" />
	</form>

 Patient: 
 
	<form action="getIdServlet" method="post">
			<input type="hidden" name="method" value="post">
	
		<h2>Create a Patient</h2>
		URL: <input type="text" name="serverURL"
			value="http://cdwebintl01.corp.internal:8085/hapi-fhir-jpaserver-example/baseDstu3" size="50px"><br />
		<p>
			Family Name: <input type="text" name="postFamilyName" value="" size="50px"><br />
			Given Name: <input type="text" name="postGivenName" value="" size="50px"><br />
			Birth Date: <input type="date" name="postDOB"><br />
		</p>
		<input type="submit" value="Post" />
	</form>
	
	<form action="getIdServlet" method="post">
		<input type="hidden" name="method" value="delete">
		<h2>Delete Patient</h2>
		URL: <input type="text" name="serverURL"
			value="http://cdwebintl01.corp.internal:8085/hapi-fhir-jpaserver-example/baseDstu3" size="50px"><br />

		<p>
			Patient ID: <input type="text" name="identifier" value="" size="50px"><br />
		</p>
		
		<input type="submit" value="Go" />
	</form>
</body>
</html>