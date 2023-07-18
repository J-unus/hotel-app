package ee.helmes.hotel.service;

import static ee.helmes.hotel.util.Constant.*;
import static java.time.ZoneOffset.UTC;

import ee.helmes.hotel.domain.Booking;
import ee.helmes.hotel.domain.Room;
import ee.helmes.hotel.domain.User;
import ee.helmes.hotel.repository.BookingRepository;
import ee.helmes.hotel.repository.RoomRepository;
import ee.helmes.hotel.repository.UserRepository;
import ee.helmes.hotel.security.AuthoritiesConstants;
import ee.helmes.hotel.security.SecurityUtils;
import ee.helmes.hotel.service.dto.BookingCreateDto;
import ee.helmes.hotel.service.dto.BookingPastFutureDto;
import ee.helmes.hotel.service.mapper.BookingMapper;
import ee.helmes.hotel.web.rest.errors.ValidationException;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final Clock clock;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    public void book(Long roomId, BookingCreateDto createDto) {
        User user = userRepository.findOneByEmailIgnoreCase(SecurityUtils.getCurrentUserLogin().orElseThrow()).orElseThrow();
        Room room = roomRepository.getReferenceById(roomId);
        Instant startDate = Clock
            .fixed(createDto.getStartDate().atTime(LocalTime.parse(CHECK_IN_TIME)).toInstant(UTC), clock.getZone())
            .instant();
        Instant endDate = Clock
            .fixed(createDto.getEndDate().atTime(LocalTime.parse(CHECK_OUT_TIME)).toInstant(UTC), clock.getZone())
            .instant();

        if (isRoomBooked(room, startDate, endDate)) {
            throw new ValidationException("booking.alreadyBooked");
        }

        Booking booking = new Booking();
        booking.setBooker(user);
        booking.setRoom(room);
        booking.setStartAt(startDate);
        booking.setEndAt(endDate);
        booking.setCanceled(false);

        bookingRepository.save(booking);
    }

    private boolean isRoomBooked(Room room, Instant startDate, Instant endDate) {
        return roomRepository.isRoomBooked(room.getId(), startDate, endDate);
    }

    @Transactional
    public void cancel(Long bookingId) {
        User user = userRepository.findOneByEmailIgnoreCase(SecurityUtils.getCurrentUserLogin().orElseThrow()).orElseThrow();
        Booking booking = bookingRepository.getReferenceById(bookingId);

        if (!isBookingBooker(booking, user) && !SecurityUtils.hasCurrentUserThisAuthority(AuthoritiesConstants.ADMIN)) {
            throw new IllegalArgumentException("User and booking do not match");
        }
        if (booking.isCanceled()) {
            throw new IllegalArgumentException("Booking already canceled");
        }
        if (hasExceededCancelDeadline(booking)) {
            throw new ValidationException("booking.cancelDeadline");
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

    @Transactional(readOnly = true)
    public BookingPastFutureDto findPastAndFutureBookings() {
        BookingPastFutureDto bookingPastFutureDto = new BookingPastFutureDto();
        List<Booking> bookings = bookingRepository.findByUserName(SecurityUtils.getCurrentUserLogin().orElseThrow());
        bookings.forEach(booking -> {
            Instant today = Instant.now();
            if (booking.getEndAt().isBefore(today) || booking.isCanceled()) {
                bookingPastFutureDto.getPastBookings().add(bookingMapper.fromEntityToDto(booking));
            } else {
                bookingPastFutureDto.getFutureBookings().add(bookingMapper.fromEntityToDto(booking));
            }
        });
        return bookingPastFutureDto;
    }

    @Transactional(readOnly = true)
    public BookingPastFutureDto findPastAndFutureBookingsByRoomId(Long roomId) {
        BookingPastFutureDto bookingPastFutureDto = new BookingPastFutureDto();
        List<Booking> bookings = bookingRepository.findByRoomId(roomId);
        bookings.forEach(booking -> {
            Instant today = Instant.now();
            if (booking.getEndAt().isBefore(today) || booking.isCanceled()) {
                bookingPastFutureDto.getPastBookings().add(bookingMapper.fromEntityToDtoWithAdminUser(booking));
            } else {
                bookingPastFutureDto.getFutureBookings().add(bookingMapper.fromEntityToDtoWithAdminUser(booking));
            }
        });
        return bookingPastFutureDto;
    }

    @Transactional
    public void rate(Long id, Integer rating) {
        User user = userRepository.findOneByEmailIgnoreCase(SecurityUtils.getCurrentUserLogin().orElseThrow()).orElseThrow();
        Booking booking = bookingRepository.getReferenceById(id);

        if (!isBookingBooker(booking, user)) {
            throw new IllegalArgumentException("User and booking do not match");
        }
        if (booking.isCanceled()) {
            throw new IllegalArgumentException("Cannot rate canceled booking");
        }

        booking.setRating(rating);
        bookingRepository.save(booking);
    }
}
