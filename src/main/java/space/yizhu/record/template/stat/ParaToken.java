

package space.yizhu.record.template.stat;


public class ParaToken extends Token {

    
    private StringBuilder content;

    public ParaToken(StringBuilder content, int row) {
        super(Symbol.PARA, row);
        this.content = content;
    }

    public String value() {
        return content.toString();
    }

    public StringBuilder getContent() {
        return content;
    }

    public String toString() {
        return content != null ? content.toString() : "null";
    }

    public void print() {
        System.out.print("[");
        System.out.print(row);
        System.out.print(", PARA, ");
        System.out.print(toString());
        System.out.println("]");
    }
}

