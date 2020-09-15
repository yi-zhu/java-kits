

package space.yizhu.record.template.stat.ast;

import space.yizhu.record.template.EngineConfig;
import space.yizhu.record.template.Env;
import space.yizhu.record.template.expr.ast.Assign;
import space.yizhu.record.template.expr.ast.Const;
import space.yizhu.record.template.expr.ast.Expr;
import space.yizhu.record.template.expr.ast.ExprList;
import space.yizhu.record.template.io.Writer;
import space.yizhu.record.template.source.ISource;
import space.yizhu.record.template.stat.Ctrl;
import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Parser;
import space.yizhu.record.template.stat.Scope;


public class Include extends Stat {

    private Assign[] assignArray;
    private Stat stat;

    public Include(Env env, ExprList exprList, String parentFileName, Location location) {
        int len = exprList.length();
        if (len == 0) {
            throw new ParseException("The parameter of #include directive can not be blank", location);
        }
        
        Expr expr = exprList.getExpr(0);
        if (expr instanceof Const && ((Const) expr).isStr()) {
        } else {
            throw new ParseException("The first parameter of #include directive must be String", location);
        }
        
        if (len > 1) {
            for (int i = 1; i < len; i++) {
                if (!(exprList.getExpr(i) instanceof Assign)) {
                    throw new ParseException("The " + (i + 1) + "th parameter of #include directive must be an assignment expression", location);
                }
            }
        }

        parseSubTemplate(env, ((Const) expr).getStr(), parentFileName, location);
        getAssignExpression(exprList);
    }

    private void parseSubTemplate(Env env, String fileName, String parentFileName, Location location) {
        String subFileName = getSubFileName(fileName, parentFileName);
        EngineConfig config = env.getEngineConfig();
        
        ISource fileSource = config.getSourceFactory().getSource(config.getBaseTemplatePath(), subFileName, config.getEncoding());
        try {
            Parser parser = new Parser(env, fileSource.getContent(), subFileName);
            if (config.isDevMode()) {
                env.addSource(fileSource);
            }
            this.stat = parser.parse().getActualStat();
        } catch (Exception e) {
            
            throw new ParseException(e.getMessage(), location, e);
        }
    }

    
    public static String getSubFileName(String fileName, String parentFileName) {
        if (parentFileName == null) {
            return fileName;
        }
        if (fileName.startsWith("/")) {
            return fileName;
        }
        int index = parentFileName.lastIndexOf('/');
        if (index == -1) {
            return fileName;
        }
        return parentFileName.substring(0, index + 1) + fileName;
    }

    private void getAssignExpression(ExprList exprList) {
        int len = exprList.length();
        if (len > 1) {
            assignArray = new Assign[len - 1];
            for (int i = 0; i < assignArray.length; i++) {
                assignArray[i] = (Assign) exprList.getExpr(i + 1);
            }
        } else {
            assignArray = null;
        }
    }

    public void exec(Env env, Scope scope, Writer writer) {
        scope = new Scope(scope);
        if (assignArray != null) {
            evalAssignExpression(scope);
        }
        stat.exec(env, scope, writer);
        scope.getCtrl().setJumpNone();
    }

    private void evalAssignExpression(Scope scope) {
        Ctrl ctrl = scope.getCtrl();
        try {
            ctrl.setLocalAssignment();
            for (Assign assign : assignArray) {
                assign.eval(scope);
            }
        } finally {
            ctrl.setWisdomAssignment();
        }
    }
}







