import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TcpEchoClient {
    // 客户端需要使用这个 socket 对象来建立连接
    private Socket socket = null;

    public TcpEchoClient(String serverIP,int serverPort) throws IOException {
        // 和服务器建立连接，就需要知道服务器在哪了
        // 这里和上节课写的 UDP 客户端差别较大了
        // UDP 客户端在 start()的时候才建立连接
        socket = new Socket(serverIP,serverPort);
    }
    public void start(){
        Scanner scanner = new Scanner(System.in);
        try(InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream()){
            while(true){
                // 1. 从控制台读取数据，构造成一个请求
                System.out.println("-> ");
                String request = scanner.next();
                // 2. 发送请求到服务器
                PrintWriter printWriter = new PrintWriter(outputStream);
                printWriter.println(request);
                // 这个 flush 不要忘记，否则可能导致请求没有真发出去
                printWriter.flush();
                // 3. 从服务器读取响应
                Scanner respScanner = new Scanner(inputStream);
                String response = respScanner.next();
                // 4. 把响应显示到界面上
                System.out.println(response);
            }
        } catch (IOException e) {
            e.printStackTrace();;
        }

    }

    public static void main(String[] args) throws IOException {
        TcpEchoClient client = new TcpEchoClient("127.0.0.1",9090);
        client.start();
    }
}
