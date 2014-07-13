package test.com.wdeanmedical.ehr.web;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
//import org.springframework.web.context.support.XmlWebApplicationContext;

import com.wdeanmedical.ehr.service.AppService;
import com.wdeanmedical.ehr.service.ExternalService;
import com.wdeanmedical.ehr.service.PatientService;
import com.wdeanmedical.ehr.web.AppServlet;
import com.wdeanmedical.ehr.web.ExternalServlet;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ExternalServlet.class, AppServlet.class})
@PowerMockIgnore("javax.management.*")
public class TestExternalServlet {
	
	static
	{
	    Logger rootLogger = Logger.getRootLogger();
	    rootLogger.setLevel(Level.INFO);
	    rootLogger.addAppender(new ConsoleAppender(new PatternLayout("%-6r [%p] %c - %m%n")));
	}
	
	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpServletResponse response;
	@Mock
	private ServletContext servletContext;
	@Mock
	private ServletConfig servletConfig;
	@Mock
	private ExternalService externalService;
	@Mock
	private PatientService patientService;
	@Mock
	private AppService appService;
	@Mock
	private ServletOutputStream outputStream;
	@Mock
	private AppServlet appServlet;
	
	//private XmlWebApplicationContext  applicationContext;
	
	@Before
	public void setUp() throws Exception {
		
		/*applicationContext = new XmlWebApplicationContext();
        String[] loc = {"file:web/WEB-INF/spring-servlet.xml"};
        applicationContext.setConfigLocations(loc);
        applicationContext.setServletContext(servletContext);
        applicationContext.refresh();
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, applicationContext);
        */ 		
		
		Mockito.when(request.getParameter("sessionId")).thenReturn("a113d333-4203-4f6c-aad7-15897e7bc4b8");//{"sessionId":"40b5c180-b196-48ed-a4fd-05610b984306"}
		Mockito.when(request.getParameter("data")).thenReturn("{\"sessionId\":\"a113d333-4203-4f6c-aad7-15897e7bc4b8\"}");
		Mockito.when(servletContext.getInitParameter("appSessionTimeout")).thenReturn("15");         
		Mockito.when(servletConfig.getServletContext()).thenReturn(servletContext);
		Mockito.when(response.getOutputStream()).thenReturn(outputStream);
         
        PowerMockito.whenNew(ExternalService.class).withNoArguments().thenReturn(externalService);       
        PowerMockito.whenNew(PatientService.class).withNoArguments().thenReturn(patientService);
        PowerMockito.whenNew(AppService.class).withNoArguments().thenReturn(appService);
        
        Method isValidSession = PowerMockito.method(AppServlet.class, "isValidSession", HttpServletRequest.class, HttpServletResponse.class);
		PowerMockito.replace(isValidSession).with(new InvocationHandler() {
			public Object invoke(Object proxy, Method method, Object[] args)
					throws Throwable {
				return true;
			}
		});
		
	    appServlet.init(servletConfig);    
        
	}
	
	
	@Test
    public void testGetPatient() throws Exception {
          
		Mockito.when(request.getPathInfo()).thenReturn("/json/getPatient/ABC123");
      
		ExternalServlet externalServlet = new ExternalServlet();

        externalServlet.init(servletConfig);
        externalServlet.doPost(request, response);
        
        Mockito.verify(outputStream).println("");
    }

}
