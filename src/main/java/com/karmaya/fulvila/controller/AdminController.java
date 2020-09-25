package com.karmaya.fulvila.controller;

import com.karmaya.fulvila.model.Admin;
import com.karmaya.fulvila.model.ResponseModel;
import com.karmaya.fulvila.model.StringConstant;
import com.karmaya.fulvila.repository.AdminRepository;
import com.karmaya.fulvila.service.SequenceGeneratorService;
import com.karmaya.fulvila.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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

    /**
     * This method handles query to register Admin
     * @param admin It should Objectified JSON of Admin type {@link com.karmaya.fulvila.model.Admin}
     * @param request This parameter is handled automatically by platform
     * @return Check Out the Response Model: {@link com.karmaya.fulvila.model.ResponseModel}
     */
    @PostMapping("/addAdmin")
    public ResponseEntity<ResponseModel> addAdmin(@RequestBody Admin admin, HttpServletRequest request) {
        //Null Value Check as pre data validation
        if(admin.getAdminName() == null || admin.getAdminEmail() == null || admin.getAdminPassword() == null) {
            //Creating Response
            responseModel.setObject(null);
            responseModel.setResponseMessage(StringConstant.MANDATORY_DATA_NOT_FOUND);
            responseModel.setResponseCode(StringConstant.MANDATORY_DATA_NOT_FOUND_CODE);
            responseModel.setTimeStamp(mUtil.generateDateAndTime().toString());
            LOGGER.error("Requested From: " + request.getRemoteAddr() + StringConstant.ADMIN_REGISTRATION_FAILED + " due to "+StringConstant.MANDATORY_DATA_NOT_FOUND);
            return new ResponseEntity<>(responseModel,HttpStatus.BAD_REQUEST);
        }

        try {
            //Generation Server Dependent Data
            admin.setId(sequenceGeneratorService.generateSequence(Admin.SEQUENCE_NAME));
            admin.setTimeStamp(mUtil.generateDateAndTime().toString());
            admin.setLastUpdateTime(mUtil.generateDateAndTime().toString());

            //Creating response
            responseModel.setObject(adminRepository.save(admin));
            responseModel.setResponseMessage(StringConstant.SUCCESSFUL_DATA_SUBMISSION);
            responseModel.setResponseCode(StringConstant.SUCCESSFUL_DATA_SUBMISSION_CODE);
            responseModel.setTimeStamp(mUtil.generateDateAndTime().toString());
            LOGGER.info("Requested From: " + request.getRemoteAddr() + " " + StringConstant.ADMIN_REGISTRATION_SUCCESS + " for "+admin.getAdminEmail());
            return new ResponseEntity<>(responseModel,HttpStatus.OK);
        } catch (DuplicateKeyException mwe){
            //Creating response
            responseModel.setObject(mwe.getLocalizedMessage());
            responseModel.setResponseMessage(StringConstant.UNIQUE_CONSTRAINT_EXCEPTION);
            responseModel.setResponseCode(StringConstant.UNIQUE_CONSTRAINT_EXCEPTION_CODE);
            responseModel.setTimeStamp(mUtil.generateDateAndTime().toString());
            LOGGER.error("Requested From: " + request.getRemoteAddr() + StringConstant.UNIQUE_CONSTRAINT_EXCEPTION + " >> "+admin.getAdminEmail());
            return new ResponseEntity<>(responseModel,HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e){
            //Creating response
            responseModel.setObject(e.getLocalizedMessage());
            responseModel.setResponseMessage(StringConstant.UNKNOWN_EXCEPTION);
            responseModel.setResponseCode(StringConstant.UNKNOWN_EXCEPTION_CODE);
            responseModel.setTimeStamp(mUtil.generateDateAndTime().toString());
            LOGGER.error("Requested From: " + request.getRemoteAddr() + StringConstant.UNKNOWN_EXCEPTION + " >> "+admin.getAdminEmail());
            return new ResponseEntity<>(responseModel,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
