package testdata;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import utils.FileOperations;

public interface Operations {

	// we define fix things , which will be never changes
	Path currentRelativePath = Paths.get("");
	String s = currentRelativePath.toAbsolutePath().toString();
	String testDataFile = s + File.separator + "testdata" + File.separator;
	String TestDataFile = testDataFile + "web";

	String LOC_COMPANY = getAdminData("lCompany");
	String LOC_ADDRESS = getAdminData("lAddress");
	String LOC_CITY = getAdminData("lCity");
	String LOC_POSTAL_CODE = getAdminData("lPostalCode");
	String LOC_STATE = getAdminData("lState");
	String LOC2_COMPANY = getAdminData("l2Company");
	String LOC2_ADDRESS = getAdminData("l2Address");
	String LOC2_CITY = getAdminData("l2City");
	String LOC2_POSTAL_CODE = getAdminData("l2PostalCode");
	String LOC2_STATE = getAdminData("l2State");
	String NOTES_1 = getAdminData("comment1");
	String NOTES_2 = getAdminData("comment2");
	String TRAILER_PREFIX = getAdminData("trailer");
	String TRAILER_STATUS = getAdminData("bTrailerStatus");
	String PONumber = getAdminData("purchaseOrderNumber");
	String WEIGHT = getAdminData("weight");
	String PIECES = getAdminData("pieces");
	String COMMODITY = getAdminData("commodity");
	String BOL_PRE = getAdminData("bol");
	String SEAL_PRE = getAdminData("seal");
	String WO_PRE = getAdminData("workOrder");
	String INTERNAL_REF = getAdminData("internalReference");
	String VESSEL = getAdminData("vessel");
	String PORT = getAdminData("port");
	String AMOUNT_1 = getAdminData("amount1");
	String INVOICE_CODE_1 = getAdminData("selectInvoiceCode1");
	String AMOUNT_2 = getAdminData("amount2");
	String INVOICE_CODE_2 = getAdminData("selectInvoiceCode2");
	
	public static String getAdminData(String key) {
		return FileOperations.readColumnValueUsingKeyFromExcel(TestDataFile, "TestData_TM3_App.xlsx", "operations", key);
	}

}
