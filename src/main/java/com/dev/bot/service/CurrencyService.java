package com.dev.bot.service;

import com.dev.bot.config.BotConfig;
import com.dev.bot.model.CurrencyModel;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Scanner;

@Service
public class CurrencyService {
    public static String getCurrencyRate(@Value("${currency.token}") String token,String targetCode, CurrencyModel model) throws IOException {
        @Deprecated
        URL url = new URL("https://v6.exchangerate-api.com/v6/"+ token + "/pair/"+targetCode+"/RUB");
        Scanner scanner = new Scanner((InputStream) url.getContent());
        StringBuilder result = new StringBuilder();
        while(scanner.hasNext()){
            result.append(scanner.nextLine());
        }
        JSONObject jsonObject = new JSONObject(result.toString());
        model.setResult(jsonObject.getString("result"));
        model.setBaseCode(jsonObject.getString("base_code"));
        model.setTargetCode(jsonObject.getString("target_code"));
        model.setConversionResult(jsonObject.getDouble("conversion_rate"));

        return "Курс " + targetCode + " на момент " + LocalDate.now() + " составляет " + model.getConversionResult() + " рублей";
    }
}
