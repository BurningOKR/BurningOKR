package org.burningokr.service.util;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DateMapper {
  public LocalDate mapDateStringToDate(String dateString) {
    var splitString = dateString.split("-");

    return LocalDate.of(Integer.parseInt(splitString[0]), Integer.parseInt(splitString[1]), Integer.parseInt(splitString[2]));
  }
}
