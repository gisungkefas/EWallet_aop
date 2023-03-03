package com.kefas.EWallet_aop.pojo.paystack;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pages {
    private long total;
    private long skipped;
    private long perPage;
    private long page;
    private long pageCount;
}
