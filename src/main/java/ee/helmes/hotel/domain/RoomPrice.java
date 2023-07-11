package ee.helmes.hotel.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "room_price", uniqueConstraints = { @UniqueConstraint(columnNames = { "room_id", "currency" }) })
public class RoomPrice extends AbstractAuditingEntity<Long> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(nullable = false)
    private Integer oneNightPriceInCents;

    @Size(min = 3, max = 3)
    @Column(nullable = false)
    private String currency;

    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Room room;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOneNightPriceInCents() {
        return oneNightPriceInCents;
    }

    public void setOneNightPriceInCents(Integer oneNightPriceInCents) {
        this.oneNightPriceInCents = oneNightPriceInCents;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }
}
