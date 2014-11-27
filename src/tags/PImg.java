package tags;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class PImg extends SimpleTagSupport {

	private String path;
	private String mclass;

    public void doTag() throws JspException, IOException
    {
            JspWriter os = this.getJspContext().getOut();
            
            final int numImg = 6;
            
            int img = (int) Math.floor(Math.random()*numImg + 1);    
            os.write("<img src='" + this.path + img + ".jpg' class='" + this.mclass + "' />");
    }
    public void setPath(String path)
    {
            this.path = path;
    }
    
    public void setMclass(String mclass) 
    {
    		this.mclass = mclass;
    }
	
}
