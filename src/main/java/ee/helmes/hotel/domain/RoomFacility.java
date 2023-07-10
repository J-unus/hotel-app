package ee.helmes.hotel.domain;

import java.io.Serializable;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "room_facility", uniqueConstraints = { @UniqueConstraint(columnNames = { "room_id", "facility_id" }) })
public class RoomFacility extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Room room;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Facility facility;
}
