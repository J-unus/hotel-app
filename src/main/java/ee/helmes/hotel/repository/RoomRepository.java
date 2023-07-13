package ee.helmes.hotel.repository;

import ee.helmes.hotel.domain.Room;
import java.time.Instant;
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
        " JOIN Booking b ON b.room = r" +
        " WHERE r.id = :roomId AND b.canceled = false AND" +
        "    b.startAt < :endAt AND b.endAt > :startAt"
    )
    boolean isRoomBooked(@Param("roomId") Long roomId, @Param("startAt") Instant startAt, @Param("endAt") Instant endAt);
}
