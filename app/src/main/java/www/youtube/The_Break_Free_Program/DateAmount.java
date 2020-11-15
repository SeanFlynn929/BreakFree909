package www.youtube.The_Break_Free_Program;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

public class DateAmount implements Serializable {
    private LocalDate date;
    private int amount;

    public DateAmount(LocalDate date, int amount) {
        this.date = date;
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
