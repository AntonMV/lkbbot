package ru.mikhaylov; /**
 * Created by mikhaylov_av on 10.11.2016.
 */


import org.telegram.telegrambots.api.methods.send.SendLocation;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Telegram extends TelegramLongPollingBot {

    private static float longitude2;
    private static float latitude2;
    private static String name;
    private static String description;

    @Override
    public String getBotUsername() {
        return "LKBBot";
    }

    @Override
    public String getBotToken() {
        return "267893758:AAGcwEL39gt5AeYuBKHNPcBizJ8Y-uzjjgc";
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        Main.log.info(message.getText());
        if (message.getLocation() != null){
            ParserATM.ParserATM(message.getLocation().getLatitude().floatValue(), message.getLocation().getLongitude().floatValue());
            sendMsg(message, "Три ближайших банкомата по увеличению дальности (1 самый близкий)");

            int min = 1;
            for (Map.Entry entry : ParserATM.getSet().entrySet()) {
                String[] value = (String[]) entry.getValue();

                for(int i = 0; i <= value.length  - 1; i++) {
                    if (i == 0) name = value[0];
                    if (i == 1) latitude2 = Float.parseFloat(value[1]);
                    if (i == 2) longitude2 = Float.parseFloat(value[2]);
                    if (i == 3) description = value[3];

                }


                
                sendMsg(message, "Банкомат №"+min+" "+"расстояние "+entry.getKey()+"м "+name+" находится по адресу "+description);
                sendLocate(message, latitude2,longitude2);

                min = min + 1;
                if (min == 4) return;
            }

            
        }
        if (message != null && message.hasText()) {
            if (message.getText().toLowerCase().contains("комбанк")){
                sendMsg(message, "Комбанк рекомендует!!! http://pikabu.ru/hot");
            }
            else if (message.getText().equals("\uD83D\uDCB3 " + "Курсы ЦБ"))
                try {
                    sendMsg(message, "<pre>"+ ParserURL.ParsingURLcurrencyCBR()+"</pre>");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            else if (message.getText().equals("\uD83D\uDCB9 " + "Курсы ЛКБ"))
                try {
                    sendMsg(message, "<pre>"+ ParserURL.ParsingURLcurrencyLKB()+"</pre>");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            else if (message.getText().equals("\uD83D\uDCBC " + "Инфо"))
                sendMsg(message, "Вы держитесь там!!!");
            else if (message.getText().equals("\uD83D\uDCB0 " + "Лучший"))
                sendMsg(message, "Комбанк он самый лучший банк!!!");
            else if (message.getText().equals("\uD83C\uDFE7 " + "Банкоматы"))
                sendMsgleve2(message, "Пришлите свое местоположение на карте.");
            else if (message.getText().equals("\uD83C\uDD99 " + "Обратно"))
                sendMsg(message, "Вы вернулись в главное меню.");
            else if (message.getText().equals("/start")) {
                sendMsg(message, "Добро пожаловать в Бот Липецккомбанка. Этот Бот разработан во благо всего рода человеческого :) ");
            }else
                sendMsg(message, "Хорошего вам настроения!!!");
        }
    }

    private void sendLocate(Message message, float Latitude, float Longitude) {
        SendLocation location = new SendLocation();
        location.setChatId(message.getChatId().toString());
        location.setLatitude(Latitude);
        location.setLongitude(Longitude);
        try {
            sendLocation(location);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.enableHtml(true);
        Main.log.info(text);

        // Создаем клавиуатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(false);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();

        // Первая строчка клавиатуры
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатуры
        keyboardFirstRow.add("\uD83D\uDCB3 " + "Курсы ЦБ");
        keyboardFirstRow.add("\uD83D\uDCB9 " + "Курсы ЛКБ");

        // Вторая строчка клавиатуры
        KeyboardRow keyboardSecondRow = new KeyboardRow();
        // Добавляем кнопки во вторую строчку клавиатуры
        keyboardSecondRow.add("\uD83D\uDCBC " + "Инфо");
        keyboardSecondRow.add("\uD83D\uDCB0 " + "Лучший");

        // Третья строчка клавиатуры
          KeyboardRow keyboardthreeRow = new KeyboardRow();
          keyboardthreeRow.add("\uD83C\uDFE7 " + "Банкоматы");

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        keyboard.add(keyboardthreeRow);

        // и устанваливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);

        sendMessage.setChatId(message.getChatId().toString());

        //sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendMsgleve2(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.enableHtml(true);
        Main.log.info(text);

        // Создаем клавиуатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboad(false);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();

        // Первая строчка клавиатуры
        KeyboardRow keyboardFirstRow = new KeyboardRow();

        // Добавляем кнопки в первую строчку клавиатуры
        KeyboardButton button = new KeyboardButton();
        button.setRequestLocation(true);
        button.setText("\uD83C\uDF10 " + "Отправить местоположение");
        keyboardFirstRow.add(button);

        // Первая строчка клавиатуры
        KeyboardRow keyboardtwoRow = new KeyboardRow();

        // Добавляем кнопки в вторую строчку клавиатуры
        keyboardtwoRow.add("\uD83C\uDD99 " + "Обратно");

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardtwoRow);

        // и устанваливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);


        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.enableHtml(true);
        //sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(text);
        try {
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}