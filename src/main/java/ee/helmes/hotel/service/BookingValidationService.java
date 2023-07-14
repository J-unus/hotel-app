package ee.helmes.hotel.service;

import ee.helmes.hotel.service.dto.RoomFilter;
import ee.helmes.hotel.web.rest.errors.ValidationException;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public class BookingValidationService {

    private final Pattern INTEGER_FROM_ONE_TO_THREE_PATTERN = Pattern.compile("[1-3]");
    private static final Integer MAX_BOOKING_TIME_IN_YEARS = 1;
    private static final List<String> VALID_PRICE_RANGES = List.of("0_100", "100_200", "200_300");

    public void validateRoomFilter(RoomFilter roomFilter) {
        validateBookingDateRange(roomFilter.getStartDate(), roomFilter.getEndDate());

        if (
            roomFilter.getRoomAmount() != null &&
            !roomFilter.getRoomAmount().equals("null") &&
            !INTEGER_FROM_ONE_TO_THREE_PATTERN.matcher(roomFilter.getRoomAmount()).matches()
        ) {
            throw new IllegalArgumentException("Room amount is not a valid integer");
        }
        if (
            roomFilter.getPriceRange() != null &&
            !roomFilter.getPriceRange().equals("null") &&
            !VALID_PRICE_RANGES.contains(roomFilter.getPriceRange())
        ) {
            throw new IllegalArgumentException("Price range is not in valid range");
        }
    }

    public void validateBookingDateRange(LocalDate startDate, LocalDate endDate) {
        var minDate = LocalDate.now();
        var maxDate = LocalDate.now().plusYears(MAX_BOOKING_TIME_IN_YEARS);

        if (startDate == null || endDate == null) {
            throw new ValidationException("booking.datesRequired");
        }
        if (startDate.isBefore(minDate)) {
            throw new ValidationException("booking.minDate");
        }
        if (endDate.isAfter(maxDate)) {
            throw new ValidationException("booking.maxDate");
        }
        if (!startDate.isBefore(endDate)) {
            throw new ValidationException("booking.atLeastOneDayLong");
        }
    }
}
