import java.io.IOException;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class UdpTranslateServer extends UdpEchoServer{
    // 翻译本质上就是 key -> value
    private Map<String,String> dict = new HashMap<>();
    public UdpTranslateServer(int port) throws SocketException {
        super(port);

        dict.put("cat","小猫");
        dict.put("dog","小狗");
        // 在这可以填入很多内容，词典程序也不过如此，不过有个很大的哈希表，包含几十万个单词

    }
    // 重写process 方法，实现查询哈希表的操作
    public String process(String request){
        return dict.getOrDefault(request,"词未在词典中找到");
    }
    // start 方法和父类完全一样，不用写了
    public static void main(String[] args) throws IOException {
        UdpTranslateServer server = new UdpTranslateServer(9090);
        server.start();
    }
}
