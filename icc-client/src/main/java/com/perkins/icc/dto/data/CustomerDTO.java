package com.perkins.icc.dto.data;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CustomerDTO {
    private Long id;
    private Long customerId;
    private Long memberId;
    private String customerName;
    private String customerType;
    @NotEmpty
    private String companyName;
    @NotEmpty
    private String source;
    private String phone;
}
