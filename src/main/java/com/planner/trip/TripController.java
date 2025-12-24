package com.planner.trip;

import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.planner.participant.ParticipantService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/trips")
public class TripController {

	public TripController(ParticipantService participantService, TripRepository tripRepository) {
		this.participantService = participantService;
		this.tripRepository = tripRepository;
	}

	private ParticipantService participantService;
	private TripRepository tripRepository;

	@PostMapping
	public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestPayload payload) {
		Trip newTrip = new Trip(payload);

		this.tripRepository.save(newTrip);

		this.participantService.registerParticipantToTrip(payload.emailsToInvite(), newTrip.getId());

		return ResponseEntity.ok(new TripCreateResponse(newTrip.getId()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id) {
		Optional<Trip> trip = this.tripRepository.findById(id);

		return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}
}
