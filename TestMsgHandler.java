import com.robut.rirc.PrivMsg;
import com.robut.rirc.PrivMsgHandler;

public class TestMsgHandler implements PrivMsgHandler{
    public void handleNewMessage(PrivMsg msg){
        System.out.printf("%s%n", msg);
    }
}
