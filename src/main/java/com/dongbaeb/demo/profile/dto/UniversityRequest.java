package com.dongbaeb.demo.profile.dto;

import jakarta.validation.constraints.NotBlank;

public record UniversityRequest(
        @NotBlank String name
) {

}