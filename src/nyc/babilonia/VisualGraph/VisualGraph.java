package nyc.babilonia.VisualGraph;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

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
import javax.swing.filechooser.FileNameExtensionFilter;

import nyc.babilonia.VisualGraph.data.GraphObject;
@SuppressWarnings("serial")
public class VisualGraph extends JFrame
{
	private GraphObject graph = new GraphObject();
	private JMenuBar menu;
	private JMenu fileMenu;
	private JMenuItem fileNew, fileOpen, fileSave, fileClose;
	private DrawSurface surface;
	private MakeConsole console;
	String [] options = {"Directed" , "Undirected"};
	private FileNameExtensionFilter openFilter = 
			new FileNameExtensionFilter("Graph Files" , "directed" , "undirected");
	private FileNameExtensionFilter directedFilter = 
			new FileNameExtensionFilter("DirectedGraph" , "undirected");
	private ChangeTracker changeTracker = ChangeTracker.getTracker();
	File dir= new File(MakeConsole.class.getProtectionDomain().getCodeSource().getLocation().getPath());
	Dimension size;
	public VisualGraph()
	{
		initUI();
	}
	private void initUI() {
        
        setTitle("Graphs");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BoxLayout(getContentPane(),BoxLayout.X_AXIS));
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        size = new Dimension(screen.width/2,screen.height -200);
        surface=new DrawSurface(graph,size);
        JScrollPane drawPane = new JScrollPane(surface);
        drawPane.setMaximumSize(size);
        drawPane.setPreferredSize(size);
        add(drawPane);
        surface.setVisible(true);
        //add Surface
        console = new MakeConsole(graph,surface,size);
        add(console);
        setSize(size);
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
				newView();
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
				fileSave.setEnabled(changeTracker.hasChanges());
			}
			
		});
		menu.add(fileMenu);
		this.setJMenuBar(menu);
	}
	private void openGraph()
	{
		JFileChooser choose = new JFileChooser(dir);
		choose.setFileFilter(openFilter);
		if(changeTracker.hasChanges())
			unsavedChangesConfirmation();
		if(choose.showOpenDialog(this)==JFileChooser.APPROVE_OPTION)
		{
			console.clearAll();
			dir = choose.getSelectedFile();
			graph.clear();
			try
			{
				graph.openGraphFromFile(dir);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			console.edgeList.addAll(graph.getEdges());
			console.updatePoints();
			console.updateEdge();
			console.updateBoxes();
			console.toggleUI(true);
			console.enableDraw();
			surface.redraw();
		}
	}
	private void unsavedChangesConfirmation()
	{
		int response = JOptionPane.showConfirmDialog(this, "There are unsaved changes would you like to save?", 
				"Unsaved changed detected", 
				JOptionPane.YES_NO_OPTION);
		if(response == JOptionPane.YES_OPTION)
			saveGraph();
		else
			confirmChangesMade();
	}
	private void confirmChangesMade()
	{
		changeTracker.changeRecognized();
	}
	private void saveGraph()
	{
		JFileChooser choose = new JFileChooser(dir);
		choose.setFileFilter(directedFilter);
		if(choose.showSaveDialog(this)==JFileChooser.APPROVE_OPTION)
		{
			dir = choose.getSelectedFile();
			System.out.println(choose.getFileFilter().getDescription());
			choose.setFileFilter(openFilter);
			try
			{
				graph.saveGraph(dir);
			} catch (IOException e)
			{
				e.printStackTrace();
			}
			confirmChangesMade();
		}
	}
	private void newView()
	{
		if(changeTracker.hasChanges())
			unsavedChangesConfirmation();
		Object response = JOptionPane.showInputDialog(null, "What type of graph would you like to draw?", "Graph Type" ,
				                     JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
		int choice;
		if(response.toString().equalsIgnoreCase("directed"))
			choice = graph.DIRECTED;
		else 
			choice = graph.UNDIRECTED;
		if(graph.getState() == choice)
			graph.clear();
		else
			graph.changeGraphType(choice);
		console.enableDraw();
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
	                VisualGraph polygons = new VisualGraph();
	                polygons.setVisible(true);
	            }
	        });
	    }
}