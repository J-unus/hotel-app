package ee.helmes.hotel.service.dto;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingDto {

    private Long id;
    private RoomDto room;
    private Instant startAt;
    private Instant endAt;
    private boolean canceled;
    private AdminUserDto booker;
}
