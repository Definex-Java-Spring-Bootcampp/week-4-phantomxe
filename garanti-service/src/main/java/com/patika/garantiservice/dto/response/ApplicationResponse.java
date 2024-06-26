package com.patika.garantiservice.dto.response;

import com.patika.garantiservice.entity.Loan;
import com.patika.garantiservice.entity.Product;
import com.patika.garantiservice.enums.ApplicationStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ApplicationResponse {

    private Long userId;
    private LocalDateTime createDate;
    private ApplicationStatus applicationStatus;
    private Loan loan;
}
