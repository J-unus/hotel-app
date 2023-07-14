package ee.helmes.hotel.web.rest;

import ee.helmes.hotel.security.Role;
import ee.helmes.hotel.service.BookingService;
import ee.helmes.hotel.service.BookingValidationService;
import ee.helmes.hotel.service.dto.BookingCreateDto;
import ee.helmes.hotel.service.dto.BookingDto;
import ee.helmes.hotel.service.dto.BookingPastFutureDto;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public-api/booking")
public class BookingController {

    private final BookingService bookingService;
    private final BookingValidationService bookingValidationService;

    @PreAuthorize(Role.HAS_ROLE_USER)
    @GetMapping("/bookings/visitor")
    public List<BookingDto> getVisitorBookings() {
        return bookingService.getVisitorBookings();
    }

    @PreAuthorize(Role.HAS_ROLE_ADMIN)
    @GetMapping("/bookings/employee")
    public Page<BookingDto> getBookingList(@RequestParam int page, @RequestParam int size) {
        return bookingService.findAll(PageRequest.of(page, size));
    }

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

    @PreAuthorize(Role.HAS_ROLE_FROM_USER)
    @GetMapping("/bookings/past-future")
    public BookingPastFutureDto findPastAndFutureBookings() {
        return bookingService.findPastAndFutureBookings();
    }
}
