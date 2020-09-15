

package space.yizhu.record.plugin.activerecord;


public interface IDbProFactory {

    DbPro getDbPro(String configName);

    static final IDbProFactory defaultDbProFactory = new IDbProFactory() {
        public DbPro getDbPro(String configName) {
            return new DbPro(configName);
        }
    };
}


