package org.projectbuendia.web.api.patients.put;

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

/**
 * Created by wwadewitte on 10/11/14.
 */
public class UpdateSpecificPatient implements ApiInterface {
    @Override
    public void call(final HttpServletRequest request, final HttpServletResponse response,final HashMap<String, String> urlVariables, final Map<String, String[]> parameterMap, final HashMap<String, String> payLoad){

        final String[] responseText = new String[]{null};

        Server.getLocalDatabase().executeUpdate(new SQLiteUpdate("" +
                "update `patients` set " +
                (payLoad.get("status") != null ? "`status`='" + payLoad.get("status") + "'" : "") +
                (payLoad.get("given_name") != null ? ",`given_name`='" + payLoad.get("given_name") + "'" : "") +
                (payLoad.get("family_name") != null ? ",`family_name`='" + payLoad.get("family_name") + "'" : "") +
                (payLoad.get("assigned_location_zone_id") != null ? ",`assigned_location_zone_id`='" + payLoad.get("assigned_location_zone_id") + "'" : "") +
                (payLoad.get("assigned_location_tent_id") != null ? ",`assigned_location_tent_id`='" + payLoad.get("assigned_location_tent_id") + "'" : "") +
                (payLoad.get("assigned_location_bed") != null ? ",`assigned_location_bed`='" + payLoad.get("assigned_location_bed") + "'" : "") +
                (payLoad.get("age_years") != null ? ",`age_years`='" + payLoad.get("age_years") + "'" : "") +
                (payLoad.get("age_months") != null ? ",`age_months`='" + payLoad.get("age_months") + "'" : "") +
                (payLoad.get("age_certainty") != null ? ",`age_certainty`='" + payLoad.get("age_certainty") + "'" : "") +
                (payLoad.get("age_type") != null ? ",`age_type`='" + payLoad.get("age_type") + "'" : "") +
                (payLoad.get("gender") != null ? ",`gender`='" + payLoad.get("gender") + "'" : "") +
                (payLoad.get("important_information") != null ? ",`important_information`='" + payLoad.get("important_information") + "'" : "") +
                (payLoad.get("pregnancy_start_timestamp") != null ? ",`pregnancy_start_timestamp`='" + payLoad.get("pregnancy_start_timestamp") + "'" : "") +
                (payLoad.get("first_showed_symptoms_timestamp") != null ? ",`first_showed_symptoms_timestamp`='" + payLoad.get("first_showed_symptoms_timestamp") + "'" : "") +
                (payLoad.get("movement") != null ? ",`movement`='" + payLoad.get("movement") + "'" : "") +
                (payLoad.get("eating") != null ? ",`eating`='" + payLoad.get("eating") + "'" : "") +
                (payLoad.get("origin_location") != null ? ",`origin_location`='" + payLoad.get("origin_location") + "'" : "") +
                (payLoad.get("next_of_kin") != null ? ",`next_of_kin`='" + payLoad.get("next_of_kin") + "'" : "") +
                " where `id` = '" + urlVariables.get("id") + "'"
        ));

        SQLiteQuery checkQuery = new SQLiteQuery("SELECT * FROM `patients` WHERE `id` = '"+urlVariables.get("id")+"'  ") {

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
