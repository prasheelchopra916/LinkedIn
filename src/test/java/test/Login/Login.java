package test.Login;

import java.util.logging.Logger;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

import common.Config;


public class Login {
	String env;
	String file;
	String userId;
	String browser;
	boolean status;
	String PSWD;
	String buildType;
	Config config;
	ReportSetup report;
	boolean testExecuted;
	ExtentReports extent;
	ExtentTest testSubflow;
	static Logger log = Logger.getLogger("AccessUnificationCreateNFID.class");
	Traceability trace=new Traceability();
	Reporter reporter = new Reporter();
	@BeforeMethod
	public void setUp() throws Exception {
		//file = "//Parent.xlsx";
		wfmcoffee = null;
		//userDir = System.getProperty("user.dir");
		ExcelData.setExcelFile("Data");
		report = new ReportSetup();
		extent = report.getExtentReport();
		aUConfig = new AUConfig();
		testSubflow = extent.startTest("AU NFID Creation");
		AUUtility.setTestSubflow(testSubflow);
		AUUtility.setReport(report);
	}

	@Test(groups = { "AccessUnificationCreateNFID" })
	public void testMethods() throws Exception {
		String buildType= aUConfig.getBuildType();
		System.out.println("BuildType:" + buildType);
		if (buildType.equalsIgnoreCase("New")) {
			String tc = ExcelData.getCellData(1, 1);
			System.out.println("TC:" + tc);
			if (tc.equals("AccessUnificationCreateNFID")) {
				AUCreateNFID();
			}
		} else if (buildType.equalsIgnoreCase("old")) {
			String pss = ExcelData.getCellData(1,2);
			System.out.println("PSS" + pss);
			if (pss.equals("Passed")) {
				AUUtility.logMessage("SKIP", "Create NFID is completed", "Create NFID passed ",
						"AU Create NFID Passed");
			} else if (pss.equals("Failed")) {
				AUCreateNFID();
			}
		}
	}
	
	public void AUCreateNFID() throws Exception {
		testExecuted = true;
		try {
			aUConfig = new AUConfig();
			nFIDCreate = "Yes";
			// aUConfig.getnFIDCreate();
			env = aUConfig.getEnvironmentUUI();
			userId = aUConfig.getUsername();
			PSWD = aUConfig.getPassword();
			browser = aUConfig.getBrowser();
			String dedicatedORswitchedORfiber =  aUConfig.getDedicatedSwitchedFiber();
			String aSite =  aUConfig.getAsite();
			String zSite =  aUConfig.getZsite();
			String first_name=aUConfig.getFirstName();
			String last_name=aUConfig.getLastName();
			String city= aUConfig.getCity();
			String equipment=aUConfig.getEquip();
			String l2AswPort = "null";
			String ldPop = "null";
			String nniID = "null";
			String nniCLLI = "null";
			String efcCFG = "null";
			String speed =  aUConfig.getProjectType();
			String Bandwidth=aUConfig.getBandwidth();
			String tdmBandwidth=aUConfig.gettdmBandwidth();
			log.info("updating the testcase in Test Space");
			
			if (dedicatedORswitchedORfiber.equalsIgnoreCase("Dedicated")) {
				l2AswPort = aUConfig.getl2aSWport();
				ldPop = aUConfig.getLDpop();
			} else if (dedicatedORswitchedORfiber.equalsIgnoreCase("Switched")) {
				nniID =  aUConfig.getvzNNIID();
//				nniCLLI =  aUConfig.getnniCLLI();
				efcCFG =  aUConfig.getefcCFG();
				//icscCode =  aUConfig.getIcscCode();			
			} 
			 else if (dedicatedORswitchedORfiber.equalsIgnoreCase("Fiber")) {
			 }
			String accVendorName =  aUConfig.getAACVendorNAme();
			String tspCode = " ";
			//String tspCode =  aUConfig.getTSPcode();
			String aam =  aUConfig.getAAM();
			testSubflow = extent.startTest("AccessUnificationCreate NFID");
			AUUtility.setTestSubflow(testSubflow);
			AUUtility.setReport(report);
			testSubflow.log(LogStatus.INFO, "Start Access Unification AccessUnificationCreateNFID", "");
			log.info("***NFID Create: " + nFIDCreate + "***");
			if ("Yes".equalsIgnoreCase(nFIDCreate)) {
				wfmcoffee = new WFMcoffeEng(browser);
				log.info("***Create NFID for Parent order started***");	 
				log.info("updating in test space");
				status = wfmcoffee.login(env, userId, PSWD);
				if (!status)
					throw new TestExecutionException("Test Execution failed due to login failure");
				
				if (dedicatedORswitchedORfiber.equalsIgnoreCase("Dedicated")) {	
					AUCreateNFIDforDedicated auCreateNFIDforDedicated = new AUCreateNFIDforDedicated(wfmcoffee);
					status = auCreateNFIDforDedicated.CreateNFID(aSite, zSite, l2AswPort, ldPop, accVendorName, aam,speed,equipment,first_name,last_name,tdmBandwidth);
				} 
				else if (dedicatedORswitchedORfiber.equalsIgnoreCase("Switched")) {
					AUCreateNFIDforSwitched auCreateNFIDforSwitched = new AUCreateNFIDforSwitched(wfmcoffee);
					status = auCreateNFIDforSwitched.CreateNFID(aSite, zSite, nniID, nniCLLI, efcCFG, accVendorName,
							aam, speed,icscCode,Bandwidth,equipment,first_name,last_name,tdmBandwidth);
				} 
				else if (dedicatedORswitchedORfiber.equalsIgnoreCase("Fiber")) {		
					AUCreateNFIDforFiber auCreateNFIDforFiber = new AUCreateNFIDforFiber(wfmcoffee);
					status = auCreateNFIDforFiber.CreateNFID(aSite, zSite, l2AswPort, ldPop,city, accVendorName, aam,
							tspCode, speed,equipment,first_name,last_name,tdmBandwidth);
					}
			}
			
			if (!status){
					throw new TestExecutionException("Test Execution Failed in NFID Creation ");}
			 else {			
				testSubflow.log(LogStatus.PASS, "NFID Search", "NFID Search PASSED"
						+ report.captureScreenShot(WFMcoffeEng.driver, "Access Unification NFID Search PASS!!!!!!!"));
				AUUtility.logMessage("PASS","Parent Order Created successfully","NFID Creation passed ", "AU Collection Passed");
				ExcelData.setCellData("Passed", 1, 2);
				Assert.assertTrue(status);
			}
		}catch (NullPointerException | TestExecutionException e) {
			log.info(e.getMessage());
			AUUtility.logMessage("FAIL", "test", "tesst", "Create NFID is Failed");
			ExcelData.setCellData("Failed", 1, 2);
			Assert.assertFalse(status);
		 } finally {
				if (wfmcoffee != null) 
				WFMcoffeEng.closeBrowser("Multiple");
			}
	}
	
	@AfterMethod
	public void tearDown() {
		if (wfmcoffee != null) {
			WFMcoffeEng.closeBrowser("Multiple");
		}
		extent.endTest(testSubflow);
		extent.flush();
	}	
}
