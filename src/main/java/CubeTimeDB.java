import java.sql.*;
import java.util.Scanner;

public class CubeTimeDB {
    static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/cube_times";
    static final String USERNAME = "alex";
    static final String PASSWORD = "blah";

    public static void main(String[] args){

        Scanner numberScanner = new Scanner(System.in);
        Scanner stringScanner = new Scanner(System.in);
        Statement statement = null;
        PreparedStatement prepStatement = null;
        Connection connection = null;

            //make the driver
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException classNotFound){
            classNotFound.printStackTrace();
            System.exit(-1);
        }

        try {
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            statement = connection.createStatement();
            //make the cube record table if its not made
            String makeTable = "CREATE TABLE IF NOT EXISTS cube_records (Cuber varchar(50), cube_time float)";
            statement.executeUpdate(makeTable);
            //make the template for inputting each row into the table
            String prepStatementInsert = "INSERT INTO cube_records VALUES ( ? , ? )";
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

            while (true) {
                //show all the cube records
                ResultSet rs = statement.executeQuery("SELECT * FROM cube_records");
                System.out.println("----Cube participants and times----");
                while (rs.next()) {
                    System.out.println(rs.getString(1) + ": " + rs.getDouble(2) + " seconds");
                }

                System.out.println("Would you like to add/edit a participant and time? Press 'q' to quit.");
                String addCuber = stringScanner.nextLine();

                if (addCuber.equalsIgnoreCase("q")) {
                    break;
                }

                System.out.println("Please enter the participant to add or edit.");
                String cuber = stringScanner.nextLine();
                double cubeTime;

                //once they enter a participant, check the database to see if theres a record of it already...
                String prepStatementGetCount = "SELECT COUNT(*) AS name_found FROM cube_records WHERE UPPER(cuber) = UPPER(?)";
                prepStatement = connection.prepareStatement(prepStatementGetCount);
                prepStatement.setString(1, cuber);
                rs = prepStatement.executeQuery();
                rs.next();
                int nameFound = rs.getInt("name_found");

                //if there isn't a time with this name, ask the user for the cube time, and add the new record
                if (nameFound == 0) {
                    System.out.println("Please enter the new cube time.");
                    cubeTime = numberScanner.nextDouble();

                    prepStatement = connection.prepareStatement(prepStatementInsert);
                    prepStatement.setString(1, cuber);
                    prepStatement.setDouble(2, cubeTime);
                    prepStatement.executeUpdate();
                }
                //if there is an exactly matching name(not counting case) for this time, update the time of that name
                else if (nameFound == 1) {
                    System.out.println("Please enter the new time for " + cuber);
                    cubeTime = numberScanner.nextDouble();

                    String prepStatementUpdate = "UPDATE cube_records SET cube_time = ? WHERE UPPER(cuber) = UPPER(?)";
                    prepStatement = connection.prepareStatement(prepStatementUpdate);
                    prepStatement.setDouble(1, cubeTime);
                    prepStatement.setString(2, cuber);
                    prepStatement.executeUpdate();
                }
            }
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        } finally{
            //close statements/connection/scanners
            numberScanner.close();
            stringScanner.close();
            try {
                statement.close();
                prepStatement.close();
                connection.close();
            } catch (SQLException sqlex){
                sqlex.printStackTrace();
            }
        }
}
}

