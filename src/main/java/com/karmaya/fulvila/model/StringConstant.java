package com.karmaya.fulvila.model;

public class StringConstant {
    //Response for failes Common Operation
    public static String E = "Exception";
    public static String MANDATORY_DATA_NOT_FOUND = "Mandatory data not found in request query";
    public static int MANDATORY_DATA_NOT_FOUND_CODE = -1000;
    public static String UNIQUE_CONSTRAINT_EXCEPTION = E+": Inserted value contains already existing element";
    public static int UNIQUE_CONSTRAINT_EXCEPTION_CODE = -3000;
    public static String UNKNOWN_EXCEPTION = "Operation failed due to unknown exception";
    public static int UNKNOWN_EXCEPTION_CODE = -5000;

    //Response for Successful Common Operation
    public static String SUCCESSFUL_DATA_SUBMISSION = "Successfully Insertion";
    public static int SUCCESSFUL_DATA_SUBMISSION_CODE = 1000;

    //Admin
    public static String ADMIN_REGISTRATION_FAILED = "Admin registration is failed";
    public static String ADMIN_REGISTRATION_SUCCESS = "Successfully register the admin";
}
