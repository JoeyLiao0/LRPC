package joey.common.util;

import com.alibaba.fastjson.JSON;

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

    public mySocket(Socket socket) {
        this.socket = socket;
        try {
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
            // 处理异常，例如关闭socket等
        }
    }

    public void sendMessage(String msgString) {
        writer.println(msgString);
        writer.println("EOF");
        writer.flush();
    }

    public Map<String, Object> receiveMessage() {
        try {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                if ("EOF".equals(line)) break;
                sb.append(line);
            }
            return JSON.parseObject(sb.toString(), new HashMap<String, Object>().getClass());
        } catch (IOException e) {
            System.out.println("---接收消息失败---");
            e.printStackTrace();
            return null;
        }
    }

    public void close() {
        try {
            if (reader != null) reader.close();
            if (writer != null) writer.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}