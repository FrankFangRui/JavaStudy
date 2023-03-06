import java.io.IOException;
import java.net.*;
import java.util.Scanner;

public class UdpEchoClient {
    private DatagramSocket socket = null;
    private String serverIP;
    private int serverPort;

    //两个参数一会会在发送数据的时候用到
    //暂时先把这两参数存起来，以备后用
    public UdpEchoClient(String serverIP,int serverPort) throws SocketException{
        // 这里并不是说就没有端口，而是让系统自动指定一个空闲的端口
        socket = new DatagramSocket();
        // 假设 serverIP 是形如 1.2.3.4 这种点分十进制的表示方式  start()方法中 2. 要用新的方式表示
        this.serverIP = serverIP;
        this.serverPort = serverPort;

    }
    public void start() throws IOException {
        Scanner scanner = new Scanner(System.in);
        while(true){
            // 1. 从控制台读取用户输入的内容
            System.out.print("-> ");
            String request = scanner.next();
            // 2. 构造一个 UDP 请求， 发送给服务器
            // DatagramPacket requestPacket = new DatagramPacket(request.getBytes(),request.getBytes().length);
            DatagramPacket requestPacket = new DatagramPacket(request.getBytes(),request.getBytes().length
                    ,InetAddress.getByName(this.serverIP),this.serverPort);
            socket.send(requestPacket);
            // 3. 从服务器读取 UDP 响应数据 ， 并解析
            DatagramPacket responsePacket = new DatagramPacket(new byte[4096],4096);
            socket.receive(responsePacket);
            String response = new String(responsePacket.getData(),0,responsePacket.getLength());
            // 4. 把服务器的响应显示到控制台上。
            System.out.println(response);
        }
    }

    public static void main(String[] args) throws IOException {
        UdpEchoClient client = new UdpEchoClient("127.0.0.1",9090);
        client.start();
    }
}
