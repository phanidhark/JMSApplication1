import javax.jms.*;
import javax.naming.InitialContext;
import java.util.concurrent.Callable;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * @author Dovgalev Roman
 *         Date: 10.11.12
 * @version 1.0
 */

public class Queue implements Callable<Message> {
    private PriorityBlockingQueue<Message> queue;
    private InitialContext initialContext;

    public Queue(PriorityBlockingQueue<Message> queue, InitialContext initialContext) {
        this.queue = queue;
        this.initialContext = initialContext;
    }

    @Override
    public Message call() throws InterruptedException {
        Message message = queue.take();
        try {
            if (!message.getText().equals("The last message queue.")) {
                QueueConnectionFactory connectionFactory = (QueueConnectionFactory) initialContext.lookup("javax.jms.QueueConnectionFactory");
                QueueConnection queueConnection = connectionFactory.createQueueConnection();
                QueueSession queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
                javax.jms.Queue queue1 = (javax.jms.Queue) initialContext.lookup("weblogic.base_domain.jms.Queue");
                javax.jms.QueueSender queueSender = queueSession.createSender(queue1);
                TextMessage textMessage = queueSession.createTextMessage();
                queueConnection.start();
                textMessage.setText(message.getText());
                queueSender.send(textMessage);
                return null;
            } else {
                queue.put(message);
                return null;
            }
        } catch (Exception e) {
            message.setReplay(true);
            message.setReplayText(message.getText());
            message.setErrors(e.getMessage());
            return message;
        }
    }
}