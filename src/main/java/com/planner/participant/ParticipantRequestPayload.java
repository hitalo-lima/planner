package com.planner.participant;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

public record ParticipantRequestPayload(
        @Schema(description = "Name of the participant", example = "John Doe") String name,
        @Email(message = "Invalid email format") @Schema(description = "Email of the participant", example = "john.doe@example.com") String email) {

}
