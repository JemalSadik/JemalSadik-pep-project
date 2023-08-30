package Controller;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
        
        // Get messages/message
        app.post("/messages", this::CreateMessageHandler); 
        app.delete("/messages/{message_id}", this::deleteMessageByMessageIdHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesForUserHandler);
        app.get("/messages", this::getAllMessageshandler);
        app.get("/messages/{message_id}", this::getMessageByMessageIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageTextHandler);
      
        app.post("/login", this::loginUserHandler);
        app.post("/register", this::registerUserHandler);
      
       
        return app;
    }

   // Handlers

   //1. Account - Register User (Create Account)
    private void registerUserHandler(Context ctx) throws JsonProcessingException {
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
    private void loginUserHandler (Context ctx) throws JsonProcessingException {
    
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
    private void getAllMessageshandler(Context ctx) throws JsonProcessingException {
        // call service method to return all messages 
        List<Message> messages = messageService.getAllMessages();
        ctx.status(200); // always set status 200, default
        // Response body - send all messages to as JSON objects
        ctx.json(messages);
    }
    //4. Messages - Get all Messages For a User Handler
    private void getAllMessagesForUserHandler(Context ctx) throws JsonProcessingException {
        // get parameter posted_by from ctx object (param)
        int posted_by = Integer.parseInt(ctx.pathParam("account_id"));

        // get all Messges for User using the service method
        List<Message> messages = messageService.getAllMessagesForUser(posted_by);
        
        ctx.status(200); // always set status 200, default
        // Response body - send all messages to as JSON objects
        ctx.json(messages);
    }
    //5. Message - get a message For message id Handler
    private void getMessageByMessageIdHandler(Context ctx) throws JsonProcessingException {
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
    private void CreateMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        // get the message object from the body of ctx object
        Message message = mapper.readValue(ctx.body(), Message.class);

        String message_text = message.getMessage_text();
        if (message_text.isEmpty() ||
          (message_text.length() > 254)) // message is not valid
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
    private void updateMessageTextHandler(Context ctx) throws JsonProcessingException {
        // get message object from ctx object (param)
        Message message = ctx.bodyAsClass(Message.class);
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));

        //Retrieve message for message_id
        Message retrievedMessaged = messageService.getMessageByMessageId(message_id); 
     
        if (retrievedMessaged == null ) // if message not found, set status = 400
           ctx.status(400);
        else
        { 
            if ((message.getMessage_text().length() < 1) ||  
                (message.getMessage_text().length() > 254) 
               ) // if message length is not valid, set status = 400
            {
               ctx.status(400);
            }
            else
            {
               // call  messageService.updateMessageText method
               message.setMessage_id(message_id);
               message.setTime_posted_epoch(retrievedMessaged.getTime_posted_epoch());
               message.setPosted_by(retrievedMessaged.getPosted_by());
  
               boolean result = messageService.updateMessageText(message);
            
              if (result == false) // if update is not successful, set status to 400  
                ctx.status(400); // this is a valid message and supposed to be updated successfully.
              else
              { 
                 ctx.status(200);
                 ctx.json(message); // send JSON representation of an object in response body
              }
            }
        }  
    }

    private void deleteMessageByMessageIdHandler(Context ctx) throws JsonProcessingException {
        // get any relevant info from ctx object (param)
        int message_id = Integer.parseInt(ctx.pathParam("message_id"));
        Message retrievedMessaged = messageService.getMessageByMessageId(message_id); 
        ctx.status(200);
        if (retrievedMessaged !=null)
        {
           // call relevant service method so it can make decisions and return info needed
           boolean result = messageService.deleteMessageByMessageId(message_id);
           if (result) // if delete was successful, send response
           {
             ctx.json(retrievedMessaged); // send JSON representation of an object in response body
           }
        }
    }

    

}