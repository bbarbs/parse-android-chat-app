## Getting Started
Sample android chat app using parse sdk and [back4app](https://www.back4app.com/) as parse server.

### Running and Configuration
You can directly run the app and test it. If you want to create a seperate parse server of your own just do the ff;
* Sign up/Login in [back4app](https://www.back4app.com/)
* Then in Dashboard create a class "Chat" and for the fields refer to: [Chat.java](https://github.com/bbarbs/parse-android-chat-app/tree/master/app/src/main/java/com/avocado/chatapp/model/Chat.java)
* Then for parse sdk config like applicationId, clientKey and server you can check it in API Reference in your dashboard, then change the configs in [ParseConfig.java](https://github.com/bbarbs/parse-android-chat-app/tree/master/app/src/main/java/com/avocado/chatapp/config).
* Then for real time messaging enable first the Live Query. See [here](https://www.back4app.com/docs/parse-server-live-query-example). make sure to select the "Chat" field.
* Then get the Live Query subdomain name and change LIVE_QUERY_URI in [ParseConfig.java](https://github.com/bbarbs/parse-android-chat-app/tree/master/app/src/main/java/com/avocado/chatapp/config).

Happy Chatting!!

