package org.projectbuendia.web.api.patients.post;

import org.projectbuendia.server.Server;
import org.projectbuendia.sqlite.SQLiteQuery;
import org.projectbuendia.sqlite.SQLiteUpdate;
import org.projectbuendia.web.api.ApiInterface;
import org.projectbuendia.web.api.SharedFunctions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by wwadewitte on 10/11/14.
 */
public class AddNewPatient implements ApiInterface {
    @Override
    public void call(final HttpServletRequest request, final HttpServletResponse response,final HashMap<String, String> urlVariables, final Map<String, String[]> parameterMap, final HashMap<String, String> payLoad){

        final String[] responseText = new String[]{null};
        final int[] lastId = new int[]{-1};
        SQLiteQuery query = new SQLiteQuery("SELECT `id`, count(1) FROM `patients` ORDER BY ROWID DESC LIMIT 1") {

            @Override
            public void execute(ResultSet result) throws SQLException {
                if(result == null) {

                } else {
                    while (result.next()) {
                        if(result.getInt("count(1)") <= 0) {
                            System.out.println("Empty result, inserting first");
                            lastId[0] = 0;
                            return;
                        }
                        System.out.println("ID IS:" + result.getString("id"));
                        lastId[0] = Integer.parseInt(result.getString("id").split(Pattern.quote("."))[2]);
                        System.out.println("got last id" + lastId[0]);
                    }
                }
            }
        };

        /* we can only execute this once at a time because we are generating the UID*/

        while(Server.isDoingPatient()) {
            try {
                System.out.println("Waiting;;;");
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Server.setDoingPatient(true);

        Server.getLocalDatabase().executeQuery(query);

        while(lastId[0] == -1) {
            try {
               /// System.out.println("Waiting.....");
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // waiting for that query to clear considering the sensitive process
        }

        int newId = lastId[0] + 1;


        Server.getLocalDatabase().executeUpdate(new SQLiteUpdate("" +
                "insert into `patients`" +
                " (" +
                "`id`" +
                (payLoad.get("status") != null ? ",`status`" : "") +
                (payLoad.get("given_name") != null ? ",`given_name`" : "") +
                (payLoad.get("family_name") != null ? ",`family_name`" : "") +
                (payLoad.get("assigned_location_zone_id") != null ? ",`assigned_location_zone_id`" : "") +
                (payLoad.get("assigned_location_tent_id") != null ? ",`assigned_location_tent_id`" : "") +
                (payLoad.get("assigned_location_bed") != null ? ",`assigned_location_bed`" : "") +
                (payLoad.get("age_years") != null ? ",`age_years`" : "") +
                (payLoad.get("age_months") != null ? ",`age_months`" : "") +
                (payLoad.get("age_certainty") != null ? ",`age_certainty`" : "") +
                (payLoad.get("age_type") != null ? ",`age_type`" : "") +
                (payLoad.get("gender") != null ? ",`gender`" : "") +
                (payLoad.get("important_information") != null ? ",`important_information`" : "") +
                (payLoad.get("pregnancy_start_timestamp") != null ? ",`pregnancy_start_timestamp`" : "") +
                (payLoad.get("first_showed_symptoms_timestamp") != null ? ",`first_showed_symptoms_timestamp`" : "") +
                (payLoad.get("movement") != null ? ",`movement`" : "") +
                (payLoad.get("eating") != null ? ",`eating`" : "") +
                (payLoad.get("origin_location") != null ? ",`origin_location`" : "") +
                (payLoad.get("next_of_kin") != null ? ",`next_of_kin`" : "") +
                ") " +
                "values" +
                " ('MSF.TS."+ newId +"'" +
                (payLoad.get("status") != null ? ",'"+payLoad.get("status") + "'": "") +
                (payLoad.get("given_name") != null ? ",'"+payLoad.get("given_name")+ "'"  : "") +
                (payLoad.get("family_name") != null ? ",'"+payLoad.get("family_name")+ "'"  : "") +
                (payLoad.get("assigned_location_zone_id") != null ? ","+payLoad.get("assigned_location_zone_id") : "") +
                (payLoad.get("assigned_location_tent_id") != null ? ","+payLoad.get("assigned_location_tent_id") : "") +
                (payLoad.get("assigned_location_bed") != null ? ","+payLoad.get("assigned_location_bed") : "") +
                (payLoad.get("age_years") != null ? ","+payLoad.get("age_years") : "") +
                (payLoad.get("age_months") != null ? ","+payLoad.get("age_months") : "") +
                (payLoad.get("age_certainty") != null ? ",'"+payLoad.get("age_certainty")+ "'"  : "") +
                (payLoad.get("age_type") != null ? ",'"+payLoad.get("age_type")+ "'"  : "") +
                (payLoad.get("gender") != null ? ",'"+payLoad.get("gender")+ "'"  : "") +
                (payLoad.get("important_information") != null ? ",'"+payLoad.get("important_information")+ "'"  : "") +
                (payLoad.get("pregnancy_start_timestamp") != null ? ","+payLoad.get("pregnancy_start_timestamp") : "") +
                (payLoad.get("first_showed_symptoms_timestamp") != null ? ","+payLoad.get("first_showed_symptoms_timestamp") : "") +
                (payLoad.get("movement") != null ? ",'"+payLoad.get("movement")+ "'"  : "") +
                (payLoad.get("eating") != null ? ",'"+payLoad.get("eating")+ "'"  : "") +
                (payLoad.get("origin_location") != null ? ",'"+payLoad.get("origin_location")+ "'"  : "") +
                (payLoad.get("next_of_kin") != null ? ",'"+payLoad.get("next_of_kin") + "'" : "") +
                ")"
        ));

        Server.setDoingPatient(false);

        SQLiteQuery checkQuery = new SQLiteQuery("SELECT * FROM `patients` WHERE `id` = 'MSF.TS."+newId+"'  ") {

            @Override
            public void execute(ResultSet result) throws SQLException {
                while (result.next()) {
                    responseText[0] = SharedFunctions.SpecificPatientResponse(result);
                }
            }
        };

        Server.getLocalDatabase().executeQuery(checkQuery);

        while(responseText[0] == null) {
            // wait until the response is given
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
        try {
            response.getWriter().write(responseText[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
