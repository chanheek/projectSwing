package view;

import dao.EventDaoImpl;
import vo.EventCalendarVo;
import vo.EventVo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

public class CalendarPanel extends JPanel {
    private JTable calendarTable;
    private DefaultTableModel calendarModel;
    private JComboBox<Integer> yearComboBox;
    private JComboBox<Integer> monthComboBox;
    private YearMonth currentYearMonth;
    private Map<LocalDate, String[]> events; // Map to hold events for each date
    private Map<LocalDate, String> notes; // Map to hold notes for each date
    private int eventCalendarId;

    public CalendarPanel(int eventCalendarId) {
        this.eventCalendarId = eventCalendarId;
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
                            showEventDialog(clickedDate, e.getX(), e.getY());
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
        JButton addEventButton = new JButton("일정 추가"); // 일정 추가 버튼

        // Create year and month selectors
        yearComboBox = new JComboBox<>();
        for (int year = 2019; year <= 2029; year++) {
            yearComboBox.addItem(year);
        }
        yearComboBox.setFont(new Font("Arial", Font.PLAIN, 18));

        monthComboBox = new JComboBox<>();
        for (int month = 1; month <= 12; month++) {
            monthComboBox.addItem(month);
        }
        monthComboBox.setFont(new Font("Arial", Font.PLAIN, 18));

        JButton goToButton = new JButton("확인"); // 추가된 버튼

        // Add components to control panel
        JPanel navPanel = new JPanel();
        navPanel.add(prevMonthButton);
        navPanel.add(nextMonthButton);
        navPanel.add(todayButton);
        navPanel.add(addEventButton); // "일정 추가" 버튼 추가
        controlPanel.add(navPanel, BorderLayout.WEST);
        controlPanel.add(monthLabel, BorderLayout.CENTER);
        JPanel comboBoxPanel = new JPanel();
        comboBoxPanel.add(yearComboBox);
        comboBoxPanel.add(monthComboBox);
        comboBoxPanel.add(goToButton); // "확인" 버튼 추가
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
            int selectedMonth = (int) monthComboBox.getSelectedItem();
            currentYearMonth = YearMonth.of(selectedYear, selectedMonth);
            updateCalendar();
        });

        addEventButton.addActionListener(e -> openAddEventDialog());
    }

    private void loadEventsFromDatabase() {
        String url = "jdbc:oracle:thin:@localhost:1521/xe"; // DB URL
        String username = "kk"; // DB 사용자 이름
        String password = "kk123"; // DB 비밀번호

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            EventDaoImpl eventDao = new EventDaoImpl(connection);
            EventCalendarVo eventCalendar = new EventCalendarVo(); // Ensure this has an appropriate ID set if needed
            eventCalendar.setId(this.eventCalendarId); // Use the event calendar ID passed to the constructor
            eventDao.getAllEvent(eventCalendar);
            this.events = eventCalendar.getEvents();
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
                    StringBuilder cellText = new StringBuilder("<html>" + day + "<br>");
                    if (events.containsKey(currentDate)) {
                        for (String event : events.get(currentDate)) {
                            if (event != null) {
                                cellText.append("<a href='#' style='color:black;text-decoration:none;'>").append(event).append("</a><br>");
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

    private void openAddEventDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "일정 추가", true);
        dialog.setLayout(new BorderLayout());

        JPanel datePanel = new JPanel(new GridLayout(2, 2));
        JLabel startDateLabel = new JLabel("시작 날짜:");
        JLabel endDateLabel = new JLabel("끝나는 날짜:");

        JComboBox<Integer> startYearComboBox = new JComboBox<>();
        JComboBox<Integer> startMonthComboBox = new JComboBox<>();
        JComboBox<Integer> startDayComboBox = new JComboBox<>();
        JComboBox<Integer> endYearComboBox = new JComboBox<>();
        JComboBox<Integer> endMonthComboBox = new JComboBox<>();
        JComboBox<Integer> endDayComboBox = new JComboBox<>();

        for (int year = 2019; year <= 2029; year++) {
            startYearComboBox.addItem(year);
            endYearComboBox.addItem(year);
        }
        for (int month = 1; month <= 12; month++) {
            startMonthComboBox.addItem(month);
            endMonthComboBox.addItem(month);
        }

        // Add action listener to update day combo box based on selected year and month
        startYearComboBox.addActionListener(e -> updateDayComboBox(startYearComboBox, startMonthComboBox, startDayComboBox));
        startMonthComboBox.addActionListener(e -> updateDayComboBox(startYearComboBox, startMonthComboBox, startDayComboBox));
        endYearComboBox.addActionListener(e -> updateDayComboBox(endYearComboBox, endMonthComboBox, endDayComboBox));
        endMonthComboBox.addActionListener(e -> updateDayComboBox(endYearComboBox, endMonthComboBox, endDayComboBox));

        updateDayComboBox(startYearComboBox, startMonthComboBox, startDayComboBox); // 초기화
        updateDayComboBox(endYearComboBox, endMonthComboBox, endDayComboBox); // 초기화

        datePanel.add(startDateLabel);
        datePanel.add(createDateSelectorPanel(startYearComboBox, startMonthComboBox, startDayComboBox));
        datePanel.add(endDateLabel);
        datePanel.add(createDateSelectorPanel(endYearComboBox, endMonthComboBox, endDayComboBox));

        JTextArea textArea = new JTextArea(10, 30);
        JScrollPane scrollPane = new JScrollPane(textArea);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        JButton saveButton = new JButton("확인");
        JButton cancelButton = new JButton("취소");

        JPanel buttonRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonRightPanel.add(saveButton);
        buttonRightPanel.add(cancelButton);
        buttonPanel.add(buttonRightPanel, BorderLayout.CENTER);

        saveButton.addActionListener(e -> {
            try {
                int startYear = (int) startYearComboBox.getSelectedItem();
                int startMonth = (int) startMonthComboBox.getSelectedItem();
                int startDay = (int) startDayComboBox.getSelectedItem();
                LocalDate startDate = LocalDate.of(startYear, startMonth, startDay);

                int endYear = (int) endYearComboBox.getSelectedItem();
                int endMonth = (int) endMonthComboBox.getSelectedItem();
                int endDay = (int) endDayComboBox.getSelectedItem();
                LocalDate endDate = LocalDate.of(endYear, endMonth, endDay);

                if (endDate.isBefore(startDate)) {
                    throw new DateTimeException("End date cannot be before start date");
                }

                String note = textArea.getText();

                // Create and save the event to the database
                String event = note; // Assuming the note is the event detail
                EventVo eventVo = new EventVo();
                eventVo.setEventCalendarId(eventCalendarId);
                eventVo.setEvent(event);
                eventVo.setStartDate(String.valueOf(startDate));
                eventVo.setEndDate(String.valueOf(endDate));

                String url = "jdbc:oracle:thin:@localhost:1521/xe"; // DB URL
                String username = "kk"; // DB 사용자 이름
                String password = "kk123"; // DB 비밀번호

                try (Connection connection = DriverManager.getConnection(url, username, password)) {
                    EventDaoImpl eventDao = new EventDaoImpl(connection);
                    eventDao.createEvent(eventVo);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                // Add the note to the notes map and update the calendar
                for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                    // Add the event to the events map
                    events.computeIfAbsent(date, k -> new String[0]);
                    String[] currentEvents = events.get(date);
                    String[] newEvents = new String[currentEvents.length + 1];
                    System.arraycopy(currentEvents, 0, newEvents, 0, currentEvents.length);
                    newEvents[currentEvents.length] = event;
                    events.put(date, newEvents);
                }
                updateCalendar();
                dialog.dispose();
            } catch (DateTimeException ex) {
                JOptionPane.showMessageDialog(CalendarPanel.this, "날짜 입력이 유효하지 않습니다.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.add(datePanel, BorderLayout.NORTH);
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setModal(false); // 모달 고정 해제
        dialog.setVisible(true);
    }

    private void updateDayComboBox(JComboBox<Integer> yearComboBox, JComboBox<Integer> monthComboBox, JComboBox<Integer> dayComboBox) {
        int year = (int) yearComboBox.getSelectedItem();
        int month = (int) monthComboBox.getSelectedItem();
        YearMonth yearMonth = YearMonth.of(year, month);
        int daysInMonth = yearMonth.lengthOfMonth();

        dayComboBox.removeAllItems();
        for (int day = 1; day <= daysInMonth; day++) {
            dayComboBox.addItem(day);
        }
    }

    private JPanel createDateSelectorPanel(JComboBox<Integer> yearComboBox, JComboBox<Integer> monthComboBox, JComboBox<Integer> dayComboBox) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3));
        panel.add(yearComboBox);
        panel.add(monthComboBox);
        panel.add(dayComboBox);
        return panel;
    }

    private void showEventDialog(LocalDate date, int x, int y) {
        if (events.containsKey(date)) {
            String[] eventList = events.get(date);
            JPopupMenu eventMenu = new JPopupMenu();
            for (String event : eventList) {
                if (event != null) {
                    JMenuItem menuItem = new JMenuItem(event);
                    menuItem.addActionListener(e -> {
                        EventVo eventVo = new EventVo();
                        eventVo.setEventCalendarId(eventCalendarId);
                        try {
                            String url = "jdbc:oracle:thin:@localhost:1521/xe"; // DB URL
                            String username = "kk"; // DB 사용자 이름
                            String password = "kk123"; // DB 비밀번호
                            try (Connection connection = DriverManager.getConnection(url, username, password)) {
                                EventDaoImpl eventDao = new EventDaoImpl(connection);
                                int eventId = eventDao.getEventId(eventVo, event, date);
                                eventVo.setId(eventId);

                                // Now you have the event ID, you can do further actions if needed
                                showEventDetails(eventVo, event);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                    eventMenu.add(menuItem);
                }
            }
            eventMenu.show(calendarTable, x, y);
        }
    }

    private void showEventDetails(EventVo eventVo, String event) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "일정 세부 정보", true);
        dialog.setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea(10, 30);
        textArea.setEditable(false);
        textArea.setText(event);
        JScrollPane scrollPane = new JScrollPane(textArea);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton deleteButton = new JButton("삭제");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int result = JOptionPane.showConfirmDialog(null, "일정을 삭제하시겠습니까?", "일정 삭제", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    String url = "jdbc:oracle:thin:@localhost:1521/xe"; // DB URL
                    String username = "kk"; // DB 사용자 이름
                    String password = "kk123"; // DB 비밀번호

                    try (Connection connection = DriverManager.getConnection(url, username, password)) {
                        EventDaoImpl eventDao = new EventDaoImpl(connection);
                        eventDao.deleteEvent(eventVo.getId());

                        // Reload events from the database
                        loadEventsFromDatabase();

                        // Immediately update the calendar
                        updateCalendar();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    JOptionPane.showMessageDialog(null, "일정이 삭제되었습니다.", "일정 삭제", JOptionPane.YES_OPTION);
                    dialog.dispose();
                }
            }
        });


        JButton closeButton = new JButton("닫기");
        closeButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(deleteButton);
        buttonPanel.add(closeButton);

        dialog.add(scrollPane, BorderLayout.CENTER);
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
