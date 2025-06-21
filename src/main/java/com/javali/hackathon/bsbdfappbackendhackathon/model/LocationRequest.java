package com.javali.hackathon.bsbdfappbackendhackathon.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationRequest {
    private double latitude;
    private double longitude;
    private double accuracy;
    private Instant timestamp;
}
