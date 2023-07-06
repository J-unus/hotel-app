package ee.helmes.hotel.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomPriceDto {

    private Integer oneNightPriceInCents;
    private String currency;
}
