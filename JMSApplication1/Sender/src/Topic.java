import javax.jms.*;
import javax.naming.InitialContext;
import java.util.concurrent.Callable;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * @author Dovgalev Roman
 *         Date: 10.11.12
 * @version 1.0
 */

public class Topic implements Callable<Message> {
    private PriorityBlockingQueue<Message> queue;
    private InitialContext initialContext;

    public Topic(PriorityBlockingQueue<Message> queue, InitialContext initialContext) {
        this.queue = queue;
        this.initialContext = initialContext;
    }

    @Override
    public Message call() throws InterruptedException {
        Message message = queue.take();
        try {
            if (!message.getText().equals("The last message queue.")) {
                TopicConnectionFactory connectionFactory = (TopicConnectionFactory) initialContext.lookup("javax.jms.QueueConnectionFactory");
                TopicConnection topicConnection = connectionFactory.createTopicConnection();
                TopicSession topicSession = topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
                javax.jms.Topic topic = (javax.jms.Topic) initialContext.lookup("weblogic.base_domain.jms.Topic");
                TopicPublisher topicPublisher = topicSession.createPublisher(topic);
                TextMessage textMessage = topicSession.createTextMessage();
                topicConnection.start();
                textMessage.setText(message.getText());
                topicPublisher.publish(textMessage);
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
