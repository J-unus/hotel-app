package ee.helmes.hotel.service;

import ee.helmes.hotel.domain.Room;
import ee.helmes.hotel.service.dto.RoomFilter;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class RoomSpecificationService {

    public Specification<Room> getRoomSpecification(RoomFilter roomFilter) { //TODO Not service?
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("roomAmount"), roomFilter.getBedAmount()));
            predicates.add(criteriaBuilder.notEqual(root.join("bookings", JoinType.LEFT).get("canceled"), true));
            predicates.add(
                criteriaBuilder.or(
                    criteriaBuilder.and(
                        criteriaBuilder.lessThanOrEqualTo(root.join("bookings", JoinType.LEFT).get("startAt"), roomFilter.getStartDate()),
                        criteriaBuilder.greaterThanOrEqualTo(root.join("bookings", JoinType.LEFT).get("endAt"), roomFilter.getStartDate())
                    ),
                    criteriaBuilder.and(
                        criteriaBuilder.lessThanOrEqualTo(root.join("bookings", JoinType.LEFT).get("startAt"), roomFilter.getEndDate()),
                        criteriaBuilder.greaterThanOrEqualTo(root.join("bookings", JoinType.LEFT).get("endAt"), roomFilter.getEndDate())
                    )
                )
            );

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
