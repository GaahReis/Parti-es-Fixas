package entities;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class Allocator extends Thread {

	private Memory memory;
	private List<Process> processes;
	private int fragmented;
	private int executed;
	private int notExecuted;
	private int timeExecution;
	private List<String> logExport;

	public Allocator(Memory memory, List<Process> processes) {
		this.memory = memory;
		this.processes = processes;
		this.logExport = new ArrayList<>();
	}

	private void showPieChart(int pe, int pd, BorderPane borderPane) {
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
				new PieChart.Data("Processos Executados", pe), new PieChart.Data("Processos Descartados", pd));

		PieChart pieChart = new PieChart(pieChartData);
		pieChart.setTitle("Processos");
		pieChart.setClockwise(true);
		pieChart.setLabelLineLength(10);
		pieChart.setLabelsVisible(false);
		pieChart.setStartAngle(100);

		borderPane.setCenter(pieChart);
	}

	private void showBarChart(Integer time, Integer frag, Integer timeMedio, BorderPane borderPane2) {
		CategoryAxis xAxis = new CategoryAxis();
		xAxis.setLabel("Quantidade");

		NumberAxis yAxis = new NumberAxis();
		yAxis.setLabel("Resultados");

		BarChart barChart = new BarChart<>(xAxis, yAxis);

		XYChart.Series data = new XYChart.Series();
		data.setName("INFO");
		data.getData().add(new XYChart.Data("TTE", time));
		data.getData().add(new XYChart.Data("TTFI", frag));
		data.getData().add(new XYChart.Data("TME", timeMedio));

		barChart.getData().add(data);

		borderPane2.setCenter(barChart);

	}

	public void run() {
		Iterator<Process> iteratorProcess = processes.iterator();

		while (iteratorProcess.hasNext()) {

			Process process = iteratorProcess.next();
//	            showPieChart(executed, notExecuted, borderPane);
			logExport.add("Executando processo: " + process.getId());
			System.out.println("Executando processo: " + process.getId());

			Iterator<Partition> iterator = memory.getPartitions().iterator();

			while (iterator.hasNext()) {

				Partition partition = iterator.next();

				if (partition.getProcessId() == 0) {

					partition.setProcessId(process.getId());
					partition.setPSize(process.getSize());

					synchronized (process) {
						process.setExecuting(true);
						executed++;
					}

					if (process.getSize() < partition.getSize()) {
						logExport.add("Houve fragmentacao interna");
						System.out.println("Houve fragmentacao interna");
						fragmented++;
						break;
					}

				} else {
					logExport.add("Pulando para proxima particao");
					System.out.println("Pulando para proxima particao");

					Process processExecuting = processes.get(partition.getProcessId() - 1);

					synchronized (partition) {
						synchronized (processExecuting) {

							int cpuTime = processExecuting.getTime();
							processExecuting.setTime(--cpuTime);
							logExport.add("Diminuindo tempo do processo: " + processExecuting.getId());
							System.out.println("Diminuindo tempo do processo: " + processExecuting.getId());
						}
					}

					if (processExecuting.getTime() <= 0) {
						logExport.add("Processo " + processExecuting.getId() + " Terminou! ");
						System.out.println("Processo " + processExecuting.getId() + " Terminou! ");

						synchronized (partition) {
							synchronized (processExecuting) {
								processExecuting.setExecuting(false);
								logExport.add("Colocando novo processo: " + process.getId());
								System.out.println("Colocando novo processo: " + process.getId());
								executed++;
							}
						}

						partition.setProcessId(process.getId());
						partition.setPSize(process.getSize());

						synchronized (process) {
							process.setExecuting(true);
						}

						if (process.getSize() < partition.getSize()) {
							logExport.add("Houve fragmentacao externa");
							System.out.println("Houve fragmentacao externa");
							fragmented++;
						}

						break;
					}
				}

				stopCPU();

			}

			timeExecution++;

			if (!process.isExecuting()) {
				logExport.add("Houve uma fragmentacao");
				System.out.println("Houve uma fragmentacao");
				notExecuted++;
			}
		}

		addRemainingTime();

	}

	private void addRemainingTime() {

		Iterator<Partition> iterator = memory.getPartitions().iterator();

		int maxCpuTimeToExecute = 0;

		while (iterator.hasNext()) {
			Process process = processes.get(iterator.next().getProcessId() - 1);

			if (process.getTime() > maxCpuTimeToExecute) {
				maxCpuTimeToExecute = process.getTime();
			}

		}

		timeExecution += maxCpuTimeToExecute;

	}

	private void stopCPU() {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void printFinalRelatorio(BorderPane borderPane, BorderPane borderPane2, BorderPane borderPane3) {
		Integer time = timeExecution;
		Integer frag = fragmented;
		Integer timeMedio = (timeExecution / executed);

		System.out.println();
		logExport.add("\n");
		System.out.println();
		logExport.add("\n");
		System.out.println("Execução Finalizada ");
		logExport.add("Execução Finalizada");
		System.out.println("Tempo Execução: " + timeExecution);
		logExport.add("Tempo Execução: " + timeExecution);
		System.out.println("Fragmentação interna: " + fragmented);
		logExport.add("Fragmentação interna: " + fragmented);
		System.out.println("Média de execução: " + (timeExecution / executed));
		logExport.add("Média de execução: " + (timeExecution / executed));
		System.out.println("Processos executados: " + executed);

		logExport.add("Processos executados: " + executed);
		System.out.println("Processos descartados: " + notExecuted);
		logExport.add("Processos descartados: " + notExecuted);

		Button btn = new Button();
		btn.setText("Gerar Arquivo Log");
		btn.setStyle("-fx-background-color: #70a6e0;");
		btn.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {
				try {
					exportLog();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		borderPane3.setCenter(btn);

		showPieChart(executed, notExecuted, borderPane);
		showBarChart(time, frag, timeMedio, borderPane2);
	}

	public void exportLog() throws IOException {
		FileOutputStream fos = new FileOutputStream("log.txt");

		for (String log : logExport) {
			fos.write(new StringBuilder().append(log).append("\n").toString().getBytes());
		}

		fos.close();

		System.out.print("Log exportada com sucesso!");
	}
}
