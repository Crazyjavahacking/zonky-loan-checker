package cz.zonky.loans.marketplace.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Loan {
    private final int id;
    private final String name;

    public Loan(@JsonProperty("id") int id, @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
