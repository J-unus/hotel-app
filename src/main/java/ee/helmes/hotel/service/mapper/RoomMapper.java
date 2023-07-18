package ee.helmes.hotel.service.mapper;

import static ee.helmes.hotel.util.Constant.DEFAULT_CURRENCY;

import ee.helmes.hotel.domain.Booking;
import ee.helmes.hotel.domain.Room;
import ee.helmes.hotel.domain.RoomPrice;
import ee.helmes.hotel.service.dto.RoomDto;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class RoomMapper {

    public List<RoomDto> fromEntityToDto(List<Room> rooms) {
        return rooms.stream().filter(Objects::nonNull).map(this::fromEntityToDto).collect(Collectors.toList());
    }

    public RoomDto fromEntityToDto(Room room) {
        if (room == null) {
            return null;
        }
        RoomDto roomDto = new RoomDto();
        roomDto.setId(room.getId());
        roomDto.setRoomAmount(room.getRoomAmount());
        roomDto.setType(room.getType());
        roomDto.setRoomNumber(room.getRoomNumber());
        roomDto.setRoomFacilities(getRoomFacilityNames(room));
        roomDto.setSize(room.getSize());
        roomDto.setAverageRating(getAverageRating(room.getBookings()));

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

    public Double getAverageRating(List<Booking> bookings) {
        OptionalDouble optionalDouble = bookings
            .stream()
            .map(Booking::getRating)
            .filter(Objects::nonNull)
            .mapToInt(Integer::intValue)
            .average();
        if (optionalDouble.isEmpty()) {
            return null;
        }
        return optionalDouble.getAsDouble();
    }
}
