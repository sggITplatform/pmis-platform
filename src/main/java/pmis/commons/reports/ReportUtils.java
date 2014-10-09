package pmis.commons.reports;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRAbstractExporter;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.util.JRLoader;

@SuppressWarnings("deprecation")
public class ReportUtils {
	
	@SuppressWarnings("unused")
	private HttpServletRequest request;  
    private HttpServletResponse response;  
    private HttpSession session;  
    
    /** 
     * 定义了报表输出类型，固定了可输出类型 
     */  
    public static enum DocType {  
        PDF, DOC
    }  

    public ReportUtils(HttpServletRequest request, HttpServletResponse response) {  
        this.request = request;  
        this.session = request.getSession();  
        this.response = response;  
    }  
    
    /** 
     * 获得JasperPrint对象;自定义填充报表时的parameter和connection 
     */  
    @SuppressWarnings("unchecked")
	public JasperPrint getJasperPrint(String filePath, Map parameter,  
            Connection  conn) throws JRException {  
        JasperReport jasperReport = null;  
        try {  
            jasperReport = (JasperReport) JRLoader.loadObjectFromFile(filePath);  
            return JasperFillManager.fillReport(jasperReport, parameter, conn) ;
           
        } catch (JRException e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
    
    /** 
     * 获得JasperPrint对象;自定义填充报表时的parameter和dataSource. 参数说明和动态表头的用法参考上一方法 
     */  
    @SuppressWarnings("unchecked")
	public JasperPrint getJasperPrint(String filePath, Map parameter,  
            JRDataSource dataSource) throws JRException {  
        JasperReport jasperReport = null;  
        try {  
            jasperReport = (JasperReport) JRLoader.loadObjectFromFile(filePath);  
            return JasperFillManager.fillReport(jasperReport, parameter,  
                    dataSource);  
        } catch (JRException e) {  
            e.printStackTrace();  
        }  
        return null;  
    }  
  
    /** 
     * 通过传入List类型数据源获取JasperPrint实例 
     */  
    @SuppressWarnings("unchecked")
	public JasperPrint getPrintWithBeanList(String filePath, Map parameter,  
            List list) throws JRException {  
        JRDataSource dataSource = new JRBeanCollectionDataSource(list);  
        return getJasperPrint(filePath, parameter, dataSource);  
    }  
    
    /** 
     * 传入类型，获取输出器 
     *  
     * @param docType 
     * @return 
     */  
    @SuppressWarnings("unchecked")
	public JRAbstractExporter getJRExporter(DocType docType) {  
        JRAbstractExporter exporter = null;  
        switch (docType) {  
        case PDF:  
            exporter = new JRPdfExporter();  
            break;   
        case DOC:  
            exporter = new JRDocxExporter();  
            break;  
        }  
        return exporter;  
    }  
    
    /** 
     * 获得相应类型的Content type 
     * @param docType 
     * @return 
     */  
    public String getContentType(DocType docType){  
        String contentType="text/html";  
        switch(docType){  
        case PDF:  
            contentType = "application/pdf";  
            break;  
        case DOC:  
            contentType = "application/msword";  
            break; 
        }  
        return contentType;  
    }  
    
    public void setAttrToPage(JasperPrint jasperPrint, String report_fileName,  
            String report_type) {  
        session.setAttribute("REPORT_JASPERPRINT", jasperPrint);  
        session.setAttribute("REPORT_FILENAME", report_fileName);  
        session.setAttribute("REPORT_TYPE", report_type);  
    }  
   
    /** 
     * 编译报表模板文件jrxml，生成jasper二进制文件 
     *  
     * @param jrxmlPath 
     * @param jrsperPath 
     * @throws JRException 
     */  
    public void complieJrxml(String jrxmlPath, String jasperPath)  
            throws JRException {  
        JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);  
    }  
    
	/**
	 * 生成不同格式报表文档
	 * javaBean
	 * @param docType
	 *            文档类型
	 * @param jasperPath
	 */
	@SuppressWarnings("unchecked")
	 public void servletExportDocument(DocType docType, String jasperPath,  
            Map params, List sourceList, String fileName) throws JRException,  
            IOException, ServletException {  

        JRAbstractExporter exporter = getJRExporter(docType);  
        // 获取后缀  
        String ext = docType.toString().toLowerCase();  
  
        if (!fileName.toLowerCase().endsWith(ext)) {  
            fileName += "." + ext;  
        }  
    
  
        response.setContentType(getContentType(docType));  
        response.setHeader("Content-Disposition", "attachment; filename=\""  
                + URLEncoder.encode(fileName, "UTF-8") + "\"");  
  
        exporter.setParameter(JRExporterParameter.JASPER_PRINT,  
                getPrintWithBeanList(jasperPath, params, sourceList));  
          
        OutputStream outStream = null;  
      
            outStream = response.getOutputStream();  
            exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outStream);  
           
        try {  
            exporter.exportReport();  
        } catch (JRException e) {  
            throw new ServletException(e);  
        } finally {  
            if (outStream != null) {  
                try {  
                    outStream.close();  
                } catch (IOException ex) {  
                }  
            }  
        }  
    }  
  
    
    /** 
     * 生成不同格式报表文档 
     *  Connection
     * @param docType 
     *            文档类型 
     * @param jasperPath 
     */  
	@SuppressWarnings("unchecked")
	public void servletExportDocument(DocType docType, String jasperPath,
			Map params, Connection conn, String fileName) throws JRException,
			IOException, ServletException {
		
		JRAbstractExporter exporter = getJRExporter(docType);
		// 获取后缀
		String ext = docType.toString().toLowerCase();

		if (!fileName.toLowerCase().endsWith(ext)) {
			fileName += "." + ext;
		}

		response.setContentType(getContentType(docType));

		response.setHeader("Content-Disposition", "attachment; filename=\""
				+ URLEncoder.encode(fileName, "UTF-8") + "\"");

		OutputStream outStream = null;

		outStream = response.getOutputStream();
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outStream);
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, getJasperPrint(
				jasperPath, params, conn));
		
		try {
			exporter.exportReport();
		} catch (JRException e) {
			throw new ServletException(e);
		} finally {
			if (outStream != null) {
				try {
					outStream.close();
				} catch (IOException ex) {
				}
			}
		}
	}
 
}
