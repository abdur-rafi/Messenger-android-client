package sample;


public class Constants {
    private static Constants constants = new Constants();
    public static Constants getInstance(){
        return constants;
    }
    public static final String host = "192.168.0.102";
    public static final int tcpPort = 4010;
    public static final int udpPort = 4020;
    public static int ADD_MESSAGE = 1;
    public static int ADD_CONTACT = 2;
    public static int SET_PROGRESS = 3;
    public static int PICK_IMAGE = 4;
    public static int CHANGE_IMAGE = 5;
    public static int CHANGE_CURRENT_IMAGE = 6;
    public static int USE_FOR_ADD_CONTACT = 7;
    public static int USE_FOR_ADD_MEMBER = 8;
    public static String format = ".png";
    public static int LOG_IN = 9;
    public static int LOG_IN_FAILURE = 10;
    public static int CREATE_ACCOUNT = 11;
    public static int CREATE_ACCOUNT_FAILURE = 12;
    public static int ADD_CONTACT_FAILURE = 13;
    public static int INFO_FOR_CURRENT = 14;
    public static int INFO_FOR_OTHER = 15;
    public static int ADD_TO_ARCHIVED = 16;
    public static int REMOVE_FROM_ARCHIVED = 17;
    public static int ADD_MESSAGE_TO_ARCHIVE = 18;
    public static int REDO_SELECTED_ITEM = 19;
    public static int size = 25;
    private Constants(){
        ;
    }
}
