package DAO;
import java.sql.*;

import Model.Account;
import Util.ConnectionUtil;
//account_id integer primary key auto_increment,
//username varchar(255) unique,
//password varchar(255)
public class AccountDAO {
    
    //Create account
    public Account createAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();
        try{
            String sql = "INSERT INTO account(username, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int gen_id = (int) pkeyResultSet.getLong(1);
                return new Account(gen_id,account.getUsername(),account.getPassword());
            }
          
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    //Get username
    public Account getAccountByUsername(String username) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Account WHERE username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
