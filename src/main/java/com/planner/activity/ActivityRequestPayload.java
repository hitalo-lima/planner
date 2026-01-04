package com.planner.activity;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

public record ActivityRequestPayload(@Schema(description = "Title of the activity", example = "Hiking") String title,
        @Schema(description = "Date and time of the activity", example = "2025-01-01T10:00:00.000") LocalDateTime occursAt) {

}
