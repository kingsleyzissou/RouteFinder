package com.got.app.helpers;

import java.io.File;
import java.util.ArrayList;
import java.io.FileInputStream;
import com.got.app.collections.Node;
import com.got.app.collections.Edge;
import com.got.app.collections.Graph;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Static class for loading data from an excel file
 *
 * Reference: https://medium.com/@ssaurel/reading-microsoft-excel-xlsx-files-in-java-2172f5aaccbe
 *
 */
public class ExcelReader {

    /**
     * Load nodes into graph from Excel spreadsheet
     *
     * @param graph element containing all the nodes and edges
     * @throws Exception
     */
    public static void loadNodes(Graph graph) throws Exception{
        File excelFile = new File("src/main/java/com/got/app/database/towns.xlsx");
        FileInputStream fis = new FileInputStream(excelFile);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);
        for (Row row : sheet) {
            if(row.getCell(0) == null) continue;
            Double id = Double.parseDouble(row.getCell(0).toString());
            String name = row.getCell(1).toString();
            double x = Double.parseDouble(row.getCell(2).toString());
            double y = Double.parseDouble(row.getCell(3).toString());
            Node node = new Node(id.intValue(), name, x, y);
            graph.addNode(node);
        }
        workbook.close();
        fis.close();
    }

    /**
     * Load edges into graph from Excel spreadsheet
     *
     * @param graph element containing all the nodes and edges
     * @throws Exception
     */
    public static void loadEdges(Graph graph) throws Exception{
        File excelFile = new File("src/main/java/com/got/app/database/towns.xlsx");
        FileInputStream fis = new FileInputStream(excelFile);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(1);
        for (Row row : sheet) {
            if(row.getCell(0) == null) continue;
            int sourceIndex = (int) Double.parseDouble(row.getCell(0).toString());
            int destinationIndex = (int) Double.parseDouble(row.getCell(1).toString());
            int difficulty = (int) Double.parseDouble(row.getCell(2).toString());
            int danger = (int) Double.parseDouble(row.getCell(3).toString());
            ArrayList<Node> nodes = graph.getNodes();
            new Edge(graph, nodes.get(sourceIndex), nodes.get(destinationIndex), difficulty, danger);
            new Edge(graph, nodes.get(destinationIndex), nodes.get(sourceIndex), difficulty, danger);
        }
        workbook.close();
        fis.close();
    }

}
