package ee.helmes.hotel.service;

import static ee.helmes.hotel.util.Constant.CHECK_IN_TIME;
import static ee.helmes.hotel.util.Constant.CHECK_OUT_TIME;
import static java.time.ZoneOffset.UTC;

import ee.helmes.hotel.domain.Booking;
import ee.helmes.hotel.domain.Room;
import ee.helmes.hotel.service.dto.RoomFilter;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalTime;
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
            Instant instantStart = Clock
                .fixed(roomFilter.getStartDate().atTime(LocalTime.parse(CHECK_IN_TIME)).toInstant(UTC), clock.getZone())
                .instant();
            Instant instantEnd = Clock
                .fixed(roomFilter.getEndDate().atTime(LocalTime.parse(CHECK_OUT_TIME)).toInstant(UTC), clock.getZone())
                .instant();
            Subquery<Room> subquery = query.subquery(Room.class);
            Root<Room> roomRoot = subquery.from(Room.class);
            final Join<Room, Booking> roomBookingJoin = roomRoot.join("bookings", JoinType.LEFT);

            subquery
                .select(roomRoot)
                .where(
                    criteriaBuilder.or(
                        criteriaBuilder.isNull(roomBookingJoin),
                        criteriaBuilder.or(
                            criteriaBuilder.equal(roomBookingJoin.get("canceled"), true),
                            criteriaBuilder
                                .and(
                                    criteriaBuilder.lessThan(roomBookingJoin.get("startAt"), instantEnd),
                                    criteriaBuilder.greaterThan(roomBookingJoin.get("endAt"), instantStart)
                                )
                                .not()
                        )
                    )
                );

            return criteriaBuilder.in(root).value(subquery);
        };
    }
}
