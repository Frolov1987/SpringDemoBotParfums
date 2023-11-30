package com.example.SpringDemoBotParfums.service;

import java.io.InputStream;

import com.example.SpringDemoBotParfums.config.BotConfig;
import com.example.SpringDemoBotParfums.model.*;
import com.example.SpringDemoBotParfums.model.User;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

import static com.example.SpringDemoBotParfums.utils.Constants.*;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdsRepository adsRepository;
    @Autowired
    private ParfumRepository parfumRepository;
    final BotConfig config;

    public TelegramBot(BotConfig config) {
        this.config = config;
        List <BotCommand> listCommands = new ArrayList<>();
        listCommands.add(new BotCommand("/start", "старт бота" ));
        listCommands.add(new BotCommand("/help", "як користуватися цим ботом?"));
        listCommands.add(new BotCommand("/register", "зареєстуватись"));
        try {
            this.execute(new SetMyCommands(listCommands, new BotCommandScopeDefault(),null));
        }
        catch (TelegramApiException e){
            log.error("Error settings bots command list:" + e.getMessage(),e);
        }
    }

    @Override
    public String getBotToken() {
        return config.getBotToken(); // Верните токен вашего бота из конфигурации
    }

    @Override
    public String getBotUsername() {
        return config.getBotName(); // Верните имя вашего бота из конфигурации
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();

            if (message.hasText()) {

                String messageText = message.getText();
                long chatId = message.getChatId();

                if (messageText.startsWith("/send") && Long.parseLong(config.getOwnerId()) == chatId) {

                    String[] commandParts = messageText.split("\\s", 2);
                    if (commandParts.length > 1) {

                        var textToSend = EmojiParser.parseToUnicode(commandParts[1]);

                        sendTextToAllUsers(textToSend);

                        if (message.hasPhoto()) {
                            processIncomingPhoto(message, chatId);

                        }
                    } else {
                        prepareAndSendMessage(chatId, "Пожалуйста, укажите сообщение для отправки после команды /send.");
                    }
                } else {
                    // Process other text messages
                    switch (messageText) {
                        case "/start":
                            registerUser(message);
                            startCommandReceived(chatId, message.getFrom().getFirstName());
                            break;

                        case "/help":
                            prepareAndSendMessage(chatId, Help_Text);
                            break;

                        case "/register":
                            log.info("Received /register command");
                            register(chatId);
                            break;

                        case "/send":
                            break;

                        case "Вітрина":
                            sendShopKeyboard(chatId, 1);
                            break;

                        case "Ganymede Marc-Antoine Barrois":
                            sendProductDetails(chatId, 4);
                            break;
                        case "Pas Сe Soir BDK Parfums":
                            sendProductDetails(chatId, 5);
                            break;
                        case "Mancera Cedrat Boise":
                            sendProductDetails(chatId, 3);
                            break;
                        case "Juliette Has A Gun Not a Perfume":
                            sendProductDetails(chatId, 6);
                            break;
                        case "Norana Perfumes Moon 1947 White":
                            sendProductDetails(chatId, 7);
                            break;
                        case "Aventus Creed":
                            sendProductDetails(chatId, 8);
                            break;
                        case "Сторінка 2":
                            sendShopKeyboard(chatId, 2);
                            break;
                        case "Montabaco Ormonde Jayne":
                            sendProductDetails(chatId, 9);
                            break;
                        case "Daisy Ever So Fresh Marc Jacobs":
                            sendProductDetails(chatId, 11);
                            break;
                        case "Mon Vetiver Essential Parfums":
                            sendProductDetails(chatId, 12);
                            break;
                        case "Dirty Mango Christian Richard":
                            sendProductDetails(chatId, 13);
                            break;
                        case "Baccarat Rouge 540 Maison Francis Kurkdjian":
                            sendProductDetails(chatId, 14);
                            break;
                        case "Cherry Max Philip":
                            sendProductDetails(chatId, 15);
                            break;
                        case "Сторінка 3":
                            sendShopKeyboard(chatId, 3);
                            break;
                        case "White Chocola Richard":
                            sendProductDetails(chatId, 16);
                            break;
                        case "Escentric 03 Escentric Molecules":
                            sendProductDetails(chatId, 17);
                            break;
                        case "Lost Cherry Tom Ford":
                            sendProductDetails(chatId, 18);
                            break;

                        case "Інтернет-магазин":
                            sendInlineKeyboard(chatId, "Перейти в інтернет-магазин", "https://kremchik.ua/customshop-24858/customshopedit#zakladki");
                            break;

                        case "Viber спільнота":
                            sendInlineKeyboard(chatId, "Перейти в Viber спільноту", "https://invite.viber.com/?g2=AQB1Wd6c0lGN2Et6eSzwNGKKfJFMeWkQmd%2FX8sQlZ7%2BUH1cP9VAvDkofd3B4QoJq");
                            break;

                        case "Instagram сторінка":
                            sendInlineKeyboard(chatId, "Перейти в Instagram сторінку", "https://instagram.com/parfumchik.raspiv?igshid=YTQwZjQ0NmI0OA%3D%3D&utm_source=qr");
                            break;

                        case "Довідка":
                            prepareAndSendMessage(chatId, Info_Text);
                            break;


                        case "Головне меню":
                            Stack<String> userState = userStates.getOrDefault(chatId, new Stack<>());

                            if (!userState.isEmpty()) {

                                userState.pop();

                                sendShopKeyboard(chatId, 1);
                            } else {
                                sendMessage(chatId, "Головне меню:");
                            }
                            break;
                        case "Назад":

                            int currentPage = userStates.getOrDefault(chatId, new Stack<>()).isEmpty() ? 1 : Integer.parseInt(userStates.get(chatId).peek());

                            currentPage = Math.max(1, currentPage - 1);

                            sendShopKeyboard(chatId, currentPage);
                            break;
                        default:
                            prepareAndSendMessage(chatId, "На жаль, команду не розпізнано");
                    }
                }
            } else if (message.hasPhoto()) {
                // Process photo messages
                long chatId = message.getChatId();
                processIncomingPhoto(message, chatId);
            }

        } else  if (update.hasCallbackQuery()){
            String callbackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();


            if(callbackData.equals(YES_BUTTON)){
                String text = "Ви зареєстровані, дякуємо";
                executeEditMessageText(text,chatId,messageId);

            }
            else if (callbackData.equals(NO_BUTTON)){
                String text = "Ви натиснули Ні";
                executeEditMessageText(text,chatId,messageId);
            }
        }

    }


    private void sendInlineKeyboard(long chatId, String text, String url) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);

        InlineKeyboardMarkup markupInline = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInline = new ArrayList<>();
        List<InlineKeyboardButton> rowInline = new ArrayList<>();

        InlineKeyboardButton inlineButton = new InlineKeyboardButton();
        inlineButton.setText("Перейти");
        inlineButton.setUrl(url);

        rowInline.add(inlineButton);
        rowsInline.add(rowInline);

        markupInline.setKeyboard(rowsInline);
        message.setReplyMarkup(markupInline);

        executeMessage(message);
    }


    private void sendTextToAllUsers(String textToSend) {
        var users = userRepository.findAll();

        for (User user : users) {
            long chatId = user.getChatId();
            prepareAndSendMessage(chatId, textToSend);
        }
    }


    private void processIncomingPhoto(Message message, long chatId) {
        List<PhotoSize> photos = message.getPhoto();

        photos.sort(Comparator.comparing(PhotoSize::getFileSize).reversed());

        if (!photos.isEmpty()) {
            PhotoSize photo = photos.get(0);
            String fileId = photo.getFileId();

            saveFileIdToDatabase(chatId, fileId);

            if (fileId != null && !fileId.isEmpty()) {
                prepareAndSendMessage(chatId, "Вы отправили фото с file_id: " + fileId);

                sendPhotoToAllUsersExceptSender(chatId, fileId, "Ваше фото:", true);
            } else {
                prepareAndSendMessage(chatId, "Не удалось получить идентификатор файла из фото");
            }
        } else {
            prepareAndSendMessage(chatId, "Вы отправили фото, но не удалось получить идентификатор файла");
        }
    }
    //   рабочий
    private void saveFileIdToDatabase(long chatId, String fileId) {
        // Добавьте код для сохранения fileId в базу данных, используя ваш репозиторий или другой механизм хранения данных
    }

    //   рабочий
    private void sendPhotoToAllUsersExceptSender(long senderChatId, String fileId, String caption, boolean isFileId) {
        var users = userRepository.findAll();

        for (User user : users) {
            long chatId = user.getChatId();

            if (chatId != senderChatId && chatId != Long.parseLong(config.getOwnerId())) {
                sendPhoto(chatId, fileId, caption, isFileId);
            }
        }
    }


    private void sendPhoto(long chatId, String identifier, String caption, boolean isFileId) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(String.valueOf(chatId));

        if (isFileId) {

            sendPhoto.setPhoto(new InputFile(identifier));
        } else {

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(identifier);

            String fileName = identifier.substring(identifier.lastIndexOf('/') + 1);

            sendPhoto.setPhoto(new InputFile(inputStream, fileName));
        }

        sendPhoto.setCaption(caption);

        try {
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            log.error(ERROR_TEXT + e.getMessage());
        }
    }


    private void sendProductDetails(long chatId, int productId) {

        Parfum product = getProductById(productId);

        if (product != null) {

            String photoUrl = URL_IMAGES_UPLOADS + product.getImg();

            sendPhoto(chatId, photoUrl, product.getName() + "\n\n" + product.getGender() + "\n\n" + product.getArticle() + "\n\n" + product.getDescription() + "\n\nЦіна: " + Math.round(product.getPrice()) + " грн/мл.", false);
        } else {

            prepareAndSendMessage(chatId, "Такого товару не знайдено");
        }
    }


    private Parfum getProductById(int productId) {
        return parfumRepository.findById((long) productId).orElse(null);
    }


    private void register(long chatId){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Ви дійсно бажаєте зареєструватись?");

        InlineKeyboardMarkup markupInLine = new InlineKeyboardMarkup();
        List<List< InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        List<InlineKeyboardButton> rowInLine = new ArrayList<>();
        var yesButton = new InlineKeyboardButton();
        yesButton.setText("Так");
        yesButton.setCallbackData(YES_BUTTON);

        var noButton = new InlineKeyboardButton();
        noButton.setText("Ні");
        noButton.setCallbackData(NO_BUTTON);

        rowInLine.add(yesButton);
        rowInLine.add(noButton);

        rowsInLine.add(rowInLine);

        markupInLine.setKeyboard(rowsInLine);
        message.setReplyMarkup(markupInLine);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(ERROR_TEXT + e.getMessage());

        }

    }


    private void registerUser(Message msg) {
        if (userRepository.findById(msg.getChatId()).isEmpty()) {
            var chatId = msg.getChatId();
            var chat = msg.getChat();
            User user = new User();
            user.setChatId(chatId);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUserName(chat.getUserName());

            Contact contact = msg.getContact();
            if (contact != null) {
                user.setPhone(contact.getPhoneNumber());
            }

            user.setRegisteredAt(Timestamp.from(Instant.now()));

            userRepository.save(user);
            log.info("user saved: " + user );
        }
    }

    private void startCommandReceived(long chatId, String name) {

        String answer = EmojiParser.parseToUnicode("Привіт! " + name + ", раді вітати!" + ":blush: ");

        sendMessage(chatId, answer);
        log.info("Replied to user " + name);
    }


    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();

        List<KeyboardRow> keyboardRows = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        KeyboardRow row1 = new KeyboardRow();
        row.add("Вітрина");
        row.add("Інтернет-магазин");

        keyboardRows.add(row);

        row1.add("Viber спільнота");
        row1.add("Instagram сторінка");
        row1.add("Довідка");

        keyboardRows.add(row1);

        keyboardMarkup.setKeyboard(keyboardRows);
        message.setReplyMarkup(keyboardMarkup);

        try {
            executeMessage(message);
        } catch (Exception e) {
            log.error(ERROR_TEXT + " Error details: " + e.getMessage(), e);
        }

    }


    private void executeEditMessageText(String text, long chatId, long messageId){
        EditMessageText message = new EditMessageText();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        message.setMessageId((int) messageId);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(ERROR_TEXT + e.getMessage());
        }
    }


    private void executeMessage(SendMessage message){
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error(ERROR_TEXT + e.getMessage());
        }
    }

    private void prepareAndSendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        executeMessage(message);
    }


    private final Map<Long, Stack<String>> userStates = new HashMap<>();


    private void sendShopKeyboard(long chatId, int page) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));

        Stack<String> userState = userStates.getOrDefault(chatId, new Stack<>());

        if (userState.isEmpty()) {
            message.setText("Зробіть вибір:");

            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
            List<KeyboardRow> keyboardRows = new ArrayList<>();

            if (page == 1) {
                KeyboardRow row = new KeyboardRow();
                row.add("Ganymede Marc-Antoine Barrois");
                row.add("Pas Сe Soir BDK Parfums");
                keyboardRows.add(row);

                row = new KeyboardRow();
                row.add("Mancera Cedrat Boise");
                row.add("Juliette Has A Gun Not a Perfume");
                keyboardRows.add(row);

                row = new KeyboardRow();
                row.add("Norana Perfumes Moon 1947 White");
                row.add("Aventus Creed");
                keyboardRows.add(row);

                row = new KeyboardRow();
                row.add("Головне меню");
                row.add("Сторінка 2");
                keyboardRows.add(row);

            } else if (page == 2) {
                KeyboardRow row = new KeyboardRow();
                row = new KeyboardRow();
                row.add("Montabaco Ormonde Jayne");
                row.add("Daisy Ever So Fresh Marc Jacobs");
                keyboardRows.add(row);

                row = new KeyboardRow();
                row.add("Mon Vetiver Essential Parfums");
                row.add("Dirty Mango Christian Richard");
                keyboardRows.add(row);

                row = new KeyboardRow();
                row.add("Baccarat Rouge 540 Maison Francis Kurkdjian");
                row.add("Cherry Max Philip");
                keyboardRows.add(row);

                row = new KeyboardRow();
                row.add("Назад");
                row.add("Сторінка 3");
                keyboardRows.add(row);
            } else if (page == 3) {

                KeyboardRow row = new KeyboardRow();
                row = new KeyboardRow();
                row.add("White Chocola Richard");
                row.add("Escentric 03 Escentric Molecules");
                keyboardRows.add(row);

                row = new KeyboardRow();
                row.add("Lost Cherry Tom Ford");
                keyboardRows.add(row);

                row = new KeyboardRow();
                row.add("Сторінка 2");
                row.add("Головне меню");
                keyboardRows.add(row);

            } else {

                sendMessage(chatId, "Головне меню:");
                return;
            }

            keyboardMarkup.setKeyboard(keyboardRows);
            message.setReplyMarkup(keyboardMarkup);

            executeMessage(message);
        } else {

            String currentState = userState.peek();
            if (currentState.equals("SELECTED_PRODUCT")) {

                ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
                List<KeyboardRow> keyboardRows = new ArrayList<>();

                KeyboardRow row = new KeyboardRow();
                row.add("Головне меню");
                keyboardRows.add(row);

                keyboardMarkup.setKeyboard(keyboardRows);
                message.setReplyMarkup(keyboardMarkup);

                executeMessage(message);

            }
        }
    }

    //@Scheduled(cron ="* * 10 * * 2")
    private void sendAds(){

        var ads = adsRepository.findAll();
        var users = userRepository.findAll();

        for(Ads ad: ads) {
            for (User user: users){
                prepareAndSendMessage(user.getChatId(), ad.getAd());
            }
        }
    }

}

