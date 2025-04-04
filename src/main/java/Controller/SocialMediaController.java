package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private AccountService aServ = new AccountService(new AccountDAO());
    private MessageService mServ = new MessageService(new MessageDAO());
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);

        app.post("/register",this::registerAccount);
        app.post("/login", this::loginAccount);

        app.post("/messages", this::createMessage);
        app.delete("/messages/{message_id}", this::deleteMessage);
        app.get("/accounts/{account_id}/messages", this::getMessagesByUser);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.patch("/messages/{message_id}", this::updateMessage);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    private void registerAccount(Context ctx) throws JsonProcessingException{
        ObjectMapper map = new ObjectMapper();
        Account account = map.readValue(ctx.body(), Account.class);
        Account created = aServ.register(account.getUsername(), account.getPassword());
        if (created != null) {
            ctx.json(map.writeValueAsString(created)).status(200);
        } 
        else {
            ctx.status(400);
        }
    }
    
    private void loginAccount(Context ctx) throws JsonProcessingException{
        ObjectMapper map = new ObjectMapper();
        Account account = map.readValue(ctx.body(), Account.class);

        Account loginA = aServ.login(account.getUsername(), account.getPassword());
        if (loginA != null) {
            ctx.json(map.writeValueAsString(loginA)).status(200);
        } 
        else {
            ctx.status(401);
        }
    }

    private void createMessage(Context ctx) throws JsonProcessingException{
        ObjectMapper map = new ObjectMapper();
        Message message = map.readValue(ctx.body(), Message.class);
        Message created = mServ.createMessage(message);
        if (created != null) {
            ctx.json(map.writeValueAsString(created)).status(200);
        } 
        else {
            ctx.status(400);
        }
    }

    private void deleteMessage(Context ctx) throws JsonProcessingException{
        ObjectMapper map = new ObjectMapper();
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message deleted = mServ.deleteMessageById(messageId);
        if (deleted != null) {
            ctx.json(map.writeValueAsString(deleted)).status(200);
        } 
        else {
            ctx.result("");
        }
    }

    private void getMessagesByUser(Context ctx) throws JsonProcessingException{
        ObjectMapper map = new ObjectMapper();
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = mServ.getMessagesByUserId(accountId);
        if(messages.isEmpty()){
            ctx.json(map.writeValueAsString(messages)).status(200);
        }
        ctx.json(messages);
    }

    private void getAllMessages(Context ctx) throws JsonProcessingException{  
        ObjectMapper map = new ObjectMapper();
        List<Message> messages = mServ.getAllMessages();
        if(messages.isEmpty()){
            ctx.json(map.writeValueAsString(messages)).status(200);
        }
        ctx.json(messages);
    }

    private void getMessageById(Context ctx) throws JsonProcessingException{
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = mServ.getMessageById(messageId);
                
        if (message==null) {
            ctx.status(200);
        } 
        else {
            ctx.json(message);
        }
    }

    private void updateMessage(Context ctx) throws JsonProcessingException{
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message updateRequest = ctx.bodyAsClass(Message.class);
        Message updated = mServ.updateMessageText(messageId, updateRequest.getMessage_text());
        if (updated != null) {
            ctx.json(updated);
        } else {
            ctx.status(400);
        }
    }
}