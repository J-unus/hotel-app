package ee.helmes.hotel.repository;

import ee.helmes.hotel.domain.Booking;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query(" SELECT b" + " FROM Booking b" + " JOIN FETCH b.room r" + " JOIN User u ON b.booker = u" + " WHERE u.email = :userName")
    List<Booking> findByUserName(@Param("userName") String userName);

    @Query(value = " SELECT b from Booking  b JOIN FETCH b.room room", countQuery = " SELECT count(b) FROM Booking b")
    Page<Booking> findAll(Pageable pageable);
}
