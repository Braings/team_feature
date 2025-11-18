package com.bridgeX.user.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {

    // @Valid 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidation(MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .sorted((a, b) -> {
                    boolean aNotEmpty = a.getCode().equals("NotEmpty");
                    boolean bNotEmpty = b.getCode().equals("NotEmpty");
                    if (aNotEmpty == bNotEmpty) return 0;
                    return aNotEmpty ? -1 : 1;
                })
                .map(err -> err.getDefaultMessage())
                .findFirst()
                .orElse("잘못된 요청입니다.");

        return ResponseEntity.badRequest().body(message);
    }

    // 서비스에서 던지는 IllegalArgumentException (중복 아이디/이메일 등)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
