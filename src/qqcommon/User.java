package qqcommon;

import java.io.Serializable;

/**
 * @author Zhiqian Tan
 * @version 1.0
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    public String userID;
    public String password;

    public User(String userID, String password) {
        this.userID = userID;
        this.password = password;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
