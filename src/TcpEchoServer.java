import org.w3c.dom.ls.LSOutput;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TcpEchoServer {
    // 代码中会涉及到多个 socket 对象， 使用不同的名字来区分
    private ServerSocket listenSocket = null;

    public TcpEchoServer(int port) throws IOException {
        listenSocket = new ServerSocket(port);
    }

    public void start() throws IOException {
        System.out.println("服务器启动");
        while(true){
            //1。 先调用accept 来接受客户端的连接
            Socket clientSocket = listenSocket.accept();
            //2. 再处理这个连接,这里应该使用多线程，或者开多个窗口，会一直等待第一个窗口响应完后，其他窗口才能执行，所以要让每个客户端连上来都分配一个新的线程负责处理
            Thread t = new Thread(()->{
                try{
                    processConnection(clientSocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            t.start();
        }
    }
    public void processConnection(Socket clientSocket) throws IOException {
        System.out.printf("[%s:%d] 客户端上线！",clientSocket.getInetAddress().toString(),clientSocket.getPort());
        // 接下来就处理客户端的请求了
        try(InputStream inputStream = clientSocket.getInputStream();
            OutputStream outputStream = clientSocket.getOutputStream()){
            while(true){
                // 1. 读取请求并解析
                Scanner scanner = new Scanner(inputStream);
                if(!scanner.hasNext()){
                    // 读完了，连接可以断开了。
                    System.out.printf("[%s:%d] 客户端下线!",clientSocket.getInetAddress().toString(),clientSocket.getPort());
                    break;
                }
                String request = scanner.next();
                // 2. 根据请求计算响应
                String response = process(request);
                // 3. 把响应写回给客户端
                PrintWriter printWriter = new PrintWriter(outputStream);
                printWriter.println(response);
                // 刷新缓存区确保数据是通过网卡发出去了
                printWriter.flush();
                System.out.printf("[%s:%d] req: %s; resp %s\n",clientSocket.getInetAddress().toString(),clientSocket.getPort(),request,response);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            // 为啥这个地方要关闭 socket ? 而前面的 listenSocket 以及  udp 程序中的 socket 为啥就没 close ?
            clientSocket.close();
        }

    }

    public String process(String request) {
        return request;
    }

    public static void main(String[] args) throws IOException {
        TcpEchoServer server = new TcpEchoServer(9090);
        server.start();
    }

}
