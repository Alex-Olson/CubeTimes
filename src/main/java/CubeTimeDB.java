import java.sql.*;
import java.util.Scanner;

public class CubeTimeDB {
    static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/cube_times";
    static final String USERNAME = "alex";
    static final String PASSWORD = "blah";


    static Statement statement = null;
    static PreparedStatement prepStatement = null;
    static Connection connection = null;
    static ResultSet rs = null;
    static String prepStatementInsert = null;

    private static CubeTimeDM cubeTimeDM;

    public static void main(String[] args) {


        //make the driver
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException classNotFound) {
            classNotFound.printStackTrace();
            System.exit(-1);
        }

        try {
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            //check to see if the table has already been made
            String showTable = "SHOW TABLES LIKE 'cube_records'";
            rs = statement.executeQuery(showTable);
            if (!rs.next()) {
                //make the cube record table if its not made
                String makeTable = "CREATE TABLE IF NOT EXISTS cube_records (Cuber varchar(50) NOT NULL, cube_time float, PRIMARY KEY (Cuber))";
                statement.executeUpdate(makeTable);
                //make the template for inputting each row into the table
                prepStatementInsert = "INSERT INTO cube_records VALUES ( ? , ? )";
                prepStatement = connection.prepareStatement(prepStatementInsert);
                //record all the info for each cuber and their times
                String[] cubers = {"Cubestormer II robot", "Fakhri Raihaan (using feet)", "Ruxin Liu (3 years old)", "Mats Valk (human WR holder)"};
                double[] cubeTimes = {5.270, 27.93, 99.33, 6.27};
                //add the cubers/times to the table
                for (int i = 0; i < cubers.length; i++) {
                    prepStatement.setString(1, cubers[i]);
                    prepStatement.setDouble(2, cubeTimes[i]);
                    prepStatement.executeUpdate();
                }
            }
            //set up the resultset
            getCubeTimes();
            //make the gui
            CubeTimeGUI gui = new CubeTimeGUI(cubeTimeDM);
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
    }

public static void getCubeTimes(){
    try {
        String selectAll = "SELECT * FROM cube_records";
        rs = statement.executeQuery(selectAll);

        if (cubeTimeDM == null) {
            cubeTimeDM = new CubeTimeDM(rs);
        } else {
            cubeTimeDM.updateModel(rs);
        }
    } catch (SQLException sqlex){
        System.out.println(sqlex);
    }
}

public static void closeDB(){
    try {
        statement.close();
        if (prepStatement != null){
            prepStatement.close();
        }
        connection.close();
    } catch (SQLException sqlex){
        sqlex.printStackTrace();
    }
}

}

