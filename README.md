# java-kits
**this is a kits for java**

上传此工具的目的是为了让更多的人免于造重复的轮子,学会自己造轮子

## 引用示例：
```
<dependency>
  <groupId>space.yizhu</groupId>
  <artifactId>kits</artifactId>
  <version>1.2.1</version>
</dependency>

```



[yizhu] (http://yizhu.space)`
http://yizhu.space
https://yizhu.space
暂无网站
xiuxingzhe@yeah.net`

user 中的.m2文件夹尽量保存

## 发布到maven 需要以下步骤

### 安装gpg 生成密钥
```
gpg --gen-key 

gpg --keyserver hkp://keyserver.ubuntu.com:11371 --send-keys 指纹后16位

```

### 打包
```
mvn install

mvn  deploy 

```

~~参数(跳过文档和测试)，加了是上传不到maven仓库的，maven库必须含有doc文档~~
` -Dmaven.javadoc.skip=true -Dmaven.test.skip=true`
# 附录一

 ## maven setting.xml
 ```xml

<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                          https://maven.apache.org/xsd/settings-1.0.0.xsd">
  <servers>
	<server>
		<id>sonatype</id>
		<username>用户名</username>
		<password>密码</password>
	</server>
  </servers>
    <profile>
      <id>gpg-dev</id>
      <activation>
        <property>
          <name>target-gpg</name>
          <value>dev</value>
        </property>
      </activation>
      <properties>
    <gpg.executable>gpg</gpg.executable>
    <gpg.passphrase>gpg密钥</gpg.passphrase>
      </properties>
    </profile>
      <mirrors>
        <mirror>  
            <id>alimaven</id>  
            <name>aliyun maven</name>  
            <url>http://maven.aliyun.com/nexus/content/groups/public/</url>  
            <mirrorOf>central</mirrorOf>          
        </mirror>  
      </mirrors>
</settings>


```