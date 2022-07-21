package com.northwest.lms.exceptions;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsEx(BadCredentialsException ex, WebRequest request){
        ApiErrorDetail errorDetail = new ApiErrorDetail(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetail,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileException.class)
    public ResponseEntity<?> handleFileException(FileException ex, WebRequest request){
        ApiErrorDetail errorDetail = new ApiErrorDetail(ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetail,HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotEligibleException.class)
    public ResponseEntity<ApiErrorDetail> handleNotElegibleException(NotEligibleException e, WebRequest request){
        ApiErrorDetail errorDetail = new ApiErrorDetail(e.getMessage(), request.getDescription(true));
        return new ResponseEntity<>(errorDetail,HttpStatus.IM_USED);
    }

    @ExceptionHandler(HODException.class)
    public ResponseEntity<ApiErrorDetail> handleHODException(HODException e, WebRequest request){
        ApiErrorDetail errorDetail = new ApiErrorDetail(e.getMessage(), request.getDescription(true));
        return new ResponseEntity<>(errorDetail,HttpStatus.IM_USED);
    }
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleGeneralException(Exception e, WebRequest request){
       ApiErrorDetail errorDetail = new ApiErrorDetail(e.getMessage(), request.getDescription(true));
        return new ResponseEntity<>(errorDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {IncorrectPasswordException.class})
    public ResponseEntity<Object> handleIncorrectPasswordException(Exception e, WebRequest request){
        ApiErrorDetail errorDetail = new ApiErrorDetail(e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {UserExistsException.class})
    public ResponseEntity<Object> handleUserExistsException(Exception e, WebRequest request){
        ApiErrorDetail errorDetail = new ApiErrorDetail(e.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(errorDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }




}

