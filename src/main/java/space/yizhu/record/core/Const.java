

package space.yizhu.record.core;

import space.yizhu.record.render.ViewType;


public interface Const {

    String JFINAL_VERSION = "3.6";

    ViewType DEFAULT_VIEW_TYPE = ViewType.JFINAL_TEMPLATE;

    String DEFAULT_BASE_UPLOAD_PATH = "upload";

    String DEFAULT_BASE_DOWNLOAD_PATH = "download";

    String DEFAULT_ENCODING = "UTF-8";

    boolean DEFAULT_DEV_MODE = false;

    String DEFAULT_URL_PARA_SEPARATOR = "-";

    String DEFAULT_VIEW_EXTENSION = ".html";

    int DEFAULT_MAX_POST_SIZE = 1024 * 1024 * 10;            

    int DEFAULT_I18N_MAX_AGE_OF_COOKIE = 999999999;

    int DEFAULT_FREEMARKER_TEMPLATE_UPDATE_DELAY = 3600;    

    String DEFAULT_TOKEN_NAME = "_jfinal_token";

    int DEFAULT_SECONDS_OF_TOKEN_TIME_OUT = 900;            

    int MIN_SECONDS_OF_TOKEN_TIME_OUT = 300;                

    int DEFAULT_CONFIG_PLUGIN_ORDER = 2;


    boolean DEFAULT_INJECT_DEPENDENCY = false;
}

