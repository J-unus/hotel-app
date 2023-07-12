package ee.helmes.hotel.service;

import static java.time.ZoneOffset.UTC;

import ee.helmes.hotel.domain.Booking;
import ee.helmes.hotel.domain.Room;
import ee.helmes.hotel.domain.User;
import ee.helmes.hotel.repository.BookingRepository;
import ee.helmes.hotel.repository.RoomRepository;
import ee.helmes.hotel.repository.UserRepository;
import ee.helmes.hotel.security.SecurityUtils;
import ee.helmes.hotel.service.dto.BookingCreateDto;
import ee.helmes.hotel.service.dto.BookingDto;
import ee.helmes.hotel.service.mapper.BookingMapper;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookingService {

    private static final int CANCEL_BOOKING_DEADLINE_IN_DAYS = 3;
    private static final String CHECK_IN_TIME = "15:00";
    private static final String CHECK_OUT_TIME = "13:00";

    private final Clock clock;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    public List<BookingDto> getVisitorBookings() {
        List<Booking> bookings = bookingRepository.findByUserName(SecurityUtils.getCurrentUserLogin().orElseThrow());
        return bookingMapper.fromEntityToDto(bookings);
    }

    public List<BookingDto> findAll() {
        return bookingMapper.fromEntityToDto(bookingRepository.findAll());
    }

    public void book(Long roomId, BookingCreateDto createDto) {
        User user = userRepository.findOneByEmailIgnoreCase(SecurityUtils.getCurrentUserLogin().orElseThrow()).orElseThrow();
        Room room = roomRepository.getReferenceById(roomId);

        if (isRoomBooked(room, createDto)) {
            throw new IllegalArgumentException("Room already booked on requested dates");
        }

        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setRoom(room);
        booking.setStartAt(
            Clock.fixed(createDto.getStartDate().atTime(LocalTime.parse(CHECK_IN_TIME)).toInstant(UTC), clock.getZone()).instant()
        );
        booking.setEndAt(
            Clock.fixed(createDto.getEndDate().atTime(LocalTime.parse(CHECK_OUT_TIME)).toInstant(UTC), clock.getZone()).instant()
        );
        booking.setCanceled(false);

        bookingRepository.save(booking);
    }

    private boolean isRoomBooked(Room room, BookingCreateDto createDto) {
        return roomRepository.isRoomBooked(room.getId(), createDto.getStartDate(), createDto.getEndDate());
    }

    public void cancel(Long bookingId) {
        User user = userRepository.findOneByEmailIgnoreCase(SecurityUtils.getCurrentUserLogin().orElseThrow()).orElseThrow();
        Booking booking = bookingRepository.getReferenceById(bookingId);

        if (!isBookingBooker(booking, user)) {
            throw new IllegalArgumentException("User and booking do not match");
        }

        if (hasExceededCancelDeadline(booking)) {
            throw new IllegalArgumentException("Cancel deadline has exceeded");
        }

        booking.setCanceled(true);

        bookingRepository.save(booking);
    }

    private boolean isBookingBooker(Booking booking, User user) {
        return booking.getBooker().equals(user);
    }

    private boolean hasExceededCancelDeadline(Booking booking) {
        Instant cancelDeadLine = booking.getStartAt().minus(CANCEL_BOOKING_DEADLINE_IN_DAYS, ChronoUnit.DAYS);

        return Instant.now().isAfter(cancelDeadLine);
    }
}
