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

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/trips")
public class TripController {

	public TripController(TripService tripService) {
		this.tripService = tripService;
	}

	private final TripService tripService;

	@PostMapping
	public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestPayload payload) {
		return ResponseEntity.created(null).body(this.tripService.createTrip(payload));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id) {
		return ResponseEntity.ok(this.tripService.findTripById(id));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Trip> updateTrip(@PathVariable UUID id, @RequestBody TripRequestPayload payload) {
		return ResponseEntity.ok(this.tripService.updateTrip(id, payload));
	}

	@PatchMapping("/{id}/confirm")
	public ResponseEntity<Trip> confirmTrip(@PathVariable UUID id) {
		return ResponseEntity.ok(this.tripService.confirmTrip(id));
	}

	@PostMapping("/{id}/activities")
	public ResponseEntity<ActivityResponse> registerActivity(@PathVariable UUID id,
			@RequestBody ActivityRequestPayload payload) {
		return ResponseEntity.created(null).body(this.tripService.registerActivity(id, payload));
	}

	@GetMapping("{id}/activities")
	public ResponseEntity<List<ActivityData>> getActivitiesByTrip(@PathVariable UUID id) {
		return ResponseEntity.ok(this.tripService.getActitiviesByTrip(id));
	}

	@PostMapping("/{id}/invite")
	public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable UUID id,
			@RequestBody ParticipantRequestPayload payload) {
		return ResponseEntity.ok(this.tripService.inviteParticipant(id, payload));
	}

	@GetMapping("{id}/participants")
	public ResponseEntity<List<ParticipantData>> getParticipantsByTrip(@PathVariable UUID id) {
		return ResponseEntity.ok(this.tripService.getParticipantsByTrip(id));
	}

	@PostMapping("/{id}/links")
	public ResponseEntity<LinkResponse> registerLink(@PathVariable UUID id,
			@RequestBody LinkRequestPayload payload) {
		return ResponseEntity.created(null).body(this.tripService.registerLink(id, payload));
	}

	@GetMapping("{id}/links")
	public ResponseEntity<List<LinkData>> getLinksByTrip(@PathVariable UUID id) {
		return ResponseEntity.ok(this.tripService.getLinksByTrip(id));
	}

}
