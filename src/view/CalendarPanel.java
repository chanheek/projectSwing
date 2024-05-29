package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

public class CalendarPanel extends JPanel {
    private JTable calendarTable;
    private DefaultTableModel calendarModel;
    private JComboBox<Integer> yearComboBox;
    private JComboBox<String> monthComboBox;
    private YearMonth currentYearMonth;
    private Map<LocalDate, String[]> events; // Map to hold events for each date
    private Map<LocalDate, String> notes; // Map to hold notes for each date

    public CalendarPanel() {
        setLayout(new BorderLayout());

        // Initialize events and notes maps
        events = new HashMap<>();
        notes = new HashMap<>();
        loadEventsFromDatabase(); // Load events from the database

        // Create a table to display the calendar
        String[] columnNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        calendarModel = new DefaultTableModel(null, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 날짜 수정 불가능
            }
        };
        calendarTable = new JTable(calendarModel) {
            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                return new CellRenderer();
            }
        };

        // Customize table appearance
        calendarTable.setRowHeight(150);
        calendarTable.setFont(new Font("Arial", Font.PLAIN, 14));
        calendarTable.setGridColor(Color.LIGHT_GRAY);
        calendarTable.setFillsViewportHeight(true);

        // Disable cell selection highlighting
        calendarTable.setSelectionBackground(Color.WHITE);
        calendarTable.setSelectionForeground(Color.BLACK);

        // Disable cell editing for cell clicks
        calendarTable.setCellSelectionEnabled(false);

        // Add mouse listener for table cells
        calendarTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = calendarTable.rowAtPoint(e.getPoint());
                int column = calendarTable.columnAtPoint(e.getPoint());
                String cellValue = (String) calendarTable.getValueAt(row, column);
                if (cellValue != null && !cellValue.isEmpty()) {
                    String dayString = cellValue.replaceAll("<[^>]*>", "").replaceAll("\\D+", "").trim();
                    if (!dayString.isEmpty()) {
                        try {
                            int day = Integer.parseInt(dayString);
                            LocalDate clickedDate = currentYearMonth.atDay(day);
                            openNoteDialog(clickedDate);
                        } catch (NumberFormatException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });

        // Create a panel to hold the month navigation controls
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BorderLayout());
        JButton prevMonthButton = new JButton("이전");
        JButton nextMonthButton = new JButton("다음");
        JLabel monthLabel = new JLabel("", SwingConstants.CENTER);
        monthLabel.setFont(new Font("Arial", Font.BOLD, 24));
        JButton todayButton = new JButton("오늘");
        JButton goToButton = new JButton("확인"); // 추가된 버튼

        // Create year and month selectors
        yearComboBox = new JComboBox<>();
        for (int year = 2019; year <= 2029; year++) {
            yearComboBox.addItem(year);
        }
        yearComboBox.setFont(new Font("Arial", Font.PLAIN, 18));

        monthComboBox = new JComboBox<>(new String[]{
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December"
        });
        monthComboBox.setFont(new Font("Arial", Font.PLAIN, 18));

        // Add components to control panel
        JPanel navPanel = new JPanel();
        navPanel.add(prevMonthButton);
        navPanel.add(nextMonthButton);
        navPanel.add(todayButton);
        navPanel.add(goToButton); // "확인" 버튼 추가
        controlPanel.add(navPanel, BorderLayout.WEST);
        controlPanel.add(monthLabel, BorderLayout.CENTER);
        JPanel comboBoxPanel = new JPanel();
        comboBoxPanel.add(yearComboBox);
        comboBoxPanel.add(monthComboBox);
        controlPanel.add(comboBoxPanel, BorderLayout.EAST);

        // Add the table and control panel to the main panel
        add(new JScrollPane(calendarTable), BorderLayout.CENTER);
        add(controlPanel, BorderLayout.NORTH);

        // Set the initial month and year
        LocalDate currentDate = LocalDate.now();
        currentYearMonth = YearMonth.of(currentDate.getYear(), currentDate.getMonthValue());
        yearComboBox.setSelectedItem(currentYearMonth.getYear());
        monthComboBox.setSelectedIndex(currentYearMonth.getMonthValue() - 1);
        updateCalendar();

        // Add action listeners to the buttons
        prevMonthButton.addActionListener(e -> {
            currentYearMonth = currentYearMonth.minusMonths(1);
            updateComboBoxes();
            updateCalendar();
        });

        nextMonthButton.addActionListener(e -> {
            currentYearMonth = currentYearMonth.plusMonths(1);
            updateComboBoxes();
            updateCalendar();
        });

        todayButton.addActionListener(e -> {
            currentYearMonth = YearMonth.now();
            updateComboBoxes();
            updateCalendar();
        });

        goToButton.addActionListener(e -> {
            int selectedYear = (int) yearComboBox.getSelectedItem();
            int selectedMonth = monthComboBox.getSelectedIndex() + 1;
            currentYearMonth = YearMonth.of(selectedYear, selectedMonth);
            updateCalendar();
        });
    }


    private void loadEventsFromDatabase() {
        String url = "jdbc:oracle:thin:@localhost:1521/xe"; // DB URL
        String username = "kk"; // DB 사용자 이름
        String password = "kk123"; // DB 비밀번호

        String query = "SELECT yyyymmdd, event FROM manager_calendar_view";

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                LocalDate date = LocalDate.parse(resultSet.getString("yyyymmdd"));
                String event = resultSet.getString("event");

                events.putIfAbsent(date, new String[3]);
                String[] eventList = events.get(date);

                for (int i = 0; i < eventList.length; i++) {
                    if (eventList[i] == null) {
                        eventList[i] = event;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateComboBoxes() {
        yearComboBox.setSelectedItem(currentYearMonth.getYear());
        monthComboBox.setSelectedIndex(currentYearMonth.getMonthValue() - 1);
    }

    private void updateCalendar() {
        int year = currentYearMonth.getYear();
        int month = currentYearMonth.getMonthValue();

        // Clear the table
        calendarModel.setRowCount(0);

        // Update the month label
        JLabel monthLabel = (JLabel) ((JPanel) getComponent(1)).getComponent(1);
        monthLabel.setText(currentYearMonth.getYear() + "-" + (month < 10 ? "0" + month : month));

        // Get the first and last day of the month
        LocalDate firstDayOfMonth = currentYearMonth.atDay(1);
        LocalDate lastDayOfMonth = currentYearMonth.atEndOfMonth();

        // Get the start day of the week for the first day of the month
        int startDayOfWeek = firstDayOfMonth.getDayOfWeek().getValue() % 7;

        // Fill the calendar with the days of the month
        int day = 1;
        int numberOfRows = (startDayOfWeek + lastDayOfMonth.getDayOfMonth() + 6) / 7;
        for (int row = 0; row < numberOfRows; row++) {
            Object[] week = new Object[7];
            for (int column = 0; column < 7; column++) {
                if (row == 0 && column < startDayOfWeek || day > lastDayOfMonth.getDayOfMonth()) {
                    week[column] = "";
                } else {
                    LocalDate currentDate = currentYearMonth.atDay(day);
                    StringBuilder cellText = new StringBuilder("<html><b>" + day + "</b><br>");
                    if (events.containsKey(currentDate)) {
                        for (String event : events.get(currentDate)) {
                            if (event != null) {
                                cellText.append(event).append("<br>");
                            }
                        }
                    }
                    if (notes.containsKey(currentDate)) {
                        cellText.append("<i>").append(notes.get(currentDate)).append("</i>");
                    }
                    cellText.append("</html>");
                    week[column] = cellText.toString();
                    day++;
                }
            }
            calendarModel.addRow(week);
        }
    }

    private void openNoteDialog(LocalDate date) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "메모 입력", true);
        dialog.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea(10, 30);
        textArea.setText(notes.getOrDefault(date, ""));
        JScrollPane scrollPane = new JScrollPane(textArea);
        dialog.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("확인");
        JButton cancelButton = new JButton("취소");

        saveButton.addActionListener(e -> {
            notes.put(date, textArea.getText());
            updateCalendar();
            dialog.dispose();
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private class CellRenderer extends JTextPane implements TableCellRenderer {
        public CellRenderer() {
            setContentType("text/html");
            setMargin(new Insets(5, 5, 5, 5));
            setEditable(false);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value != null ? value.toString() : "");
            return this;
        }
    }
}