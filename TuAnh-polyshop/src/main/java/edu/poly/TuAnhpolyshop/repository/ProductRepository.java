package edu.poly.TuAnhpolyshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.poly.TuAnhpolyshop.domain.Product;
//thêm xóa sửa cập nhập
@Repository
public interface ProductRepository  extends JpaRepository<Product,Long>{

}
