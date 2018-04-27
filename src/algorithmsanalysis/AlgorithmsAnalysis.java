/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package algorithmsanalysis;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 *
 * @author Sean
 */
public class AlgorithmsAnalysis extends JFrame{

    // Algorithm classes and helpers
    private BubbleSort bubbleSort;
    private EnBubbleSort enBubbleSort;
    private InsertionSort insertionSort;
    private SelectionSort selectionSort;
    private Array arrayHelp;
    private StopWatch watch;

    // GUI stuff
    //private ElementPanel randomPanel, sortedPanel, invertedPanel;
    private JComboBox<Integer> jcbxSize;
    private ArrayList<ElementPanel> elementPanels;

    public AlgorithmsAnalysis() {
        // initialise algorithm classes
        bubbleSort = new BubbleSort();
        enBubbleSort = new EnBubbleSort();
        insertionSort = new InsertionSort();
        selectionSort = new SelectionSort();
        
        // initialise array helper class and stopwatch
        arrayHelp = new Array();
        watch = new StopWatch();
        
        // initialise panel array list to hold random, sorted, and inverted elements
        elementPanels = new ArrayList<>();
        // initialise array list to hold JButtons
        ArrayList<JButton> sortBtns = new ArrayList<JButton>();

        // Create and set up top panel to hold sizes of array
        JPanel sizePanel = new JPanel();
        jcbxSize = new JComboBox<Integer>();
        
        // Add array sizes to combo box - 1,000, 10,000, 100,000.
        jcbxSize.addItem(Array.SMALL);
        jcbxSize.addItem(Array.MEDIUM);
        jcbxSize.addItem(Array.LARGE);
        sizePanel.add(new JLabel("Select the number of elements for the array: "));
        sizePanel.add(jcbxSize);

        // set up middle panels that holds data and add them to array list
        elementPanels.add(new ElementPanel("Random Elements"));
        elementPanels.add(new ElementPanel("Sorted Elements"));
        elementPanels.add(new ElementPanel("Inverted Elements"));
        
        // add the 3 panels to a single panel to fit in frame
        JPanel analysisPanel = new JPanel();
        analysisPanel.setLayout(new GridLayout(3, 1, 10, 10));
        for(ElementPanel p : elementPanels) {
            analysisPanel.add(p);
        }

        // create, setup and add buttons to bottom panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 5, 10, 10));
        buttonPanel.add(new JLabel());
        
        sortBtns.add(new JButton("Bubble"));
        sortBtns.add(new JButton("E. Bubble"));
        sortBtns.add(new JButton("Selection"));
        sortBtns.add(new JButton("Insertion"));
        
        for(JButton btn : sortBtns) {
            // Set mnemonic of each button to the first character
            btn.setMnemonic(btn.getText().charAt(0));
            buttonPanel.add(btn);
        }

        // add 3 panels to frame.
        add(sizePanel, BorderLayout.NORTH);
        add(analysisPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // create and add listener to buttons
        BtnActionListener listener = new BtnActionListener();
        for(JButton btn : sortBtns) {
            btn.addActionListener(listener);
        }

        // initially create 3 arrays of 1000 elements.
        int arraySize = (int)jcbxSize.getSelectedItem();
        arrayHelp.createRandom(arraySize);
        arrayHelp.createSorted(arraySize);
        arrayHelp.createInverted(arraySize);
        
        // anonymous listener to listen for the array size combo box changing. If it changes, create 3 new arrays of giving size
        jcbxSize.addActionListener((ActionEvent e) -> {
            int newArraySize = (int) jcbxSize.getSelectedItem();
            arrayHelp.createRandom(newArraySize);
            arrayHelp.createSorted(newArraySize);
            arrayHelp.createInverted(newArraySize);
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        AlgorithmsAnalysis frame = new AlgorithmsAnalysis();
        frame.pack();
        frame.setTitle("Algorithms");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    /**
     * Listener class for the algorithm buttons
     * @author Sean
     *
     */
    public class BtnActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // store times, swaps and checks on text fields to compare against the other algorithms
            switch(e.getActionCommand()) {
                case "Bubble":
                    populate(bubbleSort);
                	break;

                case "E. Bubble":
                	populate(enBubbleSort);
                    break;

                case "Selection":
                	populate(selectionSort);
                    break;

                case "Insertion":
                	populate(insertionSort);
                    break;
            }
        }

        /**
         * Times how long it takes to sort giving int array
         * @param algorithm - Sort Algorithm to use to sort. Bubble, EnBubble, selection, and insertion sort
         * @param elements - int array of elements to sort
         * @return - time it took to sort the array
         */
        private long sortTime(Sort algorithm, int[] elements) {
            // restes and starts stopwatch
        	watch.reset();
            watch.start();

            // the algorithm used to sort depends on what algorithm is an instance of
            if(algorithm instanceof BubbleSort)
                bubbleSort.sort(elements);
            else if(algorithm instanceof EnBubbleSort)
                enBubbleSort.sort(elements);
            else if(algorithm instanceof SelectionSort)
                selectionSort.sort(elements);
            else if(algorithm instanceof InsertionSort)
                insertionSort.sort(elements);

            // stop watch
            watch.stop();
            // return elapsed time
            return watch.getElapsedTime();
        }
        
        /**
         * Populates the JTextFields with time, swaps, comparisons and writes for each algorithm
         * @param algorithm - The sorting algorithm to be used
         */
        private void populate(Sort algorithm) {
        	// Create new arrayList to hold data field
        	ArrayList<JTextField> dataFields;
        	
        	// keep track of panel location
        	int count = 0;
        	// for each element panel (3 - random, sorted, inverted)
            for(ElementPanel ep : elementPanels) {
            	
            	// depending on algorithm, retrieve the corresponding array list of JTextFields
            	if(algorithm instanceof BubbleSort)
            		dataFields = ep.getBubblePanel().getDataFields();
            	else if(algorithm instanceof EnBubbleSort)
            		dataFields = ep.getEnBubblePanel().getDataFields();
            	else if(algorithm instanceof SelectionSort)
            		dataFields = ep.getSelectPanel().getDataFields();
            	else
            		dataFields = ep.getInsertPanel().getDataFields();
            	
            	// For each of the JTextFields, set time, checks, swaps, writes.
            	for(int i = 0; i < dataFields.size(); i++) {
            		if(i == 0 && count == 0)
            			dataFields.get(0).setText("" + sortTime(algorithm, arrayHelp.getRandomOriginal()));
            		else if(i == 0 && count == 1)
            			dataFields.get(0).setText("" + sortTime(algorithm, arrayHelp.getSortedOriginal()));
            		else if(i == 0 && count == 2)
            			dataFields.get(0).setText("" + sortTime(algorithm, arrayHelp.getInvertedOriginal()));
            		
            		if(i == 1)
            			dataFields.get(i).setText("" + algorithm.getSwaps());
            		else if(i == 2)
            			dataFields.get(i).setText("" + algorithm.getChecks());
            		else if(i == 3)
            			dataFields.get(i).setText("" + algorithm.getWrites());
            	}
            	//reset algorithms counters
            	algorithm.reset();
            	// to move to next panel eg. sorted
            	count++;
            }
        }
    }
}
