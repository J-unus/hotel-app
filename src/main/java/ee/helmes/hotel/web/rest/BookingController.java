package ee.helmes.hotel.web.rest;

import ee.helmes.hotel.security.Role;
import ee.helmes.hotel.service.BookingService;
import ee.helmes.hotel.service.dto.BookingCreateDto;
import ee.helmes.hotel.service.dto.BookingDto;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public-api/booking")
public class BookingController {

    private final BookingService bookingService;

    @PreAuthorize(Role.HAS_ROLE_USER)
    @GetMapping("/bookings/visitor")
    public List<BookingDto> getVisitorBookings() {
        return bookingService.getVisitorBookings();
    }

    @PreAuthorize(Role.HAS_ROLE_ADMIN)
    @GetMapping("/bookings/employee")
    public List<BookingDto> getBookingList() {
        return bookingService.findAll();
    }

    @PreAuthorize(Role.HAS_ROLE_FROM_USER)
    @PostMapping("/book/room/{roomId}")
    public void book(@PathVariable Long roomId, @Valid @RequestBody BookingCreateDto request) {
        bookingService.book(roomId, request);
    }

    @PreAuthorize(Role.HAS_ROLE_FROM_USER)
    @PostMapping("/bookings/{id}/cancel")
    public void cancel(@PathVariable Long id) {
        bookingService.cancel(id);
    }
}
