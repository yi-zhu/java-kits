

package space.yizhu.record.template.stat;


class TextToken extends Token {

    
    private StringBuilder text;

    public TextToken(StringBuilder value, int row) {
        super(Symbol.TEXT, row);
        this.text = value;
    }

    public void append(StringBuilder content) {
        if (content != null) {
            text.append(content);    
        }
    }

    
    public boolean deleteBlankTails() {
        for (int i = text.length() - 1; i >= 0; i--) {
            if (CharTable.isBlank(text.charAt(i))) {
                continue;
            }

            if (text.charAt(i) == '\n') {
                text.delete(i + 1, text.length());
                return true;
            } else {
                return false;
            }
        }

        
        text.setLength(0);
        return true;        
    }

    public String value() {
        return text.toString();
    }

    public StringBuilder getContent() {
        return text;
    }

    public String toString() {
        return text.toString();
    }

    public void print() {
        System.out.print("[");
        System.out.print(row);
        System.out.print(", TEXT, ");
        System.out.print(text.toString());
        System.out.println("]");
    }
}


