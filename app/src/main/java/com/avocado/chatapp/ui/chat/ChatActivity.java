package com.avocado.chatapp.ui.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.avocado.chatapp.R;
import com.avocado.chatapp.config.ParseConfig;
import com.avocado.chatapp.model.Chat;
import com.avocado.chatapp.navigator.Navigator;
import com.avocado.chatapp.ui.chat.adapter.ChatAdapter;
import com.avocado.chatapp.util.KeyboardUtil;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends AppCompatActivity {

    private static final String TAG = ChatActivity.class.getSimpleName();

    private ChatAdapter chatAdapter;

    private boolean isFirstLoad = true;

    private static final int MAX_CHAT_MESSAGES_TO_SHOW = 60;

    private ParseLiveQueryClient parseLiveQueryClient;

    @BindView(R.id.toolbar_chat)
    Toolbar toolbar;

    @BindView(R.id.edittext_chat_message)
    EditText chatMessageEditText;

    @BindView(R.id.recyclerview_chat)
    RecyclerView chatRecyclerView;

    @BindView(R.id.progressbar_main)
    ProgressBar mainProgressBar;

    @BindView(R.id.relativelayout_chat_background)
    RelativeLayout chatBackgroundRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        setToolbar();
        setTitle(null);
        setUpRecyclerViewAndAdapter();
        prepareToLoadChat();
        initializeParseLiveQueryClient();
        setSubscriptionHandlingToChat();
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
    }

    private void setMainProgressBarVisibility(int visibility) {
        mainProgressBar.setVisibility(visibility);
    }

    private void setChatBackgroundRelativeLayoutVisibility(int visibility) {
        chatBackgroundRelativeLayout.setVisibility(visibility);
    }

    private void prepareToLoadChat() {
        setMainProgressBarVisibility(View.VISIBLE);
        setChatBackgroundRelativeLayoutVisibility(View.GONE);

        loadChat();
    }

    private void setUpRecyclerViewAndAdapter() {
        String username = ParseUser.getCurrentUser().getUsername();
        chatAdapter = new ChatAdapter(username, new ArrayList<>());
        chatRecyclerView.setAdapter(chatAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        chatRecyclerView.setLayoutManager(linearLayoutManager);
    }

    private void constructChat(Chat chat) {
        String message = chatMessageEditText.getText().toString();
        chat.setUsername(ParseUser.getCurrentUser().getUsername());
        chat.setMessage(message);
    }

    private void sendChat(Chat chat) {
        chat.saveInBackground().onSuccess(task -> {
            runOnUiThread(() -> addNewChatInAdapter(chat));
            return task;
        });
    }

    private void loadChat() {
        ParseQuery<Chat> query = ParseQuery.getQuery(Chat.class);
        query.setLimit(MAX_CHAT_MESSAGES_TO_SHOW);
        query.orderByDescending("createdAt");
        query.findInBackground((chats, e) -> {
            initializeOnFinishLoadChat();
            if (e == null) {
                initializeOnSuccessLoadChat(chats);
            } else {
                initializeOnErrorLoadChart(e);
            }
        });
    }

    private void initializeOnFinishLoadChat() {
        setMainProgressBarVisibility(View.GONE);
        setChatBackgroundRelativeLayoutVisibility(View.VISIBLE);
    }

    private void initializeOnSuccessLoadChat(List<Chat> chats) {
        loadAllChatInAdapterOnFirstLoad(chats);
    }

    private void initializeOnErrorLoadChart(ParseException e) {
        Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
    }

    private void loadAllChatInAdapterOnFirstLoad(List<Chat> chats) {
        if (isFirstLoad) {
            chatAdapter.setChats(chats);
            chatAdapter.notifyDataSetChanged();
            chatRecyclerView.scrollToPosition(0);
            isFirstLoad = false;
        }
    }

    private void addNewChatInAdapter(Chat chat) {
        chatAdapter.addNewChat(chat);
        chatAdapter.notifyDataSetChanged();
        chatRecyclerView.scrollToPosition(0);
    }

    private void initializeParseLiveQueryClient() {
        try {
            parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient(new URI(ParseConfig.LIVE_QUERY_URI));
        } catch (URISyntaxException e) {
            Log.e(TAG, "initParseLiveQueryClient: ", e);
        }
    }

    private ParseQuery<Chat> createChatParseQuery() {
        return ParseQuery.getQuery(Chat.class)
                .whereNotEqualTo(Chat.USERNAME, ParseUser.getCurrentUser().getUsername());
    }

    private void setSubscriptionHandlingToChat() {
        ParseQuery<Chat> chatParseQuery = createChatParseQuery();
        SubscriptionHandling<Chat> subscriptionHandling = parseLiveQueryClient.subscribe(chatParseQuery);
        subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, (query, chat) -> runOnUiThread(() -> addNewChatInAdapter(chat)));
    }

    private void logOutUser() {
        ParseUser.logOutInBackground(e -> {
            if (e == null) {
                initializeOnSuccessLogOut();
            } else {
                initializeOnErrorLogout(e);
            }
            initializeOnFinishLogout();
        });
    }

    private void initializeOnFinishLogout() {
        setChatBackgroundRelativeLayoutVisibility(View.VISIBLE);
        setMainProgressBarVisibility(View.GONE);
    }

    private void initializeOnSuccessLogOut() {
        Navigator.navigateToSignUpActivity(this);
        finish();
    }

    private void initializeOnErrorLogout(ParseException e) {
        Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
    }

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @OnClick(R.id.button_chat_send)
    void onClickSendButton() {
        if (chatMessageEditText.length() == 0) {
            return;
        }
        Chat chat = new Chat();
        constructChat(chat);
        sendChat(chat);
        chatMessageEditText.setText(null);
    }

    @OnClick(R.id.button_chat_logout)
    void onClickLogoutButton() {
        KeyboardUtil.hideKeyboard(this);
        setChatBackgroundRelativeLayoutVisibility(View.GONE);
        setMainProgressBarVisibility(View.VISIBLE);
        logOutUser();
    }
}
