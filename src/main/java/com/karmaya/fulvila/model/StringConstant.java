package com.karmaya.fulvila.model;

public class StringConstant {

    //Response for failes Common Operation
    public static String E = "Exception";
    public static String MANDATORY_DATA_NOT_FOUND = "Body does not contain mandatory fields or restricted values";
    public static int MANDATORY_DATA_NOT_FOUND_CODE = -1000;
    public static String RECHECK_DATA = "Please recheck the mandatory data";
    public static int RECHECK_DATA_CODE = -1002;
    public static String UNKNOWN_EXCEPTION = "Operation failed due to unknown exception";
    public static int UNKNOWN_EXCEPTION_CODE = -3000;
    public static String UNIQUE_CONSTRAINT_EXCEPTION = E+": Inserted value contains already existing element";
    public static int UNIQUE_CONSTRAINT_EXCEPTION_CODE = -3001;

    //Response for Successful Common Operation
    public static String SUCCESSFUL_DATA_SUBMISSION = "Successfully Insertion";
    public static int SUCCESSFUL_DATA_SUBMISSION_CODE = 1000;
    public static String INFORMATION_UPDATE_SUCCESSFULLY = "Information Updated Successfully";
    public static int INFORMATION_UPDATE_SUCCESSFULLY_CODE = 1002;

    //Admin

    //Response for failed Admin Operation
    public static String ADMIN_REGISTRATION_FAILED = "Admin registration is failed";
    public static String ADMIN_LOGIN_FAILED = "Admin login failed";
    public static int ADMIN_LOGIN_FAILED_CODE = -1001;

    //Response for Successful Common Operation
    public static String ADMIN_REGISTRATION_SUCCESS = "Successfully register the admin";
    public static String ADMIN_LOGIN_SUCCESS = "Admin login successful";
    public static int ADMIN_LOGIN_SUCCESS_CODE = 1001;
}
