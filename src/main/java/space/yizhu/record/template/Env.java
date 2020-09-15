

package space.yizhu.record.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import space.yizhu.record.template.source.ISource;
import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.ast.Define;
import space.yizhu.record.template.source.ISource;
import space.yizhu.record.template.stat.Location;
import space.yizhu.record.template.stat.ParseException;
import space.yizhu.record.template.stat.ast.Define;


public class Env {

    protected EngineConfig engineConfig;
    protected Map<String, Define> functionMap = new HashMap<String, Define>(16, 0.5F);

    
    protected List<ISource> sourceList = null;

    public Env(EngineConfig engineConfig) {
        this.engineConfig = engineConfig;
    }

    public EngineConfig getEngineConfig() {
        return engineConfig;
    }

    public boolean isDevMode() {
        return engineConfig.isDevMode();
    }

    
    public void addFunction(Define function) {
        String fn = function.getFunctionName();
        if (functionMap.containsKey(fn)) {
            Define previous = functionMap.get(fn);
            throw new ParseException(
                    "Template function \"" + fn + "\" already defined in " +
                            getAlreadyDefinedLocation(previous.getLocation()),
                    function.getLocation()
            );
        }
        functionMap.put(fn, function);
    }

    private String getAlreadyDefinedLocation(Location loc) {
        StringBuilder buf = new StringBuilder();
        if (loc.getTemplateFile() != null) {
            buf.append(loc.getTemplateFile()).append(", line ").append(loc.getRow());
        } else {
            buf.append("string template line ").append(loc.getRow());
        }
        return buf.toString();
    }

    
    public Define getFunction(String functionName) {
        Define func = functionMap.get(functionName);
        return func != null ? func : engineConfig.getSharedFunction(functionName);
    }

    
    Map<String, Define> getFunctionMap() {
        return functionMap;
    }

    
    public boolean isSourceListModified() {
        if (sourceList != null) {
            for (int i = 0, size = sourceList.size(); i < size; i++) {
                if (sourceList.get(i).isModified()) {
                    return true;
                }
            }
        }
        return false;
    }

    
    public void addSource(ISource source) {
        if (sourceList == null) {
            sourceList = new ArrayList<ISource>();
        }
        sourceList.add(source);
    }
}



