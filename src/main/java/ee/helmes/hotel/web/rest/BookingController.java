package ee.helmes.hotel.web.rest;

import ee.helmes.hotel.security.Role;
import ee.helmes.hotel.service.BookingService;
import ee.helmes.hotel.service.BookingValidationService;
import ee.helmes.hotel.service.dto.BookingCreateDto;
import ee.helmes.hotel.service.dto.BookingPastFutureDto;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public-api/booking")
public class BookingController {

    private final BookingService bookingService;
    private final BookingValidationService bookingValidationService;

    @PreAuthorize(Role.HAS_ROLE_FROM_USER)
    @PostMapping("/book/room/{roomId}")
    public void book(@PathVariable Long roomId, @Valid @RequestBody BookingCreateDto request) {
        bookingValidationService.validateBookingDateRange(request.getStartDate(), request.getEndDate());
        bookingService.book(roomId, request);
    }

    @PreAuthorize(Role.HAS_ROLE_FROM_USER)
    @PostMapping("/bookings/{id}/cancel")
    public void cancel(@PathVariable Long id) {
        bookingService.cancel(id);
    }

    @PreAuthorize(Role.HAS_ROLE_USER)
    @PostMapping("/bookings/{id}/rate/{rating}")
    public void rate(@PathVariable Long id, @PathVariable Integer rating) {
        bookingService.rate(id, rating);
    }

    @PreAuthorize(Role.HAS_ROLE_FROM_USER)
    @GetMapping("/bookings/past-future")
    public BookingPastFutureDto findPastAndFutureBookings() {
        return bookingService.findPastAndFutureBookings();
    }

    @PreAuthorize(Role.HAS_ROLE_ADMIN)
    @GetMapping("/bookings/{roomId}/past-future")
    public BookingPastFutureDto findPastAndFutureBookingsByRoomId(@PathVariable Long roomId) {
        return bookingService.findPastAndFutureBookingsByRoomId(roomId);
    }
}
