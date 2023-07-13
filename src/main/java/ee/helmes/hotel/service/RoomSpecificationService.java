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

            Subquery<Long> bookedRooms = query.subquery(Long.class);
            Root<Booking> bookingRoot = bookedRooms.from(Booking.class);
            bookedRooms
                .select(bookingRoot.get("room").get("id"))
                .where(
                    criteriaBuilder.equal(bookingRoot.get("canceled"), false),
                    criteriaBuilder.lessThan(bookingRoot.get("startAt"), instantEnd),
                    criteriaBuilder.greaterThan(bookingRoot.get("endAt"), instantStart)
                );

            Subquery<Room> roomSubquery = query.subquery(Room.class);
            Root<Room> roomRoot = roomSubquery.from(Room.class);
            roomSubquery.select(roomRoot).where(roomRoot.get("id").in(bookedRooms).not());

            return criteriaBuilder.in(root).value(roomSubquery);
        };
    }
}
