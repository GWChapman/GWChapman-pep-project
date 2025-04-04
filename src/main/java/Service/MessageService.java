package Service;
import Model.Message;
import DAO.MessageDAO;
import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    public Message createMessage(Message message) {
        if (message.getMessage_text() == null || message.getMessage_text().isBlank() || message.getMessage_text().length() > 255) {
            return null;
        }
    
        return messageDAO.createMessage(message);
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public List<Message> getMessagesByUserId(int userId) {
        return messageDAO.getAllMessagesByUserId(userId);
    }

    public Message deleteMessageById(int messageId) {
        return messageDAO.deleteMessageById(messageId);
    }

    public Message updateMessageText(int messageId, String newText) {
        if (newText == null || newText.isBlank() || newText.length() > 255) {
            return null;
        }
        return messageDAO.updateMessageText(messageId, newText);
    }

    public Message getMessageById(int messageId) {
        List<Message> allMessages = messageDAO.getAllMessages();
        for (Message m : allMessages) {
            if (m.getMessage_id() == messageId) {
                return m;
            }
        }
        return null;
    }
    
}