package org.burningokr.utils;

import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Objects;

public class ErrorMessageExtractor {

  public static String extractMessageFromHandler(MvcResult result) {
    if (HttpStatus.BAD_REQUEST.value() != result.getResponse().getStatus()) return "<extraction failed: response status was not 400>";
    String msg = Objects.requireNonNull(result.getResolvedException()).getMessage().trim().split("default message")[2].trim();
    return msg.substring(1, msg.length() - 3);
  }
}
