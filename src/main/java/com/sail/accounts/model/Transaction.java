package com.sail.accounts.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @JsonProperty("type")
    private String type;

    @JsonProperty("amount")
    @NotNull
    private BigDecimal amount;

    @JsonProperty("pan")
    @NotNull
    private String pan;

    @JsonProperty("created")
    private OffsetDateTime created;
}
