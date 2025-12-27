package com.planner.participant;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.planner.trip.Trip;

@Service
public class ParticipantService {

    public ParticipantService(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    private final ParticipantRepository participantRepository;

    public void registerParticipantsToTrip(List<String> participantsToInvite, Trip trip) {
        List<Participant> participants = participantsToInvite.stream().map(email -> new Participant(email, trip))
                .toList();

        this.participantRepository.saveAll(participants);
    }

    public ParticipantCreateResponse registerParticipantToTrip(String email, Trip trip) {
        Participant newParticipant = new Participant(email, trip);

        this.participantRepository.save(newParticipant);

        return new ParticipantCreateResponse(newParticipant.getId());
    }

    public void triggerConfirmationEmailToParticipants(UUID tripId) {
    }

    public void triggerConfirmationEmailToParticipant(String email) {

    }

    public List<ParticipantData> getAllParticipantsByTrip(UUID tripId) {
        return this.participantRepository.findByTripId(tripId).stream()
                .map(participant -> new ParticipantData(participant.getId(), participant.getName(),
                        participant.getEmail(), participant.isConfirmed()))
                .toList();
    }
}
