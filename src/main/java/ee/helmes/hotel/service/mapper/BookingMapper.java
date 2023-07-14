package ee.helmes.hotel.service.mapper;

import ee.helmes.hotel.domain.Booking;
import ee.helmes.hotel.service.dto.BookingDto;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class BookingMapper {

    private final RoomMapper roomMapper;

    public BookingMapper(RoomMapper roomMapper) {
        this.roomMapper = roomMapper;
    }

    public List<BookingDto> fromEntityToDto(List<Booking> bookings) {
        return bookings.stream().filter(Objects::nonNull).map(this::fromEntityToDto).collect(Collectors.toList());
    }

    public BookingDto fromEntityToDto(Booking booking) {
        if (booking == null) {
            return null;
        }
        BookingDto dto = new BookingDto();
        dto.setRoom(roomMapper.fromEntityToDto(booking.getRoom()));
        dto.setCanceled(booking.isCanceled());
        dto.setStartAt(booking.getStartAt());
        dto.setEndAt(booking.getEndAt());

        return dto;
    }
}
