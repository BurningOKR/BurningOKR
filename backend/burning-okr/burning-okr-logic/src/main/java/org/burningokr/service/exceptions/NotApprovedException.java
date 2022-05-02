package org.burningokr.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class NotApprovedException extends RuntimeException{
  public NotApprovedException(String error) {
    super(error);
  }
}
