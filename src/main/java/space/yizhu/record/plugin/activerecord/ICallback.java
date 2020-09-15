

package space.yizhu.record.plugin.activerecord;

import java.sql.Connection;
import java.sql.SQLException;


public interface ICallback {

    
    Object call(Connection conn) throws SQLException;
}
