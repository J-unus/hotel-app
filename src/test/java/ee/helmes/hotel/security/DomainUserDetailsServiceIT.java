package ee.helmes.hotel.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import ee.helmes.hotel.IntegrationTest;
import ee.helmes.hotel.domain.User;
import ee.helmes.hotel.repository.UserRepository;
import java.util.Locale;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integrations tests for {@link DomainUserDetailsService}.
 */
@Transactional
@IntegrationTest
class DomainUserDetailsServiceIT {

    private static final String USER_ONE_EMAIL = "test-user-one@localhost";
    private static final String USER_TWO_EMAIL = "test-user-two@localhost";
    private static final String USER_THREE_EMAIL = "test-user-three@localhost";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Qualifier("userDetailsService")
    private UserDetailsService domainUserDetailsService;

    @BeforeEach
    public void init() {
        User userOne = new User();
        userOne.setPassword(RandomStringUtils.randomAlphanumeric(60));
        userOne.setActivated(true);
        userOne.setEmail(USER_ONE_EMAIL);
        userOne.setFirstName("userOne");
        userOne.setLastName("doe");
        userOne.setLangKey("en");
        userRepository.save(userOne);

        User userTwo = new User();
        userTwo.setPassword(RandomStringUtils.randomAlphanumeric(60));
        userTwo.setActivated(true);
        userTwo.setEmail(USER_TWO_EMAIL);
        userTwo.setFirstName("userTwo");
        userTwo.setLastName("doe");
        userTwo.setLangKey("en");
        userRepository.save(userTwo);

        User userThree = new User();
        userThree.setPassword(RandomStringUtils.randomAlphanumeric(60));
        userThree.setActivated(false);
        userThree.setEmail(USER_THREE_EMAIL);
        userThree.setFirstName("userThree");
        userThree.setLastName("doe");
        userThree.setLangKey("en");
        userRepository.save(userThree);
    }

    @Test
    void assertThatUserCanBeFoundByLogin() {
        UserDetails userDetails = domainUserDetailsService.loadUserByUsername(USER_ONE_EMAIL);
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(USER_ONE_EMAIL);
    }

    @Test
    void assertThatUserCanBeFoundByLoginIgnoreCase() {
        UserDetails userDetails = domainUserDetailsService.loadUserByUsername(USER_ONE_EMAIL.toUpperCase(Locale.ENGLISH));
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(USER_ONE_EMAIL);
    }

    @Test
    void assertThatUserCanBeFoundByEmail() {
        UserDetails userDetails = domainUserDetailsService.loadUserByUsername(USER_TWO_EMAIL);
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(USER_TWO_EMAIL);
    }

    @Test
    void assertThatUserCanBeFoundByEmailIgnoreCase() {
        UserDetails userDetails = domainUserDetailsService.loadUserByUsername(USER_TWO_EMAIL.toUpperCase(Locale.ENGLISH));
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(USER_TWO_EMAIL);
    }

    @Test
    void assertThatEmailIsPrioritizedOverLogin() {
        UserDetails userDetails = domainUserDetailsService.loadUserByUsername(USER_ONE_EMAIL);
        assertThat(userDetails).isNotNull();
        assertThat(userDetails.getUsername()).isEqualTo(USER_ONE_EMAIL);
    }

    @Test
    void assertThatUserNotActivatedExceptionIsThrownForNotActivatedUsers() {
        assertThatExceptionOfType(UserNotActivatedException.class)
            .isThrownBy(() -> domainUserDetailsService.loadUserByUsername(USER_THREE_EMAIL));
    }
}
