package utils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class FileOperations {

	Path currentRelativePath = Paths.get("");
	String s = currentRelativePath.toAbsolutePath().toString();
	String basePath = s + File.separator;

	/**
	 * @param filePath
	 * @param key
	 * @return
	 */
	public String getValueFromPropertyFile(String filePath, String key) {
		String keyValue = null;
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(filePath));
			keyValue = prop.getProperty(key);
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail(e.getMessage());
		}
		return keyValue;
	}

	/**
	 * @param filePath
	 * @param key
	 * @param value
	 * @throws IOException
	 * @throws ConfigurationException
	 */
	public void updateValueToPropertyFile(String filePath, String key, String value)
			throws IOException, ConfigurationException {
		 FileInputStream in = new FileInputStream(filePath);
		 Properties props = new Properties();
		 props.load(in);
		 in.close();
		
		 FileOutputStream out = new FileOutputStream(filePath);
		 props.setProperty(key, value);
		 props.store(out, null);
		 out.close();

//		PropertiesConfiguration config = new PropertiesConfiguration(filePath);
//		config.setProperty(key, value);
//		config.save();
	}

	public LinkedHashSet<Map<String, String>> readCompleteDataFromExcel(String filePath, String fileName,
			String sheetName) {
		File file = new File(filePath + "\\" + fileName);
		FileInputStream inputStream;
		Workbook workbook = null;
		Row row = null;
		LinkedHashSet<Map<String, String>> myLinkedHashSet = new LinkedHashSet<Map<String, String>>();
		LinkedHashMap<String, String> objLinkedHashMap = new LinkedHashMap<String, String>();
		try {
			inputStream = new FileInputStream(file);
			workbook = WorkbookFactory.create(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Sheet sheet = workbook.getSheet(sheetName);
		Row firstRow = sheet.getRow(0);
		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
		for (int i = 1; i <= rowCount; i++) {
			objLinkedHashMap = new LinkedHashMap<String, String>();
			row = sheet.getRow(i);
			for (int j = 0; j < sheet.getRow(i).getLastCellNum(); j++) {
				if ((firstRow.getCell(j) != null) && (!firstRow.getCell(j).getStringCellValue().trim().equals(""))) {
					if ((row.getCell(j) != null)) {
						row.getCell(j).setCellType(Cell.CELL_TYPE_STRING);
						if (!row.getCell(j).getStringCellValue().trim().equals("")) {
							objLinkedHashMap.put(firstRow.getCell(j).getStringCellValue().trim(),
									row.getCell(j).getStringCellValue().trim());
						}
					}
				}
			}
			if (!objLinkedHashMap.isEmpty())
				myLinkedHashSet.add(objLinkedHashMap);
			objLinkedHashMap = null;
		}
		return myLinkedHashSet;
	}

	public String readSingleValueFromExcel(String filePath, String fileName, String sheetName, String attributeName) {
		File file = new File(filePath + File.separator + fileName);
		String attributeValue = null;
		FileInputStream inputStream;
		Workbook workbook = null;
		Row row = null;
		try {
			inputStream = new FileInputStream(file);
			workbook = WorkbookFactory.create(inputStream);
		} catch (Exception e) {

		}
		Sheet sheet = workbook.getSheet(sheetName);
		Row firstRow = sheet.getRow(0);
		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
		for (int i = 1; i <= rowCount; i++) {

			row = sheet.getRow(i);
			for (int j = 0; j < firstRow.getLastCellNum(); j++) {
				if ((row.getCell(j) != null)) {
					row.getCell(j).setCellType(CellType.STRING);
					if (firstRow.getCell(j).getStringCellValue().equals(attributeName))
						attributeValue = sheet.getRow(1).getCell(j).getStringCellValue();
				}
			}

		}

		return attributeValue;
	}

	public List<String> readColumnDataFromExcel(String filePath, String fileName, String sheetName,
			String attributeName) {
		File file = new File(filePath + "\\" + fileName);
		List<String> attributeValue = new ArrayList<String>();
		FileInputStream inputStream;
		Workbook workbook = null;
		Row row = null;
		int columnNo = 0;
		try {
			inputStream = new FileInputStream(file);
			workbook = new WorkbookFactory().create(inputStream);
		} catch (Exception e) {

		}
		Sheet sheet = workbook.getSheet(sheetName);
		Row firstRow = sheet.getRow(0);
		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
		for (int i = 1; i <= rowCount; i++) {

			row = sheet.getRow(i);
			if (i == 1) {
				for (int j = 0; j < firstRow.getLastCellNum(); j++) {
					if ((row.getCell(j) != null)) {
						row.getCell(j).setCellType(Cell.CELL_TYPE_STRING);
						if (firstRow.getCell(j).getStringCellValue().equals(attributeName)) {
							columnNo = j;
							break;
						}
					}
				}
			}
			if ((row.getCell(columnNo) != null)) {
				row.getCell(columnNo).setCellType(Cell.CELL_TYPE_STRING);
				if ((firstRow.getCell(columnNo).getStringCellValue().equals(attributeName))
						&& (!row.getCell(columnNo).getStringCellValue().trim().equals(""))) {
					attributeValue.add(row.getCell(columnNo).getStringCellValue().trim());

				}
			}

		}
		return attributeValue;
	}
	
	public List<String> getNumericDataFromExcel(String format,String filePath, String fileName, String sheetName,
			String attributeName) {//added by sel+3
		File file = new File(filePath + "\\" + fileName);
		List<String> attributeValue = new ArrayList<String>();
		FileInputStream inputStream;
		Workbook workbook = null;
		Row row = null;
		int columnNo = 0;
		try {
			inputStream = new FileInputStream(file);
			workbook = new WorkbookFactory().create(inputStream);
		} catch (Exception e) {

		}
		Sheet sheet = workbook.getSheet(sheetName);
		Row firstRow = sheet.getRow(0);
		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
		for (int i = 1; i <= rowCount; i++) {

			row = sheet.getRow(i);
			if (i == 1) {
				for (int j = 0; j < firstRow.getLastCellNum(); j++) {
					if ((row.getCell(j) != null)) {
						row.getCell(j).setCellType(Cell.CELL_TYPE_STRING);
						if (firstRow.getCell(j).getStringCellValue().equals(attributeName)) {
							columnNo = j;
							break;
						}
					}
				}
			}
			if ((row.getCell(columnNo) != null)) {
				if ((firstRow.getCell(columnNo).getStringCellValue().equals(attributeName))) {
					row.getCell(columnNo).setCellType(Cell.CELL_TYPE_NUMERIC);
					switch (format) {
					case "Integer":
						attributeValue.add(String.valueOf((int) row.getCell(columnNo).getNumericCellValue()));
						break;
					case "float":
						attributeValue.add(String.valueOf((float) row.getCell(columnNo).getNumericCellValue()));
						break;
					case "Double":
						attributeValue.add(String.valueOf((Double)row.getCell(columnNo).getNumericCellValue()));
						break;
					case "Date":
						attributeValue.add(String.valueOf(row.getCell(columnNo).getDateCellValue()));
						break;

					}
				}

			}
		}

		return attributeValue;
	}

	@SuppressWarnings("deprecation")
	public static String readColumnValueUsingKeyFromExcel(String filePath, String fileName,String sheetName,String keyName) {
		File file = new File(filePath + File.separator + fileName);
		String value = null;
		FileInputStream inputStream;
		Workbook workbook = null;
		Row row =null;

		try{
			inputStream = new FileInputStream(file);
			workbook = WorkbookFactory.create(inputStream);
		}
		catch(Exception e){

		}
		Sheet sheet = workbook.getSheet(sheetName);
		Row firstRow = sheet.getRow(0);
		int rowCount = sheet.getLastRowNum()-sheet.getFirstRowNum();
		for (int i = 1; i <=rowCount; i++) {
			row = sheet.getRow(i);
			for (int j = 0; j < firstRow.getLastCellNum(); j++) {
				if(row.getCell(j).getStringCellValue().equals(keyName)) {
					row.getCell(j).setCellType(Cell.CELL_TYPE_STRING);
					if(!row.getCell(j).getStringCellValue().trim().equals("")){
						if(firstRow.getCell(j).getStringCellValue()!=null) {
							//value = row.getCell(j+1).getStringCellValue().trim();
							Cell cell =row.getCell(j+1);
							switch (cell.getCellType()) {
							case Cell.CELL_TYPE_STRING:
								value=cell.getRichStringCellValue().getString();
								break;
							case Cell.CELL_TYPE_NUMERIC:
								value=Integer.toString(((int)cell.getNumericCellValue()));
								break;
							}

							break;
						}
					}
				}
				else
					break;
			}


		}

		return value;
  }

	public void writeColumnValueUsingKeyInExcel(String filePath, String fileName,String sheetName,String attributeName,String textvalue) throws IOException {
		File file = new File(filePath + File.separator + fileName);// added by sel+3
		FileInputStream inputStream;
		Workbook workbook = null;
		Row row = null;
		try {
		inputStream = new FileInputStream(file);
		workbook = WorkbookFactory.create(inputStream);
		} catch (Exception e) {

		}
		Sheet sheet = workbook.getSheet(sheetName);
		Row firstRow = sheet.getRow(0);
		int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
		for (int i = 1; i <= rowCount; i++) {

		row = sheet.getRow(i);
		for (int j = 0; j < firstRow.getLastCellNum(); j++) {
		if ((row.getCell(j) != null)) {
		if (firstRow.getCell(j).getStringCellValue().equals(attributeName)) {
		sheet.getRow(1).getCell(j).setCellValue(textvalue);
		break;
	             }

	     	}
	   }
	}
		FileOutputStream fos = new FileOutputStream(file);
		workbook.write(fos);
		fos.close();
		}


	// App Specific Method
	public void createZipFileForReport() throws Exception {
		System.out.println("============================" + basePath + "Extent Reports");
		copyDir(basePath + "Extent Reports",
				basePath + "Zipped Report" + File.separator);
//		copyDir(basePath + "Failure Screenshots",
//				basePath + "Zipped Report" + File.separator);
//		copyDir(basePath + "Passed Screenshots",
//				basePath + "Zipped Report" + File.separator);

		File directoryToZip = new File(basePath + "Zipped Report" + File.separator);

//		List<File> fileList = new ArrayList<File>();
		System.out.println("---Getting references to all files in: " + directoryToZip.getCanonicalPath());
//		getAllFiles(directoryToZip, fileList);
		attachScreenshotInEmailReport();
		System.out.println("---Creating zip file");
//		writeZipFile(directoryToZip, fileList);
		writeZipFile(directoryToZip);
		System.out.println("---Done");
		moveFile(basePath + "Zipped Report.zip",
				basePath + "Zipped Report/Zipped Report.zip");
	}

	public void copyDir(String source, String destination) {

		File fromDir = new File(source);
		File toDir = new File(destination);


		try {
			FileUtils.copyDirectoryToDirectory(fromDir, toDir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void copyFile(String source, String destination) {
		File content[] = new File(source).listFiles();

		for (int i = 0; i < content.length; i++) {

			String destiny = destination + content[i].getName();
			File desc = new File(destiny);
			try {
				Files.copy(content[i].toPath(), desc.toPath(), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();

			}
		}
	}

	public void getAllFiles(File dir, List<File> fileList) {
		try {
			File[] files = dir.listFiles();
			for (File file : files) {
				fileList.add(file);
				if (file.isDirectory()) {
					System.out.println("directory:" + file.getCanonicalPath());
					getAllFiles(file, fileList);
				} else {
					System.out.println("file:" + file.getCanonicalPath());
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeZipFile(File directoryToZip) {

		try {
			new ZipFile("Zipped Report.zip").addFolder(new File(basePath + "Zipped Report"));
		} catch (ZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		try {
//			FileOutputStream fos = new FileOutputStream(directoryToZip.getName() + ".zip");
//			ZipOutputStream zos = new ZipOutputStream(fos);
//
//			for (File file : fileList) {
//				if (!file.isDirectory()) { // we only zip files, not directories
//					addToZip(directoryToZip, file, zos);
//				}
//			}
//
//			zos.close();
//			fos.close();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	public void addToZip(File directoryToZip, File file, ZipOutputStream zos)
			throws FileNotFoundException, IOException {

		FileInputStream fis = new FileInputStream(file);

		// we want the zipEntry's path to be a relative path that is relative
		// to the directory being zipped, so chop off the rest of the path
		String zipFilePath = file.getCanonicalPath().substring(directoryToZip.getCanonicalPath().length() + 1,
				file.getCanonicalPath().length());
		System.out.println("Writing '" + zipFilePath + "' to zip file");
		ZipEntry zipEntry = new ZipEntry(zipFilePath);
		zos.putNextEntry(zipEntry);

		byte[] bytes = new byte[1024];
		int length;
		while ((length = fis.read(bytes)) >= 0) {
			zos.write(bytes, 0, length);
		}

		zos.closeEntry();
		fis.close();
	}

	public void moveFile(String source, String destination) throws IOException {
		Path temp = Files.move(Paths.get(source), Paths.get(destination));
		if (temp != null) {
			System.out.println("File renamed and moved successfully");
		} else {
			System.out.println("Failed to move the file");
		}
	}

	public void cleanDir(String dirName) {
		File directory = new File(dirName);

		// Get all files in directory
		File[] files = directory.listFiles();
		for (File file : files) {
			// Delete each file
			if (!file.delete()) {
				// Failed to delete file
				System.out.println("Failed to delete " + file);
			}
		}
	}

	public void cleanFolder(String dirName) {//created by sele+4
		File directory = new File(dirName);

		// Get all files in directory
		File[] files = directory.listFiles();
		for (File file : files) {
				// Delete each file
				file.delete();
			try {
				FileUtils.deleteDirectory(file);
			} catch (Exception e) {
				System.err.print("Failed to delete " + file);
			}

		}
	}

	public void attachScreenshotInEmailReport() throws Exception {
		String OS = System.getProperty("os.name", "generic").toLowerCase();

		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		String zipCandidates = s + File.separator + "Zipped Report" + File.separator + "Extent Reports" + File.separator;

		File input = new File(zipCandidates + "extentreport.html");
		Document doc = Jsoup.parse(input, "UTF-8");

		for (Element img : doc.select("img[src]")) {
			String fileName = img.attr("src");
			String[] bits;
			if ((OS.indexOf("mac") >= 0)) {
				bits = fileName.split("/");
			} else {
				fileName.replace("\\", "\\\\");
				bits = fileName.split("\\\\");
			}

			String imgName = bits[bits.length - 1];
			img.attr("src", imgName);
		}

		for (Element img : doc.select("img[data-featherlight]")) {
			String fileName = img.attr("data-featherlight");
			String[] bits;
			if ((OS.indexOf("mac") >= 0)) {
				bits = fileName.split("/");
			} else {
				fileName.replace("\\", "\\\\");
				bits = fileName.split("\\\\");
			}
			String imgName = bits[bits.length - 1];
			img.attr("data-featherlight", imgName);
		}

		for (Element img : doc.select("img[data-src]")) {
			String fileName = img.attr("data-src");
			String[] bits;
			if ((OS.indexOf("mac") >= 0)) {
				bits = fileName.split("/");
			} else {
				fileName.replace("\\", "\\\\");
				bits = fileName.split("\\\\");
			}
			String imgName = bits[bits.length - 1];
			img.attr("data-src", imgName);
		}

		try {
			String strmb = doc.outerHtml();
			BufferedWriter bw = new BufferedWriter(new FileWriter(zipCandidates + "extentreport.html"));
			bw.write(strmb);
			bw.close();
		} catch (Exception ex) {
			System.out.println("There is an exception " + "\"" + ex.getMessage() + "\"");
		}
	}

	public List<String> getFilesNames(String filePath) {
		List<String> fileNames = new ArrayList<String>();
		File[] files = new File(filePath).listFiles();
		for (File file : files) {
			if (file.isFile()) {
				fileNames.add(file.getName().toString());
			}
		}
		return fileNames;
	}

	public boolean isStringPresentInList(List<String> fileList, String fileName) {
		boolean sts = false;
		for (String file : fileList) {
			if (file.equals(fileName)) {
				sts = true;
				break;
			}
		}
		return sts;
	}

	public boolean isExistColumnKeyFromCSV(String filePath, String fileName,String keyName) {
		boolean keyExist = false;
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		try {

			br = new BufferedReader(new FileReader(filePath + File.separator + fileName));
			while ((line = br.readLine()) != null) {
				// use comma as separator
				String[] fileData = line.split(cvsSplitBy);
				for (int i = 0; i < fileData.length; i++) {
					if(fileData[i].contains(keyName)) {
						keyExist = true;
						break;
					}
				}
				if(keyExist)
					break;

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return keyExist;
	}

	public boolean isMultipleStringPresentInList(List<String> fileList, String firstFile, String secondFile) {
		boolean downloadedFileStatus = false;
		for (String firstDownloadedFile : fileList) {
			if (firstDownloadedFile.equals(firstFile)) {
				// downloadedFileStatus = true;
				for (String secondDownloadedFile : fileList) {
					if (secondDownloadedFile.equals(secondFile)) {
						downloadedFileStatus = true;
						break;
					}
				}
			}
		}
		return downloadedFileStatus;
	}

	public synchronized void makeDirectoryEmpty(String filePath)  {
		try {
			FileUtils.cleanDirectory(new File(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public  String readQRCodeAndBarCode(String filePath) throws NotFoundException, IOException {
	    File file = new File(filePath);
	    String decodedText = null;
        BufferedImage bufferedImage = ImageIO.read(file);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result = new MultiFormatReader().decode(bitmap);
        decodedText =  result.getText();
        return decodedText;

	}

	public void takeScreenShot(RemoteWebDriver driver,String filePath) throws IOException {
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(scrFile, new File(filePath));
	}


	public  String readDataFromTextFile(String filePath,String fileName) {
		String value=null;

		try {
			FileReader fr=new FileReader(filePath+fileName);
			BufferedReader br= new BufferedReader(fr);

			String sCurrentLine="";
			String data="";
			while((sCurrentLine=br.readLine()) != null) {
				  data=data+sCurrentLine;
			}
			value=data;
			}
			catch (Exception e) {
			}

		return value;
	}



}