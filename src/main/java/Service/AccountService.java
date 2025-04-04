package Service;
import Model.Account;
import DAO.AccountDAO;
import java.util.List;
public class AccountService {

    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }
    public Account register(String username, String password){
        if (username == null || username.isBlank() ||password == null || password.isBlank()) {
            return null;
        }
        if (accountDAO.getAccountByUsername(username)!=null) {
            return null;
        }
        if(password.length()<4){
            return null;
        }
        Account newAccount = new Account(username, password);
        return accountDAO.createAccount(newAccount);
    }

    public Account login(String username, String password){
        Account account =accountDAO.getAccountByUsername(username);
        if(account!= null && account.getPassword().equals(password)){
            return account;
        }
        return null;
    }
}
