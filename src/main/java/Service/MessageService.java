package Service;

import Model.Message;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {

    private MessageDAO messageDAO;

    // constructor without parameter
    public MessageService(){
      messageDAO = new MessageDAO();
    }

    // constructor with parameter
    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    //Read all messages
    public List<Message> getAllMessages() {
      return messageDAO.RetrieveAllMessages();
    } 
    
    //Read all messages for a User
    public List<Message> getAllMessagesForUser(int posted_by) {
       return messageDAO.RetrieveAllMessagesForUser(posted_by);
    }

    //Read a single messages by message id 
    public Message getMessageByMessageId(int messageid) {
      return messageDAO.getMessageByMessageId(messageid);
    }

    // Create a new message
    public Message CreateMessage(Message message) {
      String message_text = message.getMessage_text();
      if (!message_text.isEmpty() &&
         (message_text.length() < 255))
         return messageDAO.CreateMessage(message); 
      else
         return null;  
    }    

    // Update Message text
    public boolean updateMessageText(Message message) {
      if ((message.getMessage_text().length() < 1) ||
         (message.getMessage_text().length() > 254)) 
        return false; 
      else  
        return messageDAO.updateMessageText(message);
    }
        
    // Delete a Message by Message ID
    public boolean deleteMessageByMessageId(int message_id) {
      return messageDAO.deleteMessageByMessageId(message_id);
    }
        
}
