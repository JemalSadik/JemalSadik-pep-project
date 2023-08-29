package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Message;

import Util.ConnectionUtil;

public class MessageDAO{

    // Create
    public Message CreateMessage(Message message) {

        //1. Create/Get connection
        Connection connection = ConnectionUtil.getConnection();
        try {
            //2. prepare statement
            String sql = "INSERT INTO Message(message_text, posted_by, time_posted_epoch) VALUES (?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, message.getMessage_text());
            statement.setInt(2, message.getPosted_by());
            statement.setLong(3, message.getTime_posted_epoch());
            
            //3. execute statement
            statement.executeUpdate();

            //4. process - get the keys
            ResultSet keys = statement.getGeneratedKeys();
            if (keys.next()) {
                return new Message(keys.getInt(1), 
                                   message.getPosted_by(), 
                                   message.getMessage_text(),
                                   message.getTime_posted_epoch() 
                                  );
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

   // READ All Messages
    public List<Message> RetrieveAllMessages() {
        // 1. Create/Get connection to the database
        Connection connection = ConnectionUtil.getConnection();
        // create variables needed
        List<Message> messages = new ArrayList<>();
        try {
            //2. Prepare statement 
            String sql = "SELECT * FROM Message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            //3. execute query
            ResultSet rs = preparedStatement.executeQuery();
          
            //4. process results
            while (rs.next()) {
                int retrunedId = rs.getInt("message_id");
                String message_text = rs.getString("message_text");
                int posted_by = rs.getInt("posted_by");
                long time_posted_epoch = rs.getLong("time_posted_epoch");
                            
                messages.add(new Message(retrunedId, posted_by, message_text, time_posted_epoch));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return messages;
    }
   
    // READ All Messages for User
    public List<Message> RetrieveAllMessagesForUser(int posted_by) {
        // 1. Create/Get connection
        Connection connection = ConnectionUtil.getConnection();
        // create variables needed
        List<Message> messages = new ArrayList<>();
        try {
            //2. prepare statement
            String sql = "SELECT * FROM Message WHERE posted_by = ? " + posted_by;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,posted_by);

            //3. execute query
            ResultSet rs = preparedStatement.executeQuery();
          
            //4. process results
            while (rs.next()) {
                int retrunedId = rs.getInt("message_id");
                String message_text = rs.getString("message_text");
                int returned_posted_by = rs.getInt("posted_by");
                long time_posted_epoch = rs.getLong("time_posted_epoch");
                
                messages.add(new Message(retrunedId, returned_posted_by, message_text, time_posted_epoch));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return messages;
    }

    // READ a single Message by Message Id
    public Message getMessageByMessageId(int messageid) {
        // 1. connect to db: either connection utility class would work
        Connection connection = ConnectionUtil.getConnection(); 
        try {
            //2. prepare statement
            String sql = "SELECT * FROM Message WHERE message_id = ? ";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,messageid);

            //3. execute query
            ResultSet rs = preparedStatement.executeQuery();

            // 4. process results
            while (rs.next()) {
                // get the values from the record and save them somewhere
                int retrunedId = rs.getInt("message_id");
                String message_text = rs.getString("message_text");
                int posted_by = rs.getInt("posted_by");
                long time_posted_epoch = rs.getLong("time_posted_epoch");
                
                return new Message(retrunedId, posted_by, message_text, time_posted_epoch);
             }
          } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    // Update Message text
    public boolean updateMessageText(Message message) 
    {
        // Create / Get connection
        Connection connection = ConnectionUtil.getConnection(); 
        try 
        {

            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, message.getMessage_text());
            ps.setInt(2, message.getMessage_id());

            int numberOfUpdatedRows = ps.executeUpdate();

            if (numberOfUpdatedRows != 0) {
                return true;
            }
        } catch (SQLException ex) 
        {
            ex.printStackTrace();
        }
        return false;
    }

    // Delete Message by Message ID
    public boolean deleteMessageByMessageId(int message_id) {
        try (Connection connection = ConnectionUtil.getConnection()) {

            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, message_id);

            int numberOfUpdatedRows = ps.executeUpdate();

            if (numberOfUpdatedRows != 0) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }
}
