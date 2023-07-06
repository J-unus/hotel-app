package ee.helmes.hotel.repository;

import ee.helmes.hotel.domain.Room;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>, JpaSpecificationExecutor<Room> {
    @Query(
        " SELECT count(r) > 0" +
        " FROM Room r" +
        " JOIN Booking b" +
        " WHERE b.canceled = false AND r.id = :roomId AND" +
        "   b.startAt >= :startAt AND b.startAt < :endAt OR" +
        "   b.endAt > :startAt AND b.startAt < :endAt"
    )
    boolean isRoomBooked(@Param("roomId") Long roomId, @Param("startAt") LocalDate startAt, @Param("endAt") LocalDate endAt);
}
