package ee.helmes.hotel.service.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingCreateDto {

    private LocalDate startDate;
    private LocalDate endDate;
}
