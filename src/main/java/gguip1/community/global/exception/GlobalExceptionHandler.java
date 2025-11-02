package gguip1.community.global.exception;

import gguip1.community.global.response.ApiResponse;
import gguip1.community.global.response.ErrorResponse;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.naming.SizeLimitExceededException;
import java.net.BindException;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ErrorException.class)
    public ResponseEntity<ApiResponse<Void>> handleErrorException(ErrorException e){
        ErrorCode errorCode = e.getErrorCode();

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(ApiResponse.error(errorCode.getCode(), new ErrorResponse(errorCode.getMessage())));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                "유효하지 않은 입력 값입니다."
        );

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error("validation_error", errorResponse));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ApiResponse<Void>> handleMaxSizeException(MaxUploadSizeExceededException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                "파일 크기가 최대 허용 크기(5MB)를 초과했습니다."
        );

        return ResponseEntity
                .status(413)
                .body(ApiResponse.error("file_size_exceeded", errorResponse));
    }

    @ExceptionHandler(SizeLimitExceededException.class)
    public ResponseEntity<ApiResponse<Void>> handleSizeLimitExceededException(SizeLimitExceededException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                "요청 크기가 최대 허용 크기(20MB)를 초과했습니다."
        );

        return ResponseEntity
                .status(413)
                .body(ApiResponse.error("request_size_exceeded", errorResponse));
    }

    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity<ApiResponse<Void>> handleFileSizeLimitExceededException(FileSizeLimitExceededException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                "파일 크기가 최대 허용 크기(5MB)를 초과했습니다."
        );

        return ResponseEntity
                .status(413)
                .body(ApiResponse.error("file_size_exceeded", errorResponse));
    }
}
