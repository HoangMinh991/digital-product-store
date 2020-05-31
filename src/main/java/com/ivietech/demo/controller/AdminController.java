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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Payload;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
    public String ViewOrder(Model model, HttpServletRequest request) {

        int page = 0; //default page number is 0 (yes it is weird)
        int size = 10; //default page size is 10
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }
        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
        Page<ProductDto> productDto = productRepository.findAllProductDto(PageRequest.of(page, size));
        model.addAttribute("listProduct", productDto);
        return "admin/listProduct";
    }

    @GetMapping("/admin/product/create")
    public String CreateProduct(Model model) {
        ProductDto productDto = new ProductDto();
        List<Platforms> listPlatforms = plaformRepository.findAll();
        List<Type> listType = typeRepository.findAll();
        model.addAttribute("listPlatforms", listPlatforms);
        model.addAttribute("listType", listType);
        model.addAttribute("product", productDto);
        return "admin/createProduct";
    }

    @GetMapping("/admin/product/edit")
    public String CreateProduct(Model model, @RequestParam(value = "id", required = true) Long id) {
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
        Product product = new Product();
        if (productDto.getId() != 0) {
            product = productRepository.findById(productDto.getId()).get();
        }
        String imgPath = "";

        if (file.isEmpty()) {
            if (productDto.getId() == 0) {
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
                Product product = productRepository.findById((long) row.getCell(1).getNumericCellValue()).get();
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

    @GetMapping("/admin/recharge/view")
    public String rechargeView(Model model, HttpServletRequest request) {
        int page = 0; //default page number is 0 (yes it is weird)
        int size = 10; //default page size is 10
        if (request.getParameter("page") != null && !request.getParameter("page").isEmpty()) {
            page = Integer.parseInt(request.getParameter("page")) - 1;
        }
        if (request.getParameter("size") != null && !request.getParameter("size").isEmpty()) {
            size = Integer.parseInt(request.getParameter("size"));
        }
        Page<Recharge> listRecharges = rechagerRepository.findAll(PageRequest.of(page, size,Sort.by("id").descending()));
        model.addAttribute("listRecharge", listRecharges);

        return "admin/listRecharge";
    }

    @GetMapping("/admin/recharge/change")
    public String rechargeChange(Model model, @RequestParam(value = "page", required = true) String page, @RequestParam(value = "id", required = true) Long id) throws Exception {
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
        Page<User> listUser = userRepository.findAll(PageRequest.of(page, size,Sort.by("id").descending()));
        model.addAttribute("listUser", listUser);

        return "admin/listUser";
    }
    
    @GetMapping("/admin/payment/view")
    public String paymentView(Model model){
        List<Payment> payments = paymentRepository.findAll();
        model.addAttribute("payments", payments);
        return "admin/listPayment";
    }
}
