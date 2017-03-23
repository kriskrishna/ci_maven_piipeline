package controller;

import domain.Product;
import form.ProductForm;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.ProductService;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;

@Controller
public class ProductController {

    private static final Log logger = LogFactory
            .getLog(ProductController.class);

    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/")
    public String inputProduct() {
        logger.info("inputProduct called");
        return "ProductForm";
    }

    @RequestMapping(value = "/save-product", method = RequestMethod.POST)
    public String saveProduct(ProductForm productForm, RedirectAttributes redirectAttributes) {
        logger.info("saveProduct called");
        // no need to create and instantiate a ProductForm
        // create Product
        Product product = new Product();
        product.setName(productForm.getName());
        product.setDescription(productForm.getDescription());
        try {
            product.setPrice(new BigDecimal(productForm.getPrice()));
        } catch (NumberFormatException e) {
            logger.error("Price given had incorrect format." + e);
        }

        // add product
        Product savedProduct = productService.add(product);
        
        redirectAttributes.addFlashAttribute("message", "The product was successfully added.");

        // Try DB connection
        try {
            InitialContext initContext = new InitialContext();
            DataSource ds = (DataSource) initContext.lookup("java:comp/env/jdbc/pipedemo");
            Connection con = ds.getConnection();
            con.close();
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
        return "redirect:/view-product/" + savedProduct.getId();
    }

    @RequestMapping(value = "/view-product/{id}")
    public String viewProduct(@PathVariable Long id, Model model) {
        Product product = productService.get(id);
        model.addAttribute("product", product);
        return "ProductView";
    }
}
