import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.naming.NamingException;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

/**
 * @author Dovgalev Roman
 *         Date: 14.11.12
 * @version 1.0
 */

public class Sender {

    public static void main(String[] args) throws NamingException, ExecutionException, InterruptedException {
        Locale.setDefault(Locale.ENGLISH);
        SingleConnectionDataSource dataSource = new SingleConnectionDataSource();
        dataSource.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        dataSource.setUrl("jdbc:oracle:thin:@localhost:1521:xe");
        dataSource.setUsername("Roman");
        dataSource.setPassword("LADA21099");
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        WorkWithDatabase.recordingInDatabase(jdbcTemplate);
        WorkWithServer.sendToServer(jdbcTemplate);
        dataSource.destroy();
    }
}
