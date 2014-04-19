<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
    	<title>Appengine Blobstore + GCS sample</title>
    	<style>
    		form {
    			border: 1px solid #ccc;
				padding: 0.5em 0.7em;
				display: inline-block;
				background-color: #fdfdfd;
    		}
    		code {
    			border: 1px solid #ddd;
				background-color: #fdfdfd;
				padding: 0.2em;
				line-height: 2.1em;
				white-space: nowrap;
    		}
    		.error {
    			display: inline-block;
    			padding: 0.3em;
    			color: #a00;
    			background-color: #fdd; 
    			border: 1px solid #c00;
    		}
    	</style>
    </head>
    <body>
    	<h1>Appengine Blobstore + GCS sample</h1>
    	<a href="/">&laquo; Back to files list</a>
    	
    	<p>Select a file and hit "upload" button.</p>
    	
    	<p>When you hit upload the browser will start POSTing to:<br>
    	<code>${uploadUrl}</code></p>
    	
    	<p>Note that the URL will expire at some point so if you keep this page
    	open long enough, the upload will fail. In that case refresh the
    	page to get a new upload URL.</p>
    	
    	<form action="${uploadUrl}" method="post" enctype="multipart/form-data">
    		<p><input type="file" name="files" multiple></p>
    		
    		Once the files are uploaded, each of them will have a serving URL
    		based on the following:
    		<ul>
    			<li>use Images API if content type starts with "image/"</li>
    			<li>construct a direct GCS URI link to serve from storage.googleapis.com if file size is &gt; 32Mb</li>
    			<li>otherwise, serve using ServeBlobServlet from the app</li>
    		</ul>
    		<p>Reading blobs uploaded to a GCS bucked-based URLs is not currenly supported
    		in dev server, so in "dev" mode all serving URL will have a value
    		of <code>"/dev-does-not-support-gcs-serving-yet"</code></p>
    		
    		<p><input type="submit" value="Upload"></p>
    	</form>
    	
    	<c:if test="${not empty error}">
    		<p class="error">${error}</p>
    	</c:if>
    </body>
</html>
