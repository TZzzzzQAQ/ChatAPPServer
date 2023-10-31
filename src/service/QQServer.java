package service;

import qqcommon.Message;
import qqcommon.MessageType;
import qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Zhiqian Tan
 * @version 1.0
 */
public class QQServer {
    public ServerSocket serverSocket;
    private static ConcurrentHashMap<String, String> userIDData = new ConcurrentHashMap<>();

    static {
        userIDData.put("100", "123456");
        userIDData.put("200", "123456");
        userIDData.put("300", "123456");
        userIDData.put("tzq", "123456");
        userIDData.put("tc", "123456");
        userIDData.put("给哥", "123456");
        userIDData.put("逍", "123456");
        userIDData.put("眼哥", "123456");
        userIDData.put("坤纬", "123456");
    }

    public Boolean check(String username, String password) {
        if (userIDData.containsKey(username)) {
            if (userIDData.contains(password)) {
                return true;
            }
        }
        return false;
    }

    public QQServer() {
        try {
            System.out.println("服务器正在监听。。。");
            serverSocket = new ServerSocket(9999);
            new Thread(new SendNewToAllService()).start();
            while (true) {
                Socket socket = serverSocket.accept();
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                User user = null;
                Message message = new Message();
                user = (User) objectInputStream.readObject();
                if (check(user.getUserID(), user.getPassword())) {
                    message.setMessageType(MessageType.MESSAGE_LOGIN_SUCCEED);
                    objectOutputStream.writeObject(message);

                    ServerConnectClientThread serverConnectClientThread = new ServerConnectClientThread(socket, user.getUserID());
                    serverConnectClientThread.start();
                    ManageServerConnectClientThread.addServerConnectClient(user.userID, serverConnectClientThread);
                } else {
                    message.setMessageType(MessageType.MESSAGE_LOGIN_FAIL);
                    objectOutputStream.writeObject(message);
                    socket.close();
                }
            }
        } catch (ClassNotFoundException e) {
        } catch (IOException e) {
            System.out.println("网络一场请稍后再试！");
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.out.println("网络一场请稍后再试！");
            }
        }
    }
}
