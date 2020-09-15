

package space.yizhu.record.template.stat;


public class CharTable {

    private static final char[] letterChars = buildLetterChars();
    private static final char[] letterOrDigitChars = buildLetterOrDigitChars();
    private static final char[] exprChars = buildExprChars();
    private static final char NULL = 0;
    private static final char SIZE = 128;

    private CharTable() {
    }

    private static char[] createCharArray() {
        char[] ret = new char[SIZE];
        for (char i = 0; i < SIZE; i++) {
            ret[i] = NULL;
        }
        return ret;
    }

    private static char[] buildLetterChars() {
        char[] ret = createCharArray();
        for (char i = 'a'; i <= 'z'; i++) {
            ret[i] = i;
        }
        for (char i = 'A'; i <= 'Z'; i++) {
            ret[i] = i;
        }
        ret['_'] = '_';            
        return ret;
    }

    private static char[] buildLetterOrDigitChars() {
        char[] ret = buildLetterChars();
        for (char i = '0'; i <= '9'; i++) {
            ret[i] = i;
        }
        return ret;
    }

    private static char[] buildExprChars() {
        char[] ret = createCharArray();
        ret['\t'] = '\t';
        ret['\n'] = '\n';
        ret['\r'] = '\r';
        for (char i = ' '; i <= '}'; i++) {
            ret[i] = i;
        }

        ret['#'] = NULL;
        ret['$'] = NULL;
        ret['@'] = NULL;
        ret['\\'] = NULL;
        ret['^'] = NULL;
        ret['`'] = NULL;
        return ret;
    }

    public static boolean isLetter(char c) {
        return c < SIZE && letterChars[c] != NULL;
    }

    public static boolean isLetterOrDigit(char c) {
        return c < SIZE && letterOrDigitChars[c] != NULL;
    }

    public static boolean isExprChar(char c) {
        return c < SIZE && exprChars[c] != NULL;
    }

    public static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    public static boolean isBlank(char c) {
        return c == ' ' || c == '\t';                                
    }

    public static boolean isBlankOrLineFeed(char c) {
        return c == ' ' || c == '\t' || c == '\r' || c == '\n';        
    }

    public static boolean isHexadecimalDigit(char c) {
        return (c >= '0' && c <= '9') || (c >= 'A' && c <= 'F') || (c >= 'a' && c <= 'f');
    }

    public static boolean isOctalDigit(char c) {
        return c >= '0' && c <= '7';
    }
}




