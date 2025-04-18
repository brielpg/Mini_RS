package br.com.minirs.infra;

import br.com.minirs.dto.ExceptionDto;
import br.com.minirs.exceptions.NotFoundException;
import br.com.minirs.exceptions.ResourceAlreadyActiveException;
import br.com.minirs.exceptions.ResourceDisabledException;
import br.com.minirs.exceptions.UnauthorizedException;
import br.com.minirs.exceptions.follow.InvalidFollowRequestException;
import br.com.minirs.exceptions.post.LikedPostsException;
import br.com.minirs.exceptions.user.EmailAlreadyRegisteredException;
import br.com.minirs.exceptions.user.UserNameAlreadyRegisteredException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ExceptionDto> handleUnauthorizedException(UnauthorizedException ex) {
        var dto = new ExceptionDto(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED, ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(dto);
    }

    @ExceptionHandler(InvalidFollowRequestException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ExceptionDto> handleInvalidFollowRequestException(InvalidFollowRequestException ex) {
        var dto = new ExceptionDto(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(dto);
    }

    @ExceptionHandler(LikedPostsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ExceptionDto> handleLikedPostsException(LikedPostsException ex) {
        var dto = new ExceptionDto(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(dto);
    }

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
    @ExceptionHandler(ResourceAlreadyActiveException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ExceptionDto> handleResourceAlreadyActiveException(ResourceAlreadyActiveException ex) {
        var dto = new ExceptionDto(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(dto);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException ex) {
        var dto = new ExceptionDto(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(dto);
    }

    @ExceptionHandler(ResourceDisabledException.class)
    public ResponseEntity<?> handleResourceDisabledException(ResourceDisabledException ex) {
        var dto = new ExceptionDto(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(dto);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ExceptionDto> handleGenericException(Exception ex) {
        var dto = new ExceptionDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(dto);
    }
}
