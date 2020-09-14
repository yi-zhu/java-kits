package space.yizhu.exception;/* Created by xiuxi on 2018.5.21.*/

public class NoPermissions extends Exception {
    private static final long serialVersionUID = 1L;

    public NoPermissions(String msg) {
        super(msg);
    }
}
