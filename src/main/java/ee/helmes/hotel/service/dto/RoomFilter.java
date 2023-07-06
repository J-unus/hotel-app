package ee.helmes.hotel.service.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomFilter {

    private LocalDate startDate;
    private LocalDate endDate;
    private Integer bedAmount;
}
