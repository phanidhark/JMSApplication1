import oracle.jdbc.OracleTypes;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * @author Dovgalev Roman
 *         Date: 10.11.12
 * @version 1.0
 */

public class WorkWithDatabase {

    public static void recordingInDatabase(JdbcTemplate jdbcTemplate) {
        for (int i = 0; i < 100; i++) {
            final int finalI = i;
            jdbcTemplate.update("INSERT INTO Messages VALUES (?, ?, ?, ?, ?, ?, ?)", new PreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement preparedStatement) throws SQLException {
                    preparedStatement.setLong(1, BigInteger.valueOf(System.nanoTime()).longValue());
                    if (finalI < 50) {
                        preparedStatement.setString(2, "Queue");
                        preparedStatement.setString(3, "The message" + finalI + " is sent by using the model \"point-to-point\".");
                    } else {
                        preparedStatement.setString(2, "Topic");
                        preparedStatement.setString(3, "The message" + finalI + " is sent by using the model \"publish/subscribe\".");
                    }
                    preparedStatement.setTimestamp(4, new Timestamp((new Date()).getTime()));
                    preparedStatement.setString(5, "false");
                    preparedStatement.setNull(6, OracleTypes.VARCHAR);
                    preparedStatement.setNull(7, OracleTypes.VARCHAR);
                }
            });
        }
    }
}
