package ee.helmes.hotel.service.dto;

import java.time.LocalDate;
import java.util.List;

public class RoomFilter {

    private LocalDate startDate;
    private LocalDate endDate;
    private List<Integer> bedAmounts;

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

    public List<Integer> getBedAmounts() {
        return bedAmounts;
    }

    public void setBedAmounts(List<Integer> bedAmounts) {
        this.bedAmounts = bedAmounts;
    }
}
