package ru.mikhaylov;

import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * Created by mikhaylov_av on 10.11.2016.
 */
public class Main {
    public static final Logger log = Logger.getLogger(Main.class);
    public static void main(String[] args) throws IOException {

        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new ru.mikhaylov.Telegram());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        log.info(ParserURL.ParsingURLcurrencyLKB());
        log.info(ParserURL.ParsingURLcurrencyCBR());


    }

}
