package joey.common.util;

import com.alibaba.fastjson.JSON;
import joey.common.exception.MysocketException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class mySocket {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    public mySocket(Socket socket) throws RuntimeException{
        this.socket = socket;
        try {
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            throw new MysocketException("---打开读写流失败---",e);
        }
    }

    public void sendMessage(String msgString) throws RuntimeException{
        System.out.println("---"+socket.toString()+"发送数据,数据长度："+ msgString.length()+"---");
        try{
            writer.println(msgString);
            writer.println("EOF");
            writer.flush();
        }catch (Exception e){
            throw new MysocketException("---发送数据失败---",e);
        }
    }

    public Map<String, Object> receiveMessage() throws RuntimeException{

        try {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if ("EOF".equals(line)) break;
                sb.append(line);
            }
            System.out.println("---"+socket.toString()+"接收数据，数据长度："+sb.toString().length()+"---");
            return JSON.parseObject(sb.toString(), new HashMap<String, Object>().getClass());
        } catch (IOException e) {
            throw new MysocketException("---接收消息失败---",e);
        }
    }

    public void close() throws RuntimeException{
        try {
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            throw new MysocketException("---关闭流失败---",e);
        }
    }
}