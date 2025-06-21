package com.javali.hackathon.bsbdfappbackendhackathon.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Component(
        String type,
        List<Parameter> parameters,
        @JsonProperty("sub_type") String subType,
        String index
) {}