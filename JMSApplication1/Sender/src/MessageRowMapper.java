import org.springframework.jdbc.core.RowMapper;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author Dovgalev Roman
 *         Date: 12.11.12
 * @version 1.0
 */

public class MessageRowMapper implements RowMapper {

    private static Message getMessage(ResultSet resultSet) throws SQLException {
        try {
            BigInteger id = BigInteger.valueOf(resultSet.getLong("id_message"));
            String destination = resultSet.getString("destination");
            String text = null;
            resultSet.getString("text");
            if (!resultSet.wasNull())
                text = resultSet.getString("text");
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd kk:mm:ss yyyy");
            Date dateRecording;
            dateRecording = sdf.parse(sdf.format(resultSet.getTimestamp("date_recording")));
            boolean replay = Boolean.valueOf(resultSet.getString("replay"));
            String replayText = null;
            resultSet.getString("replay_text");
            if (!resultSet.wasNull())
                replayText = resultSet.getString("replay_text");
            String errors = null;
            resultSet.getString("errors");
            if (!resultSet.wasNull())
                errors = resultSet.getString("errors");
            return new Message(id, destination,text, dateRecording, replay, replayText, errors);
        } catch (ParseException e) {
            return null;
        }
    }

    @Override
    public ArrayList<Message> mapRow(ResultSet resultSet, int i) throws SQLException {
        String destination = null;
        ArrayList<Message> messages = new ArrayList<Message>();
        Message firstMessage = getMessage(resultSet);
        if (firstMessage != null) {
            destination = firstMessage.getDestination();
            messages.add(firstMessage);
        }
        while (resultSet.next()) {
            Message message = getMessage(resultSet);
            if (message != null) {
                if (destination == null)
                    destination = message.getDestination();
                messages.add(message);
            }
        }
        Message lastMessage = new Message(BigInteger.valueOf(System.nanoTime()), destination, "The last message queue.",
                new Date(), false, null, null);
        messages.add(lastMessage);
        return messages;
    }
}
