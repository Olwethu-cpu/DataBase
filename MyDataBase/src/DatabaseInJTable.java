import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/* Sample application to show your entire database in JTable */

public class DatabaseInJTable extends JFrame {

    /**
     * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		DatabaseInJTable tab = new DatabaseInJTable();
	}

	public DatabaseInJTable() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("Information Table");

		Vector<String> columnNames = new Vector<String>();
		Vector<Vector<Object>> data = new Vector<Vector<Object>>();

		try (Connection connect = DriverManager
				.getConnection("jdbc:odbc:Registration");
				Statement st = connect.createStatement();) {
			Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			String sql = "SELECT * FROM users";
			ResultSet rs = st.executeQuery(sql);
			ResultSetMetaData metaData = rs.getMetaData();
			int columns = metaData.getColumnCount();
			for (int i = 1; i <= columns; i++) {
				columnNames.addElement(metaData.getColumnName(i));
			}
			while (rs.next()) {
				Vector<Object> row = new Vector<Object>(columns);
				for (int i = 1; i <= columns; i++) {
					row.addElement(rs.getObject(i));
				}
				data.addElement(row);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		// not editable JTable
		DefaultTableModel model = new DefaultTableModel(data, columnNames) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		JTable table = new JTable(model);
		table.setRowSelectionAllowed(true);
		JScrollPane scrollPane = new JScrollPane(table);
		getContentPane().add(scrollPane);
		JPanel buttonPanel = new JPanel();
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		pack();
		setVisible(true);
	}
}