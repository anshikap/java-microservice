package com.apssouza.eventsourcing.eventstore;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Collections.emptyList;

public interface EventStoreRepository extends JpaRepository<EventStream, Long> {

    Optional<EventStream> findByAggregateUUID(UUID uuid);

    default void saveEvents(UUID aggregateId, List<EventDescriptor> events) {
        final EventStream eventStream = findByAggregateUUID(aggregateId)
                .orElseGet(() -> new EventStream(aggregateId));
        eventStream.addEvents(events);
        save(eventStream);
    }

    default List<EventDescriptor> getEventsForAggregate(UUID aggregateId) {
        return findByAggregateUUID(aggregateId)
                .map(EventStream::getEvents)
                .orElse(emptyList());

    }
    
    default EventStream getAggregate(UUID aggregateId){
        return findByAggregateUUID(aggregateId).get();
    }
}
