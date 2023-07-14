package ee.helmes.hotel.service.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingPastFutureDto {

    private List<BookingDto> pastBookings = new ArrayList<>();
    private List<BookingDto> futureBookings = new ArrayList<>();
}
