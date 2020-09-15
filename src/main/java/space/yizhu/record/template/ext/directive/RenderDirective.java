

package space.yizhu.record.template.ext.directive;

import java.util.Map;

import space.yizhu.kits.SyncWriteMap;
import space.yizhu.record.template.Directive;
import space.yizhu.record.template.EngineConfig;
import space.yizhu.record.template.Env;
import space.yizhu.record.template.TemplateException;
import space.yizhu.record.template.expr.ast.Assign;
import space.yizhu.record.template.expr.ast.ExprList;
import space.yizhu.record.template.io.Writer;
import space.yizhu.record.template.source.ISource;
import space.yizhu.record.template.stat.Ctrl;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.Parser;
import space.yizhu.record.template.stat.Scope;
import space.yizhu.record.template.stat.ast.Define;
import space.yizhu.record.template.stat.ast.Include;
import space.yizhu.record.template.stat.ast.Stat;
import space.yizhu.record.template.stat.ast.StatList;


public class RenderDirective extends Directive {

    private String parentFileName;
    private Map<String, SubStat> subStatCache = new SyncWriteMap<String, SubStat>(16, 0.5F);

    public void setExprList(ExprList exprList) {
        int len = exprList.length();
        if (len == 0) {
            throw new ParseException("The parameter of #render directive can not be blank", location);
        }
        if (len > 1) {
            for (int i = 1; i < len; i++) {
                if (!(exprList.getExpr(i) instanceof Assign)) {
                    throw new ParseException("The " + (i + 1) + "th parameter of #render directive must be an assignment expression", location);
                }
            }
        }

        
        this.parentFileName = location.getTemplateFile();
        this.exprList = exprList;
    }

    
    private Object evalAssignExpressionAndGetFileName(Scope scope) {
        Ctrl ctrl = scope.getCtrl();
        try {
            ctrl.setLocalAssignment();
            return exprList.evalExprList(scope)[0];
        } finally {
            ctrl.setWisdomAssignment();
        }
    }

    public void exec(Env env, Scope scope, Writer writer) {
        
        scope = new Scope(scope);

        Object value = evalAssignExpressionAndGetFileName(scope);
        if (!(value instanceof String)) {
            throw new TemplateException("The parameter value of #render directive must be String", location);
        }

        String subFileName = Include.getSubFileName((String) value, parentFileName);
        SubStat subStat = subStatCache.get(subFileName);
        if (subStat == null) {
            subStat = parseSubStat(env, subFileName);
            subStatCache.put(subFileName, subStat);
        } else if (env.isDevMode()) {
            
            if (subStat.source.isModified() || subStat.env.isSourceListModified()) {
                subStat = parseSubStat(env, subFileName);
                subStatCache.put(subFileName, subStat);
            }
        }

        subStat.exec(null, scope, writer);    

        scope.getCtrl().setJumpNone();
    }

    private SubStat parseSubStat(Env env, String subFileName) {
        EngineConfig config = env.getEngineConfig();
        
        ISource subFileSource = config.getSourceFactory().getSource(config.getBaseTemplatePath(), subFileName, config.getEncoding());

        try {
            SubEnv subEnv = new SubEnv(env);
            StatList subStatList = new Parser(subEnv, subFileSource.getContent(), subFileName).parse();
            return new SubStat(subEnv, subStatList.getActualStat(), subFileSource);
        } catch (Exception e) {
            throw new ParseException(e.getMessage(), location, e);
        }
    }

    public static class SubStat extends Stat {
        public SubEnv env;
        public Stat stat;
        public ISource source;

        public SubStat(SubEnv env, Stat stat, ISource source) {
            this.env = env;
            this.stat = stat;
            this.source = source;
        }

        @Override
        public void exec(Env env, Scope scope, Writer writer) {
            stat.exec(this.env, scope, writer);
        }
    }

    
    public static class SubEnv extends Env {
        public Env parentEnv;

        public SubEnv(Env parentEnv) {
            super(parentEnv.getEngineConfig());
            this.parentEnv = parentEnv;
        }

        
        @Override
        public Define getFunction(String functionName) {
            Define func = functionMap.get(functionName);
            return func != null ? func : parentEnv.getFunction(functionName);
        }
    }
}




