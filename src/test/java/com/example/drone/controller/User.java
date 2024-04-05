package com.example.drone.controller;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    String firstName;
    String lastName;
    String email;
}