package com.project.spring.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class OrderDTO {
    private Long idOrder;
    private Long idUser;
    private BigDecimal total;
    private Date date;
    private List<OrderDetailDTO> orderDetails;
}
