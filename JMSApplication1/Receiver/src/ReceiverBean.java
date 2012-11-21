import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * @author Dovgalev Roman
 *         Date: 10.11.12
 * @version 1.0
 */

public class ReceiverBean implements MessageDrivenBean, MessageListener {
    public ReceiverBean() {}

    public void ejbCreate() throws CreateException {}

    @Override
    public void setMessageDrivenContext(MessageDrivenContext messageDrivenContext) throws EJBException {}

    @Override
    public void onMessage(Message message) {
        try {
            String text = null;
            System.out.println("===== New Message =====");
            System.out.println("Type:        " + message.getClass().getName());
            System.out.println("Destination: " + message.getJMSDestination());
            System.out.println("Message ID:  " + message.getJMSMessageID());
            text = ((TextMessage) message).getText();
            if (text != null)
                System.out.println("Content:     " + text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void ejbRemove() throws EJBException {}
}
