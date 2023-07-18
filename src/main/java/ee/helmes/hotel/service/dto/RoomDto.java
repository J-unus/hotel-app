package ee.helmes.hotel.service.dto;

import java.util.List;

public class RoomDto {

    private Long id;
    private Integer roomAmount;
    private Integer size;
    private String roomNumber;
    private String type;
    private Integer oneNightPriceInCents;
    private String currency;
    private Double averageRating;
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

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
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

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }
}
