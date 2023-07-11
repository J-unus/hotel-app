package ee.helmes.hotel.service.mapper;

import static ee.helmes.hotel.util.Constant.DEFAULT_CURRENCY;

import ee.helmes.hotel.domain.Room;
import ee.helmes.hotel.domain.RoomPrice;
import ee.helmes.hotel.service.dto.RoomDto;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class RoomMapper {

    public RoomDto fromEntityToDto(Room room) {
        if (room == null) {
            return null;
        }
        RoomDto roomDto = new RoomDto();
        roomDto.setId(room.getId());
        roomDto.setRoomAmount(room.getRoomAmount());
        roomDto.setDescription(room.getDescription());
        roomDto.setRoomFacilities(getRoomFacilityNames(room));
        roomDto.setSize(room.getSize());

        Optional<RoomPrice> roomPriceOptional = getRoomPrice(room);
        if (roomPriceOptional.isPresent()) {
            roomDto.setCurrency(roomPriceOptional.get().getCurrency());
            roomDto.setOneNightPriceInCents(roomPriceOptional.get().getOneNightPriceInCents());
        }

        return roomDto;
    }

    private List<String> getRoomFacilityNames(Room room) {
        return room.getFacilities().stream().map(roomFacility -> roomFacility.getFacility().getName()).collect(Collectors.toList());
    }

    private Optional<RoomPrice> getRoomPrice(Room room) {
        return room.getRoomPrices().stream().filter(roomPrice -> roomPrice.getCurrency().equals(DEFAULT_CURRENCY)).findFirst();
    }
}
