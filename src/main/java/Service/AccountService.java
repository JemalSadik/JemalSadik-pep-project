package Service;

import Model.Account;
import DAO.AccountDAO;

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
       
    // Create/Regiter User 
    public Account registerUser(Account account) {
      String username =  account.getUsername(); 
      String password = account.getPassword();
      if (!username.isEmpty() && 
         !password.isEmpty() &&
         (username.length() < 254) &&
         (password.length() > 3) &&
         (password.length() < 254))

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
