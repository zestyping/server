package org.projectbuendia.web.api;

@SuppressWarnings("serial")
public class Zones extends ApiHandler {

    String BASE_URL = "/zones";

    private String[] specification = new String[]{"zone_id","tents","tent_id", "portals" , "portal_id" };

    @Override
    protected String getBaseUrl() {
        return BASE_URL;
    }

    @Override
    protected String[] getSpecification() {
        return specification;
    }


}