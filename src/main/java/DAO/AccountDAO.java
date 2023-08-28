package DAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Account;

import Util.ConnectionUtil;

public class AccountDAO{
    
    // READ = Login User
    public Account loginUser(Account account) {
        // 1. Create/Get connection to database
        Connection connection = ConnectionUtil.getConnection();     
        try {
             //2. prepare statement
             String sql = "SELECT * FROM Account WHERE username = ? and password = ? ";
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             preparedStatement.setString(1, account.getUsername());
             preparedStatement.setString(2, account.getPassword());

            // 3. execute statement
            ResultSet rs = preparedStatement.executeQuery();

            // 4. process results
            while (rs.next()) {
                // get the values of each column from the result set
                int retrund_account_id = rs.getInt("account_id");
                String username = rs.getString("username");
                String password = rs.getString("password");
           
                return new Account(retrund_account_id, username, password);
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    // Create new User
    public Account registerUser(Account account) {
        // 1. Create / Get Connection to the database
        Connection connection = ConnectionUtil.getConnection();
        try  {
            //2. prepare statement
            String sql = "INSERT INTO Account(username, password) VALUES (?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());
           
            //3. execute statement
            preparedStatement.executeUpdate();

            //4. process - get the keys
            ResultSet keys = preparedStatement.getGeneratedKeys();
            if (keys.next()) {
                return new Account(keys.getInt(1), account.getUsername(), account.getPassword());
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    // READ All Users
    public List<Account> getAllUsers() {
        // 1. Create/Get connection to the database
        Connection connection = ConnectionUtil.getConnection();
        // create variables needed
        List<Account> accounts = new ArrayList<>();
        try {
            //2. Prepare statement 
            String sql = "SELECT * FROM Account";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            
            //3. execute query
            ResultSet rs = preparedStatement.executeQuery();
          
            //4. process results
            while (rs.next()) {
                int retrunedId = rs.getInt("account_id");
                String username = rs.getString("username");
                String password = rs.getString("password");
                            
                accounts.add(new Account(retrunedId, username, password));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return accounts;
    }

    // READ = get User by Id
    public Account getUserById(int account_id) {
        // 1. Create/Get connection to database
        Connection connection = ConnectionUtil.getConnection();     
        try {
             //2. prepare statement
             String sql = "SELECT * FROM Account WHERE account_id = ? ";
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             preparedStatement.setInt(1, account_id);
          
            // 3. execute statement
            ResultSet rs = preparedStatement.executeQuery();

            // 4. process results
            while (rs.next()) {
                // get the values of each column from the result set
                int retrund_account_id = rs.getInt("account_id");
                String username = rs.getString("username");
                String password = rs.getString("password");
           
                return new Account(retrund_account_id, username, password);
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return null;
    }
   
}