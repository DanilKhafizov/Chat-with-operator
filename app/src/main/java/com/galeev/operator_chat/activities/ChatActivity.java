package com.galeev.operator_chat.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.galeev.operator_chat.adapters.ChatAdapter;
import com.galeev.operator_chat.databinding.ActivityChatBinding;
import com.galeev.operator_chat.models.ChatMessage;
import com.galeev.operator_chat.models.User;
import com.galeev.operator_chat.utilities.Constants;
import com.galeev.operator_chat.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ChatActivity extends BaseActivity {
    private ActivityChatBinding binding;
    private User receiverUser;
    private List<ChatMessage> chatMessages;
    private ChatAdapter chatAdapter;
    private PreferenceManager preferenceManager;
    private FirebaseFirestore database;
    private String conversionId = null;
    private String botResponse;
    private Boolean isReceiverAvailable = false;
    private Boolean isLastBotMessageOperator = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
        loadReceiverDetails();
        init();
        listenMessages();
    }
    private void init(){
        preferenceManager = new PreferenceManager(getApplicationContext());
        chatMessages = new ArrayList<>();
        chatAdapter = new ChatAdapter(
                chatMessages,
                getBitmapFromEncodedString(receiverUser.image),
                preferenceManager.getString(Constants.KEY_USER_ID)
        );

        binding.chatRecyclerView.setAdapter(chatAdapter);
        database = FirebaseFirestore.getInstance();
    }
    private void sendMessage(){


        HashMap<String, Object> message = new HashMap<>();
        message.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
        message.put(Constants.KEY_RECEIVER_ID, receiverUser.id);
        message.put(Constants.KEY_MESSAGE, binding.inputMessage.getText().toString());
        message.put(Constants.KEY_TIMESTAMP, new Date());
        database.collection(Constants.KEY_COLLECTION_CHAT).add(message);
        String senderId = (String) message.get(Constants.KEY_SENDER_ID);
        if(receiverUser.id.equals("wX0ewhC2Mr0E03bmpZgz"))
        {
            receiverUser.role = "BOT";
        }

        String userMessage = binding.inputMessage.getText().toString();
        botResponse = "";
        if ("BOT".equals(receiverUser.role)) {
            assert senderId != null;
            database.collection("users").document(senderId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String userRole = documentSnapshot.getString("role");
                            if (Objects.equals(userRole, "Клиент")) {
                                if (userMessage.contains("модели двигателей") || userMessage.contains("модели двигатели")) {
                                    botResponse = "Российский двигатель";
                                    sendMessageToBot(botResponse);
                                    botResponse = "Американский двигатель";
                                    sendMessageToBot(botResponse);
                                    botResponse = "Немецкий двигатель";
                                    sendMessageToBot(botResponse);
                                } else if (userMessage.contains("оператор") || userMessage.contains("Оператор")) {
                                    botResponse = "Чтобы открыть чат с оператором, отправьте сообщение: 1 ";
                                    sendMessageToBot(botResponse);
                                    isLastBotMessageOperator = true;
                                } else if (userMessage.equalsIgnoreCase("1") && isLastBotMessageOperator) {
                                    getUsersFromDatabase();
                                    isLastBotMessageOperator = false;
                                } else if (userMessage.contains("сколько детали") || userMessage.contains("сколько деталей")
                                        || userMessage.contains("Сколько деталей")){
                                    if (userMessage.contains("су") || userMessage.contains("СУ") || userMessage.contains("Су")) {
                                        if (userMessage.contains("57")) {
                                            botResponse = "Двигатель Су-57 имеет около 500000 деталей";
                                            sendMessageToBot(botResponse);
                                        }
                                        if (userMessage.contains("27")) {
                                            botResponse = "Двигатель Су-27 имеет около 10000 деталей";
                                            sendMessageToBot(botResponse);
                                        }
                                        if (userMessage.contains("30")) {
                                            botResponse = "Двигатель Су-30 имеет около 90000 деталей";
                                            sendMessageToBot(botResponse);
                                        }
                                        if (userMessage.contains("35")) {
                                            botResponse = "Двигатель Су-35 имеет около 350000 деталей";
                                            sendMessageToBot(botResponse);
                                        }
                                        if (userMessage.contains("24")) {
                                            botResponse = "Двигатель Су-24 имеет около 240000 деталей";
                                            sendMessageToBot(botResponse);
                                        }
                                        if (userMessage.contains("25")) {
                                            botResponse = "Двигатель Су-25 имеет около 250000 деталей";
                                            sendMessageToBot(botResponse);
                                        }
                                        if (userMessage.contains("25")) {
                                            botResponse = "Двигатель Су-25 имеет около 150000 деталей";
                                            sendMessageToBot(botResponse);
                                        }
                                        if (userMessage.contains("34")) {
                                            botResponse = "Двигатель Су-34 имеет около 1000000 деталей";
                                            sendMessageToBot(botResponse);
                                        }
                                    }
                                } else if (userMessage.contains("брак") || userMessage.contains("дефект") || userMessage.contains("неисправность") ||
                                userMessage.contains("повреждение")) {
                                        botResponse = "Вы можете обратиться в ЦЕХ, где Вы покупали данный товар";
                                        sendMessageToBot(botResponse);
                                    }
                                else if (userMessage.contains("чертеж")) {
                                    if (userMessage.contains("двигател") || userMessage.contains("Двигател")){
                                        botResponse = "Для запроса двигателя по чертежу Вам нужно обратиться в ЦЕХ 37";
                                        sendMessageToBot(botResponse);
                                    }
                                }
                                else if (userMessage.contains("сын") || userMessage.contains("ребенок") ||
                                        userMessage.contains("доч") || userMessage.contains("ученик")) {
                                        botResponse = "С таким вопросом обратитесь в отдел кадров, где он(а) станет учеником ПУЦ после обучения" +
                                                "сможет работать у нас со своей профессии";
                                        sendMessageToBot(botResponse);
                                }

                                else {
                                    botResponse = "Извините, не могу распознать ваш вопрос";
                                    sendMessageToBot(botResponse);
                                }
                            }
                            if(Objects.equals(userRole, "Работник")) {
                                if (userMessage.contains("хотел") || userMessage.contains("хочу") || userMessage.contains("Хотел")
                                        || userMessage.contains("Хочу")) {
                                    if (userMessage.contains("устроиться на работу")) {
                                        botResponse = "Можете обратиться в отдел кадров с дальнейшими вопросами по номеру; " +
                                                "211-39-39, доб. 34-059\n";
                                        sendMessageToBot(botResponse);
                                    }
                                } else if (userMessage.contains("не выдали зарплату") || userMessage.contains("не выдали деньги") ||
                                        userMessage.contains("не заплатили")) {
                                    botResponse = "С данным вопросам подойдите в 103 кабинет бтз";
                                    sendMessageToBot(botResponse);
                                } else if (userMessage.contains("деталь с браком") || userMessage.contains("брак") ||
                                        userMessage.contains("попался брак") ||
                                        userMessage.contains("дефект")||
                                        userMessage.contains("неисправность")) {
                                    botResponse = "Деталь с браком можно отнести к контролеру";
                                    sendMessageToBot(botResponse);
                                }
                                else if (userMessage.contains("оператор") || userMessage.contains("Оператор")) {
                                    botResponse = "Чтобы открыть чат с оператором, отправьте сообщение: 1 ";
                                    sendMessageToBot(botResponse);
                                    isLastBotMessageOperator = true;
                                }
                             else if (userMessage.equalsIgnoreCase("1") && isLastBotMessageOperator) {
                                getUsersFromDatabase();
                                isLastBotMessageOperator = false;
                            }
                                else {
                                    botResponse = "Извините, не могу распознать ваш вопрос";
                                    sendMessageToBot(botResponse);
                                }
                            }
                        }
                        else {
                            showToast("Не удалось найти роль пользователя");
                        }
                    })
                    .addOnFailureListener(e -> showToast(e.getMessage()));
        }

        if(conversionId != null){
            updateConversion(binding.inputMessage.getText().toString());
        } else{
            HashMap<String, Object> conversion = new HashMap<>();
            conversion.put(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
            conversion.put(Constants.KEY_SENDER_NAME, preferenceManager.getString(Constants.KEY_NAME));
            conversion.put(Constants.KEY_SENDER_IMAGE, preferenceManager.getString(Constants.KEY_IMAGE));
            conversion.put(Constants.KEY_RECEIVER_ID, receiverUser.id);
            conversion.put(Constants.KEY_RECEIVER_NAME, receiverUser.name);
            conversion.put(Constants.KEY_RECEIVER_IMAGE, receiverUser.image);
            conversion.put(Constants.KEY_LAST_MESSAGE, binding.inputMessage.getText().toString());
            conversion.put(Constants.KEY_TIMESTAMP, new Date());
            addConversion(conversion);
        }
        binding.inputMessage.setText(null);
    }
    private void getUsersFromDatabase(){
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<User> users = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                            String userEmail = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                            String userRole = queryDocumentSnapshot.getString(Constants.KEY_ROLE);
                            User user = new User();
                            user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                            user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                            user.role = queryDocumentSnapshot.getString(Constants.KEY_ROLE);
                            user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                            user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            user.id = queryDocumentSnapshot.getId();
                            // Находим оператора
                            if ("Администратор".equals(userRole) && "shamilgaleev@gmail.com".equals(userEmail)) {
                                users.add(user);
                                openChatWithOperator(user);
                            }
                        }
                    }
                });
    }



    private void sendMessageToBot(String botResponse) {
        HashMap<String, Object> botMessage = new HashMap<>();
        botMessage.put(Constants.KEY_SENDER_ID, receiverUser.id);
        botMessage.put(Constants.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_USER_ID));
        botMessage.put(Constants.KEY_MESSAGE, botResponse);
        botMessage.put(Constants.KEY_TIMESTAMP, new Date());
        database.collection(Constants.KEY_COLLECTION_CHAT).add(botMessage);
    }

    private void openChatWithOperator(User selectedUser) {
        // Логика для открытия чата с оператором
        if ("Администратор".equals(selectedUser.role)) {
            // Передача информации об операторе в активити чата
            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
            intent.putExtra(Constants.KEY_USER, selectedUser);
            startActivity(intent);
            finish();
        } else {
            showToast("Не удается открыть чат с оператором");
        }
    }

    private void listenAvailabilityOfReceiver(){
        database.collection(Constants.KEY_COLLECTION_USERS).document(
                receiverUser.id
        ).addSnapshotListener(ChatActivity.this, (value, error) -> {
            if(error != null){
                return;
            }
            if (value != null){
                if(value.getLong(Constants.KEY_AVAILABILITY) != null){
                    int availability = Objects.requireNonNull(
                            value.getLong(Constants.KEY_AVAILABILITY)
                    ).intValue();
                    isReceiverAvailable = availability == 1;
                }
            }
            if(isReceiverAvailable){
                binding.textAvailability.setVisibility(View.VISIBLE);
            } else{
                binding.textAvailability.setVisibility(View.GONE);
            }

        });
    }

    private void listenMessages(){
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverUser.id)
                .addSnapshotListener(eventListener);
        database.collection(Constants.KEY_COLLECTION_CHAT)
                .whereEqualTo(Constants.KEY_SENDER_ID, receiverUser.id)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
                .addSnapshotListener(eventListener);

    }


    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if(error != null) {
            return;
        }
        if(value != null){
            int count = chatMessages.size();
            for(DocumentChange documentChange : value.getDocumentChanges()) {
                if(documentChange.getType() == DocumentChange.Type.ADDED){
                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    chatMessage.receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_MESSAGE);
                    chatMessage.dateTime = getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    chatMessages.add(chatMessage);
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Collections.sort(chatMessages, Comparator.comparing(obj -> obj.dateObject));
            }
            if(count == 0){
                chatAdapter.notifyDataSetChanged();
            }else {
                chatAdapter.notifyItemRangeInserted(chatMessages.size(), chatMessages.size());
                binding.chatRecyclerView.smoothScrollToPosition(chatMessages.size() - 1);
            }
            binding.chatRecyclerView.setVisibility(View.VISIBLE);
        }
        binding.progressBar.setVisibility(View.GONE);
        if(conversionId == null) {
            checkForConversion();
        }
    };

    private Bitmap getBitmapFromEncodedString(String encodedImage){
        if(encodedImage != null){
            byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } else{
            return null;
        }
    }

    private void loadReceiverDetails(){
        receiverUser = (User) getIntent().getSerializableExtra(Constants.KEY_USER);
        binding.textName.setText(receiverUser.name);
    }

    private void setListeners(){

        binding.imageBack.setOnClickListener(v -> onBackPressed());
        binding.layoutSend.setOnClickListener(v -> sendMessage());
    }

    private String getReadableDateTime(Date date){
        return new SimpleDateFormat("dd MMMM, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }

    private void addConversion(HashMap<String, Object> conversion){
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .add(conversion)
                .addOnSuccessListener(documentReference -> conversionId = documentReference.getId());
    }

    private void updateConversion(String message){
        DocumentReference documentReference =
                database.collection(Constants.KEY_COLLECTION_CONVERSATIONS).document(conversionId);
        documentReference.update(
                Constants.KEY_LAST_MESSAGE, message,
                Constants.KEY_TIMESTAMP, new Date()
        );

    }

    private void checkForConversion(){
        if(chatMessages.size() != 0){
            checkForConversionRemotely(
                    preferenceManager.getString(Constants.KEY_USER_ID),
                    receiverUser.id
            );
            checkForConversionRemotely(
                    receiverUser.id,
                    preferenceManager.getString(Constants.KEY_USER_ID)
            );
        }
    }

    private void checkForConversionRemotely(String senderId, String receiverId){
        database.collection(Constants.KEY_COLLECTION_CONVERSATIONS)
                .whereEqualTo(Constants.KEY_SENDER_ID, senderId)
                .whereEqualTo(Constants.KEY_RECEIVER_ID, receiverId)
                .get()
                .addOnCompleteListener(conversionOnCompleteListener);
    }

    private final OnCompleteListener<QuerySnapshot> conversionOnCompleteListener = task -> {
        if(task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0){
            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
            conversionId = documentSnapshot.getId();
        }
    };

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        listenAvailabilityOfReceiver();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ChatActivity.this, MainChatActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}