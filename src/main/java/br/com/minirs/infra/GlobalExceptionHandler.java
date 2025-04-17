package br.com.minirs.infra;

import br.com.minirs.dto.ExceptionDto;
import br.com.minirs.exceptions.user.EmailAlreadyRegisteredException;
import br.com.minirs.exceptions.user.UserDisabledException;
import br.com.minirs.exceptions.user.UserNameAlreadyRegisteredException;
import br.com.minirs.exceptions.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ExceptionDto> handleEmailAlreadyRegisteredException(EmailAlreadyRegisteredException ex) {
        var dto = new ExceptionDto(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(dto);
    }

    @ExceptionHandler(UserNameAlreadyRegisteredException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ExceptionDto> handleUserNameAlreadyRegisteredException(UserNameAlreadyRegisteredException ex) {
        var dto = new ExceptionDto(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(dto);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex) {
        var dto = new ExceptionDto(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dto);
    }

    @ExceptionHandler(UserDisabledException.class)
    public ResponseEntity<?> handleUserDisabledException(UserDisabledException ex) {
        var dto = new ExceptionDto(HttpStatus.FORBIDDEN.value(), HttpStatus.FORBIDDEN, ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(dto);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionDto> handleGenericException(Exception ex) {
        var dto = new ExceptionDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto);
    }
}
