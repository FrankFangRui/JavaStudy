import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.charset.StandardCharsets;

public class UdpServer {
    //服务器socket要绑定固定的端口
    private static final int PORT = 8888;
    //本地文件目录要展示的根路径
    private static final String BASE_PATH = "D://BiteStudy";
    public static void main(String[] args) throws IOException {
// 1.创建服务端DatagramSocket，指定端口，可以发送及接收UDP数据报
        DatagramSocket socket = new DatagramSocket(PORT);
//不停的接收客户端udp数据报
        while (true){
// 2.创建数据报，用于接收客户端发送的数据
            byte[] requestData = new byte[1024];
            //1m=1024kb, 1kb=1024byte, UDP最多64k（包含UDP首部8byte）
            DatagramPacket requestPacket = new DatagramPacket(requestData,
                    requestData.length);
            System.out.println("------------------------------------------------ ---");
            System.out.println("等待接收UDP数据报...");
// 3.等待接收客户端发送的UDP数据报，该方法在接收到数据报之前会一直阻塞，接收到数据报以后，DatagramPacket对象，包含数据（bytes）和客户端ip、端口号
            socket.receive(requestPacket);
            System.out.printf("客户端IP：%s%n",
                    requestPacket.getAddress().getHostAddress());
            System.out.printf("客户端端口号：%s%n", requestPacket.getPort());
// 7.接收到的数据作为请求，根据请求数据执行业务，并返回响应
            for (int i = 0; i < requestData.length; i++) {
                byte b = requestData[i];
                if(b == '\3') {
// 7-1.读取请求的数据：读取到约定好的结束符（\3），取结束符之前的内容
                    String request = new String(requestData, 0, i);
// 7-2.根据请求处理业务：本地目录根路径+请求路径，作为要展示的目录，列出下一级子文件
//请求的文件列表目录
                    System.out.printf("客户端请求的文件列表路径为：%s%n", BASE_PATH +
                            request);
                    File dir = new File(BASE_PATH + request);
//获取下一级子文件，子文件夹
                    File[] children = dir.listFiles();
// 7-3.构造要返回的响应内容：每个文件及目录名称为一行
                    StringBuilder response = new StringBuilder();
                    if(children != null){
                        for (File child : children) {
                            response.append(child.getName()+"\n");
                        }
                    }
//响应也要约定结束符
                    response.append("\3");
                    byte[] responseData =
                            response.toString().getBytes(StandardCharsets.UTF_8);
// 7-4.构造返回响应的数据报DatagramPacket，注意接收的客户端数据报包含IP和端口号，要设置到响应的数据报中
                    DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length,
                            requestPacket.getSocketAddress());
// 7-5.发送返回响应的数据报
                    socket.send(responsePacket);
                    break;
                }
            }
        }
    }
}
// Socket 是端（用户端，服务端）- DatagramSocket
// Packet 是报（数据报，通过一个数组来传送数据）- DatagramPacket
// 1.服务器代码文件这里 确定 服务器Socket 要绑定的的固定的端口
// 2.确定文件目录要展示的根路径
// 3.创建服务端 DatagramSocket ,指定端口，用于发送接收UDP数据报
// 4.一直尝试接收客户端发来的 Udp 数据报
// 5.先创建一个数据报，用于接收客户端发送的数据
// 5.1 先建立一个 requestData 数组，用于容纳发送过来的数据
// 5.2 用创建的数组来创建一个数据报
// 6. 等待接收客户端发送的 Udp 数据报，receive

//15.接收来自用户端的数据，因为接收到的数据报 requestPacket 的数据内容
//   存储在 数据报 的 requestData 数组当中 ，所以对requestData进行操作
//   将这个byte数组，进行拆分成byte变量，读取每一个byte变量，并对其进行检验
//   是否到达末尾（\3)，如果到达了，就将到达之前读取的byte内容，变成 String request
//16.此时就可以输出行读取到的目录 BASE_PATH + request
//   获取文件列表目录 new File(BASE_PATH + request)
//   获取下一级子文件，子文件夹
//17.构造要返回的响应内容：每个文件及目录名称为一行，将每个子文件连接到response中
//18.在最后要在 response 后加上 \3 表示结束
//19.把 response 打包成 byte[]responseData 数组
//20.构造返回响应的数据报 DatagramPacket,注意 发送的客户端数据要包含IP和端口号，要设置到
//   响应的数据报中
//21.发送 返回响应的数据报 并 break while循环结束
