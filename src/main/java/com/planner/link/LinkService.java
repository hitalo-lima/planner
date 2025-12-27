package com.planner.link;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.planner.trip.Trip;

@Service
public class LinkService {

    public LinkService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    private final LinkRepository linkRepository;

    public LinkResponse registerLinks(LinkRequestPayload payload, Trip trip) {
        Link newLink = new Link(payload.title(), payload.url(), trip);

        this.linkRepository.save(newLink);

        return new LinkResponse(newLink.getId());
    }

    public List<LinkData> getAllLinksByTrip(UUID tripId) {
        return this.linkRepository.findByTripId(tripId).stream()
                .map(link -> new LinkData(
                        link.getId(),
                        link.getTitle(),
                        link.getUrl()))
                .toList();
    }

}
