import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UdpEchoServer {
    private DatagramSocket socket = null;

    // 参数的端口表示服务器要绑定的窗口
    public UdpEchoServer(int port) throws SocketException {
        socket = new DatagramSocket(port);
    }
    // 通过这个方法启动服务器
    public void start() throws IOException {
        System.out.println("服务器启动");
        while(true){
            // 循环里面处理一次请求
            // 1. 读取请求并解析
            DatagramPacket requestPacket = new DatagramPacket(new byte[4096],4096);
            socket.receive(requestPacket);
            // 把这个 DatagramPacket 对象装成字符串，方便去打印
            String request = new String(requestPacket.getData(),0,requestPacket.getLength());
            // 2. 根据请求计算响应
            String response = process(request);
            // 3. 把响应写回到客户端
            DatagramPacket responsePacket = new DatagramPacket(response.getBytes(),response.getBytes().length,requestPacket.getSocketAddress());
            socket.send(responsePacket);
            // 4. 打印一个日志，记录当前的情况
            System.out.printf("[%s:%d] req: %s; resp: %s\n",requestPacket.getAddress().toString(),requestPacket.getPort(),request,response);
        }
    }
    // 当前写的是一个回显服务器
    // 响应数据就和请求是一样的
    public String process(String request){
        return request;
    }

    public static void main(String[] args) throws IOException {
        UdpEchoServer server = new UdpEchoServer(9090);
        server.start();
    }
}
