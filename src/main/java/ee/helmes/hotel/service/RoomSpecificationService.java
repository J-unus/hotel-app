package ee.helmes.hotel.service;

import static java.time.ZoneOffset.UTC;

import ee.helmes.hotel.domain.Booking;
import ee.helmes.hotel.domain.Room;
import ee.helmes.hotel.service.dto.RoomFilter;
import java.time.Clock;
import java.time.Instant;
import javax.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class RoomSpecificationService {

    private final Clock clock;

    public RoomSpecificationService(Clock clock) {
        this.clock = clock;
    }

    public Specification<Room> getRoomSpecification(RoomFilter roomFilter) {
        return (root, query, criteriaBuilder) -> {
            Instant instantStart = Clock.fixed(roomFilter.getStartDate().atStartOfDay().toInstant(UTC), clock.getZone()).instant();
            Instant instantEnd = Clock.fixed(roomFilter.getEndDate().atStartOfDay().toInstant(UTC), clock.getZone()).instant();
            Subquery<Room> subquery = query.subquery(Room.class);
            Root<Room> roomRoot = subquery.from(Room.class);
            final Join<Room, Booking> roomBookingJoin = roomRoot.join("bookings", JoinType.LEFT);

            subquery
                .select(roomRoot)
                .where(
                    criteriaBuilder.or(
                        criteriaBuilder.isNull(roomBookingJoin),
                        criteriaBuilder.and(
                            criteriaBuilder.lessThanOrEqualTo(roomBookingJoin.get("startAt"), instantStart),
                            criteriaBuilder.greaterThanOrEqualTo(roomBookingJoin.get("endAt"), instantStart)
                        ),
                        criteriaBuilder.and(
                            criteriaBuilder.lessThanOrEqualTo(roomBookingJoin.get("startAt"), instantEnd),
                            criteriaBuilder.greaterThanOrEqualTo(roomBookingJoin.get("endAt"), instantEnd)
                        )
                    )
                );

            return criteriaBuilder.in(root).value(subquery);
        };
    }
}
