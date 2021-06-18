package edu.poly.TuAnhpolyshop.controller.admin;

import java.util.List;
import java.util.Optional;import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import edu.poly.TuAnhpolyshop.domain.Category;
import edu.poly.TuAnhpolyshop.model.CategoryDto;
import edu.poly.TuAnhpolyshop.model.ProductDto;
import edu.poly.TuAnhpolyshop.service.CategoryService;
import edu.poly.TuAnhpolyshop.service.ProductService;
import edu.poly.TuAnhpolyshop.utils.Const;

@Controller
@RequestMapping("admin/products")
public class ProductController {

	@Autowired
	CategoryService categoryservice;
	
	@Autowired
	ProductService productService;

	@ModelAttribute("categores")
	public List<CategoryDto> getCategories(){
		
		return categoryservice.findAll().stream().map(item->{
			CategoryDto dto =new CategoryDto();
			BeanUtils.copyProperties(item, dto);
			return dto;
		}).collect(Collectors.toList());
	}
	
	@GetMapping("add")
	public String add(Model model) {// hiển thị form addoredit
		model.addAttribute("product", new ProductDto());
		return "admin/products/addOrEdit";

	}

	@GetMapping("edit/{productId}")//sửa thông tin
	public ModelAndView edit(ModelMap model ,@PathVariable("productId") Long productId) {// chỉnh sửa addoredit

		Optional<Category> opt =categoryservice.findById(productId);//lấy id
		CategoryDto dto= new CategoryDto();
		if(opt.isPresent()) {//kiểm tra nếu có giá trị trả về
			
			Category entity = opt.get();//lấy dữ liệu của category
			
			BeanUtils.copyProperties(entity, dto);//cooy từ entity sang dto ==> chuyển sang cho model để hiển thị
			
			dto.setIsEdit(true);//nếu mà ở chế độ update là true
			
			model.addAttribute("category",dto);//thiết lập giá trị thuộc tính category
			
			return new ModelAndView( "admin/products/addOrEdit",model);//hiển thị thông tin
		}
		model.addAttribute("message","Category không tồn tại");//trường hợp ngược lại,nếu không tồn tại sẽ thông báo
		
		return new ModelAndView("forward:/admin/products",model);
		
		
	}

	@GetMapping("delete/{productId}")
	public ModelAndView delete(ModelMap model ,  @PathVariable("productId") Long productId) {// xóa một bản ghi trong category

		categoryservice.deleteById(productId);
		
		model.addAttribute("message", "Đã xóa thành công bạn nhé");
		return new ModelAndView("forward:/admin/products/search",model);
	}

	@PostMapping("saveOrUpdate")
	public ModelAndView saveOrUpdate(ModelMap model, @Valid @ModelAttribute("category") CategoryDto dto, BindingResult result) {// lưu nội dung khi thêm mới

		if(result.hasErrors()) {
			
			return new ModelAndView("admin/products/addOrEdit");
		}
		
		Category entity = new Category();// +Tạo đối tượng Category

		BeanUtils.copyProperties(dto, entity);// +cooy từ dto sang entity

		categoryservice.save(entity);// +sau đó save entity vào cơ sở dữ liệu

		model.addAttribute("message", "Chúc mừng bạn đã Save thành công");

		return new ModelAndView("forward:/admin/products", model);
	}

	@RequestMapping("")
	public String list(ModelMap model) {// Hiển thị danh sách category

		List<Category> list = categoryservice.findAll();// trả về danh sách category có trong CSDL

		model.addAttribute("products", list);// thiết lập thuộc tính cho model

		return "admin/products/list";
	}

	@GetMapping("search")
	public String search( ModelMap model ,@RequestParam (name="name", required = false) String name ) {//tìm kiếm thông tin dựa vào name

		
		List<Category> list=null;
		if(StringUtils.hasText(name)) {//kiểm tra nội dung truyền về có nội dung hay không
			
			list=categoryservice.findByNameContaining(name);
		}else {
			list=categoryservice.findAll();
			
		}
		model.addAttribute("products",list);//thiết lập danh sách cho thuộc tính products
		
		return "admin/products/search";
	}
	
	@GetMapping("searchpaginated")
	public String search( ModelMap model ,@RequestParam (name="name", required = false) String name ,
			@RequestParam("page") Optional<Integer> page,//trang hiện tại
			@RequestParam("age") Optional<Integer> size) {//size là kích thước hiển thị trên 1 trang

		int curentPage=page.orElse(1);//nếu người dùng không chọn giá trị thì giá trị ngầm định sẽ là trang 1
		
		int pageSize =size.orElse(5);//giá trị ngầm định là 5 phần tử trên 1 trang
		
		Pageable pageable = PageRequest.of(curentPage-1, pageSize,Sort.by("name"));//sắp xếp theeo trường dữ liệu name
		
		Page<Category> resultPage = null;
		
		if(StringUtils.hasText(name)) {//kiểm tra nội dung truyền về có nội dung hay không
			
			resultPage=categoryservice.findByNameContaining(name,pageable);
			
			model.addAttribute("name",name);
		}else {
			resultPage=categoryservice.findAll(pageable);//nếu không được truyền vào thì sẽ hiện cả 
			
		}
		//tính toán số trang được hiển thị
		int totalPages= resultPage.getTotalPages(); //trả về các trang đã được phân trang
		
		if(totalPages > 0) {
			
			int start = Math.max(1, curentPage-2);
			int end = Math.min(curentPage + 2, totalPages);
			
			if(totalPages >5) {
				
				if(end == totalPages) start = end-5;
				else if(start == 1) end =start +5;
			}
			List<Integer>pageNumbers=IntStream.range(start, end)   //xác định các trang được sinh ra từ start đến end
					.boxed()
					.collect(Collectors.toList());
			
			model.addAttribute("pageNumbers",pageNumbers);
		}
		
		model.addAttribute(Const.CATEGORY_PAGE,resultPage);//thiết lập danh sách cho thuộc tính products
		
		return "admin/products/searchpaginated";//trả về searchpaginated
	}
}