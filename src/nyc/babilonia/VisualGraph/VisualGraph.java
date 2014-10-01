package nyc.babilonia.VisualGraph;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
@SuppressWarnings("serial")
public class VisualGraph extends JFrame
{
	private Graph graph = new Graph();
	private JMenuBar menu;
	private JMenu fileMenu;
	private JMenuItem fileNew, fileOpen, fileSave, fileClose;
	private DrawSurface surface;
	private MakeConsole console;
	private ChangeTracker changeTracker = ChangeTracker.getTracker();
	File dir= new File(MakeConsole.class.getProtectionDomain().getCodeSource().getLocation().getPath());
	public VisualGraph(String [] arg)
	{
		initUI(arg);
	}
	private void initUI(String [] arg) {
        
        setTitle("Graphs");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BoxLayout(getContentPane(),BoxLayout.X_AXIS));
        surface=new DrawSurface(graph);
        JScrollPane drawPane = new JScrollPane(surface);
        drawPane.setMaximumSize(new Dimension(750,1000));
        drawPane.setPreferredSize(new Dimension(750,1000));
        add(drawPane);
        surface.setVisible(true);
        //add Surface
        console = new MakeConsole(graph,surface);
        add(console);
        setSize(1100, 1000);
        createMenu();
        setLocationRelativeTo(null);        
    }
	public void createMenu()
	{
		menu = new JMenuBar();
		fileMenu = new JMenu("File");
		fileNew =  new JMenuItem("New");
		fileNew.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				resetView();
			}
		});
		fileOpen = new JMenuItem("Open");
		fileOpen.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				openGraph();
			}
		});
		
		fileSave = new JMenuItem("Save");
		fileSave.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				saveGraph();
			}
		});
		
		fileClose = new JMenuItem("Exit");
		fileClose.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				closeFrame();
			}
		});
		fileMenu.add(fileNew);
		fileMenu.add(fileOpen);
		fileMenu.add(fileSave);
		fileMenu.add(fileClose);
		
		fileMenu.addMenuListener(new MenuListener()
		{

			@Override
			public void menuCanceled(MenuEvent arg0)
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void menuDeselected(MenuEvent arg0)
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void menuSelected(MenuEvent arg0)
			{
				fileNew.setEnabled(!graph.getPoints().isEmpty());
				fileSave.setEnabled(changeTracker.hasChanges());
			}
			
		});
		menu.add(fileMenu);
		this.setJMenuBar(menu);
	}
	private void openGraph()
	{
		JFileChooser choose = new JFileChooser(dir);
		if(changeTracker.hasChanges())
			unsavedChangesConfirmation();
		if(choose.showOpenDialog(this)==JFileChooser.APPROVE_OPTION)
		{
			console.clearAll();
			dir = choose.getSelectedFile();
			graph.clear();
			graph.capture(dir);
			surface.setGraph(graph);
			console.setOpenFileField(dir);
			console.edgeList.addAll(graph.getEdges());
			console.updatePoints();
			console.updateEdge();
			console.updateBoxes();
			console.toggleUI(true);
		}
	}
	private void unsavedChangesConfirmation()
	{
		int response = JOptionPane.showConfirmDialog(this, "There are unsaved changes would you like to save?", 
				"Unsaved changed detected", 
				JOptionPane.YES_NO_OPTION);
		if(response == JOptionPane.YES_OPTION)
			saveGraph();
	}
	private void confirmChangesMade()
	{
		changeTracker.changeRecognized();
	}
	private void saveGraph()
	{
		JFileChooser choose = new JFileChooser(dir);
		if(choose.showSaveDialog(this)==JFileChooser.APPROVE_OPTION)
		{
			dir = choose.getSelectedFile();
			graph.saveGraph(dir);
			confirmChangesMade();
			console.setOpenFileField(dir);
		}
	}
	private void resetView()
	{
		if(changeTracker.hasChanges())
			unsavedChangesConfirmation();
		console.reset();
	}
	private void closeFrame()
	{
		if(changeTracker.hasChanges())
			unsavedChangesConfirmation();
		this.dispose();
	}
	 public static void main(final String[] args) 
	 {
		 
	        SwingUtilities.invokeLater(new Runnable() 
	        {
	            @Override
	            public void run() {
	            	//System.out.println('∞');
	                VisualGraph polygons = new VisualGraph(args);
	                polygons.setVisible(true);
	            }
	        });
	    }
}