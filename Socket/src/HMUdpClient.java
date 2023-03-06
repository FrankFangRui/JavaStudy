import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class HMUdpClient {
    // 初始化数据报的时候，要指定 对方的IP和端口号，自己打包接收对方数据报的时候不要
    // 7. 确定服务端 Socket 地址address，包含域名 localhost 和 端口号Port
    private static final SocketAddress ADDRESS = new InetSocketAddress("localhost",7777);
    // 8. 创建客户端 DatagramSocket,开启随机端口，用于发送和接收UDP数据报
    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket();
    // 9.10. 准备要发送的数据，以键盘数据作为发送的内容
        // 用 while(true) 来保持一直等待输入的状态
        Scanner sc = new Scanner(System.in);
        while(true) {
            // 11.创建字符串String，并接收输入，为了服务端获取到有效的内容（去除空字符串），约定 \3 为结束符
            System.out.println("请输入要展示的目录");
            String request = sc.nextLine() + "\3";
            // 12.将字符串变为 byte 数组，并确定解码（UTF_8)
            byte[] requestData = request.getBytes(StandardCharsets.UTF_8);
            // 13.将 数组 组装成要发送的 UDP 数据报，包含数组，数组长度，和服务端信息ADDRESS)
            DatagramPacket requestPacket = new DatagramPacket(requestData,requestData.length,ADDRESS);
            // 注意：requestPacket 在 用户端 创建的时候（）内多存了 ADDRESS,指向要发送的服务端，而服务端，只有接收
            // 所以不需要这个变量存在
            // 14.发送 UDP 数据报
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
                if(b == '\n') {//换行符时进行解析
//起始位置到换行符前一个索引位置为要解析的内容
                    String fileName = new String(responseData, next, i - next);
                    System.out.println(fileName);
//下次解析从换行符后一个索引开始
                    next = i + 1;
                }
            }
        }
    }
}