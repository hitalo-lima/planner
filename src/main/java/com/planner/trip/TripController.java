package com.planner.trip;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.planner.activity.ActivityData;
import com.planner.activity.ActivityRequestPayload;
import com.planner.activity.ActivityResponse;
import com.planner.link.LinkData;
import com.planner.link.LinkRequestPayload;
import com.planner.link.LinkResponse;
import com.planner.participant.ParticipantCreateResponse;
import com.planner.participant.ParticipantData;
import com.planner.participant.ParticipantRequestPayload;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/trips")
@Tag(name = "Trip management", description = "APIs for managing trips, including creation, updates, activities, participants, and links.")
public class TripController {

	public TripController(TripService tripService) {
		this.tripService = tripService;
	}

	private final TripService tripService;

	@PostMapping
	@Operation(summary = "Create a new trip", description = "Creates a new trip with the provided details.")
	public ResponseEntity<TripCreateResponse> createTrip(@RequestBody @Valid TripRequestPayload payload) {
		return ResponseEntity.created(null).body(this.tripService.createTrip(payload));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Trip> getTripDetails(@Parameter(description = "ID of the trip") @PathVariable UUID id) {
		return ResponseEntity.ok(this.tripService.findTripById(id));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Trip> updateTrip(@PathVariable UUID id, @RequestBody @Valid TripRequestPayload payload) {
		return ResponseEntity.ok(this.tripService.updateTrip(id, payload));
	}

	@PatchMapping("/{id}/confirm")
	public ResponseEntity<Trip> confirmTrip(@PathVariable UUID id) {
		return ResponseEntity.ok(this.tripService.confirmTrip(id));
	}

	@PostMapping("/{id}/activities")
	public ResponseEntity<ActivityResponse> registerActivity(@PathVariable UUID id,
			@RequestBody @Valid ActivityRequestPayload payload) {
		return ResponseEntity.created(null).body(this.tripService.registerActivity(id, payload));
	}

	@GetMapping("{id}/activities")
	public ResponseEntity<List<ActivityData>> getActivitiesByTrip(@PathVariable UUID id) {
		return ResponseEntity.ok(this.tripService.getActivitiesByTrip(id));
	}

	@PostMapping("/{id}/invite")
	public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable UUID id,
			@Valid @RequestBody ParticipantRequestPayload payload) {
		return ResponseEntity.ok(this.tripService.inviteParticipant(id, payload));
	}

	@GetMapping("{id}/participants")
	public ResponseEntity<List<ParticipantData>> getParticipantsByTrip(@PathVariable UUID id) {
		return ResponseEntity.ok(this.tripService.getParticipantsByTrip(id));
	}

	@PostMapping("/{id}/links")
	public ResponseEntity<LinkResponse> registerLink(@PathVariable UUID id,
			@Valid @RequestBody LinkRequestPayload payload) {
		return ResponseEntity.created(null).body(this.tripService.registerLink(id, payload));
	}

	@GetMapping("{id}/links")
	public ResponseEntity<List<LinkData>> getLinksByTrip(@PathVariable UUID id) {
		return ResponseEntity.ok(this.tripService.getLinksByTrip(id));
	}

}
