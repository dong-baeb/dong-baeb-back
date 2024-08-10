package com.dongbaeb.demo.profile.dto;

import jakarta.validation.constraints.NotBlank;

public record UniversityRequest(
        @NotBlank(message = "대학명은 필수 항목입니다.")
        String name
) {

}
