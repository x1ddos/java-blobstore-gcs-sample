package com.google.devrel.appengine;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

@SuppressWarnings("serial")
public class ServeBlobServlet extends HttpServlet {

    BlobstoreService blobstore = BlobstoreServiceFactory.getBlobstoreService();

    private static final Logger log = 
            Logger.getLogger(ServeBlobServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String[] parts = req.getRequestURI().split("/");
        String gsObjectName = URLDecoder.decode(parts[parts.length - 1], "UTF-8");

        log.info("Serving GCS object: " + gsObjectName);
        BlobKey blobKey = blobstore.createGsBlobKey(gsObjectName);

        blobstore.serve(blobKey, resp);
    }

}
