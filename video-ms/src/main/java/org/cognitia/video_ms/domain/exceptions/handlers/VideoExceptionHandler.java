package org.cognitia.video_ms.domain.exceptions.handlers;

import jakarta.servlet.http.HttpServletRequest;
import org.cognitia.video_ms.domain.exceptions.*;
import org.cognitia.video_ms.domain.exceptions.dto.GlobalExceptionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class VideoExceptionHandler {

    @ExceptionHandler(exception = VideoNotFoundException.class)
    public ResponseEntity<GlobalExceptionResponse> videoNotFoundHandler(
            Exception e, HttpServletRequest request
    ){
        GlobalExceptionResponse response = new GlobalExceptionResponse(
                request.getServletPath(),
                404,
                e.getMessage(),
                LocalDateTime.now().toString()
        );

        return ResponseEntity.status(response.status()).body(response);
    }

    @ExceptionHandler(exception = VideoConvertionException.class)
    public ResponseEntity<GlobalExceptionResponse> videoConvertionHandler(
            Exception e, HttpServletRequest request
    ){
        GlobalExceptionResponse response = new GlobalExceptionResponse(
                request.getServletPath(),
                500,
                e.getMessage(),
                LocalDateTime.now().toString()
        );

        return ResponseEntity.status(response.status()).body(response);
    }

    @ExceptionHandler(exception = InvalidVideoUploadException.class)
    public ResponseEntity<GlobalExceptionResponse> videoUploadException(
            Exception e, HttpServletRequest request
    ){
        GlobalExceptionResponse response = new GlobalExceptionResponse(
                request.getServletPath(),
                500,
                e.getMessage(),
                LocalDateTime.now().toString()
        );

        return ResponseEntity.status(response.status()).body(response);
    }

    @ExceptionHandler(exception = InvalidVideoThumbUploadException.class)
    public ResponseEntity<GlobalExceptionResponse> videoThumbUploadException(
            Exception e, HttpServletRequest request
    ){
        GlobalExceptionResponse response = new GlobalExceptionResponse(
                request.getServletPath(),
                400,
                e.getMessage(),
                LocalDateTime.now().toString()
        );

        return ResponseEntity.status(response.status()).body(response);
    }

    @ExceptionHandler(exception = InvalidVideoContentTypeException.class)
    public ResponseEntity<GlobalExceptionResponse> invalidVideoContentTypeHandler(
            Exception e, HttpServletRequest request
    ){
        GlobalExceptionResponse response = new GlobalExceptionResponse(
                request.getServletPath(),
                401,
                e.getMessage(),
                LocalDateTime.now().toString()
        );

        return ResponseEntity.status(response.status()).body(response);
    }


}

