import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Ranking {
    public static void main(String[] args) {
        Connection con = null;

        String server = "localhost:3308"; // MySQL 서버 주소
        String database = "sys"; // MySQL DATABASE 이름
        String user_name = "root"; // MySQL 서버 아이디
        String password = "alflarhkgkrrh1!"; // MySQL 서버 비밀번호

        // 1.드라이버 로딩
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println(" !! <JDBC 오류> Driver load 오류: " + e.getMessage());
            e.printStackTrace();
        }

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        // 2.연결
        try {
            con = DriverManager.getConnection("jdbc:mysql://" + server + "/" + database + "?useSSL=false", user_name, password);

            // SQL 쿼리문
            statement = con.createStatement();
            String sql = "SELECT * FROM gaemoewtbl";
            resultSet = statement.executeQuery(sql);

            // 결과 저장을 위한 리스트 생성
            List<RankData> rankList = new ArrayList<>();

            // 결과 저장
            while (resultSet.next()) {
                String ranking = resultSet.getString("ranking");
                String name = resultSet.getString("uname");
                String score = resultSet.getString("score");

                // RankData 객체 생성하여 리스트에 추가
                RankData rankData = new RankData(ranking, name, score);
                rankList.add(rankData);
            }

            // 순위로 정렬
            Collections.sort(rankList, new Comparator<RankData>() {
                @Override
                public int compare(RankData r1, RankData r2) {
                    int score1 = Integer.parseInt(r1.getScore());
                    int score2 = Integer.parseInt(r2.getScore());

                    return Integer.compare(score2, score1); // 내림차순 정렬
                }
            });

            // 정렬된 결과 출력
            for (int i = 0; i < rankList.size(); i++) {
                RankData rankData = rankList.get(i);
                System.out.println("Rank: " + (i + 1) + ", Name: " + rankData.getName() + ", Score: " + rankData.getScore());
            }

            System.out.println("정상적으로 연결되었습니다.");

        } catch (SQLException e) {
            System.err.println("con 오류:" + e.getMessage());
            e.printStackTrace();
        }

        // 3.해제
        try {
            if (con != null)
                con.close();
        } catch (SQLException e) {
        }
    }

    // RankData 클래스 정의
    static class RankData {
        private String ranking;
        private String name;
        private String score;

        public RankData(String ranking, String name, String score) {
            this.ranking = ranking;
            this.name = name;
            this.score = score;
        }

        public String getRanking() {
            return ranking;
        }

        public String getName() {
            return name;
        }

        public String getScore() {
            return score;
        }
    }
}
