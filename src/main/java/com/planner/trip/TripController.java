package com.planner.trip;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.planner.activity.ActivityData;
import com.planner.activity.ActivityRequestPayload;
import com.planner.activity.ActivityResponse;
import com.planner.activity.ActivityService;
import com.planner.participant.ParticipantCreateResponse;
import com.planner.participant.ParticipantData;
import com.planner.participant.ParticipantRequestPayload;
import com.planner.participant.ParticipantService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/trips")
public class TripController {

	public TripController(ParticipantService participantService, TripRepository tripRepository,
			ActivityService activityService) {
		this.participantService = participantService;
		this.tripRepository = tripRepository;
		this.activityService = activityService;
	}

	private final ParticipantService participantService;
	private final TripRepository tripRepository;
	private final ActivityService activityService;

	@PostMapping
	public ResponseEntity<TripCreateResponse> createTrip(@RequestBody TripRequestPayload payload) {
		Trip newTrip = new Trip(payload);

		this.tripRepository.save(newTrip);

		this.participantService.registerParticipantsToTrip(payload.emailsToInvite(), newTrip);

		return ResponseEntity.created(null).body(new TripCreateResponse(newTrip.getId()));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Trip> getTripDetails(@PathVariable UUID id) {
		Optional<Trip> trip = this.tripRepository.findById(id);

		return trip.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}")
	public ResponseEntity<Trip> updateTrip(@PathVariable UUID id, @RequestBody TripRequestPayload payload) {
		Optional<Trip> trip = this.tripRepository.findById(id);

		if (trip.isPresent()) {
			Trip rawTrip = trip.get();
			rawTrip.setStartsAt(LocalDateTime.parse(payload.startsAt(), DateTimeFormatter.ISO_DATE_TIME));
			rawTrip.setEndsAt(LocalDateTime.parse(payload.endsAt(), DateTimeFormatter.ISO_DATE_TIME));
			rawTrip.setDestination(payload.destination());

			this.tripRepository.save(rawTrip);

			return ResponseEntity.ok(rawTrip);
		}

		return ResponseEntity.notFound().build();
	}

	@PatchMapping("/{id}/confirm")
	public ResponseEntity<Trip> confirmTrip(@PathVariable UUID id) {
		Optional<Trip> trip = this.tripRepository.findById(id);

		if (trip.isPresent()) {
			Trip rawTrip = trip.get();

			if (!rawTrip.isConfirmed()) {
				rawTrip.setConfirmed(true);

				this.tripRepository.save(rawTrip);
				this.participantService.triggerConfirmationEmailToParticipants(id);
			}

			return ResponseEntity.ok(rawTrip);
		}

		return ResponseEntity.notFound().build();
	}

	@PostMapping("/{id}/invite")
	public ResponseEntity<ParticipantCreateResponse> inviteParticipant(@PathVariable UUID id,
			@RequestBody ParticipantRequestPayload payload) {
		Optional<Trip> trip = this.tripRepository.findById(id);

		if (trip.isPresent()) {
			Trip rawTrip = trip.get();

			ParticipantCreateResponse participantResponse = this.participantService
					.registerParticipantToTrip(payload.email(), rawTrip);

			if (rawTrip.isConfirmed())
				this.participantService.triggerConfirmationEmailToParticipant(payload.email());

			return ResponseEntity.ok(participantResponse);
		}

		return ResponseEntity.notFound().build();
	}

	@GetMapping("{id}/participants")
	public ResponseEntity<List<ParticipantData>> getParticipantsByTrip(@PathVariable UUID id) {
		Optional<Trip> trip = this.tripRepository.findById(id);

		if (trip.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		List<ParticipantData> participantList = this.participantService.getAllParticipantsByTrip(id);

		return ResponseEntity.ok(participantList);
	}

	@PostMapping("/{id}/activities")
	public ResponseEntity<ActivityResponse> registerActivity(@PathVariable UUID id,
			@RequestBody ActivityRequestPayload payload) {
		Optional<Trip> trip = this.tripRepository.findById(id);

		if (trip.isPresent()) {
			Trip rawTrip = trip.get();

			ActivityResponse activityResponse = this.activityService
					.registerActivity(payload, rawTrip);

			return ResponseEntity.created(null).body(activityResponse);
		}

		return ResponseEntity.notFound().build();
	}

	@GetMapping("{id}/activities")
	public ResponseEntity<List<ActivityData>> getActivitiesByTrip(@PathVariable UUID id) {
		Optional<Trip> trip = this.tripRepository.findById(id);

		if (trip.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		List<ActivityData> activityList = this.activityService.getAllActivitiesByTrip(id);

		return ResponseEntity.ok(activityList);
	}

}
