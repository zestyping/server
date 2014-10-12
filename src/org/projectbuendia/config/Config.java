package org.projectbuendia.config;


public final class Config {

    /*
    NOT OVERWRITTEN BY CONFIG.PROP BUT USED BY THE SERVER:
     */

    public static String CONFIGURATION_FILE = "config.prop";





    /*
    CONFIG.PROP
     */


    //General server information
    public static String SERVER_NAME = "patient_tracker_demo01";
    public static int SERVER_VERSION = 1;
    public static String SERVER_TIMEZONE = "UTC";

    //External Functionality Toggles
    public static boolean ENABLE_MONGOPUSH = true;


    //server.Server Ports
    public static int HTTP_PORT = 80;

    //hosts

    public static String SQLITE_PATH = "projectbuendia.db";
    public static String MONGODB_HOST = "localhost";
    public static String MONGODB_DB = "projectbuendia";

    //security measures
    public static String ERROR_PATH = "/";
    public static int MAX_ERROR_FOLDER_SIZE_MB = 10;



}