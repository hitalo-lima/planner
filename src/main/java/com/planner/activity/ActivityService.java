package com.planner.activity;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.planner.trip.Trip;

@Service
public class ActivityService {

    public ActivityService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    private final ActivityRepository activityRepository;

    public ActivityResponse registerActivity(ActivityRequestPayload payload, Trip trip) {
        Activity newActivity = new Activity(payload.title(), payload.occursAt(), trip);

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
