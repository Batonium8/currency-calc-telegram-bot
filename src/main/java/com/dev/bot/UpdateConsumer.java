package com.dev.bot;

import com.dev.bot.config.BotConfig;
import com.dev.bot.model.CurrencyModel;
import com.dev.bot.service.CurrencyService;
import lombok.SneakyThrows;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Component
public class UpdateConsumer implements LongPollingSingleThreadUpdateConsumer {
    private final TelegramClient telegramClient;
    private final BotConfig botConfig;
    @Autowired
    public UpdateConsumer(BotConfig botConfig) {
        this.botConfig = botConfig;
        this.telegramClient = new OkHttpTelegramClient(botConfig.getBotToken());
    }
    @Override
    public void consume(Update update) {
        CurrencyModel currencyModel = new CurrencyModel();
        if (update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            var chatId = update.getMessage().getChatId();
            if (Objects.equals(messageText.toLowerCase(), "/start")){
                startCommand(chatId, update.getMessage().getChat().getFirstName());
            } else if (Objects.equals(messageText.toLowerCase(), "/list")){
                listCommand(chatId);
            }
            else{
                try {
                    sendMessage(chatId,CurrencyService.getCurrencyRate(botConfig.getCurrencyToken(), update.getMessage().getText().toUpperCase(), currencyModel));
                } catch (IOException e) {
                    sendMessage(chatId, "Произошла ошибка, возможно вы неправильно ввели код валюты или команду, воспользуйтесь командой /list для просмотра популярных валют мира");
                }
            }
        }


    }

    private void listCommand(Long chatId) {
        String list = """
                Здесь приведен список наиболее популярных кодов валют
                
                USD - Доллар США
                EUR - Евро
                GBP - Фунт Стерлингов
                KRW - Вон
                JPY - Йена
                CNY - Китайский юань
                """;
        sendMessage(chatId, list);
    }

    @SneakyThrows
    private void startCommand(Long chatId, String name){
        String answer = """
                Здравствуйте, %s, введите код валюты, курс которой хотите получить (например USD)
                
                Для получения списка популярных кодов валют мира введите команду
                /list
                
                """.formatted(name);

        sendMessage(chatId,answer);
    }

    private void sendMessage(Long chatId, String message){
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), message);
        try{
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e){

        }
    }
}
