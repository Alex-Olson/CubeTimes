import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class CubeTimeGUI extends JFrame {
    private JPanel rootPanel;
    private JTable cubeTimeTable;
    private JTextField cuberTextField;
    private JTextField timeTextField;
    private JButton addTimeButton;
    private JButton deleteTimeButton;

    CubeTimeGUI(final CubeTimeDM cubeTimeDM){
        super("Cube Times");
        setContentPane(rootPanel);
        pack();
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        setVisible(true);

        cubeTimeTable.setModel(cubeTimeDM);


        addTimeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cubeTimeDM.addRow(cuberTextField.getText(), Double.parseDouble(timeTextField.getText()));
            }
        });
        deleteTimeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cubeTimeTable.getSelectedRow() != -1){
                    cubeTimeDM.deleteRow(cubeTimeTable.getSelectedRow());
                    CubeTimeDB.getCubeTimes();
                }

            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                CubeTimeDB.closeDB();
                dispose();
                System.exit(0);
            }
        });
    }
}
