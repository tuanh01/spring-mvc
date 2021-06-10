package edu.poly.TuAnhpolyshop.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.poly.TuAnhpolyshop.domain.Category;
//là file interface extends JpaRepository cho phép cung cấp phương thức thực hiện các thao tác trên entity category như thêm xóa sửa cập nhập tìm kiếm 
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
	
	List<Category>findByNameContaining(String name);
}
