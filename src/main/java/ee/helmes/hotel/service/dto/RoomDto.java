package ee.helmes.hotel.service.dto;

import java.util.List;

public class RoomDto {

    private Long id;
    private Integer roomAmount;
    private Integer size;
    private String description;
    private Integer oneNightPriceInCents;
    private String currency;
    private List<String> roomFacilities;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRoomAmount() {
        return roomAmount;
    }

    public void setRoomAmount(Integer roomAmount) {
        this.roomAmount = roomAmount;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public List<String> getRoomFacilities() {
        return roomFacilities;
    }

    public void setRoomFacilities(List<String> roomFacilities) {
        this.roomFacilities = roomFacilities;
    }
}
