package org.burningokr.service.dashboard.helper;

import org.burningokr.model.dashboard.LineChartLineKeyValues;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class CompanyProgressHelperTest {
  @Test()
  public void getProgressForCompany_shouldCalculateForNoValues() {
    List<LineChartLineKeyValues> lineChartKeyValues = List.of();

    long numberOfDays = 0;

    LineChartLineKeyValues result = ProgressHelper.getProgressForCompany(lineChartKeyValues, numberOfDays);

    assertEquals(0, result.getData().size());
  }

  @Test()
  public void getProgressForCompany_shouldCalculateForZeroDays() {
    LineChartLineKeyValues value1 = new LineChartLineKeyValues();
    value1.setName("my name");
    value1.setData(List.of(10D, 20D, 30D, 40D));
    List<LineChartLineKeyValues> lineChartKeyValues = List.of(value1);

    long numberOfDays = 0;

    LineChartLineKeyValues result = ProgressHelper.getProgressForCompany(lineChartKeyValues, numberOfDays);

    assertEquals(0, result.getData().size());
  }

  @Test()
  public void getProgressForCompany_shouldCalculateForOneInputLine() {
    LineChartLineKeyValues value1 = new LineChartLineKeyValues();
    value1.setName("my name");
    value1.setData(List.of(10D, 20D, 30D, 40D));
    List<LineChartLineKeyValues> lineChartKeyValues = List.of(value1);

    long numberOfDays = 4;

    LineChartLineKeyValues result = ProgressHelper.getProgressForCompany(lineChartKeyValues, numberOfDays);

    assertEquals(4, result.getData().size());
    assertEquals(10, result.getData().get(0));
    assertEquals(20, result.getData().get(1));
    assertEquals(30, result.getData().get(2));
    assertEquals(40, result.getData().get(3));
  }

  @Test()
  public void getProgressForCompany_shouldCalculateForMultipleInputs() {
    LineChartLineKeyValues value1 = new LineChartLineKeyValues();
    value1.setName("my name");
    value1.setData(List.of(10D, 20D, 30D, 40D));
    LineChartLineKeyValues value2 = new LineChartLineKeyValues();
    value2.setName("my name 2");
    value2.setData(List.of(90D, 80D, 70D, 60D));
    List<LineChartLineKeyValues> lineChartKeyValues = List.of(value1, value2);

    long numberOfDays = 4;

    LineChartLineKeyValues result = ProgressHelper.getProgressForCompany(lineChartKeyValues, numberOfDays);

    assertEquals(4, result.getData().size());
    assertEquals(50, result.getData().get(0));
    assertEquals(50, result.getData().get(1));
    assertEquals(50, result.getData().get(2));
    assertEquals(50, result.getData().get(3));
  }

  @Test()
  public void getProgressForCompany_shouldCalculateComplexValues() {
    LineChartLineKeyValues value1 = new LineChartLineKeyValues();
    value1.setName("my name");
    value1.setData(List.of(10D, 20D, 30D, 40D));
    LineChartLineKeyValues value2 = new LineChartLineKeyValues();
    value2.setName("my name 2");
    value2.setData(List.of(10D, 20D, 30D, 40D));
    LineChartLineKeyValues value3 = new LineChartLineKeyValues();
    value3.setName("my name 3");
    value3.setData(List.of(10D, 20D, 30D, 40D));
    List<LineChartLineKeyValues> lineChartKeyValues = List.of(value1, value2, value3);

    long numberOfDays = 4;

    LineChartLineKeyValues result = ProgressHelper.getProgressForCompany(lineChartKeyValues, numberOfDays);

    assertEquals(4, result.getData().size());
    assertEquals(30D/3, result.getData().get(0));
    assertEquals(60D/3, result.getData().get(1));
    assertEquals(90D/3, result.getData().get(2));
    assertEquals(120D/3, result.getData().get(3));
  }

  @Test()
  public void getProgressForCompany_shouldCalculateValuesMissingNull() {
    LineChartLineKeyValues value1 = new LineChartLineKeyValues();
    value1.setName("my name");
    value1.setData(List.of(10D, 20D));
    LineChartLineKeyValues value2 = new LineChartLineKeyValues();
    value2.setName("my name 2");
    value2.setData(List.of(90D, 80D));
    List<LineChartLineKeyValues> lineChartKeyValues = List.of(value1, value2);

    long numberOfDays = 4;

    LineChartLineKeyValues result = ProgressHelper.getProgressForCompany(lineChartKeyValues, numberOfDays);

    assertEquals(4, result.getData().size());
    assertEquals(50, result.getData().get(0));
    assertEquals(50, result.getData().get(1));
    assertNull(result.getData().get(2));
    assertNull(result.getData().get(3));
  }
  @Test()
  public void getProgressForCompany_shouldCalculateSomeValuesMissingNull() {
    LineChartLineKeyValues value1 = new LineChartLineKeyValues();
    value1.setName("my name");
    value1.setData(List.of(10D, 20D, 30D, 40D));
    LineChartLineKeyValues value2 = new LineChartLineKeyValues();
    value2.setName("my name 2");
    value2.setData(List.of(90D, 80D));
    List<LineChartLineKeyValues> lineChartKeyValues = List.of(value1, value2);

    long numberOfDays = 4;

    LineChartLineKeyValues result = ProgressHelper.getProgressForCompany(lineChartKeyValues, numberOfDays);

    assertEquals(4, result.getData().size());
    assertEquals(50, result.getData().get(0));
    assertEquals(50, result.getData().get(1));
    assertEquals(30, result.getData().get(2));
    assertEquals(40, result.getData().get(3));
  }
}
