package DAO;
import java.sql.*;

import Model.Message;
import Util.ConnectionUtil;
import java.util.List;
import java.util.ArrayList;

public class MessageDAO {
    public Message createMessage(Message message) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, message.getPosted_by());
            stmt.setString(2, message.getMessage_text());
            stmt.setLong(3, message.getTime_posted_epoch());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int gen_id = (int) rs.getLong(1);
                return new Message(gen_id,message.getPosted_by(),message.getMessage_text(),message.getTime_posted_epoch());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        Connection conn = ConnectionUtil.getConnection();
        try {
                String sql = "SELECT * FROM Message";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                messages.add(new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public List<Message> getAllMessagesByUserId(int account_id) {
        Connection conn = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        
        try {
                String sql = "SELECT * FROM Message WHERE posted_by = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, account_id);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                messages.add(new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public Message getMessageById(int id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            
            String sql = "SELECT * FROM Message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

         
            preparedStatement.setInt(1, id);

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),
                        rs.getInt("message_text"),
                        rs.getString("time_posted"),
                        rs.getInt("foreign key"));
                return message;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Message deleteMessageById(int messageId) {
        Connection conn = ConnectionUtil.getConnection();
        Message deletedMessage = null;
        try {       
                String selectSql = "SELECT * FROM Message WHERE message_id = ?";
                String deleteSql = "DELETE FROM Message WHERE message_id = ?";
                PreparedStatement selectStmt = conn.prepareStatement(selectSql);
                selectStmt.setInt(1, messageId);
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                deletedMessage = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch"));
            }
            if (deletedMessage != null) {
                try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                    deleteStmt.setInt(1, messageId);
                    deleteStmt.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deletedMessage;
    }

    public Message updateMessageText(int messageId, String newText) {
        Connection conn = ConnectionUtil.getConnection();
        try {
            String updateSql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
            String selectSql = "SELECT * FROM Message WHERE message_id = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
            updateStmt.setString(1, newText);
            updateStmt.setInt(2, messageId);
            int rowsUpdated = updateStmt.executeUpdate();
            if (rowsUpdated > 0) {
                try (PreparedStatement selectStmt = conn.prepareStatement(selectSql)) {
                    selectStmt.setInt(1, messageId);
                    ResultSet rs = selectStmt.executeQuery();
                    if (rs.next()) {
                        return new Message(
                                rs.getInt("message_id"),
                                rs.getInt("posted_by"),
                                rs.getString("message_text"),
                                rs.getLong("time_posted_epoch"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }



}