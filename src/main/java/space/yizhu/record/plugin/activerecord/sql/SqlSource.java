package space.yizhu.record.plugin.activerecord.sql;

import space.yizhu.record.template.source.ISource;
import space.yizhu.record.template.source.ISource;


class SqlSource {

    String file;
    ISource source;

    SqlSource(String file) {
        this.file = file;
        this.source = null;
    }

    SqlSource(ISource source) {
        this.file = null;
        this.source = source;
    }

    boolean isFile() {
        return file != null;
    }
}



