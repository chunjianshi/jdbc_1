package org.example;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Properties;

/**
 * @author shichunjian
 * @create 2024-10-17 14:26
 */
public class ConnectionTest {
    /*// 方式一：
    @Test
    public void testConnection1() throws SQLException {
        // 获取Driver实现类对象
        Driver driver = new com.mysql.jdbc.Driver();

        // url:http://localhost:8080/gmall/keyboard.jpg
        // jdbc:mysql:协议
        // localhost:ip地址
        // 3306：默认mysql的端口号
        // test:test数据库
        String url = "jdbc:mysql://192.168.20.33:3306/test";
        // 将用户名和密码封装在Properties中
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "PasswordForTest2024_");

        Connection conn = driver.connect(url, info);

        System.out.println(conn);
    }*/
    //方式五(final版)：将数据库连接需要的4个基本信息声明在配置文件中，通过读取配置文件的方式，获取连接
    /*
     * 此种方式的好处？
     * 1.实现了数据与代码的分离。实现了解耦
     * 2.如果需要修改配置文件信息，可以避免程序重新打包。
     */
   @Test
   public void getConnection5() throws Exception{

       //1.读取配置文件中的4个基本信息
       InputStream is = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
//       if (is == null) {
//           throw new FileNotFoundException("Property file 'jdbc.properties' not found in the classpath");
//       }

       Properties pros = new Properties();
       pros.load(is);
//       System.out.println("Classpath: " + System.getProperty("java.class.path"));

       String user = pros.getProperty("user");
       String password = pros.getProperty("password");
       String url = pros.getProperty("url");
       String driverClass = pros.getProperty("driverClass");

       //2.加载驱动
       Class.forName(driverClass);

       //3.获取连接
       Connection conn = DriverManager.getConnection(url, user, password);
       System.out.println(conn);

       //4、预编译SQL语句，返回PreparedStatement实例
       String sql = "insert into customers(name,email,birth)values(?,?,?)";
       PreparedStatement ps = conn.prepareStatement(sql);

       //5、填充占位符
       ps.setString(1,"史春见");
       ps.setString(2,"2373538479@qq.com");
       SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd");
       java.util.Date date = sdf.parse("1991-04-12");
       ps.setDate(3,new Date(date.getTime()));

        //6、执行SQL操作
       ps.execute();
       System.out.println("SQL执行完毕！");

       //7、资源关闭
       ps.close();
       conn.close();
       System.out.println("资源关闭完成");


   }
}
