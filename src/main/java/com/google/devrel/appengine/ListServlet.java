package com.google.devrel.appengine;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

@SuppressWarnings("serial")
public class ListServlet extends HttpServlet {

    private DatastoreService datastore = 
            DatastoreServiceFactory.getDatastoreService();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Query query = new Query("FileObject").addSort("created", SortDirection.DESCENDING);
        
        List<Entity> entities = datastore.prepare(query).asList(FetchOptions.Builder.withLimit(100));
        req.setAttribute("uploads", entities);
        req.getRequestDispatcher("/WEB-INF/list.jsp").forward(req, resp);
    }

}
