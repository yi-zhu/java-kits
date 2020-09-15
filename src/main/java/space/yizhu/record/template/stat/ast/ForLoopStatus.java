

package space.yizhu.record.template.stat.ast;


public class ForLoopStatus {

    private Object outer;
    private int index;

    public ForLoopStatus(Object outer) {
        this.outer = outer;
        this.index = 0;
    }

    void nextState() {
        index++;
    }

    public Object getOuter() {
        return outer;
    }

    public int getIndex() {
        return index;
    }

    public int getCount() {
        return index + 1;
    }

    public boolean getFirst() {
        return index == 0;
    }

    public boolean getOdd() {
        return index % 2 == 0;
    }

    public boolean getEven() {
        return index % 2 != 0;
    }
}



