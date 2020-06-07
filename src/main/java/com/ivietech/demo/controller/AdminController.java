/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.controller;

import com.ivietech.demo.dao.BalanceRepository;
import com.ivietech.demo.dao.CodeGiftCardRepository;
import com.ivietech.demo.dao.OrderRepository;
import com.ivietech.demo.dao.PaymentRepository;
import com.ivietech.demo.dao.PlaformRepository;
import com.ivietech.demo.dao.ProductRepository;
import com.ivietech.demo.dao.RechagerRepository;
import com.ivietech.demo.dao.TypeRepository;
import com.ivietech.demo.dao.UserRepository;
import com.ivietech.demo.dto.ProductDto;
import com.ivietech.demo.entity.CodeGiftCard;
import com.ivietech.demo.entity.OrderDetails;
import com.ivietech.demo.entity.Orders;
import com.ivietech.demo.entity.Payment;
import com.ivietech.demo.entity.Platforms;
import com.ivietech.demo.entity.Product;
import com.ivietech.demo.entity.Recharge;
import com.ivietech.demo.entity.Type;
import com.ivietech.demo.entity.User;
import com.ivietech.demo.service.BalanceService;
import com.ivietech.demo.validation.ProductValidator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author HoangMinh
 */
@Controller
public class AdminController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private BalanceRepository balanceRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private TypeRepository typeRepository;
    @Autowired
    private PlaformRepository plaformRepository;
    @Autowired
    private CodeGiftCardRepository codeGiftCardRepository;
    @Autowired
    private RechagerRepository rechagerRepository;
    @Autowired
    private ProductValidator productValidator;
    @Autowired
    private BalanceService balanceService;
    @Autowired
    private PaymentRepository paymentRepository;

    @GetMapping("/admin/product/view")
    public String viewProduct(Model model,
            @RequestParam(value = "filter_product_id", required = false, defaultValue = "") String productId,
            @RequestParam(value = "filter_product_name", required = false, defaultValue = "") String productName,
            @RequestParam(value = "filter_type_name", required = false, defaultValue = "") String typeName,
            @RequestParam(value = "filter_plaform_name", required = false, defaultValue = "") String plaformName,
            @RequestParam(value = "filter_price_from", required = false, defaultValue = "0") long priceLow,
            @RequestParam(value = "filter_price_to", required = false, defaultValue = "999999999999999") long priceHigh,
            HttpServletRequest request) {

        int page = 0; //default page number is 0 (yes it is weird)
        int size = 10; //default page size is 10
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }
        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
        List<Platforms> listPlatforms = plaformRepository.findAll();
        List<Type> listType = typeRepository.findAll();
        model.addAttribute("listPlatforms", listPlatforms);
        model.addAttribute("listType", listType);
        Page<ProductDto> productDto = productRepository.findAllProductDto(productId, productName, priceLow, priceHigh, typeName, plaformName, PageRequest.of(page, size));
        model.addAttribute("listProduct", productDto);
        return "admin/listProduct";
    }

    @GetMapping("/admin/product/create")
    public String createProduct(Model model) {
        ProductDto productDto = new ProductDto();
        List<Platforms> listPlatforms = plaformRepository.findAll();
        List<Type> listType = typeRepository.findAll();
        model.addAttribute("listPlatforms", listPlatforms);
        model.addAttribute("listType", listType);
        model.addAttribute("product", productDto);
        return "admin/createProduct";
    }

    @GetMapping("/admin/product/edit")
    public String CreateProduct(Model model, @RequestParam(value = "id", required = true) String id) {
        ProductDto productDto = new ProductDto();
        List<Platforms> listPlatforms = plaformRepository.findAll();
        List<Type> listType = typeRepository.findAll();
        productDto = productRepository.findProductDtoById(id).get();
        model.addAttribute("listPlatforms", listPlatforms);
        model.addAttribute("listType", listType);
        model.addAttribute("product", productDto);
        return "admin/createProduct";
    }

    @PostMapping("/admin/product/add")
    public String SaveProduct(@ModelAttribute("product") ProductDto productDto, BindingResult bindingResult, @RequestParam("file") MultipartFile file,
            Model model) {
        productValidator.validate(productDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return "admin/createProduct";
        }
        System.out.println("abcsds");
        Product product = new Product();
        if (!productDto.getId().isEmpty()) {
            System.out.println(productDto.getId());
            product = productRepository.findById(productDto.getId()).get();
        }
        String imgPath = "";

        if (file.isEmpty()) {
            if (productDto.getId() == null) {
                return "redirect:uploadStatus";
            }
        } else {
            try {
                byte[] bytes = file.getBytes();
                java.nio.file.Path path = Paths.get("uploads/img_p/" + file.getOriginalFilename());
                Files.write(path, bytes);
                imgPath = '/' + path.toString();
            } catch (IOException e) {
                return "redirect:uploadStatus";
            }
        }
        product.setName(productDto.getName());
        product.setPriceNew(productDto.getPriceNew());
        product.setPriceOld(productDto.getPriceOld());
        product.setType(typeRepository.findByName(productDto.getTypeName()));
        product.setPlatforms(plaformRepository.findByName(productDto.getPlatformsName()));
        product.setBest(productDto.isBest());
        if (!imgPath.isEmpty()) {
            product.setImg(imgPath);
        }
        productRepository.save(product);
        return "redirect:/admin/product/view";

    }

    @GetMapping("/admin/product/delete")
    public String deleteProduct(Model model, @RequestParam(value = "id", required = true) String id) {
        Product product = new Product();
        product = productRepository.findById(id).get();
        if (product.getListCodeGiftCard().isEmpty() && product.getListOrderDetail().isEmpty()) {
            product.setPlatforms(null);
            product.setType(null);
            product = productRepository.save(product);
            productRepository.delete(product);
        }
        return "redirect:/admin/product/view";
    }

    @GetMapping("/admin/type/view")
    public String viewType(Model model) {
        List<Type> types = typeRepository.findAll();
        model.addAttribute("types", types);
        return "admin/listType";
    }

    @GetMapping("/admin/type/create")
    public String createType(Model model) {
        Type type = new Type();
        model.addAttribute("type", type);
        return "admin/createType";
    }

    @GetMapping("/admin/type/edit")
    public String editType(Model model, @RequestParam(value = "id", required = true) Long id) {
        Type type = typeRepository.findById(id).get();
        model.addAttribute("type", type);
        return "admin/createType";
    }

    @PostMapping("/admin/type/add")
    public String saveType(@ModelAttribute("type") @Valid Type type, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/createType";
        }
        typeRepository.save(type);
        return "redirect:/admin/type/view";
    }

    @GetMapping("/admin/type/delete")
    public String deleteType(Model model, @RequestParam(value = "id", required = true) Long id) {
        Type type = typeRepository.findById(id).get();
        if (type.getListProducts().isEmpty()) {
            typeRepository.delete(type);
        }
        return "redirect:/admin/type/view";
    }

    @GetMapping("/admin/platform/view")
    public String viewPlatforms(Model model) {
        List<Platforms> platforms = plaformRepository.findAll();
        model.addAttribute("platforms", platforms);
        return "admin/listPlatform";
    }

    @GetMapping("/admin/platform/create")
    public String createPlatforms(Model model) {
        Platforms platform = new Platforms();
        model.addAttribute("platform", platform);
        return "admin/createPlatform";
    }

    @GetMapping("/admin/platform/edit")
    public String editPlatforms(Model model, @RequestParam(value = "id", required = true) Long id) {
        Platforms platform = plaformRepository.findById(id).get();
        model.addAttribute("platform", platform);
        return "admin/createPlatform";
    }

    @PostMapping("/admin/platform/add")
    public String savePlatform(@ModelAttribute("platform") @Valid Platforms platform, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/createPlatform";
        }
        plaformRepository.save(platform);
        return "redirect:/admin/platform/view";
    }

    @GetMapping("/admin/platform/delete")
    public String deletePlatform(Model model, @RequestParam(value = "id", required = true) Long id) {
        Platforms platform = plaformRepository.findById(id).get();
        if (platform.getListProducts().isEmpty()) {
            plaformRepository.delete(platform);
        }
        return "redirect:/admin/platform/view";
    }

    @GetMapping("/admin/code/view")
    public String ViewCode(Model model, HttpServletRequest request) {

        int page = 0; //default page number is 0 (yes it is weird)
        int size = 10; //default page size is 10
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }
        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
        Page<CodeGiftCard> codeGiftCards = codeGiftCardRepository.findAll(PageRequest.of(page, size));
        model.addAttribute("listCode", codeGiftCards);
        return "admin/listCode";
    }

    @PostMapping("/admin/upload/code")
    public String uploadCode(@RequestParam("file") MultipartFile reapExcelDataFile, Model model) throws IOException {

        List<CodeGiftCard> listCodeGiftCard = new ArrayList<>();
        List<CodeGiftCard> listCodeGiftCardError = new ArrayList<>();
        XSSFWorkbook workbook = new XSSFWorkbook(reapExcelDataFile.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);
        List<Integer> errorRow = new ArrayList<Integer>();
        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            CodeGiftCard codeGiftCard = new CodeGiftCard();
            XSSFRow row = worksheet.getRow(i);
            try {
                codeGiftCard.setCode(row.getCell(0).getStringCellValue());
                Product product = productRepository.findById(row.getCell(1).getStringCellValue()).get();
                codeGiftCard.setProduct(product);
                listCodeGiftCard.add(codeGiftCardRepository.save(codeGiftCard));
            } catch (Exception e) {
                errorRow.add(i + 1);
            }
        }
        model.addAttribute("error", errorRow);
        model.addAttribute("listCode", listCodeGiftCard);
        return "admin/uploadCodeInfo";
    }

    @GetMapping("/admin/code/edit")
    public String editCode(Model model, @RequestParam(value = "id", required = true) Long id) {
        CodeGiftCard code = new CodeGiftCard();
        code = codeGiftCardRepository.findById(id).get();
        model.addAttribute("code", code);
        return "admin/editcode";
    }

    @GetMapping("/admin/code/delete")
    public String deleteCode(Model model, @RequestParam(value = "id", required = true) Long id) {
        CodeGiftCard code = new CodeGiftCard();
        code = codeGiftCardRepository.findById(id).get();
        if (code.getOrderDetails() == null) {
            code.setProduct(null);
            code = codeGiftCardRepository.save(code);
            codeGiftCardRepository.delete(code);
        }
        return "redirect:/admin/code/view";
    }

    @GetMapping("/admin/recharge/view")
    public String rechargeView(Model model, @RequestParam(value = "filter_recharge_id", required = false, defaultValue = "") String rechargeId,
            @RequestParam(value = "filter_status", required = false, defaultValue = "") String status,
            @RequestParam(value = "filter_date_added_from", required = false, defaultValue = "1999-01-01") String date_from,
            @RequestParam(value = "filter_total_from", required = false, defaultValue = "0") long total_from,
            @RequestParam(value = "filter_date_added_to", required = false, defaultValue = "2030-01-01") String date_to,
            @RequestParam(value = "filter_total_to", required = false, defaultValue = "9999999999999999") long total_to,
            HttpServletRequest request) throws ParseException {
        int page = 0; //default page number is 0 (yes it is weird)
        int size = 10; //default page size is 10
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }
        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
        Page<Recharge> recharges = rechagerRepository.findRechargeSearch(rechargeId, status, total_from, total_to, date_from, date_to, PageRequest.of(page, size, Sort.by("id").descending()));

        model.addAttribute("listRecharge", recharges);
        return "admin/listRecharge";
    }

    @GetMapping("/admin/recharge/change")
    public String rechargeChange(Model model, @RequestParam(value = "page", required = true) String page, @RequestParam(value = "id", required = true) String id) throws Exception {
        Optional<Recharge> recharge = rechagerRepository.findById(id);
        if (recharge.isPresent()) {
            if (recharge.get().getStatus().equalsIgnoreCase("Đang đợi")) {
                recharge.get().setStatus("Thành công");
                balanceService.changeMoney(recharge.get().getUser().getBalance(), recharge.get().getMoney());
                rechagerRepository.save(recharge.get());
            }

        }
        return "redirect:/admin/recharge/view?page=" + page;
    }

    @GetMapping("/admin/user/view")
    public String userView(Model model, HttpServletRequest request) {
        int page = 0; //default page number is 0 (yes it is weird)
        int size = 10; //default page size is 10
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }
        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
        Page<User> listUser = userRepository.findAll(PageRequest.of(page, size, Sort.by("id").descending()));
        model.addAttribute("listUser", listUser);

        return "admin/listUser";
    }

    @GetMapping("/admin/payment/view")
    public String paymentView(Model model) {
        List<Payment> payments = paymentRepository.findAll();
        model.addAttribute("payments", payments);
        return "admin/listPayment";
    }

    @GetMapping("/admin/payment/edit")
    public String editPayment(Model model, @RequestParam(value = "id", required = true) Long id) {
        Payment payment = new Payment();
        payment = paymentRepository.findById(id).get();

        model.addAttribute("payment", payment);
        return "admin/createPayment";
    }

    @GetMapping("/admin/payment/create")
    public String CreatePayment(Model model) {
        Payment payment = new Payment();
        model.addAttribute("payment", payment);
        return "admin/createPayment";
    }

    @PostMapping("/admin/payment/add")
    public String SavePayment(@ModelAttribute("payment") @Valid Payment payment, @RequestParam("file") MultipartFile file,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/createPlatform";
        }

        Payment p = new Payment();

        if (payment.getId() != 0) {
            p = paymentRepository.findById(payment.getId()).get();
        }
        String imgPath = "";

        if (file.isEmpty()) {
            if (payment.getId() == 0) {
                return "redirect:uploadStatus";
            }
        } else {
            try {
                byte[] bytes = file.getBytes();
                java.nio.file.Path path = Paths.get("uploads/img_p/" + file.getOriginalFilename());
                Files.write(path, bytes);
                imgPath = '/' + path.toString();
            } catch (IOException e) {
                return "redirect:uploadStatus";
            }
        }

        p.setBankNumber(payment.getBankNumber());
        p.setDescription(payment.getDescription());
        p.setNamePerson(payment.getNamePerson());
        p.setNamePayment(payment.getNamePayment());
        p.setEnabled(payment.isEnabled());
        if (!imgPath.isEmpty()) {
            p.setImg(imgPath);
        }
        paymentRepository.save(p);
        return "redirect:/admin/payment/view";

    }

    @GetMapping("/admin/payment/delete")
    public String deletePayment(Model model, @RequestParam(value = "id", required = true) Long id) {
        Payment payment = new Payment();
        payment = paymentRepository.findById(id).get();
        if (payment.getRecharge().isEmpty()) {
            paymentRepository.delete(payment);
        }
        return "redirect:/admin/payment/view";
    }

    @GetMapping("/admin/order/view")
    public String orderView(@RequestParam(value = "filter_order_id", required = false, defaultValue = "") String order_id,
            @RequestParam(value = "filter_date_added_from", required = false, defaultValue = "1999-1-1") String date_from,
            @RequestParam(value = "filter_total_from", required = false, defaultValue = "0") long total_from,
            @RequestParam(value = "filter_date_added_to", required = false, defaultValue = "2030-1-1") String date_to,
            @RequestParam(value = "filter_total_to", required = false, defaultValue = "99999999999") long total_to, 
            Model model, HttpServletRequest request) {
        int page = 0; //default page number is 0 (yes it is weird)
        int size = 5; //default page size is 10
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }
        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
        Page<Orders> listOrder = orderRepository.listOrderAdminSearch(order_id, total_from, total_to, date_from, date_to, PageRequest.of(page, size));
        model.addAttribute("listOrder", listOrder);
//        Page<ProductDto> productDto = productRepository.findAllProductDto(PageRequest.of(page, size));
//        model.addAttribute("listProduct", productDto);
        return "admin/listOrder";
    }

    @GetMapping("/admin/order/detail")
    public String orderViewDetail(Model model, HttpServletRequest request, @RequestParam(value = "orderId", required = true) String orderId) {
        Optional<Orders> order = orderRepository.findById(orderId);
        model.addAttribute("order", order.get());
        List<OrderDetails> orderDetails = order.get().getOrderDetails();
        model.addAttribute("orderDetails", orderDetails);
        return "admin/orderDetail";
    }
}
