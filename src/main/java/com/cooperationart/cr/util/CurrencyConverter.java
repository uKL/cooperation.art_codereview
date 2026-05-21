package com.cooperationart.cr.util;

import java.math.BigDecimal;

public class CurrencyConverter {
    public static BigDecimal convert(BigDecimal amount, double rate) {
        return amount.multiply(BigDecimal.valueOf(rate));
    }
}
