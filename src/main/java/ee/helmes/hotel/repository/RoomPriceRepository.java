package ee.helmes.hotel.repository;

import ee.helmes.hotel.domain.RoomPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomPriceRepository extends JpaRepository<RoomPrice, Long> {}
