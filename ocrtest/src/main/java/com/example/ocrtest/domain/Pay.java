package com.example.ocrtest.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor

// 주문을 결제할때의 그 도메인
public class Pay {

    @Id @GeneratedValue
    @Column(name = "pay_id")
    private Long id;
    private String cardNumber;
    private String cardExpirationYear;
    private String cardExpirationMonth;
    private String cardPassword;

    @OneToOne
    private Member member;


}
