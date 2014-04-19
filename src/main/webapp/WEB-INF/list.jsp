<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
    	<title>Appengine Blobstore + GCS sample</title>
    </head>
    <body>
    	<h1>Appengine Blobstore + GCS sample</h1>
    	<a href="/upload">Upload a new file &raquo;</a>
    	
    	<p>Here's a list of file uploaded so far.</p>
    	
    	<ul>
    		<c:forEach items="${uploads}" var="item">
    			<li><a href="${item.properties.url}">${item.properties.name}</a>
    				<ul>
    					<li>key: ${item.key}</li>
    					<c:forEach items="${item.properties}" var="prop">
    					  <li>${prop.key}: ${prop.value}</li>
    					</c:forEach>
    				</ul>
    			</li>
    		</c:forEach>
    	</ul>
    </body>
</html>
