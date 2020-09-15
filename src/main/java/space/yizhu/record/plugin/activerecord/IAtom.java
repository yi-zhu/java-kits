

package space.yizhu.record.plugin.activerecord;

import java.sql.SQLException;


public interface IAtom {

    
    boolean run() throws SQLException;
}
