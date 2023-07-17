package ee.helmes.hotel.service;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import ee.helmes.hotel.domain.Booking;
import ee.helmes.hotel.domain.Room;
import ee.helmes.hotel.domain.User;
import ee.helmes.hotel.repository.BookingRepository;
import ee.helmes.hotel.repository.RoomRepository;
import ee.helmes.hotel.repository.UserRepository;
import ee.helmes.hotel.security.AuthoritiesConstants;
import ee.helmes.hotel.service.dto.BookingCreateDto;
import ee.helmes.hotel.service.mapper.BookingMapper;
import ee.helmes.hotel.web.rest.errors.ValidationException;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

class BookingServiceUnitTest {

    private static final String CURRENT_USER_EMAIL = "user@example.com";
    private static final String CHECK_IN_TIME = "14:00:00";
    private static final String CHECK_OUT_TIME = "12:00:00";
    private static final ZoneOffset UTC = ZoneOffset.UTC;

    @Mock
    private Clock clock;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingMapper bookingMapper;

    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookingService = new BookingService(clock, userRepository, roomRepository, bookingRepository, bookingMapper);
    }

    @BeforeEach
    @AfterEach
    void setUpContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void book_ValidBooking_NoExceptionThrown() {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.USER));
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(CURRENT_USER_EMAIL, "user", authorities));
        SecurityContextHolder.setContext(securityContext);
        Long roomId = 1L;
        BookingCreateDto createDto = new BookingCreateDto();
        createDto.setStartDate(LocalDate.now().plusDays(1));
        createDto.setEndDate(LocalDate.now().plusDays(2));

        User user = new User();
        user.setEmail(CURRENT_USER_EMAIL);
        when(userRepository.findOneByEmailIgnoreCase(eq(CURRENT_USER_EMAIL))).thenReturn(Optional.of(user));

        Room room = new Room();
        room.setId(roomId);
        when(roomRepository.getReferenceById(eq(roomId))).thenReturn(room);

        Instant startDate = LocalDateTime.of(createDto.getStartDate(), LocalTime.parse(CHECK_IN_TIME)).toInstant(UTC);
        Instant endDate = LocalDateTime.of(createDto.getEndDate(), LocalTime.parse(CHECK_OUT_TIME)).toInstant(UTC);
        when(clock.getZone()).thenReturn(UTC);
        when(clock.instant()).thenReturn(Instant.now());

        when(roomRepository.isRoomBooked(eq(room.getId()), eq(startDate), eq(endDate))).thenReturn(false);

        Assertions.assertDoesNotThrow(() -> bookingService.book(roomId, createDto));

        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void book_RoomAlreadyBooked_ThrowsValidationException() {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.USER));
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(CURRENT_USER_EMAIL, "user", authorities));
        SecurityContextHolder.setContext(securityContext);
        Long roomId = 1L;
        BookingCreateDto createDto = new BookingCreateDto();
        createDto.setStartDate(LocalDate.now().plusDays(1));
        createDto.setEndDate(LocalDate.now().plusDays(2));

        User user = new User();
        user.setEmail(CURRENT_USER_EMAIL);
        when(userRepository.findOneByEmailIgnoreCase(eq(CURRENT_USER_EMAIL))).thenReturn(Optional.of(user));

        Room room = new Room();
        room.setId(roomId);
        when(roomRepository.getReferenceById(eq(roomId))).thenReturn(room);
        when(clock.getZone()).thenReturn(UTC);
        when(clock.instant()).thenReturn(Instant.now());

        when(roomRepository.isRoomBooked(any(), any(), any())).thenReturn(true);

        Assertions.assertThrows(ValidationException.class, () -> bookingService.book(roomId, createDto));

        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void cancel_BookingByAdmin_NoExceptionThrown() {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.ADMIN));
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(CURRENT_USER_EMAIL, "user", authorities));
        SecurityContextHolder.setContext(securityContext);
        Long bookingId = 1L;

        User user = new User();
        user.setEmail(CURRENT_USER_EMAIL);
        when(userRepository.findOneByEmailIgnoreCase(eq(CURRENT_USER_EMAIL))).thenReturn(Optional.of(user));

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setBooker(user);
        booking.setStartAt(Instant.now().plus(5, ChronoUnit.DAYS));
        when(bookingRepository.getReferenceById(eq(bookingId))).thenReturn(booking);

        Assertions.assertDoesNotThrow(() -> bookingService.cancel(bookingId));

        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void cancel_BookingByDifferentUser_ThrowsIllegalArgumentException() {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.USER));
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken("user2", "user", authorities));
        SecurityContextHolder.setContext(securityContext);
        Long bookingId = 1L;

        User user = new User();
        user.setEmail("user2");
        when(userRepository.findOneByEmailIgnoreCase(eq("user2"))).thenReturn(Optional.of(user));

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setBooker(new User());
        when(bookingRepository.getReferenceById(eq(bookingId))).thenReturn(booking);

        Assertions.assertThrows(IllegalArgumentException.class, () -> bookingService.cancel(bookingId));

        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void cancel_BookingAlreadyCanceled_ThrowsIllegalArgumentException() {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.USER));
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(CURRENT_USER_EMAIL, "user", authorities));
        SecurityContextHolder.setContext(securityContext);
        Long bookingId = 1L;

        User user = new User();
        user.setEmail(CURRENT_USER_EMAIL);
        when(userRepository.findOneByEmailIgnoreCase(eq(CURRENT_USER_EMAIL))).thenReturn(Optional.of(user));

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setBooker(user);
        booking.setCanceled(true);
        when(bookingRepository.getReferenceById(eq(bookingId))).thenReturn(booking);

        Assertions.assertThrows(IllegalArgumentException.class, () -> bookingService.cancel(bookingId));

        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void cancel_ExceededCancelDeadline_ThrowsValidationException() {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(AuthoritiesConstants.USER));
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(CURRENT_USER_EMAIL, "user", authorities));
        SecurityContextHolder.setContext(securityContext);
        Long bookingId = 1L;

        User user = new User();
        user.setEmail(CURRENT_USER_EMAIL);
        when(userRepository.findOneByEmailIgnoreCase(eq(CURRENT_USER_EMAIL))).thenReturn(Optional.of(user));

        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setBooker(user);
        booking.setStartAt(Instant.now().minus(4, ChronoUnit.DAYS));
        when(bookingRepository.getReferenceById(eq(bookingId))).thenReturn(booking);

        Assertions.assertThrows(ValidationException.class, () -> bookingService.cancel(bookingId));

        verify(bookingRepository, never()).save(any(Booking.class));
    }
}
