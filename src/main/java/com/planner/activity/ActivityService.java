package com.planner.activity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.planner.trip.Trip;
import com.planner.utils.DateUtils;

@Service
public class ActivityService {

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    private final ActivityRepository activityRepository;

    public ActivityResponse registerActivity(ActivityRequestPayload payload, Trip trip) {
        Activity newActivity = new Activity(payload.title(), payload.occursAt(), trip);

        LocalDateTime activityDate = DateUtils.parseISODateTime(payload.occursAt());

        String formattedStartsAt = DateUtils.formatDateTime(trip.getStartsAt());
        String formattedEndsAt = DateUtils.formatDateTime(trip.getEndsAt());

        if (!DateUtils.isDateBetween(activityDate, trip.getStartsAt(), trip.getEndsAt())) {
            throw new IllegalArgumentException("A atividade deve ocorrer dentro do período da viagem: "
                    + formattedStartsAt + " - " + formattedEndsAt);
        }

        this.activityRepository.save(newActivity);

        return new ActivityResponse(newActivity.getId());
    }

    public List<ActivityData> getAllActivitiesByTrip(UUID tripId) {
        return this.activityRepository.findByTripId(tripId).stream()
                .map(activity -> new ActivityData(
                        activity.getId(),
                        activity.getTitle(),
                        activity.getOccursAt().toString()))
                .toList();
    }
}
