package service;

import qqcommon.Message;
import qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.Scanner;
import java.util.Set;

/**
 * @author Zhiqian Tan
 * @version 1.0
 */
public class SendNewToAllService extends Thread {
    Scanner scanner = new Scanner(System.in);

    @Override
    public void run() {
        while (true) {
            System.out.println("请输入想要通知所有人的信息，输入exit结束该功能！");
            String content = scanner.nextLine();
            if (content.equals("exit")) {
                break;
            }
            Set<String> set = ManageServerConnectClientThread.getHashMap().keySet();
            for (String str :
                    set) {
                Message message = new Message("服务器", str, new Date().toString(), content, MessageType.MESSAGE_TO_ALL);
                try {
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                            ManageServerConnectClientThread.getServerConnectClient(str).getSocket().getOutputStream()
                    );
                    objectOutputStream.writeObject(message);
                } catch (IOException e) {
                    System.out.println("网络异常请稍后再试！");
                }
            }
        }
    }
}
