package com.example.student.dto;

import lombok.Data;
import java.math.BigDecimal;
@Data
public class StudentDTO {
    private int id;
    private String firstName;
    private String lastName;
    private int age;
    private BigDecimal marks;

}
