package com.planner.trip;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.planner.activity.ActivityData;
import com.planner.activity.ActivityRequestPayload;
import com.planner.activity.ActivityResponse;
import com.planner.activity.ActivityService;
import com.planner.exceptions.ResourceNotFoundException;
import com.planner.link.LinkData;
import com.planner.link.LinkRequestPayload;
import com.planner.link.LinkResponse;
import com.planner.link.LinkService;
import com.planner.participant.ParticipantCreateResponse;
import com.planner.participant.ParticipantData;
import com.planner.participant.ParticipantRequestPayload;
import com.planner.participant.ParticipantService;
import com.planner.utils.DateUtils;

@Service
public class TripService {
    public TripService(TripRepository tripRepository, ParticipantService participantService,
            ActivityService activityService, LinkService linkService) {
        this.tripRepository = tripRepository;
        this.participantService = participantService;
        this.activityService = activityService;
        this.linkService = linkService;
    }

    private final TripRepository tripRepository;
    private final ParticipantService participantService;
    private final ActivityService activityService;
    private final LinkService linkService;

    public Trip findTripById(UUID id) {
        return this.tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trip not found"));
    }

    public TripCreateResponse createTrip(TripRequestPayload payload) {
        Trip newTrip = new Trip(payload);

        final LocalDateTime startDateFormatted = DateUtils.parseISODateTime(payload.startsAt());
        final LocalDateTime endDateFormatted = DateUtils.parseISODateTime(payload.endsAt());

        if (endDateFormatted.isBefore(startDateFormatted)) {
            throw new IllegalArgumentException("End date must be after start date.");
        }

        this.tripRepository.save(newTrip);

        this.participantService.registerParticipantsToTrip(payload.emailsToInvite(), newTrip);

        return new TripCreateResponse(newTrip.getId());
    }

    public Trip updateTrip(UUID id, TripRequestPayload payload) {
        Optional<Trip> trip = this.tripRepository.findById(id);

        if (!trip.isPresent())
            throw new ResourceNotFoundException("Trip not found.");

        Trip rawTrip = trip.get();

        rawTrip.setStartsAt(DateUtils.parseISODateTime(payload.startsAt()));
        rawTrip.setEndsAt(DateUtils.parseISODateTime(payload.endsAt()));
        rawTrip.setDestination(payload.destination());

        this.tripRepository.save(rawTrip);

        return rawTrip;
    }

    public Trip confirmTrip(UUID id) {
        Optional<Trip> trip = this.tripRepository.findById(id);

        if (!trip.isPresent())
            throw new ResourceNotFoundException("Trip not found.");

        Trip rawTrip = trip.get();

        if (!rawTrip.isConfirmed()) {
            rawTrip.setConfirmed(true);

            this.tripRepository.save(rawTrip);
            this.participantService.triggerConfirmationEmailToParticipants(id);
        }

        return rawTrip;
    }

    public ActivityResponse registerActivity(UUID tripId, ActivityRequestPayload payload) {
        Optional<Trip> trip = this.tripRepository.findById(tripId);

        if (!trip.isPresent())
            throw new ResourceNotFoundException("Trip not found.");

        Trip rawTrip = trip.get();

        ActivityResponse activityResponse = this.activityService
                .registerActivity(payload, rawTrip);
        return activityResponse;
    }

    public List<ActivityData> getActitiviesByTrip(UUID tripId) {
        Optional<Trip> trip = this.tripRepository.findById(tripId);

        if (!trip.isPresent())
            throw new ResourceNotFoundException("Trip not found.");

        return this.activityService.getAllActivitiesByTrip(tripId);
    }

    public ParticipantCreateResponse inviteParticipant(UUID tripId, ParticipantRequestPayload payload) {
        Optional<Trip> trip = this.tripRepository.findById(tripId);

        if (!trip.isPresent())
            throw new ResourceNotFoundException("Trip not found.");

        Trip rawTrip = trip.get();

        ParticipantCreateResponse participantResponse = this.participantService
                .registerParticipantToTrip(payload.email(), rawTrip);

        if (rawTrip.isConfirmed())
            this.participantService.triggerConfirmationEmailToParticipant(payload.email());

        return participantResponse;
    }

    public List<ParticipantData> getParticipantsByTrip(UUID tripId) {
        Optional<Trip> trip = this.tripRepository.findById(tripId);

        if (!trip.isPresent())
            throw new ResourceNotFoundException("Trip not found.");

        return this.participantService.getAllParticipantsByTrip(tripId);
    }

    public LinkResponse registerLink(UUID tripId, LinkRequestPayload payload) {
        Optional<Trip> trip = this.tripRepository.findById(tripId);

        if (!trip.isPresent())
            throw new ResourceNotFoundException("Trip not found.");

        Trip rawTrip = trip.get();

        LinkResponse linkResponse = this.linkService
                .registerLinks(payload, rawTrip);

        return linkResponse;
    }

    public List<LinkData> getLinksByTrip(UUID tripId) {
        Optional<Trip> trip = this.tripRepository.findById(tripId);

        if (!trip.isPresent())
            throw new ResourceNotFoundException("Trip not found.");

        return this.linkService.getAllLinksByTrip(tripId);
    }
}
