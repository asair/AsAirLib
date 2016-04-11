/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.manager;

import api.model.AsAirLibDataProvider;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.LinkedList;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author furma_000
 */
public class AsAirLibGuiManagerFrame extends JFrame implements ActionListener, KeyListener, ListSelectionListener {
    private final static String CATEGORY_SINGLE = "CatSingle";
    private final static String CATEGORY_DOUBLE = "CatDouble";
    private final static String CATEGORY_FILTER_TEXT = "category!filter_text";
        
    private final AsAirLibDataProvider data;
    private LinkedList<String[]> dataCache;
    private final HashMap<String, Object> items;
    
    public AsAirLibGuiManagerFrame(AsAirLibDataProvider dataProvider) {
        super("AsAir Library Mnager");
        
        this.data = dataProvider;
        this.dataCache = new LinkedList<>();
        this.items = new HashMap<>();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void openManager() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        getContentPane().add(mainPanel);
        
        // data type radio
        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEADING));
        mainPanel.add(radioPanel, BorderLayout.PAGE_START);
        
        ButtonGroup dataTypeRadio = new ButtonGroup();
        boolean firstRadioButton = true;
        for(String dataType : AsAirLibDataProvider.supportedDataTypes) {
            JRadioButton radioButton = new JRadioButton(dataType);
            radioButton.setActionCommand(dataType);
            radioButton.setSelected(firstRadioButton);
            radioButton.addActionListener(this);
            dataTypeRadio.add(radioButton);
            radioPanel.add(radioButton);
            firstRadioButton = false;
        }
        
        // category list
        JPanel categoryPanel = new JPanel();
        mainPanel.add(categoryPanel, BorderLayout.LINE_START);
        categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.PAGE_AXIS));
        JTextField categoryFilter = new JTextField();
        this.items.put("category!textFilter", categoryFilter);
        categoryFilter.setMaximumSize(new Dimension(Short.MAX_VALUE, 24));
        categoryFilter.addActionListener(this);
        categoryFilter.addKeyListener(this);
        categoryFilter.setActionCommand(AsAirLibGuiManagerFrame.CATEGORY_FILTER_TEXT);
        categoryPanel.add(categoryFilter);
        
        JPanel categoryPanelCards = new JPanel(new CardLayout());
        categoryPanel.add(categoryPanelCards);
        this.items.put("category!panelMain", categoryPanelCards);
        
        this.items.put("category!mainModel", new DefaultListModel());
        this.items.put("category!subModel", new DefaultListModel());
        
        JPanel categoryPanel1 = new JPanel(new GridLayout(0, 1));
        categoryPanelCards.add(categoryPanel1, AsAirLibGuiManagerFrame.CATEGORY_DOUBLE);
        JList categoryList1 = new JList((DefaultListModel)this.items.get("category!mainModel"));
        categoryList1.getSelectionModel().addListSelectionListener(this);
        this.items.put("category!mainList1", categoryList1);
        categoryPanel1.add(new JScrollPane(categoryList1));
        JList subcategoryList1 = new JList((DefaultListModel)this.items.get("category!subModel"));
        subcategoryList1.getSelectionModel().addListSelectionListener(this);
        this.items.put("category!subList1", subcategoryList1);
        categoryPanel1.add(new JScrollPane(subcategoryList1));
        
        JPanel categoryPanel2 = new JPanel(new GridLayout(0, 1));
        categoryPanelCards.add(categoryPanel2, AsAirLibGuiManagerFrame.CATEGORY_SINGLE);
        JList categoryList2 = new JList((DefaultListModel)this.items.get("category!mainModel"));
        categoryList2.getSelectionModel().addListSelectionListener(this);
        this.items.put("category!mainList2", categoryList2);
        categoryPanel2.add(new JScrollPane(categoryList2));

        // Track List area
        JPanel trackPanel = new JPanel();
        mainPanel.add(trackPanel, BorderLayout.CENTER);
        
        Object[][] rowData = {};
        AbstractTableModel trackModel = new AbstractTableModel() {
            public String getColumnName(int col) {
                return data.supportedColFormats[col].toString();
            }
            public int getRowCount() { return dataCache.size(); }
            public int getColumnCount() { return data.supportedColFormats.length; }
            public Object getValueAt(int row, int col) {
                return dataCache.get(row)[col];
            }
            public boolean isCellEditable(int row, int col)
                { return false; }
            public void setValueAt(Object value, int row, int col) {
                dataCache.get(row)[col] = (String)value;
                fireTableCellUpdated(row, col);
            }
        };
        this.items.put("track!tableModel", trackModel);
        
        JTable trackList = new JTable(trackModel);
        JScrollPane scrollTrackPane = new JScrollPane(trackList);
        trackList.setFillsViewportHeight(true);
        trackPanel.add(scrollTrackPane);
        
        this.refreshCategory();
        
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(AsAirLibGuiManagerFrame.CATEGORY_FILTER_TEXT))
             ;
        else
            this.updateType(e.getActionCommand());
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            if (e.getSource() == ((JList)this.items.get("category!mainList1")).getSelectionModel())
                this.refreshSubcategory();
            if (e.getSource() == ((JList)this.items.get("category!mainList2")).getSelectionModel())
                this.refreshDataCache();
            if (e.getSource() == ((JList)this.items.get("category!subList1")).getSelectionModel())
                this.refreshDataCache();
        }
    }
    
    
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        this.refreshCategory();
    }
    
    private void updateType(String typeName) {
        String category;
        if (typeName.equals("disc")) {
            category = AsAirLibGuiManagerFrame.CATEGORY_DOUBLE;
        } else {
            category = AsAirLibGuiManagerFrame.CATEGORY_SINGLE;
        }
        CardLayout cl = (CardLayout)(((JPanel)this.items.get("category!panelMain")).getLayout());
        cl.show(((JPanel)this.items.get("category!panelMain")), category);
        
        // Renew the Category List
        if (this.data.setType(typeName))
            this.refreshCategory();
    }
    
    private void refreshCategory() {
        JTextField catFilter = (JTextField)this.items.get("category!textFilter");
        String filterText = null;
        if (!catFilter.getText().equals(""))
            filterText = catFilter.getText().toLowerCase();
        
        JList catList = (JList)this.items.get("category!mainList2");
        if (this.data.getType().equals("disc"))
            catList = (JList)this.items.get("category!mainList1");
        
        Object selectedValue = catList.getSelectedValue();
        DefaultListModel catModel = (DefaultListModel)this.items.get("category!mainModel");
        catModel.clear();
        this.data.getCategories(filterText).stream().forEach((String cat) -> {
            catModel.addElement(cat);
        });
        catList.setSelectedIndex(catModel.indexOf(selectedValue));
    }
    
    private void refreshSubcategory() {
        DefaultListModel subModel = (DefaultListModel)this.items.get("category!subModel");
        subModel.clear();
        
        JList catList = (JList)this.items.get("category!mainList1");
        String catName = (String)catList.getSelectedValue();
        if (catName != null)
            this.data.getSubcategories(catName).stream().forEach((cat) -> {
                subModel.addElement(cat);
            });
    }
    
    private void refreshDataCache() {
        JList catList = (JList)this.items.get("category!mainList2");
        JList subList = null;
        if (this.data.getType().equals("disc")) {
            catList = (JList)this.items.get("category!mainList1");
            subList = (JList)this.items.get("category!subList1");
        }
        
        String catName = (String)catList.getSelectedValue();
        String subName = null;
        if (subList != null)
            subName = (String)subList.getSelectedValue();
        
        if (catName == null || (subName == null && subList != null))
            this.dataCache = new LinkedList<>();
        else
            this.dataCache = this.data.getData(catName, subName, this.data.supportedColFormats,
                new String[this.data.supportedColFormats.length], 1);
        ((AbstractTableModel)this.items.get("track!tableModel")).fireTableDataChanged();
    }
}
