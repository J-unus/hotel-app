package ee.helmes.hotel.service;

import static ee.helmes.hotel.util.Constant.CHECK_IN_TIME;
import static ee.helmes.hotel.util.Constant.CHECK_OUT_TIME;
import static java.time.ZoneOffset.UTC;

import ee.helmes.hotel.domain.Booking;
import ee.helmes.hotel.domain.Room;
import ee.helmes.hotel.domain.RoomPrice;
import ee.helmes.hotel.service.dto.RoomFilter;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
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
        return (root, query, cb) -> {
            Instant instantStart = Clock
                .fixed(roomFilter.getStartDate().atTime(LocalTime.parse(CHECK_IN_TIME)).toInstant(UTC), clock.getZone())
                .instant();
            Instant instantEnd = Clock
                .fixed(roomFilter.getEndDate().atTime(LocalTime.parse(CHECK_OUT_TIME)).toInstant(UTC), clock.getZone())
                .instant();
            String[] priceRangeValues = roomFilter.getPriceRange().split("_");
            Integer roomAmount = roomFilter.getRoomAmount().equals("null") ? null : Integer.parseInt(roomFilter.getRoomAmount());
            List<Predicate> predicates = new ArrayList<>();

            Subquery<Long> bookedRooms = query.subquery(Long.class);
            Root<Booking> bookingRoot = bookedRooms.from(Booking.class);
            bookedRooms
                .select(bookingRoot.get("room").get("id"))
                .where(
                    cb.equal(bookingRoot.get("canceled"), false),
                    cb.lessThan(bookingRoot.get("startAt"), instantEnd),
                    cb.greaterThan(bookingRoot.get("endAt"), instantStart)
                );

            Subquery<Room> roomSubquery = query.subquery(Room.class);
            Root<Room> roomRoot = roomSubquery.from(Room.class);
            Join<Room, RoomPrice> roomRoomPriceJoin = roomRoot.join("roomPrices", JoinType.LEFT);

            if (roomAmount != null) {
                predicates.add(cb.equal(roomRoot.get("roomAmount"), roomAmount));
            }
            if (priceRangeValues.length == 2) {
                int minPrice = Integer.parseInt(priceRangeValues[0]) * 100;
                int maxPrice = Integer.parseInt(priceRangeValues[1]) * 100;
                predicates.add(cb.between(roomRoomPriceJoin.get("oneNightPriceInCents"), minPrice, maxPrice));
            }

            predicates.add(roomRoot.get("id").in(bookedRooms).not());
            roomSubquery.select(roomRoot).where(predicates.toArray(new Predicate[] {}));

            return cb.in(root).value(roomSubquery);
        };
    }
}
