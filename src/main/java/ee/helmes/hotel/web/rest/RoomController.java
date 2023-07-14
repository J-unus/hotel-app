package ee.helmes.hotel.web.rest;

import ee.helmes.hotel.service.BookingValidationService;
import ee.helmes.hotel.service.RoomService;
import ee.helmes.hotel.service.dto.RoomDto;
import ee.helmes.hotel.service.dto.RoomFilter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public-api/room/rooms")
public class RoomController {

    private final RoomService roomService;
    private final BookingValidationService bookingValidationService;

    @GetMapping
    public ResponseEntity<List<RoomDto>> query(@ParameterObject Pageable pageable, @ParameterObject RoomFilter roomFilter) {
        bookingValidationService.validateRoomFilter(roomFilter);
        final Page<RoomDto> page = roomService.query(pageable, roomFilter);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public RoomDto getById(@PathVariable Long id) {
        return roomService.getById(id);
    }
}
