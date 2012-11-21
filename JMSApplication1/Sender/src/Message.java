import java.math.BigInteger;
import java.util.Date;

/**
 * @author Dovgalev Roman
 *         Date: 11.11.12
 * @version 1.0
 */

public class Message implements Comparable<Message> {
    private BigInteger id;
    private String destination;
    private String text;
    private Date dateRecording;
    private boolean replay;
    private String replayText;
    private String errors;

    public Message(BigInteger id, String destination, String text, Date dateRecording, boolean replay, String replayText,
                   String errors) {
        setId(id);
        setDestination(destination);
        setText(text);
        setDateRecording(dateRecording);
        setReplay(replay);
        setReplayText(replayText);
        setErrors(errors);
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDateRecording() {
        return dateRecording;
    }

    public void setDateRecording(Date dateRecording) {
        this.dateRecording = dateRecording;
    }

    public boolean isReplay() {
        return replay;
    }

    public void setReplay(boolean replay) {
        this.replay = replay;
    }

    public String getReplayText() {
        return replayText;
    }

    public void setReplayText(String replayText) {
        this.replayText = replayText;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }

    @Override
    public int compareTo(Message o) {
        return dateRecording.compareTo(o.getDateRecording());
    }
}
