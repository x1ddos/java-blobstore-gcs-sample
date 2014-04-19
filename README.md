# (Java) App Engine Blobstore API + GCS sample app

This app uses Blobstore API and Google Cloud Storage (GCS) to let users upload arbitrary file
contents, display a list of all files, and serve uploaded contents using 3 different ways (for example purposes):

  * using [Images API][2]
  * creating a direct [Request URI][3] that points to GCS front-end
  * using [BlobstoreService.serve()][4]

## Setup

Create a new project on [Google Cloud Console][0].
For more info on Console see this:
[developers.google.com/console/help/new/][5]

Note your Project ID. You'll need it later.

While you're in the console, go to Cloud Storage section and create a new bucket.
The bucket has to have Default Object ACL of `public-read`, otherwise serving URLs
of files larger than 32Mb will not be accessible.

You can set Default Object ACL using `gsutil`:

  ```gsutil defacl set public-read gs://<my-bucket-name>```

## Running the app

Clone the repo and optionally import it into your favorite IDE.

Open `pom.xml` and update the Project ID in:

  ```<appengine.app.id>my-app-id-goes-here</appengine.app.id>```

Last thing is to set correct bucket name in [UploadServlet][1]:

  ```
  public static final String GCS_BUCKET_NAME = "my-bucket-name-goes-here";
  ```

To upload the app execute `mvn appengine:update`

Once the deployment is complete, visit `https://<my-project-id>.appspot.com`


To see all the available goals for the App Engine plugin, run

    mvn help:describe -Dplugin=appengine


[0]: https://cloud.google.com/console
[1]: https://github.com/crhym3/java-blobstore-gcs-sample/blob/master/src/main/java/com/google/devrel/appengine/UploadServlet.java
[2]: https://developers.google.com/appengine/docs/java/images/
[3]: https://developers.google.com/storage/docs/reference-uris
[4]: https://developers.google.com/appengine/docs/java/blobstore/
[5]: https://developers.google.com/console/help/new/