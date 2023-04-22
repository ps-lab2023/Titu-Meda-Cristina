package com.nagarro.af.bookingtablesystem.exception.handler;

import com.nagarro.af.bookingtablesystem.exception.CorruptedFileException;
import com.nagarro.af.bookingtablesystem.exception.NotFoundException;
import jakarta.annotation.Nullable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiException> handleNotFoundException(Exception ex) {
        return getResponseEntity(ex, HttpStatus.NOT_FOUND);
    }

    @Override
    @Nullable
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                               @NonNull HttpHeaders headers,
                                                               @NonNull HttpStatusCode status,
                                                               @NonNull WebRequest request) {
        ApiException apiException = new ApiException(HttpStatus.BAD_REQUEST);
        ex.getBindingResult().getFieldErrors().forEach((error) -> insertErrorMessages(apiException, error));

        return new ResponseEntity<>(apiException, apiException.getStatus());
    }

    public ResponseEntity<ApiException> getResponseEntity(Exception exception, HttpStatus httpStatus) {
        ApiException apiException = new ApiException(httpStatus);
        apiException.setMessage(exception.getMessage());
        apiException.setTimestamp(LocalDateTime.now());
        return new ResponseEntity<>(apiException, apiException.getStatus());
    }

    public void insertErrorMessages(ApiException apiException, FieldError errorField) {
        String errorMessage = errorField.getDefaultMessage();
        apiException.setMessage(errorMessage);
        apiException.setTimestamp(LocalDateTime.now());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiException> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        ApiException apiException = new ApiException(HttpStatus.EXPECTATION_FAILED, "File too large!");
        return new ResponseEntity<>(apiException, apiException.getStatus());
    }

    @ExceptionHandler(CorruptedFileException.class)
    public ResponseEntity<ApiException> handleCorruptedFileException(CorruptedFileException ex) {
        ApiException apiException = new ApiException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getMessage());
        return new ResponseEntity<>(apiException, apiException.getStatus());
    }
}
