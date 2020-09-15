

package space.yizhu.record.plugin.activerecord;


public class PageSqlKit {

    private static final int start = "select ".length();

    private static final char NULL = 0;
    private static final char SIZE = 128;
    private static char[] charTable = buildCharTable();

    private static char[] buildCharTable() {
        char[] ret = new char[SIZE];
        for (char i = 0; i < SIZE; i++) {
            ret[i] = NULL;
        }

        ret['('] = '(';
        ret[')'] = ')';

        ret['f'] = 'f';
        ret['F'] = 'f';
        ret['r'] = 'r';
        ret['R'] = 'r';
        ret['o'] = 'o';
        ret['O'] = 'o';
        ret['m'] = 'm';
        ret['M'] = 'm';

        ret[' '] = ' ';
        ret['\r'] = ' ';
        ret['\n'] = ' ';
        ret['\t'] = ' ';
        return ret;
    }

    
    private static int getIndexOfFrom(String sql) {
        int parenDepth = 0;
        char c;
        for (int i = start, end = sql.length() - 5; i < end; i++) {
            c = sql.charAt(i);
            if (c >= SIZE) {
                continue;
            }

            c = charTable[c];
            if (c == NULL) {
                continue;
            }

            if (c == '(') {
                parenDepth++;
                continue;
            }

            if (c == ')') {
                if (parenDepth == 0) {
                    throw new RuntimeException("Can not match left paren '(' for right paren ')': " + sql);
                }
                parenDepth--;
                continue;
            }
            if (parenDepth > 0) {
                continue;
            }

            if (c == 'f'
                    && charTable[sql.charAt(i + 1)] == 'r'
                    && charTable[sql.charAt(i + 2)] == 'o'
                    && charTable[sql.charAt(i + 3)] == 'm') {
                c = sql.charAt(i + 4);
                
                if (charTable[c] == ' ' || c == '(') {        
                    c = sql.charAt(i - 1);
                    if (charTable[c] == ' ' || c == ')') {    
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    public static String[] parsePageSql(String sql) {
        int index = getIndexOfFrom(sql);
        if (index == -1) {
            return null;
        }

        String[] ret = new String[2];
        ret[0] = sql.substring(0, index);
        ret[1] = sql.substring(index);
        return ret;
    }
}



