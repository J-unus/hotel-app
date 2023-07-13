package ee.helmes.hotel.service.dto;

import java.time.LocalDate;

public class RoomFilter {

    private LocalDate startDate;
    private LocalDate endDate;
    private String roomAmount;
    private String priceRange;

    public String getRoomAmount() {
        return roomAmount;
    }

    public void setRoomAmount(String roomAmount) {
        this.roomAmount = roomAmount;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRangeEnum) {
        this.priceRange = priceRangeEnum;
    }
}
