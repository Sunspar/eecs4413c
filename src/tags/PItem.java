package tags;

import java.io.IOException;
import java.text.NumberFormat;

import javax.naming.NamingException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import model.ItemBean;
import model.Product;

public class PItem extends SimpleTagSupport {

	private String itemNum;
	private String mclass;
	private String context;

    public void doTag() throws JspException, IOException
    {
    		Product p;
			try {
				p = new Product();
			
    		ItemBean item = p.getItem(itemNum);
            JspWriter os = this.getJspContext().getOut();
            
            final int numImg = 6;
            
            int img = (int) Math.floor(Math.random()*numImg + 1);    
            //os.write("<img src='" + this.itemNum + img + ".jpg' class='" + this.mclass + "' />");
            
            StringBuilder out = new StringBuilder();
            NumberFormat nf = NumberFormat.getCurrencyInstance();
            
            out.append("<div class='" + this.mclass + "'>"
            		+ "<h4><a class='item-link' href='" + this.context + "/e/Item?num=" + item.number + "'>" + item.name + "</a></h4>"
            		+ "<h6>" + item.number + "</h6 ><h5 align='right'>"
            				+ nf.format(item.price) +"</h5>"
            						+ "<a href='" + this.context + "/e/Item?num=" + item.number + "'>"
            						+ "<div class='product-img-container'>"
            						+ "<div class='add-splash' id='splash-" + item.number +"'>"
            								+ "Added to Cart!"
            								+ "</div>"
            								+ "<img src='" + this.context + "/res/img/cat-" + item.catID + "/" + img + ".jpg' class='img-responsive img-thumbnail' />"
            								+ "</div>"
            						+ "</a>"
            								+ "<div class='cart-btn-container' align='right'>"
            								+ "<button type='button' class='btn btn-default btn-sm' onclick=\"fade('splash-" + item.number + "'); "
            										+ "doAddCart('" + item.name + "', '" + item.number + "', '" + this.context + "', this);\">"
            								+ "<span class='glyphicon glyphicon-plus' aria-hidden='true'></span>"
            										+ "<span class='glyphicon glyphicon-shopping-cart' aria-hidden='true'></span>"
            												+ "</button>"
            												+ "</div>"
            												+ "</div>");
            
            os.write(out.toString());
			} catch (NamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
    }
    
    public void setItemNum(String itemNum)
    {
            this.itemNum = itemNum;
    }
    
    public void setMclass(String mclass)
    {
            this.mclass = mclass;
    }

	public void setContext(String context) {
		this.context = context;
	}
    
    
    
}
