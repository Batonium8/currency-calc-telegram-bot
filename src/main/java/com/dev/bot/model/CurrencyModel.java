package com.dev.bot.model;

import lombok.Data;

@Data
public class CurrencyModel {
    String result;
    String baseCode;
    String targetCode;
    Double conversionResult;
}
