

package space.yizhu.record.template.stat;

import java.util.ArrayList;
import java.util.List;


class Lexer {

    static final char EOF = (char) -1;
    static final int TEXT_STATE_DIAGRAM = 999;

    char[] buf;
    int state = 0;
    int lexemeBegin = 0;
    int forward = 0;
    int beginRow = 1;
    int forwardRow = 1;
    TextToken previousTextToken = null;

    List<Token> tokens = new ArrayList<Token>();
    String fileName;

    public Lexer(StringBuilder content, String fileName) {
        int len = content.length();
        buf = new char[len + 1];
        content.getChars(0, content.length(), buf, 0);
        buf[len] = EOF;
        this.fileName = fileName;
    }

    
    public List<Token> scan() {
        while (peek() != EOF) {
            if (peek() == '#') {
                if (scanDire()) {
                    continue;
                }
                if (scanSingleLineComment()) {
                    continue;
                }
                if (scanMultiLineComment()) {
                    continue;
                }
                if (scanNoParse()) {
                    continue;
                }
            }

            scanText();
        }
        return tokens;
    }

    
    boolean scanDire() {
        String id = null;
        StringBuilder para = null;
        Token idToken = null;
        Token paraToken = null;
        while (true) {
            switch (state) {
                case 0:
                    if (peek() == '#') {                    
                        next();
                        skipBlanks();
                        state = 1;
                        continue;
                    }
                    return fail();
                case 1:
                    if (peek() == '(') {                    
                        para = scanPara("");
                        idToken = new Token(Symbol.OUTPUT, beginRow);
                        paraToken = new ParaToken(para, beginRow);
                        return addOutputToken(idToken, paraToken);
                    }
                    if (CharTable.isLetter(peek())) {        
                        state = 10;
                        continue;
                    }
                    if (peek() == '@') {                    
                        next();
                        skipBlanks();
                        if (CharTable.isLetter(peek())) {    
                            state = 20;
                            continue;
                        }
                    }
                    return fail();
                
                case 10:    
                    id = scanId();
                    Symbol symbol = Symbol.getKeywordSym(id);
                    
                    if (symbol == null) {
                        state = 11;
                        continue;
                    }

                    
                    if (symbol == Symbol.DEFINE) {
                        state = 12;
                        continue;
                    }

                    
                    if (symbol == Symbol.ELSE) {
                        if (foundFollowingIf()) {
                            id = "else if";
                            symbol = Symbol.ELSEIF;
                        }
                    }

                    
                    if (symbol.noPara()) {
                        return addNoParaToken(new Token(symbol, id, beginRow));
                    }

                    
                    skipBlanks();
                    if (peek() == '(') {
                        para = scanPara(id);
                        idToken = new Token(symbol, beginRow);
                        paraToken = new ParaToken(para, beginRow);
                        return addIdParaToken(idToken, paraToken);
                    }
                    throw new ParseException("#" + id + " directive requires parentheses \"()\"", new Location(fileName, beginRow));
                case 11:    
                    skipBlanks();
                    if (peek() == '(') {
                        para = scanPara(id);
                        idToken = new Token(Symbol.ID, id, beginRow);
                        paraToken = new ParaToken(para, beginRow);
                        return addIdParaToken(idToken, paraToken);
                    }
                    return fail();    
                case 12:            
                    skipBlanks();
                    if (CharTable.isLetter(peek())) {
                        id = scanId();    
                        skipBlanks();
                        if (peek() == '(') {
                            para = scanPara("define " + id);
                            idToken = new Token(Symbol.DEFINE, id, beginRow);
                            paraToken = new ParaToken(para, beginRow);
                            return addIdParaToken(idToken, paraToken);
                        }
                        throw new ParseException("#define " + id + " : template function definition requires parentheses \"()\"", new Location(fileName, beginRow));
                    }
                    throw new ParseException("#define directive requires identifier as a function name", new Location(fileName, beginRow));
                case 20:    
                    id = scanId();
                    skipBlanks();
                    boolean hasQuestionMark = peek() == '?';
                    if (hasQuestionMark) {
                        next();
                        skipBlanks();
                    }
                    if (peek() == '(') {
                        para = scanPara(hasQuestionMark ? "@" + id + "?" : "@" + id);
                        idToken = new Token(hasQuestionMark ? Symbol.CALL_IF_DEFINED : Symbol.CALL, id, beginRow);
                        paraToken = new ParaToken(para, beginRow);
                        return addIdParaToken(idToken, paraToken);
                    }
                    return fail();
                default:
                    return fail();
            }
        }
    }

    boolean foundFollowingIf() {
        int p = forward;
        while (CharTable.isBlank(buf[p])) {
            p++;
        }
        if (buf[p++] == 'i') {
            if (buf[p++] == 'f') {
                while (CharTable.isBlank(buf[p])) {
                    p++;
                }
                
                if (buf[p] == '(') {
                    forward = p;
                    return true;
                }
            }
        }
        return false;
    }

    
    String scanId() {
        int idStart = forward;
        while (CharTable.isLetterOrDigit(next())) {
            ;
        }
        return subBuf(idStart, forward - 1).toString();
    }

    
    StringBuilder scanPara(String id) {
        char quotes = '"';
        int localState = 0;
        int parenDepth = 1;    
        next();
        int paraStart = forward;
        while (true) {
            switch (localState) {
                case 0:
                    for (char c = peek(); true; c = next()) {
                        if (c == ')') {
                            parenDepth--;
                            if (parenDepth == 0) {    
                                next();
                                return subBuf(paraStart, forward - 2);
                            }
                            continue;
                        }

                        if (c == '(') {
                            parenDepth++;
                            continue;
                        }

                        if (c == '"' || c == '\'') {
                            quotes = c;
                            localState = 1;
                            break;
                        }

                        if (CharTable.isExprChar(c)) {
                            continue;
                        }

                        if (c == EOF) {
                            throw new ParseException("#" + id + " parameter can not match the end char ')'", new Location(fileName, beginRow));
                        }

                        throw new ParseException("#" + id + " parameter exists illegal char: '" + c + "'", new Location(fileName, beginRow));
                    }
                    break;
                case 1:
                    for (char c = next(); true; c = next()) {
                        if (c == quotes) {
                            if (buf[forward - 1] != '\\') {    
                                next();
                                localState = 0;
                                break;
                            } else {
                                continue;
                            }
                        }

                        if (c == EOF) {
                            throw new ParseException("#" + id + " parameter error, the string parameter not ending", new Location(fileName, beginRow));
                        }
                    }
                    break;
            }
        }
    }

    
    boolean scanSingleLineComment() {
        while (true) {
            switch (state) {
                case 100:
                    if (peek() == '#' && next() == '#' && next() == '#') {
                        state = 101;
                        continue;
                    }
                    return fail();
                case 101:
                    for (char c = next(); true; c = next()) {
                        if (c == '\n') {
                            if (deletePreviousTextTokenBlankTails()) {
                                return prepareNextScan(1);
                            } else {
                                return prepareNextScan(0);
                            }
                        }
                        if (c == EOF) {
                            deletePreviousTextTokenBlankTails();
                            return prepareNextScan(0);
                        }
                    }
                default:
                    return fail();
            }
        }
    }

    
    boolean scanMultiLineComment() {
        while (true) {
            switch (state) {
                case 200:
                    if (peek() == '#' && next() == '-' && next() == '-') {
                        state = 201;
                        continue;
                    }
                    return fail();
                case 201:
                    for (char c = next(); true; c = next()) {
                        if (c == '-' && buf[forward + 1] == '-' && buf[forward + 2] == '#') {
                            forward = forward + 3;
                            if (lookForwardLineFeedAndEof() && deletePreviousTextTokenBlankTails()) {
                                return prepareNextScan(peek() != EOF ? 1 : 0);
                            } else {
                                return prepareNextScan(0);
                            }
                        }
                        if (c == EOF) {
                            throw new ParseException("The multiline comment start block \"#--\" can not match the end block: \"--#\"", new Location(fileName, beginRow));
                        }
                    }
                default:
                    return fail();
            }
        }
    }

    
    boolean scanNoParse() {
        while (true) {
            switch (state) {
                case 300:
                    if (peek() == '#' && next() == '[' && next() == '[') {
                        state = 301;
                        continue;
                    }
                    return fail();
                case 301:
                    for (char c = next(); true; c = next()) {
                        if (c == ']' && buf[forward + 1] == ']' && buf[forward + 2] == '#') {
                            addTextToken(subBuf(lexemeBegin + 3, forward - 1));    
                            return prepareNextScan(3);
                        }
                        if (c == EOF) {
                            throw new ParseException("The \"no parse\" start block \"#[[\" can not match the end block: \"]]#\"", new Location(fileName, beginRow));
                        }
                    }
                default:
                    return fail();
            }
        }
    }

    boolean scanText() {
        for (char c = peek(); true; c = next()) {
            if (c == '#' || c == EOF) {
                addTextToken(subBuf(lexemeBegin, forward - 1));
                return prepareNextScan(0);
            }
        }
    }

    boolean fail() {
        if (state < 300) {
            forward = lexemeBegin;
            forwardRow = beginRow;
        }
        if (state < 100) {
            state = 100;
        } else if (state < 200) {
            state = 200;
        } else if (state < 300) {
            state = 300;
        } else {
            state = TEXT_STATE_DIAGRAM;
        }
        return false;
    }

    char next() {
        if (buf[forward] == '\n') {
            forwardRow++;
        }
        return buf[++forward];
    }

    char peek() {
        return buf[forward];
    }

    void skipBlanks() {
        while (CharTable.isBlank(buf[forward])) {
            next();
        }
    }

    
    StringBuilder subBuf(int start, int end) {
        if (start > end) {
            return null;
        }
        StringBuilder ret = new StringBuilder(end - start + 1);
        for (int i = start; i <= end; i++) {
            ret.append(buf[i]);
        }
        return ret;
    }

    boolean prepareNextScan(int moveForward) {
        for (int i = 0; i < moveForward; i++) {
            next();
        }

        state = 0;
        lexemeBegin = forward;
        beginRow = forwardRow;
        return true;
    }

    void addTextToken(StringBuilder text) {
        if (text == null || text.length() == 0) {
            return;
        }

        if (previousTextToken != null) {
            previousTextToken.append(text);
        } else {
            previousTextToken = new TextToken(text, beginRow);
            tokens.add(previousTextToken);
        }
    }

    
    boolean addOutputToken(Token idToken, Token paraToken) {
        tokens.add(idToken);
        tokens.add(paraToken);
        previousTextToken = null;
        return prepareNextScan(0);
    }

    
    boolean lookForwardLineFeedAndEof() {
        int forwardBak = forward;
        int forwardRowBak = forwardRow;
        for (char c = peek(); true; c = next()) {
            if (CharTable.isBlank(c)) {
                continue;
            }
            if (c == '\n' || c == EOF) {
                return true;
            }
            forward = forwardBak;
            forwardRow = forwardRowBak;
            return false;
        }
    }

    
    boolean addIdParaToken(Token idToken, Token paraToken) {
        tokens.add(idToken);
        tokens.add(paraToken);

        
        if (lookForwardLineFeedAndEof() && deletePreviousTextTokenBlankTails()) {
            prepareNextScan(peek() != EOF ? 1 : 0);
        } else {
            prepareNextScan(0);
        }
        previousTextToken = null;
        return true;
    }

    
    boolean addNoParaToken(Token noParaToken) {
        tokens.add(noParaToken);
        if (CharTable.isBlank(peek())) {
            next();    
        }

        if (lookForwardLineFeedAndEof() && deletePreviousTextTokenBlankTails()) {
            prepareNextScan(peek() != EOF ? 1 : 0);
        } else {
            prepareNextScan(0);
        }
        previousTextToken = null;
        return true;
    }

    
    boolean deletePreviousTextTokenBlankTails() {
        
        return previousTextToken == null || previousTextToken.deleteBlankTails();
    }
}




