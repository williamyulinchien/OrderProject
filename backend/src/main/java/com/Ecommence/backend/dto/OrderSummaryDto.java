package com.Ecommence.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummaryDto {
    private Long id;           // 订单 ID
    private Double totalPrice; // 订单总价
    private String status;     // 订单状态
}
