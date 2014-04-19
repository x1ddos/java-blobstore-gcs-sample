package com.google.devrel.appengine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.blobstore.FileInfo;
import com.google.appengine.api.blobstore.UploadOptions;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;

@SuppressWarnings("serial")
public class UploadServlet extends HttpServlet {

    /**
     * Internal callback executed by Blobstore service when an uploaded
     * successfully inished.
     */
    public static final String UPLOAD_CALLBACK = "/upload";

    /**
     * Google Cloud Storage (GCS) bucket name where the uploaded content will
     * reside in.
     */
    public static final String GCS_BUCKET_NAME = "my-bucket-name-goes-here";

    private final ServeUrl serveUrl = new ServeUrl();

    private final BlobstoreService blobstore = 
            BlobstoreServiceFactory.getBlobstoreService();

    private final DatastoreService datastore = 
            DatastoreServiceFactory.getDatastoreService();

    private static final Logger log = 
            Logger.getLogger(UploadServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        dispatchUploadForm(req, resp, null);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // prefer this method over getBlobInfos() or getUploads()
        // when uploading to GCS
        Map<String, List<FileInfo>> uploads = blobstore.getFileInfos(req);
        List<FileInfo> fileInfos = uploads.get("files");
        if (fileInfos == null || fileInfos.size() == 0) {
            dispatchUploadForm(req, resp, "No file has been uploaded");
            return;
        }

        // store info about uploaded files in the Datastore for later use
        // in HomeServlet to list all uploaded content so far.
        List<Entity> entities = new ArrayList<Entity>(fileInfos.size());
        for (FileInfo fileInfo : fileInfos) {
            log.info("Processing upload blobkey: " + fileInfo);

            Entity ent = new Entity("FileObject", fileInfo.getGsObjectName());
            ent.setUnindexedProperty("type", fileInfo.getContentType());
            ent.setUnindexedProperty("name", fileInfo.getFilename());
            ent.setUnindexedProperty("size", fileInfo.getSize());
            ent.setProperty("created", fileInfo.getCreation());
            ent.setUnindexedProperty("url", serveUrl.guessServingUrl(fileInfo));

            entities.add(ent);
        }

        datastore.put(entities);
        resp.sendRedirect("/");
    }

    protected void dispatchUploadForm(
            HttpServletRequest req,
            HttpServletResponse resp, 
            String errorMsg
    ) throws ServletException, IOException {
        UploadOptions opts = UploadOptions.Builder
                .withGoogleStorageBucketName(GCS_BUCKET_NAME);
        String uploadUrl = blobstore.createUploadUrl("/upload", opts);

        req.setAttribute("uploadUrl", uploadUrl);
        if (errorMsg != null) {
            req.setAttribute("error", errorMsg);
        }

        req.getRequestDispatcher("/WEB-INF/upload-form.jsp").forward(req, resp);
    }
}
