package org.burningokr.controller.exceptions;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import org.burningokr.exceptions.InvalidDtoException;
import org.burningokr.service.exceptions.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  /**
   * Global handler for AzureUserFetchExceptions.
   *
   * @param request HttpServletRequest
   * @param ex AzureUserFetchException
   * @return a ResponseEntity with HttpStatus Bad Request
   */
  @ExceptionHandler(AzureUserFetchException.class)
  public ResponseEntity handleAzureUserFetchException(
      HttpServletRequest request, AzureUserFetchException ex) {
    ErrorInformation errorInformation = new ErrorInformation(ex.getMessage());
    logger.error(
        "UserList could not be fetched from Azure -> HTTP 500 response - ID: "
            + errorInformation.getErrorId(),
        ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorInformation);
  }

  /**
   * Global handler for AzureAPiException.
   *
   * @param request HttpServletRequest
   * @param ex AzureUserFetchException
   * @return a ResponseEntity with HttpStatus Bad Request
   */
  @ExceptionHandler(AzureApiException.class)
  public ResponseEntity handleAzureApiException(
      HttpServletRequest request, AzureUserFetchException ex) {
    ErrorInformation errorInformation = new ErrorInformation(ex.getMessage());
    logger.error(
        "Error while calling azure api -> HTTP 500 response - ID: " + errorInformation.getErrorId(),
        ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorInformation);
  }

  /**
   * Global handler for JpaObjectRetrivalFailureExecption.
   *
   * @param request HttpServletRequest
   * @param ex EntityNotFoundException
   * @return a ResponseEntity with HttpStatus Not Found
   */
  @ExceptionHandler(JpaObjectRetrievalFailureException.class)
  public ResponseEntity handleJpaObjectRetrievalFailure(
      HttpServletRequest request, EntityNotFoundException ex) {
    ErrorInformation errorInformation = new ErrorInformation(ex.getMessage());
    logger.error(
        "EntityNotFoundException handler executed -> HTTP 404 response - ID: "
            + errorInformation.getErrorId(),
        ex); // <- ex im log, für stacktrace
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorInformation);
  }

  /**
   * Global handler for EntityNotFoundException.
   *
   * @param request HttpServletRequest
   * @param ex EntityNotFoundException
   * @return a ResponseEntity with HttpStatus Not Found
   */
  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity handleNotFoundException(
      HttpServletRequest request, EntityNotFoundException ex) {
    ErrorInformation errorInformation = new ErrorInformation(ex.getMessage());
    logger.error(
        "EntityNotFoundException handler executed -> HTTP 404 response - ID: "
            + errorInformation.getErrorId(),
        ex); // <- ex im log, für stacktrace
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorInformation);
  }

  /**
   * Global handler for IllegalArgumentException.
   *
   * @param request HttpServletRequest
   * @param ex IllegalArgumentException
   * @return a ResponseEntity with HttpStatus Bad Request
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity handleIllegalArgumentExeption(
      HttpServletRequest request, IllegalArgumentException ex) {
    ErrorInformation errorInformation = new ErrorInformation(ex.getMessage());
    logger.error(
        "IllegalArgumentException handler executed -> HTTP 400 response - ID: "
            + errorInformation.getErrorId(),
        ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorInformation);
  }

  /**
   * Global handler for InvalidDeleteRequestException.
   *
   * @param request HttpServletRequest
   * @param ex InvalidDeleteRequestException
   * @return a ResponseEntity with HttpStatus Bad Request
   */
  @ExceptionHandler(InvalidDeleteRequestException.class)
  public ResponseEntity handleInvalidDeleteRequestExeption(
      HttpServletRequest request, InvalidDeleteRequestException ex) {
    ErrorInformation errorInformation = new ErrorInformation(ex.getMessage());
    logger.error(
        "IllegalArgumentException handler executed -> HTTP 400 response - ID: "
            + errorInformation.getErrorId(),
        ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorInformation);
  }

  /**
   * Global handler for InvalidDtoException.
   *
   * @param request HttpServletRequest
   * @param ex InvalidDtoException
   * @return a ResponseEntity with HttpStatus Bad Request
   */
  @ExceptionHandler(InvalidDtoException.class)
  public ResponseEntity handleInvalidDtoException(
      HttpServletRequest request, InvalidDtoException ex) {
    ErrorInformation errorInformation = new ErrorInformation(ex.getMessage());
    logger.error(
        "InvalidDtoException handler executed -> HTTP 400 response - ID: "
            + errorInformation.getErrorId(),
        ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorInformation);
  }

  /**
   * Global handler for InvalidEmailAddressException.
   *
   * @param request HttpServletRequest
   * @param ex InvalidEmailAddressException
   * @return a ResponseEntity with HttpStatus Bad Request
   */
  @ExceptionHandler(InvalidEmailAddressException.class)
  public ResponseEntity handleInvalidEmailAdressException(
      HttpServletRequest request, InvalidEmailAddressException ex) {
    ErrorInformation errorInformation = new ErrorInformation(ex.getMessage());
    logger.error(
        "InvalidEmailAddressException handler executed -> HTTP 400 response - ID: "
            + errorInformation.getErrorId(),
        ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorInformation);
  }

  /**
   * Global handler for KeyResultOverflowException.
   *
   * @param request HttpServletRequest
   * @param ex KeyResultOverflowException
   * @return a ResponseEntity with HttpStatus Bad Request
   */
  @ExceptionHandler(KeyResultOverflowException.class)
  public ResponseEntity handleKeyRessultOverflowExeption(
      HttpServletRequest request, KeyResultOverflowException ex) {
    ErrorInformation errorInformation = new ErrorInformation(ex.getMessage());
    logger.error(
        "KeyResultOverflowException handler executed -> HTTP 400 response - ID: "
            + errorInformation.getErrorId(),
        ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorInformation);
  }

  /**
   * Global handler for PutIdConflictException.
   *
   * @param request HttpServletRequest
   * @param ex PutIdConflictException
   * @return a ResponseEntity with HttpStatus Bad Request
   */
  @ExceptionHandler(PutIdConflictException.class)
  public ResponseEntity handlePutIdConflictException(
      HttpServletRequest request, PutIdConflictException ex) {
    ErrorInformation errorInformation = new ErrorInformation(ex.getMessage());
    logger.error(
        "PutIdConflictException handler executed -> HTTP 400 response - ID: "
            + errorInformation.getErrorId(),
        ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorInformation);
  }

  /**
   * Global handler for AccessDeniedException.
   *
   * @param request HttpServletRequest
   * @param ex RuntimeException
   * @return a ResponseEntity with HttpStatus Unauthorized
   */
  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity handleUnauthorizedAccessException(
      HttpServletRequest request, RuntimeException ex) {
    ErrorInformation errorInformation = new ErrorInformation(ex.getMessage());
    logger.error(
        "AccessDeniedException handler executed -> HTTP 403 response - ID: "
            + errorInformation.getErrorId(),
        ex);
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorInformation);
  }

  /**
   * Global handler for UnauthorizedToChangeNoteException.
   *
   * @param request HttpServletRequest
   * @param ex RuntimeException
   * @return a ResponseEntity with HttpStatus Internal Server Error
   */
  @ExceptionHandler(UnauthorizedToChangeNoteException.class)
  public ResponseEntity handleUnauthorizedToChangeNoteException(
      HttpServletRequest request, RuntimeException ex) {
    ErrorInformation errorInformation = new ErrorInformation(ex.getMessage());
    logger.error(
        "UnauthorizedToChangeNoteException handler executed -> HTTP 500 response - ID: "
            + errorInformation.getErrorId(),
        ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorInformation);
  }

  /**
   * Global handler for MissingAnnotationException.
   *
   * @param request HttpServletRequest
   * @param ex RuntimeException
   * @return a ResponseEntity with HttpStatus Internal Server Error
   */
  @ExceptionHandler(MissingAnnotationException.class)
  public ResponseEntity handleMissingAnnotationException(
      HttpServletRequest request, RuntimeException ex) {
    ErrorInformation errorInformation = new ErrorInformation(ex.getMessage());
    logger.error(
        "MissingAnnotationException handler excecuted -> HTTP 500 response - ID: "
            + errorInformation.getErrorId(),
        ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorInformation);
  }

  /**
   * Global handler for IdDeviationException.
   *
   * @param request HttpServletRequest
   * @param ex RuntimeException
   * @return a ResponseEntity with HttpStatus Bad Request
   */
  @ExceptionHandler(IdDeviationException.class)
  public ResponseEntity handleIdDeviationException(
      HttpServletRequest request, RuntimeException ex) {
    ErrorInformation errorInformation = new ErrorInformation(ex.getMessage());
    logger.error(
        "IdDeviationException handler executed -> HTTP 400 response - ID: "
            + errorInformation.getErrorId(),
        ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorInformation);
  }

  /**
   * Global handler for ForbiddenException.
   *
   * @param request HttpServletRequest
   * @param ex RuntimeException
   * @return a ResponseEntity with HttpStatus Forbidden
   */
  @ExceptionHandler(ForbiddenException.class)
  public ResponseEntity handleForbiddenException(HttpServletRequest request, RuntimeException ex) {
    ErrorInformation errorInformation = new ErrorInformation(ex.getMessage());
    logger.error(
        "ForbiddenException handler executed -> HTTP 403 response - ID: "
            + errorInformation.getErrorId(),
        ex);
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorInformation);
  }

  /**
   * Global handler for SendingMailFailedException.
   *
   * @param request HttpServletRequest
   * @param ex SendingMailFailedException
   * @return a ResponseEntity with HttpStatus Internal Server Error
   */
  @ExceptionHandler(SendingMailFailedException.class)
  public ResponseEntity handleSendingMailFailedException(
      HttpServletRequest request, SendingMailFailedException ex) {
    ErrorInformation errorInformation = new ErrorInformation(ex.getMessage());
    logger.error(
        "SendingMailFailedException handler executed -> HTTP 500 response - ID: "
            + errorInformation.getErrorId(),
        ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorInformation);
  }

  /**
   * Global handler for RuntimeException.
   *
   * @param request HttpServletRequest
   * @param ex RuntimeException
   * @return a ResponseEntity with HttpStatus Internal Server Error
   */
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity handleRuntimeExeption(HttpServletRequest request, RuntimeException ex) {
    ErrorInformation errorInformation = new ErrorInformation(ex.getMessage());
    logger.error(
        "RuntimeException handler executed -> HTTP 500 response - ID: "
            + errorInformation.getErrorId(),
        ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorInformation);
  }

  /**
   * Global handler for InvalidInitStateException.
   *
   * @param request HttpServletRequest
   * @param ex InvalidInitStateException
   * @return a ResponseEntity with HttpStatus Bad Request
   */
  @ExceptionHandler(InvalidInitStateException.class)
  public ResponseEntity handleInvalidInitStateException(
      HttpServletRequest request, InvalidInitStateException ex) {
    ErrorInformation errorInformation = new ErrorInformation(ex.getMessage());
    logger.error(
        "InvalidInitStateException handler executed -> HTTP 400 response - ID: "
            + errorInformation.getErrorId(),
        ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorInformation);
  }

  /**
   * Global handler for DuplicateTeamMemberException.
   *
   * @param request HttpServletRequest
   * @param ex InvalidInitStateException
   * @return a ResponseEntity with HttpStatus Bad Request
   */
  @ExceptionHandler(DuplicateTeamMemberException.class)
  public ResponseEntity handleDuplicateTeamMemberException(
      HttpServletRequest request, DuplicateTeamMemberException ex) {
    ErrorInformation errorInformation = new ErrorInformation(ex.getMessage());
    logger.error(
        "DuplicateTeamMemberException handler executed -> HTTP 400 response - ID: "
            + errorInformation.getErrorId(),
        ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorInformation);
  }
}
