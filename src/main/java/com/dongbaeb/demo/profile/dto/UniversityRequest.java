package com.dongbaeb.demo.profile.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UniversityRequest(
        @NotNull @NotEmpty String name
) {

}