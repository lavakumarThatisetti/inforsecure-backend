package com.inforsecure.fiuapi.web.rest.resource;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopUsers {
    private String userName;
    private int wealthScore;
}
