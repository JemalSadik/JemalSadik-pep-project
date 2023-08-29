package Service;

import Model.Account;
import DAO.AccountDAO;

import java.util.List;

public class AccountService {

    private AccountDAO accountDAO;

    // constructor without parameter
    public AccountService(){
        accountDAO = new AccountDAO();
    }

    // constructor with parameter
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public List<Account> getAllUsers() {
        return accountDAO.getAllUsers(); 
    }    
    // Create/Regiter User 
    public Account registerUser(Account account) {
      if (account.isValid()) 
         if (accountDAO.getUserByUserName(account.getUsername()) == null)
           return accountDAO.registerUser(account);
      
       return null;   
    }  
    
    public Account loginUser(Account account) {
        return accountDAO.loginUser(account);
     } 

    public Account getUserById(int account_id) {
        return accountDAO.getUserById(account_id);  
    }

}
