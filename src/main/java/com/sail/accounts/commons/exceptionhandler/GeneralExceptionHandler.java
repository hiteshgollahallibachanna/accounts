package com.sail.accounts.commons.exceptionhandler;

import com.sail.accounts.commons.controller.BaseController;
import com.sail.accounts.commons.exceptions.UnAuthorizedException;
import com.sail.accounts.commons.exceptions.ValidationException;
import com.sail.accounts.commons.model.Error;
import com.sail.accounts.commons.model.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
@Component
public class GeneralExceptionHandler extends BaseController {
    private final Logger logger = LoggerFactory.getLogger(GeneralExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    ResponseEntity<Object> handleGlobalException(Exception ex) {
        logger.error("exception : {0}", ex);
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setMessage("request could not be processed");
        errorResponse.setCode("gle1");
        return createResponse(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleValidationExceptions(ConstraintViolationException validationException) {
        logger.error("exception : {0}", validationException);
        Set<ConstraintViolation<?>> constraintViolations = validationException.getConstraintViolations();

        List<Error> errors = constraintViolations.stream().map(f -> new Error(f.getPropertyPath().toString(), f.getMessage())).collect(Collectors.toList());

        return createErrorResponse(HttpStatus.BAD_REQUEST, "input parameter invalid", "br1", errors);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<Object> handleUnAuthorizedException(UnAuthorizedException unAuthorizedException) {
        logger.error("exception : {0}", unAuthorizedException);

        return createErrorResponse(HttpStatus.UNAUTHORIZED, unAuthorizedException.getMessage(), "br1", new ArrayList<>());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Object> handleValidationException(ValidationException validationException) {
        logger.error("exception : {0}", validationException);

        return createErrorResponse(HttpStatus.BAD_REQUEST, validationException.getMessage(), "br1", new ArrayList<>());
    }

}
