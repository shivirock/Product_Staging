package com.karmaya.fulvila.controller;

import com.karmaya.fulvila.model.Admin;
import com.karmaya.fulvila.model.ResponseModel;
import com.karmaya.fulvila.model.StringConstant;
import com.karmaya.fulvila.repository.AdminRepository;
import com.karmaya.fulvila.service.SequenceGeneratorService;
import com.karmaya.fulvila.util.PasswordEncDec;
import com.karmaya.fulvila.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Class is a rest controller to handle Admin Operation
 */
@RestController
public class AdminController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    ResponseModel responseModel;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    Util mUtil;

    @Autowired
    private SequenceGeneratorService sequenceGeneratorService;

    @GetMapping("/")
    public String landing(HttpServletRequest request){
        LOGGER.error("Request Address: "+request.getRemoteAddr());
        return "Welcome to Fulvila";
    }

    /**
     * This method handles query to register Admin
     * @param admin It should Objectified JSON of Admin type {@link com.karmaya.fulvila.model.Admin}
     * @param request This parameter is handled automatically by platform
     * @return Check Out the Response Model: {@link com.karmaya.fulvila.model.ResponseModel}
     */
    @PostMapping("/addAdmin")
    public ResponseEntity<ResponseModel> addAdmin(@RequestBody Admin admin, HttpServletRequest request) {
        //Null Value Check as pre da ta validation
        if(admin.getName() == null || admin.getName().equals("") || admin.getEmail() == null ||
                admin.getEmail().equals("") || admin.getAuthProvider() == null || admin.getRegistrationType() == null) {
            LOGGER.error("Requested From: " + request.getRemoteAddr() + " " + StringConstant.ADMIN_REGISTRATION_FAILED + " due to "+StringConstant.MANDATORY_DATA_NOT_FOUND);
            return new ResponseEntity<>(responseModel.buildResponse(
                    null,
                    StringConstant.MANDATORY_DATA_NOT_FOUND,
                    StringConstant.MANDATORY_DATA_NOT_FOUND_CODE,
                    mUtil.generateDateAndTime().toString(),
                    "/addAdmin"),
                    HttpStatus.BAD_REQUEST);
        }

        try {
            //Generating Server Dependent Data
            admin.setId(sequenceGeneratorService.generateSequence(Admin.SEQUENCE_NAME));
            admin.setTimeStamp(mUtil.generateDateAndTime().toString());
            admin.setLastUpdateTime(mUtil.generateDateAndTime().toString());
            //Checking for local authentication type
            if(!(admin.getAuthProvider() == Admin.AuthProvider.facebook) && !(admin.getAuthProvider() == Admin.AuthProvider.google)) {
                if(admin.getPassword() == null) {
                    LOGGER.error("Requested From: " + request.getRemoteAddr() + " " + StringConstant.ADMIN_REGISTRATION_FAILED + " due to "+StringConstant.MANDATORY_DATA_NOT_FOUND);
                    return new ResponseEntity<>(responseModel.buildResponse(
                            null,
                            StringConstant.MANDATORY_DATA_NOT_FOUND,
                            StringConstant.MANDATORY_DATA_NOT_FOUND_CODE,
                            mUtil.generateDateAndTime().toString(),
                            "/addAdmin"),
                            HttpStatus.BAD_REQUEST);
                }
                //Calling password hashing encryption
                admin.setPassword(PasswordEncDec.encrypt(admin.getPassword()));
            } else {
                //Clearing password if logging with google or facebook
                admin.setPassword(null);
            }
            admin.setActivationStatus(Admin.ActivationStatus.Active);
            admin = adminRepository.save(admin);
            //Hiding the password before returning it to client
            admin.setPassword(null);
            LOGGER.info("Requested From: " + request.getRemoteAddr() + " " + StringConstant.ADMIN_REGISTRATION_SUCCESS + " for "+admin.getEmail());
            return new ResponseEntity<>(responseModel.buildResponse(
                    admin,
                    StringConstant.SUCCESSFUL_DATA_SUBMISSION,
                    StringConstant.SUCCESSFUL_DATA_SUBMISSION_CODE,
                    mUtil.generateDateAndTime().toString(),
                    "/addAdmin"),
                    HttpStatus.OK);
        } catch (DuplicateKeyException mwe){
            //Creating response after catching unique element constraint
            LOGGER.error("Requested From: " + request.getRemoteAddr() + StringConstant.UNIQUE_CONSTRAINT_EXCEPTION + " : "+admin.getEmail());
            return new ResponseEntity<>(responseModel.buildResponse(
                    mwe.getLocalizedMessage(),
                    StringConstant.UNIQUE_CONSTRAINT_EXCEPTION,
                    StringConstant.UNIQUE_CONSTRAINT_EXCEPTION_CODE,
                    mUtil.generateDateAndTime().toString(),
                    "/addAdmin"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e){
            //Creating response for unknown exception
            e.printStackTrace();
            LOGGER.error("Requested From: " + request.getRemoteAddr() + StringConstant.UNKNOWN_EXCEPTION + " :/n"+admin.toString());
            return new ResponseEntity<>(responseModel.buildResponse(
                    e.getLocalizedMessage(),
                    StringConstant.UNKNOWN_EXCEPTION,
                    StringConstant.UNKNOWN_EXCEPTION_CODE,
                    mUtil.generateDateAndTime().toString(),
                    "/addAdmin"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * This method handles query to login Admin
     * @param admin It should Objectified JSON of Admin type {@link com.karmaya.fulvila.model.Admin}
     * @param request This parameter is handled automatically by platform
     * @return Check Out the Response Model: {@link com.karmaya.fulvila.model.ResponseModel}
     */
    @GetMapping("/adminLogin")
    public ResponseEntity<ResponseModel> adminLogin(@RequestBody Admin admin, HttpServletRequest request) {
        //Mandatory Value Check
        if(admin.getEmail() == null || admin.getAuthProvider() == null) {
            //Creating Response
            LOGGER.error("Requested From: " + request.getRemoteAddr() + " " + StringConstant.ADMIN_REGISTRATION_FAILED + " due to "+StringConstant.MANDATORY_DATA_NOT_FOUND);
            return new ResponseEntity<>(responseModel.buildResponse(
                    null,
                    StringConstant.MANDATORY_DATA_NOT_FOUND,
                    StringConstant.MANDATORY_DATA_NOT_FOUND_CODE,
                    mUtil.generateDateAndTime().toString(),
                    "/adminLogin"),
                    HttpStatus.BAD_REQUEST);
        }

        //Check for local login
        if(admin.getAuthProvider() == Admin.AuthProvider.local){
            //Check for mandatory field
            if(admin.getPassword() == null) {
                //Creating Response
                LOGGER.error("Requested From: " + request.getRemoteAddr() + StringConstant.ADMIN_REGISTRATION_FAILED + " due to "+StringConstant.MANDATORY_DATA_NOT_FOUND);
                return new ResponseEntity<>(responseModel.buildResponse(
                        null,
                        StringConstant.MANDATORY_DATA_NOT_FOUND,
                        StringConstant.MANDATORY_DATA_NOT_FOUND_CODE,
                        mUtil.generateDateAndTime().toString(),
                        "/adminLogin"),
                        HttpStatus.BAD_REQUEST);
            }
        }

        //Getting Admin Data by Email
        //Admin _admin = adminRepository.findByEmail(admin.getEmail());
        Admin _admin = adminRepository.findByEmailAndAuthProvider(admin.getEmail(), admin.getAuthProvider());

        if(_admin == null) {
            //creating response for admin, which does not exist in Fulvila
            LOGGER.error("Requested From: " + request.getRemoteAddr() + " " + StringConstant.ADMIN_LOGIN_FAILED + " : "+admin.getEmail());
            return new ResponseEntity<>(responseModel.buildResponse(
                    null,
                    StringConstant.ADMIN_LOGIN_FAILED,
                    StringConstant.ADMIN_LOGIN_FAILED_CODE,
                    mUtil.generateDateAndTime().toString(),
                    "/adminLogin"),
                    HttpStatus.UNAUTHORIZED);
        }
        try {
            //validating password via unhashing method
            if(admin.getAuthProvider() == Admin.AuthProvider.local) {
                if(!PasswordEncDec.validatePassword(admin.getPassword(), _admin.getPassword())){
                    LOGGER.error("Requested From: " + request.getRemoteAddr() + " " + StringConstant.ADMIN_LOGIN_FAILED + " :\nPassword Doesn't Match");
                    return new ResponseEntity<>(responseModel.buildResponse(
                            null,
                            StringConstant.ADMIN_LOGIN_FAILED,
                            StringConstant.ADMIN_LOGIN_FAILED_CODE,
                            mUtil.generateDateAndTime().toString(),
                            "/adminLogin"),
                            HttpStatus.UNAUTHORIZED);
                }
                //clearing the password while returning the data to client
                _admin.setPassword("*******");
            }

            LOGGER.info("Requested From: " + request.getRemoteAddr() + " " + StringConstant.ADMIN_LOGIN_SUCCESS + " :\n"+admin.getEmail());
            return new ResponseEntity<>(responseModel.buildResponse(
                    _admin,
                    StringConstant.ADMIN_LOGIN_SUCCESS,
                    StringConstant.ADMIN_LOGIN_SUCCESS_CODE,
                    mUtil.generateDateAndTime().toString(),
                    "/adminLogin"),
                    HttpStatus.OK);
        } catch (NoSuchAlgorithmException e) {
            //Creating response when exception through by unhashing password method
            LOGGER.error( "Requested From: " + request.getRemoteAddr() + StringConstant.UNKNOWN_EXCEPTION + " :\n"+admin.toString());
            return new ResponseEntity<>(responseModel.buildResponse(
                    e.getLocalizedMessage(),
                    StringConstant.UNKNOWN_EXCEPTION,
                    StringConstant.UNKNOWN_EXCEPTION_CODE,
                    mUtil.generateDateAndTime().toString(),
                    "/adminLogin"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (InvalidKeySpecException e) {
            //Creating response when exception through by unhashing password method
            LOGGER.error("Requested From: " + request.getRemoteAddr() + StringConstant.UNKNOWN_EXCEPTION + " :/n"+admin.toString());
            return new ResponseEntity<>(responseModel.buildResponse(
                    e.getLocalizedMessage(),
                    StringConstant.UNKNOWN_EXCEPTION,
                    StringConstant.UNKNOWN_EXCEPTION_CODE,
                    mUtil.generateDateAndTime().toString(),
                    "/adminLogin"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * This method handles query to update Admin Information for Full Name, Other Contacts, Designation,
     * Registration Type & LinkedIn Profile
     * @param admin It should Objectified JSON of Admin type {@link com.karmaya.fulvila.model.Admin}
     * @ param request This parameter is handled automatically by platform
     * @return Check Out the Response Model: {@link com.karmaya.fulvila.model.ResponseModel}
     */

    @PutMapping("/updateAdminInformation")
    public ResponseEntity<ResponseModel> updateAdminInformation(@RequestBody Admin admin, HttpServletRequest request){
        //Mandatory Value Check
        if(admin.getEmail() == null) {
            //Creating Response
            LOGGER.error("Requested From: " + request.getRemoteAddr() + StringConstant.ADMIN_REGISTRATION_FAILED + " due to "+StringConstant.MANDATORY_DATA_NOT_FOUND);
            return new ResponseEntity<>(responseModel.buildResponse(
                    null,
                    StringConstant.MANDATORY_DATA_NOT_FOUND,
                    StringConstant.MANDATORY_DATA_NOT_FOUND_CODE,
                    mUtil.generateDateAndTime().toString(),
                    "/updateAdminInformation"),
                    HttpStatus.BAD_REQUEST);
        }

        Admin _admin = adminRepository.findByEmail(admin.getEmail());

        if(_admin == null) {
            //creating response for admin, which does not exist in Fulvila
            LOGGER.error("Requested From: " + request.getRemoteAddr() + " " + StringConstant.RECHECK_DATA + " : "+admin.getEmail());
            return new ResponseEntity<>(responseModel.buildResponse(
                    _admin,
                    StringConstant.RECHECK_DATA,
                    StringConstant.RECHECK_DATA_CODE,
                    mUtil.generateDateAndTime().toString(),
                    "/updateAdminInformation"),
                    HttpStatus.BAD_REQUEST);
        }

        //Checking for the pass elements to detect field for updates
        if(admin.getName() != null && !admin.getName().equals("")) {
            _admin.setName(admin.getName());
        }

        if(admin.getRegistrationType() != null && admin.getRegistrationType().size() != 0) {
            _admin.setRegistrationType(admin.getRegistrationType());
        }

        if(admin.getOtherContact() != null && admin.getOtherContact().size() != 0) {
            _admin.setOtherContact(admin.getOtherContact());
        }

        if(admin.getLinkdinProfileURL() != null && !admin.getLinkdinProfileURL().equals("")){
            _admin.setLinkdinProfileURL(admin.getLinkdinProfileURL());
        }

        if(admin.getDesignation() != null && !admin.getDesignation().equals("")) {
            _admin.setDesignation(admin.getDesignation());
        }

        if(admin.getAboutAdmin() != null && !admin.getAboutAdmin().equals("")) {
            _admin.setAboutAdmin(admin.getAboutAdmin());
        }
        //Checking Done
        //Saving updated Admin Object
        try {
            //Creating response for successfull Update
            _admin.setLastUpdateTime(mUtil.generateDateAndTime().toString());
            _admin = adminRepository.save(_admin);
            _admin.setPassword(null);
            LOGGER.info("Requested From: " + request.getRemoteAddr() + " " + StringConstant.INFORMATION_UPDATE_SUCCESSFULLY + " for "+_admin.getEmail());
            return new ResponseEntity<>(responseModel.buildResponse(
                    _admin,
                    StringConstant.INFORMATION_UPDATE_SUCCESSFULLY,
                    StringConstant.INFORMATION_UPDATE_SUCCESSFULLY_CODE,
                    mUtil.generateDateAndTime().toString(),
                    "/updateAdminInformation"),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            //Creating response when exception through by unhashing password method
            LOGGER.error("Requested From: " + request.getRemoteAddr() + StringConstant.UNKNOWN_EXCEPTION + " While updating information for "+admin.getEmail());
            return new ResponseEntity<>(responseModel.buildResponse(
                    e.getLocalizedMessage(),
                    StringConstant.UNKNOWN_EXCEPTION,
                    StringConstant.UNKNOWN_EXCEPTION_CODE,
                    mUtil.generateDateAndTime().toString(),
                    "/updateAdminInformation"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //TODO
    @DeleteMapping("/unregisterAdmin")
    public ResponseEntity<ResponseModel> unregisterAdmin(@RequestBody Admin admin, HttpServletRequest request){
        return null;
    }

    //TODO
    @PutMapping("/updatePassword")
    public ResponseEntity<ResponseModel> updatePassword(@RequestBody Admin admin, HttpServletRequest request){
        return null;
    }

    //TODO
    @PutMapping("/updatePrimaryContact")
    public ResponseEntity<ResponseModel> updatePrimaryContact(@RequestBody Admin admin, HttpServletRequest request){
        return null;
    }

    @PutMapping("/statusChange")
    public ResponseEntity<ResponseModel> statusChange(@RequestBody Admin admin, HttpServletRequest request){
        //Mandatory Value Check
        if(admin.getEmail() == null || admin.getEmail().equals("") || admin.getActivationStatus() == null) {
            //Creating Response
            LOGGER.error("Requested From: " + request.getRemoteAddr() + StringConstant.ADMIN_REGISTRATION_FAILED + " due to "+StringConstant.MANDATORY_DATA_NOT_FOUND);
            return new ResponseEntity<>(responseModel.buildResponse(
                    null,
                    StringConstant.MANDATORY_DATA_NOT_FOUND,
                    StringConstant.MANDATORY_DATA_NOT_FOUND_CODE,
                    mUtil.generateDateAndTime().toString(),
                    "/statusChange"),
                    HttpStatus.BAD_REQUEST);
        }

        Admin _admin = adminRepository.findByEmail(admin.getEmail());

        if(_admin == null) {
            //creating response for admin, which does not exist in Fulvila
            LOGGER.error("Requested From: " + request.getRemoteAddr() + " " + StringConstant.RECHECK_DATA + " : "+admin.getEmail());
            return new ResponseEntity<>(responseModel.buildResponse(
                    _admin,
                    StringConstant.RECHECK_DATA,
                    StringConstant.RECHECK_DATA_CODE,
                    mUtil.generateDateAndTime().toString(),
                    "/statusChange"),
                    HttpStatus.BAD_REQUEST);
        }

        //Saving updated Admin Object
        try {
            //updating the timestamp
            _admin.setLastUpdateTime(mUtil.generateDateAndTime().toString());
            //Setting inactive Status
            _admin.setActivationStatus(admin.getActivationStatus());
            _admin = adminRepository.save(_admin);
            _admin.setPassword(null);
            LOGGER.info("Requested From: " + request.getRemoteAddr() + StringConstant.INFORMATION_UPDATE_SUCCESSFULLY + " for "+_admin.getEmail() +" to "+admin.getActivationStatus());
            return new ResponseEntity<>(responseModel.buildResponse(
                    _admin,
                    StringConstant.INFORMATION_UPDATE_SUCCESSFULLY,
                    StringConstant.INFORMATION_UPDATE_SUCCESSFULLY_CODE,
                    mUtil.generateDateAndTime().toString(),
                    "/statusChange"),
                    HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            //Creating response when exception through by unhashing password method
            LOGGER.error("Requested From: " + request.getRemoteAddr() + StringConstant.UNKNOWN_EXCEPTION + " While changing thr status for "+admin.getEmail() +" to "+admin.getActivationStatus());
            return new ResponseEntity<>(responseModel.buildResponse(
                    e.getLocalizedMessage(),
                    StringConstant.UNKNOWN_EXCEPTION,
                    StringConstant.UNKNOWN_EXCEPTION_CODE,
                    mUtil.generateDateAndTime().toString(),
                    "/statusChange"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Method that check against {@code @Valid} Objects passed to controller endpoints
     * @param exception
     * @return a {@code ResponseModel}
     */
    @ExceptionHandler({HttpMessageNotReadableException.class, HttpMessageNotWritableException.class})
    public ResponseEntity<ResponseModel> handleException(HttpMessageNotReadableException exception, HandlerMethod handlerMethod) {
        LOGGER.error("Request failed due to:\n"+exception.getLocalizedMessage());
        return new ResponseEntity(responseModel.buildResponse(
                exception.getMessage(),
                StringConstant.MANDATORY_DATA_NOT_FOUND,
                StringConstant.MANDATORY_DATA_NOT_FOUND_CODE,
                mUtil.generateDateAndTime().toString(),
                "/"+handlerMethod.getMethod().getName()),
                HttpStatus.BAD_REQUEST);
    }
}
