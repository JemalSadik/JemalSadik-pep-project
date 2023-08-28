package Controller;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import java.util.List;

import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */

     AccountService accountService;
     MessageService messageService;
 
     public SocialMediaController(){
         this.accountService = new AccountService();
         this.messageService = new MessageService();
     }
 
    
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("/login", this::loginUserHandler);
        app.post("/register", this::registerUserHandler);
        
        // Get messages/message
        app.get("/messages", this::getAllMessageshandler);
        app.get("accounts/{account_id}/messages", this::getAllMessagesForUserHandler);
        app.get("/messages/{message_id}", this::getMessageByMessageIdHandler);
        app.post("/messages", this::CreateMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageTextHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByMessageIdHandler);

        return app;
    }

   // Handlers

   //1. Account - Register User (Create Account)
    private void registerUserHandler(Context ctx) {
        // get the Account object from the body of ctx object
        Account account = ctx.bodyAsClass(Account.class);

        // Call registerUser service method
        Account registeredAccount = accountService.registerUser(account);

        // Response Body
        if (registeredAccount == null) 
          ctx.status(400);
        else 
        {
           ctx.status(200);  // if successful set status to 200
           ctx.json(registeredAccount); // send JSON representation of an object in response body
        } 
    }

    //2. Account - login User Handler
    private void loginUserHandler(Context ctx) {
    
        // get account object from the body of ctx object (param)
        Account account = ctx.bodyAsClass(Account.class);
   
        // Find the user account using the service method
        Account retrievedAccount = accountService.loginUser(account);
        
        // Response body - send all messages to as JSON objects
        if (retrievedAccount != null) // if succesful, set status 200
        {
           ctx.status(200); 
           ctx.json(retrievedAccount);
        }   
        else 
           ctx.status(401); // if not succesful, set status 401
    }
    
    //3. Message - get all Messages Handler
    private void getAllMessageshandler(Context ctx) {
        // call service method to return all messages 
        List<Message> messages = messageService.getAllMessages();
        ctx.status(200); // always set status 200, default
        // Response body - send all messages to as JSON objects
        ctx.json(messages);
    }
    //4. Messages - Get all Messages For a User Handler
    private void getAllMessagesForUserHandler(Context ctx) {
        // get parameter posted_by from ctx object (param)
        int posted_by = Integer.parseInt(ctx.pathParam("posted_by"));

        // get all Messges for User using the service method
        List<Message> messages = messageService.getAllMessagesForUser(posted_by);
        
        ctx.status(200); // always set status 200, default
        // Response body - send all messages to as JSON objects
        ctx.json(messages);
    }

    //5. Message - get a message For message id Handler
    private void getMessageByMessageIdHandler(Context ctx) {
        // get parameter message_id from ctx object (param)
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));

        // get all Messges for User using the service method
        Message message = messageService.getMessageByMessageId(message_id);
        
        ctx.status(200); // always set status 200, default
    
        // Response body - send all messages to as JSON objects
        if (message != null)
           ctx.json(message);
    }

    //6. Message - Create a Message
    private void CreateMessageHandler(Context ctx) {
        // get the message object from the body of ctx object
        Message message = ctx.bodyAsClass(Message.class);
        
        if (!message.isValid()) // message is not valid
           ctx.status(400);  
        else
        {
           int posted_by = message.getPosted_by();
           Account retrievedAccount = accountService.getUserById(posted_by);   
           if (retrievedAccount == null) // posted_by User is not found
              ctx.status(400);
           else  
           {
              // Call CreateMessage service method so it can make decisions and return info needed
              Message createdMessageme = messageService.CreateMessage(message);

              // Response Body
              if (createdMessageme == null) // couldn't create message
                 ctx.status(400);
              else 
              {
                 ctx.json(createdMessageme); // send JSON representation of an object in response body
                 ctx.status(200);  // if successful set status to 200
              }
           }
       }
    }

    // 7. Message - Update Message Text Handler
    private void updateMessageTextHandler(Context ctx) {
        // get message object from ctx object (param)
        Message message = ctx.bodyAsClass(Message.class);
        
        if (!message.isValid()) 
           ctx.status(400);
        else
        { 
           int message_id =  message.getMessage_id();   
            Message retrievedMessaged = messageService.getMessageByMessageId(message_id); 
            if (retrievedMessaged == null) // message does not exists
               ctx.status(400);
            else 
            {
                // call  messageService.updateMessageText method
                boolean result = messageService.updateMessageText(message);
                // If update is Not successful, set status = 400 
                if (result == false)
                   ctx.status(400);
                else
                { 
                    ctx.status(200);
                    ctx.json(message); // send JSON representation of an object in response body
                }
            }   
        }  
    }

    private void deleteMessageByMessageIdHandler(Context ctx) {
        // get any relevant info from ctx object (param)
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        // call relevant service method so it can make decisions and return info needed
        boolean result = messageService.deleteMessageByMessageId(message_id);
        ctx.status(200);
         // if delete was successful, send response
        if (result) 
          ctx.result("now-deleted");
    }

    

}