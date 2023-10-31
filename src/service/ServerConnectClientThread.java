package service;

import qqcommon.Message;
import qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Zhiqian Tan
 * @version 1.0
 */
public class ServerConnectClientThread extends Thread {
    private Socket socket;
    private String userID;

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public ServerConnectClientThread(Socket socket, String userID) {
        this.socket = socket;
        this.userID = userID;
    }

    @Override
    public void run() {
        while (true) {
            try {
                System.out.println("持续监听" + userID);
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) objectInputStream.readObject();

                if (message.getMessageType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)) {
                    System.out.println("正在给" + userID + "返回在线用户列表");
                    Message message1 = new Message(ManageServerConnectClientThread.getOnlineUser(), MessageType.MESSAGE_RET_ONLINE_FRIEND);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject(message1);

                } else if (message.getMessageType().equals(MessageType.MESSAGE_CLIENT_EXIT)) {
                    System.out.println("用户" + message.getSender() + "正在请求退出服务器");
                    ManageServerConnectClientThread.printAllThread();
                    ManageServerConnectClientThread.removeServerConnectClientThread(message.getSender());
                    System.out.println("用户" + message.getSender() + "已经成功退出服务器");
                    socket.close();
                    break;
                } else if (message.getMessageType().equals(MessageType.MESSAGE_COMM_MES)) {
                    System.out.println("接受到转发消息请求！");
                    System.out.println("由用户：" + message.getSender() + "发向用户：" + message.getReceiver());

                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(ManageServerConnectClientThread.getServerConnectClient(message.getReceiver()).getSocket().getOutputStream());
                    objectOutputStream.writeObject(message);
                    System.out.println("已转发！");
                } else if (message.getMessageType().equals(MessageType.MESSAGE_TO_ALL)) {
                    System.out.println("接收到群发消息请求！");
                    System.out.println("由用户：" + message.getSender() + "发向所有人");

                    HashMap<String, ServerConnectClientThread> hashMap = ManageServerConnectClientThread.getHashMap();
                    Set<String> set = hashMap.keySet();
                    for (String str :
                            set) {
                        if (str.equals(message.getSender())) {
                            continue;
                        }
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                                hashMap.get(str).getSocket().getOutputStream());
                        objectOutputStream.writeObject(message);
                    }
                } else if (message.getMessageType().equals(MessageType.MESSAGE_FILE_SEND_REQUEST)) {
                    System.out.println("收到转发文件请求，由用户：" + message.getSender() + "发送给用户：" + message.getReceiver());
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                            ManageServerConnectClientThread.getServerConnectClient(message.getReceiver()).getSocket().getOutputStream());
                    objectOutputStream.writeObject(message);
                    System.out.println("已转发！");
                } else if (message.getMessageType().equals(MessageType.MESSAGE_AGREE_REQUEST)) {
                    System.out.println("收到用户同意接受文件，由用户：" + message.getSender() + "发送给用户：" + message.getReceiver());
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                            ManageServerConnectClientThread.getServerConnectClient(message.getReceiver()).getSocket().getOutputStream());
                    objectOutputStream.writeObject(message);
                    System.out.println("已转发！");
                } else if (message.getMessageType().equals(MessageType.MESSAGE_REJECT_REQUEST)) {
                    System.out.println("收到用户拒绝接受文件，由用户：" + message.getReceiver() + "发送给用户：" + message.getReceiver());
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                            ManageServerConnectClientThread.getServerConnectClient(message.getSender()).getSocket().getOutputStream());
                    objectOutputStream.writeObject(message);
                    System.out.println("已转发！");
                } else if (message.getMessageType().equals(MessageType.MESSAGE_FILE)) {
                    System.out.println("收到用户发送文件，由用户：" + message.getSender() + "发送给用户：" + message.getReceiver());
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                            ManageServerConnectClientThread.getServerConnectClient(message.getReceiver()).getSocket().getOutputStream()
                    );
                    objectOutputStream.writeObject(message);
                    System.out.println("已转发！");
                }

            } catch (IOException e) {
                System.out.println("网络错误请稍后再试！");
            } catch (ClassNotFoundException e) {
            }
        }
    }
}
