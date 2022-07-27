package com.perkins.icc.dto;

import com.alibaba.cola.dto.PageQuery;
import lombok.Data;

@Data
public class CustomerListQry extends PageQuery {
   private String name;
}
