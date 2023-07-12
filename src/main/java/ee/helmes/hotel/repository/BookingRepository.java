package ee.helmes.hotel.repository;

import ee.helmes.hotel.domain.Booking;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query(" SELECT b" + " FROM Booking b" + " JOIN User u" + " WHERE u.email = :userName")
    List<Booking> findByUserName(@Param("userName") String userName);
}
