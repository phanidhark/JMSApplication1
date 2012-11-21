import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.*;

/**
 * @author Dovgalev Roman
 *         Date: 14.11.12
 * @version 1.0
 */

public class WorkWithServer {

    public static void sendToServer(JdbcTemplate jdbcTemplate) throws NamingException, ExecutionException,
            InterruptedException {
        int number;
        final ArrayList<Message> messagesQueue = (ArrayList<Message>) jdbcTemplate.queryForObject("SELECT * FROM Messages " +
                "WHERE destination = 'Queue' ORDER BY date_recording", new MessageRowMapper());
        PriorityBlockingQueue<Message> priorityQueue = new PriorityBlockingQueue<Message>(messagesQueue);
        ArrayList<Message> messagesTopic = (ArrayList<Message>) jdbcTemplate.queryForObject("SELECT * FROM Messages " +
                "WHERE destination = 'Topic' ORDER BY date_recording", new MessageRowMapper());
        PriorityBlockingQueue<Message> priorityTopic = new PriorityBlockingQueue<Message>(messagesTopic);
        Properties properties = System.getProperties();
        properties.put(Context.INITIAL_CONTEXT_FACTORY, "weblogic.jndi.WLInitialContextFactory");
        properties.put(Context.PROVIDER_URL, "t3://localhost:7001");
        properties.put(Context.SECURITY_PRINCIPAL, "weblogic");
        properties.put(Context.SECURITY_CREDENTIALS, "LADA21099");
        InitialContext initialContext = new InitialContext(properties);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        if (priorityQueue.size() > priorityTopic.size()) {
            number = priorityQueue.size();
        } else if (priorityQueue.size() < priorityTopic.size()) {
            number = priorityTopic.size();
        } else {
            number = priorityQueue.size();
        }
        for (int i = 0; i < number; i++) {
            Future<Message> futureQueue = executorService.submit(new Queue(priorityQueue, initialContext));
            final Message replayQueue = futureQueue.get();
            if (replayQueue != null) {
                jdbcTemplate.update("UPDATE Messages SET date_recording = ?, replay = ?, replay_text = ?, errors = ? " +
                        "WHERE id_message = ?", new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement) throws SQLException {
                        preparedStatement.setTimestamp(1, new Timestamp((new Date()).getTime()));
                        preparedStatement.setString(2, String.valueOf(replayQueue.isReplay()));
                        preparedStatement.setString(3, replayQueue.getReplayText());
                        preparedStatement.setString(4, replayQueue.getErrors());
                        preparedStatement.setLong(5, replayQueue.getId().longValue());
                    }
                });
            }
            Future<Message> futureTopic = executorService.submit(new Topic(priorityTopic, initialContext));
            final Message replayTopic = futureTopic.get();
            if (replayTopic != null) {
                jdbcTemplate.update("UPDATE Messages SET date_recording = ?, replay = ?, replay_text = ?, errors = ? " +
                        "WHERE id_message = ?", new PreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement) throws SQLException {
                        preparedStatement.setTimestamp(1, new Timestamp((new Date()).getTime()));
                        preparedStatement.setString(2, String.valueOf(replayTopic.isReplay()));
                        preparedStatement.setString(3, replayTopic.getReplayText());
                        preparedStatement.setString(4, replayTopic.getErrors());
                        preparedStatement.setLong(5, replayTopic.getId().longValue());
                    }
                });
            }
        }
        executorService.shutdown();
    }
}