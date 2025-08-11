package com.dev.bot;

import com.dev.bot.config.BotConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;

@Component
public class TelegramBot implements SpringLongPollingBot {

    private final UpdateConsumer updateConsumer;
    private final BotConfig botConfig;
    @Autowired
    public TelegramBot(UpdateConsumer updateConsumer, BotConfig botConfig) {
        this.updateConsumer = updateConsumer;
        this.botConfig = botConfig;
    }
    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return updateConsumer;
    }
}
