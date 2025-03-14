package mybatis.common;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;

import javax.sql.DataSource;

public class MyBatisConfig {
    private static SqlSessionFactory sqlSessionFactory;

    static {
        try {
            // 데이터베이스 연결 정보 설정
            DataSource dataSource = new PooledDataSource(
                "com.mysql.cj.jdbc.Driver",  
                "jdbc:mysql://localhost:3306/phonestoredb",
                "root",  
                ""   
            );

            // 환경 설정 (트랜잭션 관리)
            Environment environment = new Environment("development", new JdbcTransactionFactory(), dataSource);
            Configuration configuration = new Configuration(environment);

            // 매퍼 등록 (여기에 DAO 클래스 추가)
            configuration.addMapper(mybatis.dao.ProductDAO.class);
            configuration.addMapper(mybatis.dao.CustomerDAO.class);
            configuration.addMapper(mybatis.dao.OrderDAO.class);
            

            
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("MyBatis 설정 로드 실패");
        }
    }

    public static SqlSession getSession() {
        return sqlSessionFactory.openSession(true);
    }
}
