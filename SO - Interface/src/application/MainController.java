package application;

import java.util.ArrayList;
import java.util.List;

import entities.Allocator;
import entities.Memory;
import entities.ProcessBuild;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class MainController {

	private static final int MEMORY_SIZE = 1024;

	@FXML
	private AnchorPane anchorPane;

	@FXML
	private Button startButton;

	@FXML
	private BorderPane borderPane;
    
    @FXML
    private BorderPane borderPane2;
    
    @FXML
    private BorderPane borderPane3;
	

	@FXML
	void startOperation(MouseEvent event) throws InterruptedException {
		List<Text> textList = new ArrayList<>();
		Integer i, j;

		// Criar processos (Process run)
		// Aloca memória (Memory run)
		// Executa (Allocator run)
		// Log

		Memory memory = new Memory(MEMORY_SIZE);
		ProcessBuild processMaker = new ProcessBuild(MEMORY_SIZE);

		try {

			processMaker.run();
			processMaker.join();

			memory.run();
			memory.join();

			Allocator alocador = new Allocator(memory, processMaker.getProcess());
			alocador.run();
			alocador.join();

			alocador.printFinalRelatorio(borderPane, borderPane2, borderPane3);
			// alocador.exportLog();

		} catch (Exception e) {

		}

		
	}

}
