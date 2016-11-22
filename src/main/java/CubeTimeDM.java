import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CubeTimeDM extends AbstractTableModel{


    private int rows = 0;

    ResultSet rs;

    public CubeTimeDM(ResultSet rs){
        this.rs = rs;
    }

    @Override
    public int getRowCount() {

        countRows();

        return rows;
    }

public void countRows(){
    rows = 0;
    try {
        rs.beforeFirst();
        while (rs.next()){
            rows++;
        }
    } catch (SQLException sqlex){
        System.out.println(sqlex);
    }
}

    @Override
    public int getColumnCount() {
        try {
            return rs.getMetaData().getColumnCount();
        } catch (SQLException sqlex) {
            System.out.println(sqlex);
            return 0;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try{
            rs.absolute(rowIndex+1);
            Object o = rs.getObject(columnIndex+1);
            return o.toString();
        } catch (SQLException sqlex){
            System.out.println(sqlex);
            return sqlex.toString();
        }
    }

    @Override
    public boolean isCellEditable(int row, int col){
        if (col == 1){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void setValueAt(Object newDouble, int row, int column){
        double newTime;
        try {
            newTime = Double.parseDouble(newDouble.toString());
        } catch (Exception ex) {
            System.out.println(ex);
            return;
        }

        try {
            rs.absolute(row +1);
            rs.updateDouble("cube_time", newTime);
            rs.updateRow();
            fireTableDataChanged();
        } catch (SQLException sqlex){
            System.out.println(sqlex);
        }
    }
    public void addRow (String cuber, double cubeTime){
        try{
            rs.moveToInsertRow();
            rs.updateString("Cuber", cuber);
            rs.updateDouble("cube_time", cubeTime);
            rs.insertRow();
            rs.moveToCurrentRow();
            fireTableDataChanged();
        } catch (SQLException sqlex){
            System.out.println(sqlex);
        }
    }

    public void deleteRow(int row){
        try {
            rs.absolute(row + 1);
            rs.deleteRow();
            fireTableDataChanged();


        } catch (SQLException sqlex){
            System.out.println(sqlex);
        }
    }

    public void updateModel(ResultSet rs){
        this.rs = rs;
        countRows();

    }


}
