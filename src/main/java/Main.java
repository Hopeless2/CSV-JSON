import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
//        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
//        String fileName = "data.csv";
//        List<Employee> list = parseCSV(columnMapping, fileName);
//        String json = listToJson(list);
//        writeString(json);
        List<Employee> list = parseXML("data2.xml");
        String json = listToJson(list);
        writeString(json);


    }

    private static void writeString(String json) throws IOException {
        try (FileWriter file = new
                FileWriter("new_data2.json")) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            throw new IOException();
        }

    }

    private static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        String convertedJson = gson.toJson(list, listType);
        return convertedJson;
    }

    private static List<Employee> parseCSV(String[] columnMapping, String fileName) throws IOException {
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {

            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .build();

            List<Employee> convertedFromCSVList = csv.parse();
            return convertedFromCSVList;
        } catch (IOException e) {
            throw new IOException();
        }

    }

    private static List<Employee> parseXML(String fileName) throws ParserConfigurationException, IOException, SAXException {
        List<Employee> convertedFromXMLList = new ArrayList<>();
        long id = 0;
        String firstName = null;
        String lastName = null;
        String country = null;
        int age = 0;
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(fileName));

            Node root = doc.getDocumentElement();

            NodeList employeesList = root.getChildNodes();
            for(int i = 0; i < employeesList.getLength(); i++){

                if(Node.ELEMENT_NODE == employeesList.item(i).getNodeType()){
                    NodeList employeesAtributes = employeesList.item(i).getChildNodes();

                    for(int j = 0; j < employeesAtributes.getLength(); j++){

                        if(Node.ELEMENT_NODE == employeesAtributes.item(j).getNodeType()){
                            switch (employeesAtributes.item(j).getNodeName()){
                                case "id": {
                                    id = Long.parseLong(employeesAtributes.item(j).getTextContent());
                                    break;
                                }
                                case "firstName": {
                                    firstName = employeesAtributes.item(j).getTextContent();
                                    break;
                                }
                                case "lastName": {
                                    lastName = employeesAtributes.item(j).getTextContent();
                                    break;
                                }
                                case "country": {
                                    country = employeesAtributes.item(j).getTextContent();
                                    break;
                                }
                                case "age": {
                                    age = Integer.parseInt(employeesAtributes.item(j).getTextContent());
                                    break;
                                }
                            }

                        }
                    }
                    Employee employee = new Employee(id, firstName, lastName, country, age);
                    convertedFromXMLList.add(employee);
                }


            }
            return convertedFromXMLList;
        }catch(IOException e){
            throw new IOException();
        }catch(SAXException e){
            throw new SAXException();
        }catch(ParserConfigurationException e){
            throw new ParserConfigurationException();
        }


    }


//    private static List<Employee> parseXML() throws ParserConfigurationException, IOException, SAXException {
//        try {
//            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//            DocumentBuilder builder = factory.newDocumentBuilder();
//            Document doc = builder.parse(new File("data2.xml"));
//
//            Node root = doc.getDocumentElement();
//
//            NodeList nodeList = root.getChildNodes();
//
//            ArrayList<Employee> convertedFromXMLList = new ArrayList<>();
//            for (int i = 0; i < nodeList.getLength(); i++) {
//                Node node = nodeList.item(i);
//                if (Node.ELEMENT_NODE == node.getNodeType()) {
//                    Element element = (Element) node;
//                    long id = Long.parseLong(element.getAttribute("id"));
//                    String firstName = element.getAttribute("firstName");
//                    String lastName = element.getAttribute("lastName");
//                    String country = element.getAttribute("country");
//                    int age = Integer.parseInt(element.getAttribute("age"));
//                    Employee employee = new Employee(id, firstName, lastName, country, age);
////                    Employee employee = new Employee(
////                            Long.parseLong(element.getAttribute("id")),
////                            element.getAttribute("firstName"),
////                            element.getAttribute("lastName"),
////                            element.getAttribute("country"),
////                            Integer.parseInt(element.getAttribute("age")));
//                    convertedFromXMLList.add(employee);
//                }
//            }
//            return convertedFromXMLList;
//        }catch(IOException e){
//            throw new IOException();
//        }catch(SAXException e){
//            throw new SAXException();
//        }catch(ParserConfigurationException e){
//            throw new ParserConfigurationException();
//        }
//    }


}
