package com.sail.accounts.model;

import com.sail.accounts.commons.util.LoginGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private String firstName;

    private String lastName;

    @NotNull
    private String pan;

    @NotNull(groups = LoginGroup.class)
    private Integer pin;

    private boolean signedIn = false;

    private BigDecimal balance;

    private List<Transaction> transactions;

}
