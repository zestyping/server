package org.projectbuendia.web;

import java.util.EnumSet;
import java.util.HashMap;

import org.projectbuendia.config.Config;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.projectbuendia.web.api.ApiInterface;
import org.projectbuendia.web.api.Flags;
import org.projectbuendia.web.api.ServletFilter;
import org.projectbuendia.web.api.Patients;
import org.projectbuendia.web.api.flags.get.FilterFlags;
import org.projectbuendia.web.api.patients.get.FilterPatients;
import org.projectbuendia.web.api.patients.get.ShowPatientData;
import org.projectbuendia.web.api.patients.post.AddNewPatient;
import org.projectbuendia.web.api.patients.put.UpdateSpecificPatient;

import javax.servlet.DispatcherType;

public class JettyServer {

    private static HashMap<String, ApiInterface> putStructure = new HashMap<String, ApiInterface>();
    private static HashMap<String, ApiInterface> delStructure = new HashMap<String, ApiInterface>();
    private static HashMap<String, ApiInterface> postStructure = new HashMap<String, ApiInterface>();
    private static HashMap<String, ApiInterface> getStructure = new HashMap<String, ApiInterface>();

    public static void start() throws Exception {
        Server server = new Server(Config.HTTP_PORT);
        ServletHandler handler = new ServletHandler();

        server.setHandler(handler);
        handler.addFilterWithMapping(ServletFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));

        handler.addServletWithMapping(Patients.class, "/patients/*");
        handler.addServletWithMapping(Patients.class, "/patients");

        handler.addServletWithMapping(Flags.class, "/flags/*");
        handler.addServletWithMapping(Flags.class, "/flags");



        getStructure.put("/patients", new FilterPatients());
        getStructure.put("/patients/*", new ShowPatientData());

        getStructure.put("/flags", new FilterFlags());

        postStructure.put("/patients", new AddNewPatient());

        putStructure.put("/patients/*", new UpdateSpecificPatient());



        server.start();
        server.join();
    }

    public static HashMap<String, ApiInterface> getApiStructure(String method) {
        if(method.equalsIgnoreCase("get")) {
            return getStructure;
        } else if(method.equalsIgnoreCase("post")) {
            return postStructure;
        } else if(method.equalsIgnoreCase("put")) {
            return putStructure;
        } else if(method.equalsIgnoreCase("delete")) {
            return delStructure;
        }

        return null;
    }

    public static final String[] zones = new String[] {
        "triage",           //0
        "suspect zone",     //1
        "probable zone",    //2
        "confirmed zone",   //3
        "mortuary",         //4
        "outisde",          //5
    };

    public static final Object[][] tents = new Object[][]{
            /*
            [0] = tent name
            [1] = zone id
            [2] = tent id
             */
            {"T1",1,1},
            {"S1",2,1},
            {"P1",3,1},
            {"C1",4,1},
        };

    public static final String[] flag_types = new String[] {
            "lab tests",           //0
            "transfers",     //1
            "hygiene",    //2
            "feeding",   //3
            "drugs"         //4
    };

    public static final Object[][] flag_subtypes = new Object[][] {
            /*
            [0] = subtype_name
            [1] = flag id
            [2] subtype_id
             */
            {"T1",1,1},
            {"S1",2,1},
            {"P1",3,1},
            {"C1",4,1},
    };

}