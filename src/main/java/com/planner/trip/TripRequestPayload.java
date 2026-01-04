package com.planner.trip;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;

public record TripRequestPayload(
		@Schema(description = "Destination of the trip", example = "Florianópolis, SC") String destination,
		@Schema(description = "Start date of the trip", example = "2025-01-01T08:00:00.000") LocalDateTime startsAt,
		@Schema(description = "End date of the trip", example = "2025-01-02T21:00:00.000") LocalDateTime endsAt,
		@Schema(description = "Name of the trip owner", example = "José Braga") String ownerName,
		@Email(message = "Invalid email format") @Schema(description = "Email of the trip owner", example = "josebraga@gmail.com") String ownerEmail,
		@Schema(description = "List of emails to invite to the trip") List<@Email(message = "Invalid email format") String> emailsToInvite) {

}
