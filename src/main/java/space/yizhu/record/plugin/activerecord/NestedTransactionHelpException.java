

package space.yizhu.record.plugin.activerecord;


public class NestedTransactionHelpException extends RuntimeException {

    private static final long serialVersionUID = 3813238946083156753L;

    public NestedTransactionHelpException(String message) {
        super(message);
    }

    
    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}



