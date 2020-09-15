

package space.yizhu.record.template.stat;


public class Ctrl {

    private static final int JUMP_NONE = 0;
    private static final int JUMP_BREAK = 1;
    private static final int JUMP_CONTINUE = 2;
    private static final int JUMP_RETURN = 3;

    private static final int WISDOM_ASSIGNMENT = 0;
    private static final int LOCAL_ASSIGNMENT = 1;
    private static final int GLOBAL_ASSIGNMENT = 2;

    private int jump = JUMP_NONE;
    private int assignmentType = WISDOM_ASSIGNMENT;
    private boolean nullSafe = false;

    public boolean isJump() {
        return jump != JUMP_NONE;
    }

    public boolean notJump() {
        return jump == JUMP_NONE;
    }

    public boolean isBreak() {
        return jump == JUMP_BREAK;
    }

    public void setBreak() {
        jump = JUMP_BREAK;
    }

    public boolean isContinue() {
        return jump == JUMP_CONTINUE;
    }

    public void setContinue() {
        jump = JUMP_CONTINUE;
    }

    public boolean isReturn() {
        return jump == JUMP_RETURN;
    }

    public void setReturn() {
        jump = JUMP_RETURN;
    }

    public void setJumpNone() {
        jump = JUMP_NONE;
    }

    public boolean isWisdomAssignment() {
        return assignmentType == WISDOM_ASSIGNMENT;
    }

    public void setWisdomAssignment() {
        assignmentType = WISDOM_ASSIGNMENT;
    }

    public boolean isLocalAssignment() {
        return assignmentType == LOCAL_ASSIGNMENT;
    }

    public void setLocalAssignment() {
        assignmentType = LOCAL_ASSIGNMENT;
    }

    public boolean isGlobalAssignment() {
        return assignmentType == GLOBAL_ASSIGNMENT;
    }

    public void setGlobalAssignment() {
        assignmentType = GLOBAL_ASSIGNMENT;
    }

    public boolean isNullSafe() {
        return nullSafe;
    }

    public boolean notNullSafe() {
        return !nullSafe;
    }

    public void setNullSafe(boolean nullSafe) {
        this.nullSafe = nullSafe;
    }
}






