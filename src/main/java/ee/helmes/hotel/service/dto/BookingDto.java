package ee.helmes.hotel.service.dto;

import ee.helmes.hotel.domain.Room;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingDto {

    private Room room;
    private Instant startAt;
    private Instant endAt;
    private boolean canceled;
}
