package ee.helmes.hotel.service.dto;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoomDto {

    private Long id;
    private Integer roomAmount;
    private List<RoomPriceDto> roomPrices;
}
