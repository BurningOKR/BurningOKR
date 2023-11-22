package org.burningokr.exceptions.handler;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.burningokr.exceptions.InvalidDtoException;
import org.burningokr.exceptions.PutIdConflictException;
import org.burningokr.model.errorHandling.ErrorInformation;
import org.burningokr.service.exceptions.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Global handler for JpaObjectRetrievalFailureException.
   *
   * @return a ResponseEntity with HttpStatus Not Found
   */
  @ExceptionHandler(JpaObjectRetrievalFailureException.class)
  public ResponseEntity<ErrorInformation> handleJpaObjectRetrievalFailure(
    HttpServletRequest request, EntityNotFoundException ex
  ) {
    ErrorInformation errorInformation = new ErrorInformation(ex.getMessage());
    log.error(
      String.format(
        "EntityNotFoundException handler executed -> HTTP 404 response - ID: %s, %s",
        errorInformation.getErrorId(),
        ex)
    ); // <- ex im log, für stacktrace
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorInformation);
  }

  /**
   * Global handler for EntityNotFoundException.
   *
   * @return a ResponseEntity with HttpStatus Not Found
   */
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorInformation> handleNotFoundException(
    HttpServletRequest request, EntityNotFoundException ex
  ) {
    ErrorInformation errorInformation = new ErrorInformation(ex.getMessage());
    log.error(
      String.format(
        "EntityNotFoundException handler executed -> HTTP 404 response - ID: %s, %s",
        errorInformation.getErrorId(),
        ex)
    ); // <- ex im log, für stacktrace
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorInformation);
  }

  /**
   * Global handler for IllegalArgumentException.
   *
   * @return a ResponseEntity with HttpStatus Bad Request
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorInformation> handleIllegalArgumentException(
    HttpServletRequest request, IllegalArgumentException ex
  ) {
    ErrorInformation errorInformation = new ErrorInformation(ex.getMessage());
    log.error(
      String.format(
        "IllegalArgumentException handler executed -> HTTP 400 response - ID: %s, %s",
        errorInformation.getErrorId(),
        ex)
    ); // <- ex im log, für stacktrace
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorInformation);
  }

  /**
   * Global handler for InvalidDeleteRequestException.
   *
   * @return a ResponseEntity with HttpStatus Bad Request
   */
  @ExceptionHandler(InvalidDeleteRequestException.class)
  public ResponseEntity<ErrorInformation> handleInvalidDeleteRequestException(
    HttpServletRequest request, InvalidDeleteRequestException ex
  ) {
    ErrorInformation errorInformation = new ErrorInformation(ex.getMessage());
    log.error(
      String.format(
        "IllegalArgumentException handler executed -> HTTP 400 response - ID: %s, %s",
        errorInformation.getErrorId(),
        ex)
    ); // <- ex im log, für stacktrace
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorInformation);
  }

  /**
   * Global handler for InvalidDtoException.
   *
   * @return a ResponseEntity with HttpStatus Bad Request
   */
  @ExceptionHandler(InvalidDtoException.class)
  public ResponseEntity<ErrorInformation> handleInvalidDtoException(
    HttpServletRequest request, InvalidDtoException ex
  ) {
    ErrorInformation errorInformation = new ErrorInformation(ex.getMessage());
    log.error(
      String.format(
        "InvalidDtoException handler executed -> HTTP 400 response - ID: %s, %s",
        errorInformation.getErrorId(),
        ex)
    ); // <- ex im log, für stacktrace
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorInformation);
  }

  /**
   * Global handler for InvalidEmailAddressException.
   *
   * @return a ResponseEntity with HttpStatus Bad Request
   */
  @ExceptionHandler(InvalidEmailAddressException.class)
  public ResponseEntity<ErrorInformation> handleInvalidEmailAddressException(
    HttpServletRequest request, InvalidEmailAddressException ex
  ) {
    ErrorInformation errorInformation = new ErrorInformation(ex.getMessage());
    log.error(
      String.format(
        "InvalidEmailAddressException handler executed -> HTTP 400 response - ID: %s, %s",
        errorInformation.getErrorId(),
        ex)
    ); // <- ex im log, für stacktrace
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorInformation);
  }

  /**
   * Global handler for KeyResultOverflowException.
   *
   * @return a ResponseEntity with HttpStatus Bad Request
   */
  @ExceptionHandler(KeyResultOverflowException.class)
  public ResponseEntity<ErrorInformation> handleKeyResultOverflowException(
    HttpServletRequest request, KeyResultOverflowException ex
  ) {
    ErrorInformation errorInformation = new ErrorInformation(ex.getMessage());
    log.error(
      String.format(
        "KeyResultOverflowException handler executed -> HTTP 400 response - ID: %s, %s",
        errorInformation.getErrorId(),
        ex)
    ); // <- ex im log, für stacktrace
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorInformation);
  }

  /**
   * Global handler for PutIdConflictException.
   *
   * @return a ResponseEntity with HttpStatus Bad Request
   */
  @ExceptionHandler(PutIdConflictException.class)
  public ResponseEntity<ErrorInformation> handlePutIdConflictException(
    HttpServletRequest request, PutIdConflictException ex
  ) {
    ErrorInformation errorInformation = new ErrorInformation(ex.getMessage());
    log.error(
      String.format(
        "PutIdConflictException handler executed -> HTTP 400 response - ID: %s, %s",
        errorInformation.getErrorId(),
        ex)
    ); // <- ex im log, für stacktrace
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorInformation);
  }

  /**
   * Global handler for AccessDeniedException.
   *
   * @return a ResponseEntity with HttpStatus Unauthorized
   */
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ErrorInformation> handleUnauthorizedAccessException(
    HttpServletRequest request, RuntimeException ex
  ) {
    ErrorInformation errorInformation = new ErrorInformation(ex.getMessage());
    log.error(
      String.format(
        "AccessDeniedException handler executed -> HTTP 403 response - ID: %s, %s",
        errorInformation.getErrorId(),
        ex)
    ); // <- ex im log, für stacktrace
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorInformation);
  }

  /**
   * Global handler for UnauthorizedToChangeNoteException.
   *
   * @return a ResponseEntity with HttpStatus Internal Server Error
   */
  @ExceptionHandler(UnauthorizedToChangeNoteException.class)
  public ResponseEntity<ErrorInformation> handleUnauthorizedToChangeNoteException(
    HttpServletRequest request, RuntimeException ex
  ) {
    ErrorInformation errorInformation = new ErrorInformation(ex.getMessage());
    log.error(
      String.format(
        "UnauthorizedToChangeNoteException handler executed -> HTTP 500 response - ID: %s, %s",
        errorInformation.getErrorId(),
        ex)
    ); // <- ex im log, für stacktrace
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorInformation);
  }

  /**
   * Global handler for MissingAnnotationException.
   *
   * @return a ResponseEntity with HttpStatus Internal Server Error
   */
  @ExceptionHandler(MissingAnnotationException.class)
  public ResponseEntity<ErrorInformation> handleMissingAnnotationException(
    HttpServletRequest request, RuntimeException ex
  ) {
    ErrorInformation errorInformation = new ErrorInformation(ex.getMessage());
    log.error(
      String.format(
        "MissingAnnotationException handler executed -> HTTP 500 response - ID: %s, %s",
        errorInformation.getErrorId(),
        ex)
    ); // <- ex im log, für stacktrace
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorInformation);
  }

  /**
   * Global handler for IdDeviationException.
   *
   * @return a ResponseEntity with HttpStatus Bad Request
   */
  @ExceptionHandler(IdDeviationException.class)
  public ResponseEntity<ErrorInformation> handleIdDeviationException(
    HttpServletRequest request, RuntimeException ex
  ) {
    ErrorInformation errorInformation = new ErrorInformation(ex.getMessage());
    log.error(
      String.format(
        "IdDeviationException handler executed -> HTTP 400 response - ID: %s, %s",
        errorInformation.getErrorId(),
        ex)
    ); // <- ex im log, für stacktrace
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorInformation);
  }

  /**
   * Global handler for ForbiddenException.
   *
   * @return a ResponseEntity with HttpStatus Forbidden
   */
  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity<ErrorInformation> handleForbiddenException(HttpServletRequest request, RuntimeException ex) {
    ErrorInformation errorInformation = new ErrorInformation(ex.getMessage());
    log.error(
      String.format(
        "ForbiddenException handler executed -> HTTP 403 response - ID: %s, %s",
        errorInformation.getErrorId(),
        ex)
    ); // <- ex im log, für stacktrace
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorInformation);
  }

  /**
   * Global handler for SendingMailFailedException.
   *
   * @return a ResponseEntity with HttpStatus Internal Server Error
   */
  @ExceptionHandler(SendingMailFailedException.class)
  public ResponseEntity<ErrorInformation> handleSendingMailFailedException(
    HttpServletRequest request, SendingMailFailedException ex
  ) {
    ErrorInformation errorInformation = new ErrorInformation(ex.getMessage());
    log.error(
      String.format(
        "SendingMailFailedException handler executed -> HTTP 500 response - ID: %s, %s",
        errorInformation.getErrorId(),
        ex)
    ); // <- ex im log, für stacktrace
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorInformation);
  }

  /**
   * Global handler for RuntimeException.
   *
   * @return a ResponseEntity with HttpStatus Internal Server Error
   */
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ErrorInformation> handleRuntimeException(HttpServletRequest request, RuntimeException ex) {
    ErrorInformation errorInformation = new ErrorInformation(ex.getMessage());
    log.error(
      String.format(
        "RuntimeException handler executed -> HTTP 500 response - ID: %s, %s",
        errorInformation.getErrorId(),
        ex)
    ); // <- ex im log, für stacktrace
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorInformation);
  }

  /**
   * Global handler for InvalidInitStateException.
   *
   * @return a ResponseEntity with HttpStatus Bad Request
   */
  @ExceptionHandler(InvalidInitStateException.class)
  public ResponseEntity<ErrorInformation> handleInvalidInitStateException(
    HttpServletRequest request, InvalidInitStateException ex
  ) {
    ErrorInformation errorInformation = new ErrorInformation(ex.getMessage());
    log.warn(
      String.format(
        "InvalidInitStateException handler executed -> HTTP 400 response - ID: %s, %s",
        errorInformation.getErrorId(),
        ex)
    ); // <- ex im log, für stacktrace
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorInformation);
  }

  /**
   * Global handler for DuplicateTeamMemberException.
   *
   * @return a ResponseEntity with HttpStatus Bad Request
   */
  @ExceptionHandler(DuplicateTeamMemberException.class)
  public ResponseEntity<ErrorInformation> handleDuplicateTeamMemberException(
    HttpServletRequest request, DuplicateTeamMemberException ex
  ) {
    ErrorInformation errorInformation = new ErrorInformation(ex.getMessage());
    log.warn(
      String.format(
        "DuplicateTeamMemberException handler executed -> HTTP 400 response - ID: %s, %s",
        errorInformation.getErrorId(),
        ex)
    ); // <- ex im log, für stacktrace
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorInformation);
  }

  /**
   * Global handler for DuplicateEmailException.
   *
   * @return a ResponseEntity with HttpStatus Bad Request
   */
  @ExceptionHandler(DuplicateEmailException.class)
  public ResponseEntity<ErrorInformation> handleDuplicateEmailException(
    HttpServletRequest request, DuplicateEmailException ex
  ) {
    ErrorInformation errorInformation = new ErrorInformation(ex.getMessage());
    log.warn(
      String.format(
        "DuplicateEmailException handler executed -> HTTP 400 response - ID: %s, %s",
        errorInformation.getErrorId(),
        ex)
    ); // <- ex im log, für stacktrace
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorInformation);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
    List<String> errors = ex.getBindingResult().getFieldErrors()
            .stream().map(this::constructErrorMessage).collect(Collectors.toList());
    return new ResponseEntity<>(createErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
  }

  private String constructErrorMessage(FieldError error) {
    return String.format(
      "Regarding field '%s.%s': %s",
      error.getObjectName(),
      error.getField(),
      error.getDefaultMessage());
  }

  private Map<String, List<String>> createErrorsMap(List<String> errors) {
    Map<String, List<String>> errorResponse = new HashMap<>();
    errorResponse.put("errors", errors);
    return errorResponse;
  }

}
