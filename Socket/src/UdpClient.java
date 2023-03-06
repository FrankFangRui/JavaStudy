import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class UdpClient {
    // 服务端socket地址，包含域名或IP，及端口号
    private static final SocketAddress ADDRESS = new
            InetSocketAddress("localhost", 8888);
    public static void main(String[] args) throws IOException {
// 4.创建客户端DatagramSocket，开启随机端口就行，可以发送及接收UDP数据报
        DatagramSocket socket = new DatagramSocket();
// 5-1.准备要发送的数据：这里调整为键盘输入作为发送的内容
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("------------------------------------------------ ---");
            System.out.println("请输入要展示的目录：");
// 5-2.每输入新行（回车），就作为UDP发送的数据报，为了接收端获取有效的内容（去除空字符串），约定\3为结束
            String request = scanner.nextLine() + "\3";
            byte[] requestData = request.getBytes(StandardCharsets.UTF_8);
// 5-3.组装要发送的UDP数据报，包含数据，及发送的服务端信息（服务器IP+端口号）
            DatagramPacket requestPacket = new DatagramPacket(requestData,
                    requestData.length, ADDRESS);
// 6.发送UDP数据报
            socket.send(requestPacket);
// 8.接收服务端响应的数据报，并根据响应内容决定下个步骤（我们这里简单的打印即可）
// 8-1.创建数据报，用于接收服务端返回（发送）的响应
            byte[] responseData = new byte[1024];

            DatagramPacket responsePacket = new DatagramPacket(responseData,
                    responseData.length);
// 8-2.接收响应数据报
            socket.receive(responsePacket);
            System.out.println("该目录下的文件列表为：");
// byte[]下次解析的起始位置
            int next = 0;
            for (int i = 0; i < responseData.length; i++) {
                byte b = responseData[i];
                if(b == '\3')//结束符退出
                    break;
                if(b == '\n'){//换行符时进行解析
//起始位置到换行符前一个索引位置为要解析的内容
                    String fileName = new String(responseData, next, i-next);
                    System.out.println(fileName);
//下次解析从换行符后一个索引开始
                    next = i+1;
                }
            }
        }
    }
}