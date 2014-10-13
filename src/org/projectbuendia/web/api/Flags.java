package org.projectbuendia.web.api;

@SuppressWarnings("serial")
public class Flags extends ApiHandler {

    String BASE_URL = "/flags";

    private String[] specification = new String[]{"id","spec","spec_id", "spec_spec", "spec_spec_id"};
    // /patients/MSF.KH.1010/events/1919191

    @Override
    protected String getBaseUrl() {
        return BASE_URL;
    }
    @Override
    protected String[] getSpecification() {
        return specification;
    }


}