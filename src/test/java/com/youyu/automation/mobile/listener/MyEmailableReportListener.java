package com.youyu.automation.mobile.listener;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.IInvokedMethod;
import org.testng.IReporter;
import org.testng.IResultMap;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestClass;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.collections.Lists;
import org.testng.internal.Utils;
import org.testng.log4testng.Logger;
import org.testng.xml.XmlSuite;

import com.youyu.automation.mobile.testcase.AutoTestCase;

public class MyEmailableReportListener implements IReporter {
	private static final Logger L = Logger.getLogger(MyEmailableReportListener.class);

	// ~ Instance fields ------------------------------------------------------

	private PrintWriter m_out;

	private Integer m_testIndex;

	// ~ Methods --------------------------------------------------------------

	/** Creates summary of the run */
	@Override
	public void generateReport(List<XmlSuite> xml, List<ISuite> suites, String outdir) {
		try {
			m_out = createWriter(outdir);
		} catch (IOException e) {
			L.error("output file", e);
			return;
		}
		startHtml(m_out);
		generateSuiteSummaryReport(suites);
//		generateMethodSummaryReport(suites);
		generateMethodDetailReport(suites);
		endHtml(m_out);
		m_out.flush();
		m_out.close();
	}

	protected PrintWriter createWriter(String outdir) throws IOException {
		new File(outdir).mkdirs();
		return new PrintWriter(new File(outdir, "TestReport.html"), "utf-8");
	}

	/**
	 * Creates a table showing the highlights of each test method with links to
	 * the method details
	 */
	protected void generateMethodSummaryReport(List<ISuite> suites) {
		startResultSummaryTable("methodOverview");
		int testIndex = 1;
		for (ISuite suite : suites) {
			if (suites.size() > 1) {
				titleRow(suite.getName(), 5);
			}
			Map<String, ISuiteResult> r = suite.getResults();
			for (ISuiteResult r2 : r.values()) {
				ITestContext testContext = r2.getTestContext();
				m_testIndex = testIndex;
				resultSummary(suite, testContext.getFailedTests(), "以下是测试", "失败的用例", "");
				testIndex++;
			}
		}
		m_out.println("</table>");
	}

	/** Creates a section showing known results for each method */
	protected void generateMethodDetailReport(List<ISuite> suites) {
		for (ISuite suite : suites) {
			Map<String, ISuiteResult> r = suite.getResults();
			for (ISuiteResult r2 : r.values()) {
				ITestContext testContext = r2.getTestContext();
				resultDetail(testContext.getFailedTests());
			}
		}
	}

	/**
	 * @param tests
	 */
	private void resultSummary(ISuite suite, IResultMap tests, String testname, String style, String details) {
		if (tests.getAllResults().size() > 0) {
			StringBuffer buff = new StringBuffer();
			String lastClassName = "";
			int mq = 0;
			int cq = 0;
			for (ITestNGMethod method : getMethodSet(tests, suite)) {
				ITestClass testClass = method.getTestClass();
				String className = testClass.getName();
				if (mq == 0) {
					String id = (m_testIndex == null ? null : "t" + Integer.toString(m_testIndex));
					titleRow(testname + " &#8212; " + style + details, 5, id);
					m_testIndex = null;
				}
				if (!className.equalsIgnoreCase(lastClassName)) {
					if (mq > 0) {
						cq += 1;
						m_out.print("<tr class=\"" + style + (cq % 2 == 0 ? "even" : "odd") + "\">" + "<td");
						if (mq > 1) {
							m_out.print(" rowspan=\"" + mq + "\"");
						}
						m_out.println(">" + lastClassName + "</td>" + buff);
					}
					mq = 0;
					buff.setLength(0);
					lastClassName = className;
				}
				Set<ITestResult> resultSet = tests.getResults(method);
				long end = Long.MIN_VALUE;
				long start = Long.MAX_VALUE;
				for (ITestResult testResult : tests.getResults(method)) {
					if (testResult.getEndMillis() > end) {
						end = testResult.getEndMillis();
					}
					if (testResult.getStartMillis() < start) {
						start = testResult.getStartMillis();
					}
				}
				mq += 1;
				if (mq > 1) {
					buff.append("<tr class=\"" + style + (cq % 2 == 0 ? "odd" : "even") + "\">");
				}
				String description = method.getDescription();
				String testInstanceName = resultSet.toArray(new ITestResult[] {})[0].getTestName();

				long second = (end - start) / 1000;
				String timeDesc = "";
				if (second > 60) {
					timeDesc = second / 60 + "分" + (second % 60) + "秒";
				} else {
					timeDesc = second + "秒";
				}

				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = new Date(start);

				// buff.append("<td><a href=\"#m" + m_methodIndex + "\">"
				buff.append("<td><a href=\"#" + method.getMethodName() + "\">" + qualifiedName(method) + " "
						+ (description != null && description.length() > 0 ? "(\"" + description + "\")" : "") + "</a>"
						+ (null == testInstanceName ? "" : "<br>(" + testInstanceName + ")") + "</td>" + "<td>"
						+ df.format(date) + "</td>" + "<td>" + df.format(date) + "</td>" + "<td class=\"numi\">"
						+ timeDesc + "</td>" + "</tr>");
			}
			if (mq > 0) {
				cq += 1;
				m_out.print("<tr class=\"" + style + (cq % 2 == 0 ? "even" : "odd") + "\">" + "<td");
				if (mq > 1) {
					m_out.print(" rowspan=\"" + mq + "\"");
				}
				m_out.println(">" + lastClassName + "</td>" + buff);
			}
		}
	}

	/** Starts and defines columns result summary table */
	private void startResultSummaryTable(String style) {
		tableStart(style, "summary");
		m_out.println("<tr><th>执行类</th>" + "<th>执行方法</th><th>开始时间</th><th>结束时间</th><th>消耗时间</th></tr>");
	}

	private String qualifiedName(ITestNGMethod method) {
		StringBuilder addon = new StringBuilder();
		String[] groups = method.getGroups();
		int length = groups.length;
		if (length > 0 && !"basic".equalsIgnoreCase(groups[0])) {
			addon.append("(");
			for (int i = 0; i < length; i++) {
				if (i > 0) {
					addon.append(", ");
				}
				addon.append(groups[i]);
			}
			addon.append(")");
		}

		return "<b>" + method.getMethodName() + "</b> " + addon;
	}

	private void resultDetail(IResultMap tests) {
		for (ITestResult result : tests.getAllResults()) {
			ITestNGMethod method = result.getMethod();
			Set<ITestResult> resultSet = tests.getResults(method);
			generateForResult(result, method, resultSet.size());
			m_out.println("<p class=\"totop\"><a href=\"#summary\">back to top</a></p>");

		}
	}

	private void generateForResult(ITestResult ans, ITestNGMethod method, int resultSetSize) {
		Object[] parameters = ans.getParameters();
		boolean hasParameters = parameters != null && parameters.length > 0;
		if (hasParameters) {
			tableStart("result", null);
			m_out.print("<tr class=\"param\">");
			for (int x = 1; x <= parameters.length; x++) {
				m_out.print("<th>参数 " + x + "</th>");
			}
			m_out.println("</tr>");
			m_out.print("<tr class=\"param stripe\">");
			for (Object p : parameters) {
				m_out.println("<td>" + Utils.toString(p) + "</td>");
			}
			m_out.println("</tr>");
		}
		List<String> msgs = Reporter.getOutput(ans);
		boolean hasReporterOutput = msgs.size() > 0;
		Throwable exception = ans.getThrowable();
		boolean hasThrowable = exception != null;
		if (hasReporterOutput || hasThrowable) {
			if (hasParameters) {
				m_out.print("<tr><td");
				if (parameters.length > 1) {
					m_out.print(" colspan=\"" + parameters.length + "\"");
				}
				m_out.println(">");
			} else {
				m_out.println("<div>");
			}
			if (hasReporterOutput) {
				if (hasThrowable) {
					m_out.println("<h3>测试详情</h3>");
				}
				for (String line : msgs) {
					m_out.println(line + "<br/>");
				}
			}

			if (AutoTestCase.getMethod().equals("debug")) {
				File classpathRoot = new File(System.getProperty("user.dir"));
				String picPath = classpathRoot.getAbsolutePath() + "/screenshots/";
				m_out.println("<h3>失败截图</h3><img width=400 height=711 src='" + picPath + parameters[1].toString()
						+ ".png'/><br/> ");
			} else {
				m_out.println(
						"<h3>失败截图</h3><img width=400 height=711 src='http://10.9.8.44:8000/" + AutoTestCase.getExecuteId() + "/" + parameters[1].toString() + ".png'/><br/> ");
			}

			if (hasParameters) {
				m_out.println("</td></tr>");
			} else {
				m_out.println("</div>");
			}
		}
		if (hasParameters) {
			m_out.println("</table>");
		}
	}

	protected void generateExceptionReport(Throwable exception, ITestNGMethod method) {
		m_out.print("<div class=\"stacktrace\">");
		m_out.print(Utils.stackTrace(exception, true)[0]);
		m_out.println("</div>");
	}

	/**
	 * Since the methods will be sorted chronologically, we want to return the
	 * ITestNGMethod from the invoked methods.
	 */
	private Collection<ITestNGMethod> getMethodSet(IResultMap tests, ISuite suite) {
		List<IInvokedMethod> r = Lists.newArrayList();
		List<IInvokedMethod> invokedMethods = suite.getAllInvokedMethods();
		for (IInvokedMethod im : invokedMethods) {
			if (tests.getAllMethods().contains(im.getTestMethod())) {
				r.add(im);
			}
		}
		Arrays.sort(r.toArray(new IInvokedMethod[r.size()]), new TestSorter());
		List<ITestNGMethod> result = Lists.newArrayList();

		// Add all the invoked methods
		for (IInvokedMethod m : r) {
			result.add(m.getTestMethod());
		}

		// Add all the methods that weren't invoked (e.g. skipped) that we
		// haven't added yet
		for (ITestNGMethod m : tests.getAllMethods()) {
			if (!result.contains(m)) {
				result.add(m);
			}
		}
		return result;
	}

	public void generateSuiteSummaryReport(List<ISuite> suites) {
		tableStart("testOverview", null);
		m_out.print("<tr>");
		tableColumnStart("通过数(Pass)");
		tableColumnStart("未执行数(Skip)");
		tableColumnStart("失败数(Fail)");
		m_out.println("</tr>");
		int qty_tests = 0;
		int qty_pass = 0;
		int qty_skip = 0;
		int qty_fail = 0;
		long time_start = Long.MAX_VALUE;
		long time_end = Long.MIN_VALUE;
		m_testIndex = 1;
		for (ISuite suite : suites) {
			Map<String, ISuiteResult> tests = suite.getResults();
			for (ISuiteResult r : tests.values()) {
				qty_tests += 1;
				ITestContext overview = r.getTestContext();
				
				int q = overview.getPassedTests().size();
				qty_pass += q;
				summaryCell(q, 0);
				
				q = overview.getSkippedTests().size();
				qty_skip += q;
				summaryCell(q, 0);
				
				q = overview.getFailedTests().size();
				qty_fail += q;
				summaryCell(q, 0);
				
				time_start = Math.min(overview.getStartDate().getTime(), time_start);
				time_end = Math.max(overview.getEndDate().getTime(), time_end);

//				long seconds = (time_end - time_start) / 1000;
//				String timeDesc = "";
//				if (seconds > 60) {
//					timeDesc = seconds / 60 + "分" + seconds % 60 + "秒";
//				} else {
//					timeDesc = seconds + "秒";
//				}
//				summaryCell(timeDesc, true);
				m_out.println("</tr>");
				m_testIndex++;
			}
		}
		long totalTime = (time_end - time_start) / 1000;
		String timeDesc = "";
		if (totalTime > 60) {
			timeDesc = totalTime / 60 + "分" + totalTime % 60 + "秒";
		} else {
			timeDesc = totalTime + "秒";
		}

		if (qty_tests > 1) {
			m_out.println("<tr class=\"total\"><td>Total</td>");
			summaryCell(qty_pass, 0);
			summaryCell(qty_skip, 0);
			summaryCell(qty_fail, 0);
			summaryCell(timeDesc, true);
			m_out.println("<td colspan=\"2\">&nbsp;</td></tr>");
		}
		m_out.println("</table>");
	}

	private void summaryCell(String v, boolean isgood) {
		m_out.print("<td class=\"numi\">" + v + "</td>");
	}

	private void summaryCell(int v, int maxexpected) {
		summaryCell(String.valueOf(v), v <= maxexpected);
	}

	private void tableStart(String cssclass, String id) {
		m_out.println("<table cellspacing=\"0\" cellpadding=\"0\""
				+ (cssclass != null ? " class=\"" + cssclass + "\"" : " style=\"padding-bottom:2em\"")
				+ (id != null ? " id=\"" + id + "\"" : "") + ">");
	}

	private void tableColumnStart(String label) {
		m_out.print("<th>" + label + "</th>");
	}

	private void titleRow(String label, int cq) {
		titleRow(label, cq, null);
	}

	private void titleRow(String label, int cq, String id) {
		m_out.print("<tr");
		if (id != null) {
			m_out.print(" id=\"" + id + "\"");
		}
		m_out.println("><th colspan=\"" + cq + "\">" + label + "</th></tr>");
	}

	/** Starts HTML stream */
	protected void startHtml(PrintWriter out) {
		out.println(
				"<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.1//EN\" \"http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd\">");
		out.println("<html xmlns=\"http://www.w3.org/1999/xhtml\">");
		out.println("<head>");
		out.println("<meta charset='utf-8'></meta>");
		out.println("<title>TestNG Report</title>");
		out.println("<style type=\"text/css\">");
		out.println("table {margin-bottom:10px;border-collapse:collapse;empty-cells:show}");
		out.println("td,th {border:1px solid #009;padding:.25em .5em}");
		out.println(".result {margin-top:2%;width:100%}");
		out.println(".testOverview {margin-top:2%;width:100%}");
		out.println(".methodOverview {margin-top:2%;width:100%}");
		out.println(".result th {vertical-align:bottom}");
		out.println(".param th {padding-left:1em;padding-right:1em}");
		out.println(".param td {padding-left:.5em;padding-right:2em}");
		out.println(".stripe td,.stripe th {background-color: #E6EBF9}");
		out.println(".numi,.numi_attn {text-align:right}");
		out.println(".total td {font-weight:bold}");
		out.println(".passedodd td {background-color: #0A0}");
		out.println(".passedeven td {background-color: #3F3}");
		out.println(".skippedodd td {background-color: #CCC}");
		out.println(".skippedodd td {background-color: #DDD}");
		out.println(".failedodd td,.numi_attn {background-color: #F33}");
		out.println(".failedeven td,.stripe .numi_attn {background-color: #D00}");
		out.println(".stacktrace {white-space:pre;font-family:monospace}");
		out.println(".totop {font-size:85%;text-align:center;border-bottom:2px solid #000}");
//		out.println(".testBody {position:absolute;float:left;margin-left:5%;width:90%}");
		out.println("</style>");
		out.println("</head>");
		out.println("<body id=\"summary\">");
		out.println("<div class=\"testBody\">");
	}

	/** Finishes HTML stream */
	protected void endHtml(PrintWriter out) {
		out.println("</div></body></html>");
	}

	// ~ Inner Classes --------------------------------------------------------
	/** Arranges methods by classname and method name */
	private static final class TestSorter implements Comparator<IInvokedMethod> {
		// ~ Methods
		// -------------------------------------------------------------

		/** Arranges methods by classname and method name */
		@Override
		public int compare(IInvokedMethod o1, IInvokedMethod o2) {
			// System.out.println("Comparing " + o1.getMethodName() + " " +
			// o1.getDate()
			// + " and " + o2.getMethodName() + " " + o2.getDate());
			return (int) (o1.getDate() - o2.getDate());
			// int r = ((T) o1).getTestClass().getName().compareTo(((T)
			// o2).getTestClass().getName());
			// if (r == 0) {
			// r = ((T) o1).getMethodName().compareTo(((T) o2).getMethodName());
			// }
			// return r;
		}
	}
}