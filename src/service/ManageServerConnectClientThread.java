package service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Zhiqian Tan
 * @version 1.0
 */
public class ManageServerConnectClientThread {
    private static HashMap<String, ServerConnectClientThread> hashMap = new HashMap<>();

    public static HashMap<String, ServerConnectClientThread> getHashMap() {
        return hashMap;
    }

    public static void addServerConnectClient(String userID, ServerConnectClientThread connectClientThread) {
        hashMap.put(userID, connectClientThread);
    }

    public static ServerConnectClientThread getServerConnectClient(String userID) {
        return hashMap.get(userID);
    }

    public static void removeServerConnectClientThread(String userName) {
        hashMap.remove(userName);
    }

    public static void printAllThread() {
        Set<String> set = hashMap.keySet();
        for (String str :
                set) {
            System.out.println(str);
        }
    }

    public static String getOnlineUser() {
        Iterator<String> iterator = hashMap.keySet().iterator();
        String onlineUser = new String();
        while (iterator.hasNext()) {
            onlineUser += iterator.next().toString() + " ";
        }
        return onlineUser;
    }

}
