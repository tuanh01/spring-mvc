package edu.poly.TuAnhpolyshop.controller.admin;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import edu.poly.TuAnhpolyshop.service.CategoryService;

@Controller
@RequestMapping("admin/categories")
public class CategoryController {

	@Autowired
	CategoryService categoryservice;

	@GetMapping("add")
	public String add(Model model) {// hiển thị form addoredit
		model.addAttribute("category", new Category());
		return "admin/categories/addOrEdit";

	}

	@GetMapping("edit/{categoryId}")//sửa thông tin
	public ModelAndView edit(ModelMap model ,@PathVariable("categoryId") Long categoryId) {// chỉnh sửa addoredit

		Optional<Category> opt =categoryservice.findById(categoryId);//lấy id
		CategoryDto dto= new CategoryDto();
		if(opt.isPresent()) {//kiểm tra nếu có giá trị trả về
			
			Category entity = opt.get();//lấy dữ liệu của category
			
			BeanUtils.copyProperties(entity, dto);//cooy từ entity sang dto ==> chuyển sang cho model để hiển thị
			
			dto.setIsEdit(true);//nếu mà ở chế độ update là true
			
			model.addAttribute("category",dto);//thiết lập giá trị thuộc tính category
			
			return new ModelAndView( "admin/categories/addOrEdit",model);//hiển thị thông tin
		}
		model.addAttribute("message","Category không tồn tại");//trường hợp ngược lại,nếu không tồn tại sẽ thông báo
		
		return new ModelAndView("forward:/admin/categories",model);
		
		
	}

	@GetMapping("delete/{categoryId}")
	public ModelAndView delete(ModelMap model ,  @PathVariable("categoryId") Long categoryId) {// xóa một bản ghi trong category

		categoryservice.deleteById(categoryId);
		
		model.addAttribute("message", "Đã xóa thành công bạn nhé");
		return new ModelAndView("forward:/admin/categories/search",model);
	}

	@PostMapping("saveOrUpdate")
	public ModelAndView saveOrUpdate(ModelMap model, @Valid @ModelAttribute("category") CategoryDto dto, BindingResult result) {// lưu nội dung khi thêm mới

		if(result.hasErrors()) {
			
			return new ModelAndView("admin/categories/addOrEdit");
		}
		
		Category entity = new Category();// +Tạo đối tượng Category

		BeanUtils.copyProperties(dto, entity);// +cooy từ dto sang entity

		categoryservice.save(entity);// +sau đó save entity vào cơ sở dữ liệu

		model.addAttribute("message", "Chúc mừng bạn đã Save thành công");

		return new ModelAndView("forward:/admin/categories", model);
	}

	@RequestMapping("")
	public String list(ModelMap model) {// Hiển thị danh sách category

		List<Category> list = categoryservice.findAll();// trả về danh sách category có trong CSDL

		model.addAttribute("categories", list);// thiết lập thuộc tính cho model

		return "admin/categories/list";
	}

	@GetMapping("search")
	public String search( ModelMap model ,@RequestParam (name="name", required = false) String name ) {//tìm kiếm thông tin dựa vào name

		
		List<Category> list=null;
		if(StringUtils.hasText(name)) {//kiểm tra nội dung truyền về có nội dung hay không
			
			list=categoryservice.findByNameContaining(name);
		}else {
			list=categoryservice.findAll();
			
		}
		model.addAttribute("categories",list);//thiết lập danh sách cho thuộc tính categories
		
		return "admin/categories/search";
	}
}
