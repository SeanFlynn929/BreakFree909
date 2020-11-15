package www.youtube.The_Break_Free_Program;

import java.io.Serializable;
import java.time.LocalDate;

public class JournalEntry implements Serializable {
    private LocalDate date;
    private String entry;

    public JournalEntry(LocalDate date, String entry) {
        this.date = date;
        this.entry = entry;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }
}
